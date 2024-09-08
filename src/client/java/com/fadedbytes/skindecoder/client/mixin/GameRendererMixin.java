package com.fadedbytes.skindecoder.client.mixin;

import com.fadedbytes.skindecoder.client.texture.SkinTextureManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void checkSkinTextureManagerTasks(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        SkinTextureManager.checkAndRunRenderThreadTasks();
    }

}
