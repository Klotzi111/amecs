package de.siphalor.amecs.mixin.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.siphalor.amecs.mixinimpl.MixinScreenImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public abstract class MixinScreen_1_14 {

	@Redirect(
		method = "resize(Lnet/minecraft/client/MinecraftClient;II)V",
		remap = false,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/class_437;init(Lnet/minecraft/class_310;II)V",
			remap = false))
	public void resize(Screen screen, MinecraftClient client, int width, int height) {
		MixinScreenImpl.resize(screen, client, width, height);
	}

}
