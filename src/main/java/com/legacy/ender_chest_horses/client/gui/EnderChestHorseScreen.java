package com.legacy.ender_chest_horses.client.gui;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;

public class EnderChestHorseScreen extends AbstractContainerScreen<EnderHorseInventoryMenu>
{
	private static final ResourceLocation ENDER_HORSE_TEXTURE = EnderChestedMod.locate("textures/gui/ender_chest_horse.png");
	private final AbstractHorse horse;

	public EnderChestHorseScreen(EnderHorseInventoryMenu container, Inventory playerInv, Component text)
	{
		super(container, playerInv, text);
		this.horse = container.horse;
		++this.imageHeight;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y)
	{
		this.font.draw(matrixStack, this.title, (float) this.titleLabelX + 240 - this.font.width(this.title.getString()), (float) this.titleLabelY, 4210752);
		this.font.draw(matrixStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
		this.font.draw(matrixStack, Component.translatable("container.enderchest"), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, ENDER_HORSE_TEXTURE);

		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth + 80, this.imageHeight - 1);

		if (this.horse != null)
		{
			// 0, 166
			// saddled
			if (this.horse.isSaddleable())
				this.blit(matrixStack, i + 178, j + 35 - 18, 0 + 18, 166, 18, 18);

			if (this.horse.canWearArmor())
				this.blit(matrixStack, i + 178, j + 35, 0, 166, 18, 18);

			InventoryScreen.renderEntityInInventory(i + 222, j + 61, 18, (float) (i + 222) - x, (float) (j + 75 - 61) - y, this.horse);
		}
	}
}