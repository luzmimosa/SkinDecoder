package com.fadedbytes.skindecoder.client;

import com.fadedbytes.skindecoder.client.texture.SkinTextureManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class SkinDecoderClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        KeyBinding kb = new KeyBinding("uwu.uwu.uwu", GLFW.GLFW_KEY_K, "category.uwu");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (kb.wasPressed() && client.player != null) {
                SkinTextureManager.loadSkinToImage(
                        client.player.getUuid(),
                        image -> {
                            for (int y = 0; y < 64; y++) {
                                MutableText text = Text.empty();
                                for (int x = 0; x < 64; x++) {
                                    text.append(Text.literal("■ ").withColor(
                                            image.getRed(x, y) << 16 |
                                                    image.getGreen(x, y) << 8 |
                                                    image.getBlue(x, y)
                                    ));
                                }
                                client.player.sendMessage(text);

                            }
                        }
                );
            }
        });

    }
}
