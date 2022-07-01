package de.siphalor.amecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import de.klotzi111.fabricmultiversionhelper.api.text.TextWrapper;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyBindingUtils;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.api.input.InputEventHandler;
import de.siphalor.amecs.api.input.InputHandlerManager;
import de.siphalor.amecs.impl.duck.IKeyBindingEntry;
import de.siphalor.amecs.keybinding.DropEntireStackKeyBinding;
import de.siphalor.amecs.keybinding.HotbarScrollKeyBinding;
import de.siphalor.amecs.keybinding.SkinLayerKeyBinding;
import de.siphalor.amecs.keybinding.ToggleAutoJumpKeyBinding;
import de.siphalor.amecs.mixin.ControlsListWidgetKeyBindingEntryAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * Main class of Amecs (Alt-Meta-Escape-Control-Shift)
 */
@Environment(EnvType.CLIENT)
public class Amecs implements ClientModInitializer {
	/**
	 * The mod id of Amecs
	 */
	public static final String MOD_ID = "amecs";
	public static final String MOD_NAME_SHORT = "Amecs";

	private static final String LOG_PREFIX = "[" + MOD_NAME_SHORT + "] ";
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String SKIN_LAYER_CATEGORY = MOD_ID + ".key.categories.skin_layers";
	private static final String MOVEMENT_CATEGORY = "key.categories.movement";
	private static final String INVENTORY_CATEGORY = "key.categories.inventory";

	// keybindings
	/**
	 * this list is that we can (but not actually doing right now) remove/unregister or re-register them at a later point in time
	 */
	public static List<KeyBinding> ALL_KEYBINDINGS = new ArrayList<>();

	public static DropEntireStackKeyBinding KEYBINDING_DROP_STACK;
	public static KeyBinding ESCAPE_KEYBINDING;
	// -keybindings

	private static String makeKeyID(String keyName) {
		return "key." + MOD_ID + "." + keyName;
	}

	@Override
	public void onInitializeClient() {
		VersionedLogicMethodHelper.initLogicMethodsForClasses(Arrays.asList(HotbarScrollKeyBinding.class, DropEntireStackKeyBinding.class, ToggleAutoJumpKeyBinding.class));

		createKeyBindings();
	}

	// keybindings
	private static boolean registerKeyBinding(KeyBinding keyBinding) {
		if (KeyBindingHelper.registerKeyBinding(keyBinding) == keyBinding) {
			ALL_KEYBINDINGS.add(keyBinding);
			return true;
		}
		return false;
	}

	private static <K extends KeyBinding & InputEventHandler> void registerKeyBindingWithHandler(K keyBinding) {
		if (registerKeyBinding(keyBinding)) {
			InputHandlerManager.registerInputEventHandler(keyBinding);
		}
	}

	private static void createKeyBindings() {
		// auto jump
		registerKeyBindingWithHandler(new ToggleAutoJumpKeyBinding(makeKeyID("toggle_auto_jump"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, MOVEMENT_CATEGORY, new KeyModifiers()));

		// skin layers
		Arrays.stream(PlayerModelPart.values())
			.map(playerModelPart -> new SkinLayerKeyBinding(makeKeyID("toggle_" + playerModelPart.getName().toLowerCase(Locale.ENGLISH)), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, SKIN_LAYER_CATEGORY,
				playerModelPart))
			.forEach(Amecs::registerKeyBindingWithHandler);

		// hotbar scroll
		registerKeyBindingWithHandler(new HotbarScrollKeyBinding(makeKeyID("hotbar.scroll.up"), InputUtil.Type.MOUSE, KeyBindingUtils.MOUSE_SCROLL_UP, INVENTORY_CATEGORY, new KeyModifiers(), true));
		registerKeyBindingWithHandler(new HotbarScrollKeyBinding(makeKeyID("hotbar.scroll.down"), InputUtil.Type.MOUSE, KeyBindingUtils.MOUSE_SCROLL_DOWN, INVENTORY_CATEGORY, new KeyModifiers(), false));

		// drop entire stack
		// we intentionally do not register the drop stack keybinding for input handling because it is called from MixinMinecraftClient
		KEYBINDING_DROP_STACK = new DropEntireStackKeyBinding(makeKeyID("drop.stack"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Q, INVENTORY_CATEGORY, new KeyModifiers().setControl(true));
		registerKeyBinding(KEYBINDING_DROP_STACK);

		// we intentionally do not register the escape keybinding for input handling because it is checked before normal input checking and is translated to GLFW_KEY_ESCAPE
		ESCAPE_KEYBINDING = new AmecsKeyBinding(makeKeyID("alternative_escape"), InputUtil.Type.KEYSYM, -1, "key.categories.ui", new KeyModifiers());
		registerKeyBinding(ESCAPE_KEYBINDING);
	}
	// -keybindings

	public static void sendToggleMessage(PlayerEntity playerEntity, boolean value, Text option) {
		playerEntity.sendMessage(TextWrapper.translatable("amecs.toggled." + (value ? "on" : "off"), option), true);
	}

	// controls gui search
	public static boolean entryKeyMatches(ControlsListWidget.KeyBindingEntry entry, String keyFilter) {
		if (keyFilter == null) {
			return true;
		}
		switch (keyFilter) {
			case "":
				return ((IKeyBindingEntry) entry).amecs$getKeyBinding().isUnbound();
			case "%":
				return ((ControlsListWidgetKeyBindingEntryAccessor) entry).getEditButton().getMessage().getStyle().getColor() == TextColor.fromFormatting(Formatting.RED);
			default:
				return StringUtils.containsIgnoreCase(((IKeyBindingEntry) entry).amecs$getKeyBinding().getBoundKeyLocalizedText().getString(), keyFilter);
		}
	}

	public static boolean entryMatches(ControlsListWidget.KeyBindingEntry entry, KeyBindingEntryFilterSettings filterSettings) {
		if (!Amecs.entryKeyMatches(entry, filterSettings.keyFilter)) {
			return false;
		}

		KeyBinding binding = ((IKeyBindingEntry) entry).amecs$getKeyBinding();
		// we do not check whether the the last category is the same as the current before we check if it contains
		// because checking the quality is not really cheaper and check both if they are not equal is overhead
		boolean categoryContains = StringUtils.containsIgnoreCase(I18n.translate(binding.getCategory()), filterSettings.searchText);

		String entryName = TextWrapper.getAsString(((de.siphalor.amecs.duck.IKeyBindingEntry) entry).amecs$getBindingName());
		// this fixes alternative keybindings from nmuk
		// without this they are searched by their untranslateable translation key
		// we could also search alternatives by thei parent but this way you can search for only alternatives
		// in order to know to which binding they belong the filter method is changed
		if (!entryName.equals("    ->")) {
			entryName = I18n.translate(binding.getTranslationKey());
		}
		boolean keyNameContains = filterSettings.searchText == null || StringUtils.containsIgnoreCase(entryName, filterSettings.searchText);

		return (categoryContains || keyNameContains);
	}

	public static void log(Level level, String message) {
		LOGGER.log(level, LOG_PREFIX + message);
	}

	public static void logException(Level level, Throwable e) {
		LOGGER.catching(level, e);
	}
}
