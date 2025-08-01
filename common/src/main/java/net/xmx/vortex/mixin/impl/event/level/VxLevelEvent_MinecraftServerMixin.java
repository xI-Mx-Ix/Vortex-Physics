package net.xmx.vortex.mixin.impl.event.level;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.xmx.vortex.event.api.VxLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class VxLevelEvent_MinecraftServerMixin {

    @Inject(method = "createLevels", at = @At("RETURN"))
    private void onCreateLevels(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        for (ServerLevel level : server.getAllLevels()) {
            if (level != null) {
                VxLevelEvent.Load.EVENT.invoker().onLevelLoad(new VxLevelEvent.Load(level));
            }
        }
    }

    @Inject(method = "stopServer", at = @At("RETURN"))
    private void onStopServer(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        for (ServerLevel level : server.getAllLevels()) {
            if (level != null) {
                VxLevelEvent.Unload.EVENT.invoker().onLevelUnload(new VxLevelEvent.Unload(level));
            }
        }
    }
}
