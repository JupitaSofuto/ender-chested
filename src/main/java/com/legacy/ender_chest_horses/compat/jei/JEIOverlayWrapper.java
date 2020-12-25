package com.legacy.ender_chest_horses.compat.jei;

import com.legacy.ender_chest_horses.client.gui.EnderChestHorseScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.inventory.container.Container;

import java.util.ArrayList;
import java.util.List;

public class JEIOverlayWrapper<C extends Container> implements IGuiContainerHandler<EnderChestHorseScreen>
{
    /**
     * Give JEI information about extra space that the {@link ContainerScreen} takes up.
     * Used for moving JEI out of the way of extra things like gui tabs.
     *
     * @param containerScreen
     * @return the space that the gui takes up besides the normal rectangle defined by {@link ContainerScreen}.
     */
    @Override
    public List<Rectangle2d> getGuiExtraAreas(EnderChestHorseScreen containerScreen)
    {
        List<Rectangle2d> areas = new ArrayList<>();
        int x = containerScreen.getGuiLeft() + 90;
        int y = containerScreen.getGuiTop();
        int height = 80;
        areas.add(new Rectangle2d(x, y, 166, height));
        return areas;
    }
}
