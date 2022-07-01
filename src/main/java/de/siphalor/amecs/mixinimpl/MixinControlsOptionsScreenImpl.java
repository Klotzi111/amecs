package de.siphalor.amecs.mixinimpl;

import de.siphalor.amecs.gui.SearchFieldControlsListWidget;
import de.siphalor.amecs.impl.duck.IKeybindsScreen;
import de.siphalor.amecs.version.ParentElementVersionHelper;
import de.siphalor.amecs.version.ScreenVersionHelper;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;

public class MixinControlsOptionsScreenImpl {

	public static void addSearchEntry(ControlsOptionsScreen screen) {
		ControlsListWidget listWidget = ((IKeybindsScreen) screen).amecs$getControlsList();

		@SuppressWarnings("resource")
		SearchFieldControlsListWidget searchEntry = new SearchFieldControlsListWidget(screen, ScreenVersionHelper.getClient(screen));

		ParentElementVersionHelper.getChildren(listWidget).add(0, searchEntry);
	}

}
