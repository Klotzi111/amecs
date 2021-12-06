package de.siphalor.amecs.mixin.versioned;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;

@Mixin(KeybindsScreen.class)
public interface KeybindsScreenAccessor {
	@Accessor
	ControlsListWidget getControlsList();
}
