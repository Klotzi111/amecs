package de.siphalor.amecs.mixin.versioned;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

@Mixin(ControlsOptionsScreen.class)
public abstract class MixinControlsOptionsScreen_1_14 extends GameOptionsScreen {

	// ignored
	public MixinControlsOptionsScreen_1_14(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(
		method = "init()V",
		remap = false,
		slice = @Slice(
			from = @At(
				value = "NEW",
				target = "(Lnet/minecraft/class_458;Lnet/minecraft/class_310;)Lnet/minecraft/class_459;" // (Lnet/minecraft/client/gui/screen/options/ControlsOptionsScreen;Lnet/minecraft/client/MinecraftClient;)Lnet/minecraft/client/gui/screen/options/ControlsListWidget;
			)),
		at = @At(
			value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			shift = Shift.AFTER,
			target = "Lnet/minecraft/class_458;field_2728:Lnet/minecraft/class_459;" // Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;keyBindingListWidget:Lnet/minecraft/client/gui/screen/option/ControlsListWidget;
		))
	public void init_afterConstructListWidget(CallbackInfo ci) {
		// FIXME: This does not work because methods inside SearchFieldControlsListWidget implement the methods from the interface and so have intermediary names
		// but mc < 1.16 has plain intermediary names and does not match. Also the method signatures are different
		// we need multiple source sets that compile a class with 2 different mc mappings

		// MixinControlsOptionsScreenImpl.addSearchEntry((ControlsOptionsScreen) (Object) this);
	}

}
