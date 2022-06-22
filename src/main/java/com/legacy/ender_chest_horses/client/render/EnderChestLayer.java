package com.legacy.ender_chest_horses.client.render;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.legacy.ender_chest_horses.client.HorseClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class EnderChestLayer<T extends AbstractHorse, M extends HorseModel<T>> extends RenderLayer<T, M>
{
	private static final ResourceLocation TEXTURE = EnderChestedMod.locate("textures/entity/ender_chest.png");
	private final EnderChestedHorseModel<T> model;
	private final EntityModel<T> parentModel;

	public EnderChestLayer(RenderLayerParent<T, M> renderer)
	{
		super(renderer);

		this.model = new EnderChestedHorseModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(HorseClient.ENDER_CHEST));
		this.parentModel = renderer.getModel();
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		matrixStackIn.pushPose();

		if (entityIn instanceof AbstractHorse && EnderHorseCapability.get(entityIn) != null && EnderHorseCapability.get(entityIn).isEnderChested())
		{
			VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.parentModel.renderType(TEXTURE), false, false);

			if (this.parentModel instanceof HorseModel)
			{
				this.parentModel.copyPropertiesTo(this.model);
				this.model.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTicks);
				this.model.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			}

			model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrixStackIn.popPose();
	}
}
