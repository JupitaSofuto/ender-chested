package com.legacy.ender_chest_horses.client.render;

import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderChestedHorseModel<T extends AbstractHorseEntity> extends HorseModel<T>
{
	private final ModelRenderer rightChest = new ModelRenderer(this, 26, 21);
	private final ModelRenderer leftChest;

	public EnderChestedHorseModel(float scale)
	{
		super(scale);
		this.rightChest.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
		this.leftChest = new ModelRenderer(this, 26, 21);
		this.leftChest.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
		this.rightChest.rotateAngleY = (-(float) Math.PI / 2F);
		this.leftChest.rotateAngleY = ((float) Math.PI / 2F);
		this.rightChest.setRotationPoint(6.0F, -8.0F, 0.0F);
		this.leftChest.setRotationPoint(-6.0F, -8.0F, 0.0F);
		this.body.addChild(this.rightChest);
		this.body.addChild(this.leftChest);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.rightChest.showModel = true;
		this.leftChest.showModel = true;
	}
}