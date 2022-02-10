package de.siphalor.amecs.mixin;

import static de.siphalor.amecs.impl.mixin.AmecsAPIMixinConfig.MIXIN_VERSIONED_PACKAGE;
import static de.siphalor.amecs.impl.mixin.AmecsAPIMixinConfig.prependMixinPackages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import de.siphalor.amecs.impl.version.MinecraftVersionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AmecsMixinConfig implements IMixinConfigPlugin {

	private List<String> finalAdditionalMixinClasses = new ArrayList<>();

	private List<String> additionalMixinClasses = new ArrayList<>();

	private void addMixins(String... mixinNames) {
		Collections.addAll(additionalMixinClasses, mixinNames);
	}

	private void pushMixinsToFinal() {
		finalAdditionalMixinClasses.addAll(additionalMixinClasses);
		additionalMixinClasses.clear();
	}

	@Override
	public void onLoad(String mixinPackage) {
		// TODO: add a json config file where for each mixinClassName a modID requirement can be made. Like in the fabric.mod.json#depends.
		// for now doing it in here

		// the order of the if statements is important. The highest version must be checked first
		if (MinecraftVersionHelper.IS_AT_LEAST_V1_18) {
			addMixins("MixinKeybindsScreen");
		} else {
			// Minecraft 1.17 and below
			addMixins("MixinControlsOptionsScreen");
		}

		additionalMixinClasses = prependMixinPackages(additionalMixinClasses, MIXIN_VERSIONED_PACKAGE);
		pushMixinsToFinal();
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
		return finalAdditionalMixinClasses == null ? null : (finalAdditionalMixinClasses.isEmpty() ? null : finalAdditionalMixinClasses);
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

}
