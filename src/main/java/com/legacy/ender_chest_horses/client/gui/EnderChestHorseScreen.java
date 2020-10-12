package com.legacy.ender_chest_horses.client.gui;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderChestHorseScreen extends ContainerScreen<EnderHorseInventoryContainer>
{
	private static final ResourceLocation GUI_TEXTURE = EnderChestedMod.locate("textures/gui/ender_chest_horse.png");
	private final AbstractHorseEntity horse;

	public EnderChestHorseScreen(EnderHorseInventoryContainer container, PlayerInventory playerInv, ITextComponent text)
	{
		super(container, playerInv, text);
		this.horse = container.horse;
		++this.ySize;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y)
	{
		this.font.func_243248_b(matrixStack, this.title, (float) this.titleX + 240 - this.font.getStringWidth(this.title.getString()), (float) this.titleY, 4210752);
		this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float) this.playerInventoryTitleX, (float) this.playerInventoryTitleY, 4210752);
		this.font.func_243248_b(matrixStack, new TranslationTextComponent("container.enderchest"), (float) this.titleX, (float) this.titleY, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
	{
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.xSize + 80, this.ySize - 1);

		if (this.horse != null)
		{
			// 0, 166
			// saddled
			if (this.horse.func_230264_L__())
			{
				this.blit(matrixStack, i + 178, j + 35 - 18, 0 + 18, 166, 18, 18);
			}

			if (this.horse.func_230276_fq_())
			{
				this.blit(matrixStack, i + 178, j + 35, 0, 166, 18, 18);
			}

			InventoryScreen.drawEntityOnScreen(i + 222, j + 61, 18, (float) (i + 222) - x, (float) (j + 75 - 61) - y, this.horse);
		}
	}
}