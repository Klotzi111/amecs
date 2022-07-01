package de.siphalor.amecs.mixin.versioned;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import de.klotzi111.fabricmultiversionhelper.api.text.TextWrapper;
import de.siphalor.amecs.duck.IKeyBindingEntry;
import net.minecraft.client.gui.screen.option.ControlsListWidget.KeyBindingEntry;
import net.minecraft.text.Text;

@Mixin(KeyBindingEntry.class)
public abstract class MixinKeyBindingEntry_1_14 implements IKeyBindingEntry {

	@Mutable
	@Shadow(aliases = {"field_2741", "bindingName"}, remap = false)
	@Final
	private String bindingName;

	@Override
	public void amecs$setBindingName(Text bindingName) {
		this.bindingName = bindingName.getString();
	}

	@Override
	public Text amecs$getBindingName() {
		return TextWrapper.literal(bindingName);
	}

}
