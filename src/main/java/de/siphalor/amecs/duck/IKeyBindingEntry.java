package de.siphalor.amecs.duck;

import net.minecraft.text.Text;

public interface IKeyBindingEntry {
	Text amecs$getBindingName();

	void amecs$setBindingName(Text bindingName);
}
