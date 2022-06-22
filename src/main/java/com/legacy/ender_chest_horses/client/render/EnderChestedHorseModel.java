package com.legacy.ender_chest_horses.client.render;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class EnderChestedHorseModel<T extends AbstractHorse> extends HorseModel<T>
{
	private final ModelPart leftChest;
	private final ModelPart rightChest;

	public EnderChestedHorseModel(ModelPart model)
	{
		super(model);

		this.leftChest = this.body.getChild("left_chest");
		this.rightChest = this.body.getChild("right_chest");
	}

	public static LayerDefinition createChestLayer()
	{
		MeshDefinition mesh = HorseModel.createBodyMesh(CubeDeformation.NONE);
		PartDefinition root = mesh.getRoot();
		PartDefinition body = root.getChild("body");
		CubeListBuilder chestCube = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
		body.addOrReplaceChild("left_chest", chestCube, PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, (-(float) Math.PI / 2F), 0.0F));
		body.addOrReplaceChild("right_chest", chestCube, PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, 0.0F, ((float) Math.PI / 2F), 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T p_102366_, float p_102367_, float p_102368_, float p_102369_, float p_102370_, float p_102371_)
	{
		super.setupAnim(p_102366_, p_102367_, p_102368_, p_102369_, p_102370_, p_102371_);
		this.leftChest.visible = true;
		this.rightChest.visible = true;
	}
}