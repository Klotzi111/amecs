package de.siphalor.amecs.keybinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import de.siphalor.amecs.VersionedLogicMethodHelper.ReflectionExceptionProxiedMethod;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class DropEntireStackKeyBinding extends AmecsKeyBinding implements DropItemStackTriggerListener {
	@SuppressWarnings("unused") // used via reflection
	private static final String Method_dropEntireStackLogic_PREFIX = "dropEntireStackLogic$";
	private static ReflectionExceptionProxiedMethod Method_dropEntireStackLogic;

	private static final Method ClientPlayerEntity_dropSelectedItem;

	static {
		Class<?> CLASS_FOR_dropSelectedItem = PlayerEntity.class;
		if (MinecraftVersionHelper.isMCVersionAtLeast("1.17")) {
			CLASS_FOR_dropSelectedItem = ClientPlayerEntity.class;
		}
		Class<?> returnType = MinecraftVersionHelper.isMCVersionAtLeast("1.15") ? boolean.class : ItemEntity.class;
		ClientPlayerEntity_dropSelectedItem = MappingHelper.mapAndGetMethod(CLASS_FOR_dropSelectedItem, "method_7290", returnType, boolean.class);
	}

	public DropEntireStackKeyBinding(String id, InputUtil.Type type, int code, String category, KeyModifiers defaultModifiers) {
		super(id, type, code, category, defaultModifiers);
	}

	// from minecraft code: MinecraftClient with feedback addition

	// only version 1.14 and above because we do not have mappings for lower versions of minecraft and fabric anyways
	// the logic changed in 1.14 (compared to the newer versions)
	@SuppressWarnings("unused") // used via reflection
	private static boolean dropEntireStackLogic$1_14(MinecraftClient client) {
		ClientPlayerEntity player = client.player;

		if (!player.isSpectator()) {
			// true to always drop an entire stack

			// in 1.14 this method returns 'ItemStack' but it is always null anyways
			// but because the method signature changed we need to call it via reflection
			// player.dropSelectedItem(true);
			try {
				ClientPlayerEntity_dropSelectedItem.invoke(player, true);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Amecs.log(Level.ERROR, "Failed to call method \"ClientPlayerEntity::dropSelectedItem\"");
				Amecs.logException(Level.ERROR, e);
				return false;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused") // used via reflection
	private static boolean dropEntireStackLogic$1_15(MinecraftClient client) {
		ClientPlayerEntity player = client.player;

		// true to always drop an entire stack
		if (!player.isSpectator() && player.dropSelectedItem(true)) {
			player.swingHand(Hand.MAIN_HAND);
			return true;
		}
		return false;
	}

	private static boolean dropEntireStackLogic_currentVersion(MinecraftClient client) {
		return (boolean) Method_dropEntireStackLogic.invoke(null, client);
	}

	// this method is NOT called by the InputHandlerManager (because we do not register it there) it is called from MixinMinecraftClient
	@Override
	public boolean handleDropItemStackEvent(MinecraftClient client) {
		boolean dropped = false;
		while (wasPressed()) {
			dropped |= dropEntireStackLogic_currentVersion(client);
		}
		return dropped;
	}
}
