package de.siphalor.amecs.mixin.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.SpectatorHud;

// set very high "priority" because we want to do it late after all other mods did their thing
@Mixin(value = Mouse.class, priority = 2000)
public abstract class MixinMouse_1_18_2 {

	// we redirect the following methods to do nothing because we made a keybinding for this and we do not want that these actions are done double

	@Redirect(
		method = "onMouseScroll",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/SpectatorHud;cycleSlot(I)V"))
	private void redirect_cycleSlot(SpectatorHud hud, int offset) {
		// do nothing
	}

}
