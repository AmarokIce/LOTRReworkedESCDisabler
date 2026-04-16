package club.someoneice.lotrtweakers.mixin;

import lotr.client.gui.LOTRGuiDownloadTerrain;
import lotr.client.gui.LOTRGuiMainMenu;
import lotr.common.LOTRConfig;
import lotr.common.world.LOTRWorldProvider;
import lotr.core.handler.LOTRGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LOTRGuiHandler.class)
public class MixinGuiHandler {
  @Inject(method = "onGuiOpen", at = @At("HEAD"), remap = false, cancellable = true)
  public void onGuiOpen(GuiOpenEvent event, CallbackInfo ci) {
    Minecraft mc = Minecraft.getMinecraft();
    GuiScreen gui = event.gui;
    if (LOTRConfig.customMainMenu && gui != null && gui.getClass() == GuiMainMenu.class) {
      event.gui = gui = new LOTRGuiMainMenu();
    }

    if (gui != null && gui.getClass() == GuiDownloadTerrain.class) {
      WorldProvider provider = mc.theWorld.provider;
      if (provider instanceof LOTRWorldProvider) {
        event.gui = new LOTRGuiDownloadTerrain(mc.thePlayer.sendQueue);
      }
    }

    ci.cancel();
  }
}
