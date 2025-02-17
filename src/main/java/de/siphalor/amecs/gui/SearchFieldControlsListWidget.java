package de.siphalor.amecs.gui;

import java.util.*;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.text.IMutableText;
import de.klotzi111.fabricmultiversionhelper.api.text.TextWrapper;
import de.siphalor.amecs.Amecs;
import de.siphalor.amecs.KeyBindingEntryFilterSettings;
import de.siphalor.amecs.compat.NMUKProxy;
import de.siphalor.amecs.impl.duck.IKeyBindingEntry;
import de.siphalor.amecs.impl.duck.IKeybindsScreen;
import de.siphalor.amecs.version.KeyBindingEntryVersionHelper;
import de.siphalor.amecs.version.ScreenVersionHelper;
import de.siphalor.amecs.version.TextFieldWidgetVersionHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsListWidget.KeyBindingEntry;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;

public class SearchFieldControlsListWidget extends ControlsListWidget.Entry {

	public final TextFieldWidget textFieldWidget;

	private int lastChildrenCount = 0;
	private final Set<ControlsListWidget.KeyBindingEntry> entries = new TreeSet<>(Comparator.comparing(o -> ((IKeyBindingEntry) o).amecs$getKeyBinding()));
	private boolean isFirstCompile = true;

	@SuppressWarnings("unused") // but maybe needed some time and it does not harm
	private void copyKeyBindingEntrysFromChildren(List<ControlsListWidget.Entry> children) {
		entries.clear();
		for (ControlsListWidget.Entry entry : children) {
			if (entry instanceof ControlsListWidget.KeyBindingEntry) {
				entries.add((ControlsListWidget.KeyBindingEntry) entry);
			}
		}
		lastChildrenCount = children.size();
	}

	private void recompileChildrenList(ControlsListWidget listWidget, MinecraftClient client) {
		entries.clear();
		KeyBinding[] keyBindings = client.options.allKeys.clone();
		Arrays.sort(keyBindings);
		for (KeyBinding keyBinding : keyBindings) {
			ControlsListWidget.KeyBindingEntry entry = (KeyBindingEntry) KeyBindingEntryVersionHelper.createKeyBindingEntry(listWidget, keyBinding, TextWrapper.translatable(keyBinding.getTranslationKey()));
			if (entry == null) {
				Amecs.log(Level.ERROR, "An unexpected error occured during recompilation of controls list!");
				entries.clear();
				return;
			}
			entries.add(entry);
		}
		isFirstCompile = false;
	}

	private void filterChildrenList(ControlsListWidget listWidget, List<ControlsListWidget.Entry> children, KeyBindingEntryFilterSettings filterSettings) {
		String lastCat = null;
		boolean lastMatched = false;
		ControlsListWidget.Entry lastBaseEntry = null;
		boolean lastBaseEntryAdded = false;
		for (ControlsListWidget.KeyBindingEntry entry : entries) {
			KeyBinding binding = ((IKeyBindingEntry) entry).amecs$getKeyBinding();
			// only add the alternatives of a base if the base was matched itself
			if (NMUKProxy.NMUK_LOADED && lastMatched && !lastBaseEntryAdded && NMUKProxy.isAlternative(binding)) {
				children.add(entry);
				continue;
			}

			String cat = binding.getCategory();
			lastMatched = Amecs.entryMatches(entry, filterSettings);
			if (lastMatched) {
				if (!Objects.equals(cat, lastCat)) {
					children.add(listWidget.new CategoryEntry(TextWrapper.translatable(cat)));
					lastCat = cat;
				}

				// TODO: update maxKeyNameLength? Or is it irritating if the screen layout changes, when filtering?

				// if we found an alternative make sure we also add the base
				if (NMUKProxy.isAlternative(binding)) {
					if (lastBaseEntry != null && !lastBaseEntryAdded) {
						children.add(lastBaseEntry);
						lastBaseEntryAdded = true;
					}
				}

				children.add(entry);
			}

			KeyBinding base = NMUKProxy.getBase(binding);
			if (base == null && (lastBaseEntry == null || binding != ((IKeyBindingEntry) lastBaseEntry).amecs$getKeyBinding())) {
				lastBaseEntry = entry;
				lastBaseEntryAdded = false;
			}

		}
	}

	/**
	 * Actual type of 'parent' changes for mc version:
	 * <br>
	 * - 1.17 ControlsOptionsScreen
	 * <br>
	 * - 1.18 KeybindsScreen
	 *
	 * @param parent
	 * @param client
	 */
	public SearchFieldControlsListWidget(GameOptionsScreen parent, MinecraftClient client) {
		// this.parent = parent;
		// this.client = client;
		TextRenderer textRenderer = client.textRenderer;
		assert parent != null;

		textFieldWidget = TextFieldWidgetVersionHelper.createTextFieldWidget(textRenderer, ScreenVersionHelper.getWidth(parent) / 2 - 125, 0, 250, 20, "");
		textFieldWidget.setSuggestion(I18n.translate("amecs.search.placeholder"));
		textFieldWidget.setChangedListener(inputText -> {
			ControlsListWidget listWidget = ((IKeybindsScreen) parent).amecs$getControlsList();
			assert listWidget != null;
			// scroll back up to the top
			listWidget.setScrollAmount(0);
			List<ControlsListWidget.Entry> children = listWidget.children();

			if (inputText.isEmpty()) {
				textFieldWidget.setSuggestion(I18n.translate("amecs.search.placeholder"));
			} else {
				textFieldWidget.setSuggestion("");
			}

			// controls list has more or less children than we had last time
			// TODO: this is not ideal. We might NOT update if for example some external source remove one and adds one entry
			// but external changes SHOULD NOT happen anyways
			if (children.size() != lastChildrenCount) {
				if (!isFirstCompile) {
					Amecs.log(Level.INFO, "Controls search results changed externally - recompiling the list!");
				}
				recompileChildrenList(listWidget, client);
			}

			children.clear();
			children.add(this); // here we add ourself

			KeyBindingEntryFilterSettings filterSettings = KeyBindingEntryFilterSettings.parseFromInputString(inputText);

			// the method could get the children via the listWidget itself. But its more understandable this way. And about same performance
			filterChildrenList(listWidget, children, filterSettings);
			lastChildrenCount = children.size();

			if (lastChildrenCount <= 1) {
				IMutableText noResultsText = (IMutableText) TextWrapper.translatable(Amecs.MOD_ID + ".search.no_results");
				noResultsText.fmvh$setStyle(noResultsText.fmvh$getStyle().withColor(Formatting.GRAY));
				children.add(listWidget.new CategoryEntry(noResultsText));
				lastChildrenCount++;
			}
		});
	}

	@Override
	public List<? extends Element> children() {
		return Collections.singletonList(textFieldWidget);
	}

	@Override
	public boolean mouseClicked(double double_1, double double_2, int int_1) {
		return textFieldWidget.mouseClicked(double_1, double_2, int_1);
	}

	@Override
	public boolean mouseReleased(double double_1, double double_2, int int_1) {
		return textFieldWidget.mouseReleased(double_1, double_2, int_1);
	}

	@Override
	public boolean keyPressed(int int_1, int int_2, int int_3) {
		return textFieldWidget.keyPressed(int_1, int_2, int_3);
	}

	@Override
	public boolean charTyped(char char_1, int int_1) {
		return textFieldWidget.charTyped(char_1, int_1);
	}

	@Override
	public boolean changeFocus(boolean boolean_1) {
		return textFieldWidget.changeFocus(boolean_1);
	}

	@Override
	public void render(MatrixStack matrixStack, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
		textFieldWidget.y = var2;
		textFieldWidget.render(matrixStack, var6, var7, var9);
	}

	@Override
	public List<? extends Selectable> selectableChildren() {
		return Collections.singletonList(textFieldWidget);
	}
}
