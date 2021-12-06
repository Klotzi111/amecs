package de.siphalor.amecs.gui;

import de.siphalor.amecs.MinecraftVersionHelper;
import de.siphalor.amecs.mixin.versioned.ControlsOptionsScreenAccessor;
import de.siphalor.amecs.mixin.versioned.KeybindsScreenAccessor;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;

@SuppressWarnings("deprecation")
public class ControlsListWidgetHelper {

	public static final Class<?> KEYBINDING_SEARCH_ENTRY_PARENT_CLASS;

	private static final Class<?> KeybindsScreen_class;

	static {
		Class<?> local_KEYBINDING_SEARCH_ENTRY_PARENT_CLASS = ControlsOptionsScreen.class;
		if (MinecraftVersionHelper.SEMANTIC_MINECRAFT_VERSION.compareTo(MinecraftVersionHelper.V1_18) >= 0) {
			KeybindsScreen_class = KeybindsScreen.class;
			local_KEYBINDING_SEARCH_ENTRY_PARENT_CLASS = KeybindsScreen_class;
		} else {
			KeybindsScreen_class = null;
		}
		KEYBINDING_SEARCH_ENTRY_PARENT_CLASS = local_KEYBINDING_SEARCH_ENTRY_PARENT_CLASS;
	}

	private static boolean isClass(Class<?> actualClass, Class<?> classShouldBe) {
		if (classShouldBe == null || actualClass == null) {
			return false;
		}
		return classShouldBe.equals(actualClass);
	}

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
