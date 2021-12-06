package de.siphalor.amecs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerInventory;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory {

	@Shadow
	public int selectedSlot;

	private static int modAbs(int i, int m) {
		return ((i % m) + m) % m;
	}

	// TODO: add scroll speed gui setting
	// this fixes the weird vanilla behavior that was possible slow but definitely annoying because scroll speed was limited if event callbacks hang for some reason
	@Overwrite
	public void scrollInHotbar(double scrollAmount) {
		selectedSlot = modAbs(selectedSlot - (int) scrollAmount, 9);
	}
}
