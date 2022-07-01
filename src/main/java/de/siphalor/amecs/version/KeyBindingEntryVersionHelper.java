package de.siphalor.amecs.version;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import de.siphalor.amecs.duck.IKeyBindingEntry;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.ControlsListWidget.KeyBindingEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public class KeyBindingEntryVersionHelper {

	private static final Constructor<KeyBindingEntry> KeyBindingEntry_contructor;

	static {
		String SIGNATURE_KeyBindingEntry_contructor = null;
		if (MinecraftVersionHelper.isMCVersionAtLeast("1.16")) {
			SIGNATURE_KeyBindingEntry_contructor = MappingHelper.createSignature("(%s%s%s)V", ControlsListWidget.class, KeyBinding.class, Text.class);
		} else {
			SIGNATURE_KeyBindingEntry_contructor = MappingHelper.createSignature("(%s%s)V", ControlsListWidget.class, KeyBinding.class);
		}
		KeyBindingEntry_contructor = (Constructor<KeyBindingEntry>) MappingHelper.getConstructor(KeyBindingEntry.class, SIGNATURE_KeyBindingEntry_contructor);
	}

	public static IKeyBindingEntry createKeyBindingEntry(ControlsListWidget listWidget, KeyBinding binding, Text bindingName) {
		try {
			Object[] instanceArgs = null;
			if (MinecraftVersionHelper.isMCVersionAtLeast("1.16")) {
				instanceArgs = new Object[] {listWidget, binding, bindingName};
			} else {
				// if we are below minecraft 1.16 we can not parse the text to the constructors ...
				instanceArgs = new Object[] {listWidget, binding};
			}
			IKeyBindingEntry ret = (IKeyBindingEntry) KeyBindingEntry_contructor.newInstance(instanceArgs);
			if (!MinecraftVersionHelper.isMCVersionAtLeast("1.16")) {
				// ... so we set it afterwards and hope for the best
				ret.amecs$setBindingName(bindingName);
			}
			return ret;
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			Amecs.log(Level.ERROR, "Failed to create new instance of \"KeyBindingEntry\"");
			Amecs.logException(Level.ERROR, e);
		}
		return null;
	}

}
