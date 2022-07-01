package de.siphalor.amecs.keybinding;

import de.siphalor.amecs.Amecs;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.api.input.InputEventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.InputUtil;

public class SkinLayerKeyBinding extends AmecsKeyBinding implements InputEventHandler {
	private final PlayerModelPart playerModelPart;

	public SkinLayerKeyBinding(String id, InputUtil.Type type, int code, String category, PlayerModelPart playerModelPart) {
		super(id, type, code, category, new KeyModifiers());
		this.playerModelPart = playerModelPart;
	}

	@Override
	public void handleInput(MinecraftClient client) {
		while (wasPressed()) {
			client.options.togglePlayerModelPart(playerModelPart, !client.options.isPlayerModelPartEnabled(playerModelPart));
			Amecs.sendToggleMessage(client.player, client.options.isPlayerModelPartEnabled(playerModelPart), playerModelPart.getOptionName());
		}
	}
}
