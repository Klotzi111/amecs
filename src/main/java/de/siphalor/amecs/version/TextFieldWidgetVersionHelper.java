package de.siphalor.amecs.version;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.text.TextWrapper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldWidgetVersionHelper {

	private static final Constructor<TextFieldWidget> TextFieldWidget_contructor;

	static {
		Class<?> argumentClass = MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? Text.class : String.class;
		String SIGNATURE_TextFieldWidget_contructor = MappingHelper.createSignature("(%sIIII%s)V", TextRenderer.class, argumentClass);
		TextFieldWidget_contructor = (Constructor<TextFieldWidget>) MappingHelper.getConstructor(TextFieldWidget.class, SIGNATURE_TextFieldWidget_contructor);
	}

	public static TextFieldWidget createTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, String text) {
		try {
			Object[] instanceArgs = new Object[] {textRenderer, x, y, width, height, MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? TextWrapper.literal(text) : text};
			return TextFieldWidget_contructor.newInstance(instanceArgs);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			Amecs.log(Level.ERROR, "Failed to create new instance of \"TextFieldWidget\"");
			Amecs.logException(Level.ERROR, e);
		}
		return null;
	}

}
