package de.siphalor.amecs.version;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.Level;

import de.klotzi111.fabricmultiversionhelper.api.mapping.MappingHelper;
import de.klotzi111.fabricmultiversionhelper.api.version.MinecraftVersionHelper;
import de.siphalor.amecs.Amecs;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.widget.EntryListWidget;

public class ParentElementVersionHelper {

	private static final Method ParentElement_children;

	static {
		String INTERMEDIARY_Screen_children = MinecraftVersionHelper.isMCVersionAtLeast("1.16") ? "method_25396" : "children";
		ParentElement_children = MappingHelper.mapAndGetMethod(ParentElement.class, INTERMEDIARY_Screen_children, List.class);
	}

	@SuppressWarnings("unchecked")
	public static List<Element> getChildren(EntryListWidget<?> listWidget) {
		try {
			return (List<Element>) ParentElement_children.invoke(listWidget);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Amecs.log(Level.ERROR, "Failed to call method \"ParentElement::children\"");
			Amecs.logException(Level.ERROR, e);
			return null;
		}
	}

}
