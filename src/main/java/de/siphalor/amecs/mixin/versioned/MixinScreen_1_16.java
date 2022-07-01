package de.siphalor.amecs.mixin.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.siphalor.amecs.mixinimpl.MixinScreenImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public abstract class MixinScreen_1_16 {

	@Redirect(
		method = "resize(Lnet/minecraft/client/MinecraftClient;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V"))
	public void resize(Screen screen, MinecraftClient client, int width, int height) {
		MixinScreenImpl.resize(screen, client, width, height);
	}

}
