package de.siphalor.amecs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.siphalor.amecs.gui.SearchFieldControlsListWidget;
import de.siphalor.amecs.impl.duck.IKeybindsScreen;
import de.siphalor.amecs.impl.version.KeybindsScreenVersionHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsListWidget.Entry;

@Mixin(Screen.class)
public abstract class MixinScreen {

	@Redirect(
		method = "resize(Lnet/minecraft/client/MinecraftClient;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V"))
	public void resize(Screen screen, MinecraftClient client, int width, int height) {
		// screen can only be non-null here
		if (KeybindsScreenVersionHelper.ACTUAL_KEYBINDS_SCREEN_CLASS.isAssignableFrom(screen.getClass())) {
			ControlsListWidget listWidget = ((IKeybindsScreen) screen).amecs$getControlsList();
			// the first element might not be the search widget
			// for example when controlling (mod) is loaded and it uses its own KeybindsScreen but we do (and want) not add it there
			Entry entry = listWidget.children().size() > 0 ? listWidget.children().get(0) : null;
			if (entry instanceof SearchFieldControlsListWidget) {
				SearchFieldControlsListWidget searchWidget = (SearchFieldControlsListWidget) entry;
				String oldSearchText = searchWidget.textFieldWidget.getText();
				screen.init(client, width, height);
				listWidget = ((IKeybindsScreen) screen).amecs$getControlsList();
				searchWidget = (SearchFieldControlsListWidget) listWidget.children().get(0);
				searchWidget.textFieldWidget.setText(oldSearchText);
				return;
			}
		}
		screen.init(client, width, height);
	}
}
