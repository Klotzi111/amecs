package de.siphalor.amecs.mixinimpl;

import java.util.List;

import de.siphalor.amecs.gui.SearchFieldControlsListWidget;
import de.siphalor.amecs.impl.duck.IKeybindsScreen;
import de.siphalor.amecs.impl.version.KeybindsScreenVersionHelper;
import de.siphalor.amecs.version.ParentElementVersionHelper;
import de.siphalor.amecs.version.ScreenVersionHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsListWidget.Entry;

public class MixinScreenImpl {

	public static void resize(Screen screen, MinecraftClient client, int width, int height) {
		// screen can only be non-null here
		if (KeybindsScreenVersionHelper.ACTUAL_KEYBINDS_SCREEN_CLASS.isAssignableFrom(screen.getClass())) {
			ControlsListWidget listWidget = ((IKeybindsScreen) screen).amecs$getControlsList();
			// the first element might not be the search widget
			// for example when controlling (mod) is loaded and it uses its own KeybindsScreen but we do (and want) not add it there

			List<? extends Element> children = ParentElementVersionHelper.getChildren(listWidget);
			Entry entry = children.size() > 0 ? (Entry) children.get(0) : null;
			if (entry instanceof SearchFieldControlsListWidget) {
				SearchFieldControlsListWidget searchWidget = (SearchFieldControlsListWidget) entry;
				String oldSearchText = searchWidget.textFieldWidget.getText();
				ScreenVersionHelper.init(screen, client, width, height);
				listWidget = ((IKeybindsScreen) screen).amecs$getControlsList();
				searchWidget = (SearchFieldControlsListWidget) children.get(0);
				searchWidget.textFieldWidget.setText(oldSearchText);
				return;
			}
		}
		ScreenVersionHelper.init(screen, client, width, height);
	}

}
