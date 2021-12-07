package de.siphalor.amecs.gui;

import static de.siphalor.amecs.impl.version.KeybindsScreenVersionHelper.KeybindsScreen_class;
import static de.siphalor.amecs.impl.version.KeybindsScreenVersionHelper.isClass;

import de.siphalor.amecs.mixin.versioned.ControlsOptionsScreenAccessor;
import de.siphalor.amecs.mixin.versioned.KeybindsScreenAccessor;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;

public class ControlsListWidgetHelper {

	/**
	 * Actual type of 'parent' changes for mc version:
	 * <br>
	 * - 1.17 ControlsOptionsScreen
	 * <br>
	 * - 1.18 KeybindsScreen
	 *
	 * @param parent
	 * @return ControlsListWidget
	 */
	public static ControlsListWidget getControlsListWidgetFromParent(GameOptionsScreen parent) {
		Class<?> actualClass = parent.getClass();
		if (isClass(KeybindsScreen_class, actualClass)) {
			// in mc version 1.18
			return ((KeybindsScreenAccessor) parent).getControlsList();
		} else if (isClass(ControlsOptionsScreen.class, actualClass)) {
			// in mc version 1.17
			return ((ControlsOptionsScreenAccessor) parent).getKeyBindingListWidget();
		}
		return null;
	}

}
