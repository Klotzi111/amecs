package de.siphalor.amecs.version;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ScreenVersionHelper {

	private static final Field Screen_client;
	private static final Field Screen_width;
	private static final Method Screen_init;

	static {
		String INTERMEDIARY_Screen_client = MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? "field_22787" : "minecraft";
		Screen_client = MappingHelper.mapAndGetField(Screen.class, INTERMEDIARY_Screen_client, MinecraftClient.class);
		String INTERMEDIARY_Screen_width = MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? "field_22789" : "width";
		Screen_width = MappingHelper.mapAndGetField(Screen.class, INTERMEDIARY_Screen_width, int.class);
		String INTERMEDIARY_Screen_init = MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? "method_25423" : "init";
		Screen_init = MappingHelper.mapAndGetMethod(Screen.class, INTERMEDIARY_Screen_init, void.class, MinecraftClient.class, int.class, int.class);
	}

	public static MinecraftClient getClient(Screen screen) {
		try {
			return (MinecraftClient) Screen_client.get(screen);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Amecs.log(Level.ERROR, "Failed to get field \"Screen::client\"");
			Amecs.logException(Level.ERROR, e);
			return null;
		}
	}

	public static int getWidth(Screen screen) {
		try {
			return Screen_width.getInt(screen);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Amecs.log(Level.ERROR, "Failed to get field \"Screen::width\"");
			Amecs.logException(Level.ERROR, e);
			return 0;
		}
	}

	public static void init(Screen screen, MinecraftClient client, int width, int height) {
		try {
			Screen_init.invoke(screen, client, width, height);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Amecs.log(Level.ERROR, "Failed to call method \"Screen::init\"");
			Amecs.logException(Level.ERROR, e);
		}
	}

}
