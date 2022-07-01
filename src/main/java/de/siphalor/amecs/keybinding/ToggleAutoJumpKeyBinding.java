package de.siphalor.amecs.keybinding;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.text.TextWrapper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import de.siphalor.amecs.VersionedLogicMethodHelper.ReflectionExceptionProxiedMethod;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.api.input.InputEventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;

public class ToggleAutoJumpKeyBinding extends AmecsKeyBinding implements InputEventHandler {
	@SuppressWarnings("unused") // used via reflection
	private static final String Method_toggleAutoJump_PREFIX = "toggleAutoJump$";
	private static ReflectionExceptionProxiedMethod Method_toggleAutoJump;

	private static final Field GameOptions_autoJump;

	static {
		if (!MinecraftVersionHelper.isMCVersionAtLeast("1.19")) {
			GameOptions_autoJump = MappingHelper.mapAndGetField(GameOptions.class, "field_1848", boolean.class);
		} else {
			GameOptions_autoJump = null;
		}
	}

	public ToggleAutoJumpKeyBinding(String id, InputUtil.Type type, int code, String category, KeyModifiers defaultModifiers) {
		super(id, type, code, category, defaultModifiers);
	}

	// only version 1.14 and above because we do not have mappings for lower versions of minecraft and fabric anyways
	@SuppressWarnings("unused") // used via reflection
	private static boolean toggleAutoJump$1_14(GameOptions options) {
		try {
			boolean newValue = !GameOptions_autoJump.getBoolean(options);
			GameOptions_autoJump.setBoolean(options, newValue);
			return newValue;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Amecs.log(Level.ERROR, "Failed to get field \"GameOptions::autoJump\"");
			Amecs.logException(Level.ERROR, e);
			return false;
		}
	}

	@SuppressWarnings("unused") // used via reflection
	private static boolean toggleAutoJump$1_19(GameOptions options) {
		SimpleOption<Boolean> autoJump = options.getAutoJump();
		boolean newValue = !autoJump.getValue();
		autoJump.setValue(newValue);
		return newValue;
	}

	private static boolean toggleAutoJump_currentVersion(GameOptions options) {
		return (boolean) Method_toggleAutoJump.invoke(null, options);
	}

	@Override
	public void handleInput(MinecraftClient client) {
		while (wasPressed()) {
			boolean autoJumpEnabled = toggleAutoJump_currentVersion(client.options);
			Amecs.sendToggleMessage(client.player, autoJumpEnabled, TextWrapper.translatable("amecs.toggled.auto_jump"));
		}
	}
}
