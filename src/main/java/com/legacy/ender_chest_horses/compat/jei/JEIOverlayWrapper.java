package com.legacy.ender_chest_horses.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.legacy.ender_chest_horses.client.gui.EnderChestHorseScreen;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class JEIOverlayWrapper<C extends AbstractContainerMenu> implements IGuiContainerHandler<EnderChestHorseScreen>
{
	/**
	 * Give JEI information about extra space that the {@link ContainerScreen} takes
	 * up. Used for moving JEI out of the way of extra things like gui tabs.
	 *
	 * @param containerScreen
	 * @return the space that the gui takes up besides the normal rectangle defined
	 *         by {@link ContainerScreen}.
	 */
	@Override
	public List<Rect2i> getGuiExtraAreas(EnderChestHorseScreen containerScreen)
	{
		List<Rect2i> areas = new ArrayList<>();
		int x = containerScreen.getGuiLeft() + 90;
		int y = containerScreen.getGuiTop();
		int height = 80;
		areas.add(new Rect2i(x, y, 166, height));
		return areas;
	}
}
