package com.legacy.ender_chest_horses.compat.jei;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.client.gui.EnderChestHorseScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
@SuppressWarnings("unused")
public class EnderChestedJeiPlugin implements IModPlugin
{
    /**
     * The unique ID for this mod plugin.
     * The namespace should be your mod's modId.
     */
    @Override
    public ResourceLocation getPluginUid()
    {
        return EnderChestedMod.locate("jei_plugin");
    }

    /**
     * Register various GUI-related things for your mod.
     * This includes adding clickable areas in your guis to open JEI,
     * and adding areas on the screen that JEI should avoid drawing.
     *
     * @param registration
     */
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addGuiContainerHandler(EnderChestHorseScreen.class, new JEIOverlayWrapper());
    }
}
