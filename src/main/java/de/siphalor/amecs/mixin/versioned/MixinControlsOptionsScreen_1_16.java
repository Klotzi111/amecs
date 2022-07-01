package de.siphalor.amecs.mixin.versioned;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.siphalor.amecs.mixinimpl.MixinControlsOptionsScreenImpl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

@Mixin(ControlsOptionsScreen.class)
public abstract class MixinControlsOptionsScreen_1_16 extends GameOptionsScreen {

	// ignored
	public MixinControlsOptionsScreen_1_16(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(
		method = "init()V",
		slice = @Slice(
			from = @At(
				value = "NEW",
				target = "(Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;Lnet/minecraft/client/MinecraftClient;)Lnet/minecraft/client/gui/screen/option/ControlsListWidget;")),
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			shift = Shift.AFTER,
			target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;keyBindingListWidget:Lnet/minecraft/client/gui/screen/option/ControlsListWidget;"))
	public void init_afterConstructListWidget(CallbackInfo ci) {
		MixinControlsOptionsScreenImpl.addSearchEntry((ControlsOptionsScreen) (Object) this);
	}

}
