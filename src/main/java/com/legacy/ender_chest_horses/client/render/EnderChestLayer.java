package com.legacy.ender_chest_horses.client.render;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.ResourceLocation;

public class EnderChestLayer<T extends AbstractHorseEntity, M extends HorseModel<T>> extends LayerRenderer<T, M>
{
	private static final ResourceLocation TEXTURE = EnderChestedMod.locate("textures/entity/ender_chest.png");
	private final EnderChestedHorseModel<T> model = new EnderChestedHorseModel<>(1.1F);
	private final EntityModel<T> parentModel;

	public EnderChestLayer(IEntityRenderer<T, M> renderer)
	{
		super(renderer);
		this.parentModel = renderer.getEntityModel();
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		matrixStackIn.push();

		if (entityIn instanceof AbstractHorseEntity && EnderHorseCapability.get(entityIn) != null && EnderHorseCapability.get(entityIn).isEnderChested())
		{
			IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, this.parentModel.getRenderType(TEXTURE), false, false);

			if (this.parentModel instanceof HorseModel)
			{
				this.parentModel.copyModelAttributesTo(this.model);
				this.model.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
				this.model.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			}

			model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrixStackIn.pop();
	}
}
