package com.fadedbytes.skindecoder.client.texture;

import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class SkinTextureManager {

    private static final int SKIN_HEIGHT = 64;
    private static final int SKIN_WIDTH  = 64;

    private static final ConcurrentLinkedQueue<Runnable> renderThreadTasks = new ConcurrentLinkedQueue<>();

    public static void loadSkinToImage(UUID playerUUID, Consumer<NativeImage> onLoaded) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            renderThreadTasks.add(() -> loadSkinToImage(playerUUID, onLoaded));
        } else {
            MinecraftClient client = MinecraftClient.getInstance();
            ProfileResult profileResult = client.getSessionService().fetchProfile(playerUUID, false);
            SkinTextures skinTextures = profileResult != null
                    ? client.getSkinProvider().getSkinTextures(profileResult.profile())
                    : DefaultSkinHelper.getSkinTextures(playerUUID);

            AbstractTexture texture = client.getTextureManager().getTexture(skinTextures.texture());
            if (texture instanceof ResourceTexture) {
                RenderSystem.bindTexture(texture.getGlId());

                NativeImage skinImage = new NativeImage(SKIN_WIDTH, SKIN_HEIGHT, false);
                skinImage.loadFromTextureImage(0, false);

                onLoaded.accept(skinImage);
            }
        }
    }

    public static void checkAndRunRenderThreadTasks() {
        if (!renderThreadTasks.isEmpty()) {
            renderThreadTasks.poll().run();
        }
    }

}
