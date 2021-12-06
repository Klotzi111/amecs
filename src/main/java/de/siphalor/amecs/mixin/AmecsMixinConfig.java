package de.siphalor.amecs.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import de.siphalor.amecs.MinecraftVersionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AmecsMixinConfig implements IMixinConfigPlugin {

	private List<String> additionalMixinClasses = null;

	private static final String MIXIN_VERSIONED_PACKAGE = "versioned";

	private static String prependMixinPackage(String className) {
		return MIXIN_VERSIONED_PACKAGE + "." + className;
	}

	private static List<String> prependMixinPackages(List<String> classNames) {
		List<String> ret = new ArrayList<>(classNames.size());
		for (String className : classNames) {
			ret.add(prependMixinPackage(className));
		}
		return ret;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onLoad(String mixinPackage) {
		// TODO: add a json config file where for each mixinClassName a modID requirement can be made. Like in the fabric.mod.json#depends.
		// for now doing it in here

		// the order of the if statements is important. The highest version must be checked first
		// we need to use the deprecated compareTo method because older minecraft versions do not support the new/non deprecated way
		if (MinecraftVersionHelper.SEMANTIC_MINECRAFT_VERSION.compareTo(MinecraftVersionHelper.V1_18) >= 0) {
			additionalMixinClasses = Arrays.asList("KeybindsScreenAccessor", "MixinKeybindsScreen");
		} else if (MinecraftVersionHelper.SEMANTIC_MINECRAFT_VERSION.compareTo(MinecraftVersionHelper.V1_17) >= 0) {
			additionalMixinClasses = Arrays.asList("ControlsOptionsScreenAccessor", "MixinControlsOptionsScreen");
		}

		additionalMixinClasses = prependMixinPackages(additionalMixinClasses);
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return additionalMixinClasses == null ? null : (additionalMixinClasses.isEmpty() ? null : additionalMixinClasses);
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

}
