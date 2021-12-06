package de.siphalor.amecs.compat;

import java.util.Collections;
import java.util.List;

import de.siphalor.nmuk.api.NMUKAlternatives;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;

public class NMUKProxy {

	public static final boolean NMUK_LOADED = FabricLoader.getInstance().isModLoaded("nmuk");

	public static boolean isAlternative(KeyBinding binding) {
		if (!NMUK_LOADED) {
			return false;
		}
		return NMUKAlternatives.isAlternative(binding);
	}

	public static List<KeyBinding> getAlternatives(KeyBinding binding) {
		if (!NMUK_LOADED) {
			return Collections.emptyList();
		}
		return NMUKAlternatives.getAlternatives(binding);
	}

	public static KeyBinding getBase(KeyBinding binding) {
		if (!NMUK_LOADED) {
			return null;
		}
		return NMUKAlternatives.getBase(binding);
	}

}
