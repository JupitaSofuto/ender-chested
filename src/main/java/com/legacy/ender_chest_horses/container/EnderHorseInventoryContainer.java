package com.legacy.ender_chest_horses.container;

import com.legacy.ender_chest_horses.registry.HorseContainers;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderHorseInventoryContainer extends Container
{
	private final IInventory horseInventory, playerInventory;
	private final EnderChestInventory enderChestInventory;
	public final AbstractHorseEntity horse;

	public EnderHorseInventoryContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(id, playerInventory, (AbstractHorseEntity) playerInventory.player.world.getEntityByID(buffer.readInt()));
	}

	public EnderHorseInventoryContainer(int id, PlayerInventory playerInventory, final AbstractHorseEntity horse)
	{
		super(HorseContainers.ENDER_HORSE_INVENTORY, id);
		this.enderChestInventory = playerInventory.player.getInventoryEnderChest();
		this.horse = horse;
		this.horseInventory = horse.horseChest;
		this.playerInventory = playerInventory;

		playerInventory.openInventory(playerInventory.player);
		this.enderChestInventory.openInventory(playerInventory.player);
		horseInventory.openInventory(playerInventory.player);

		this.addSlot(new Slot(horseInventory, 0, 179, 18)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() == Items.SADDLE && !this.getHasStack() && horse.func_230264_L__();
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public boolean isEnabled()
			{
				return horse.func_230264_L__();
			}
		});
		this.addSlot(new Slot(horseInventory, 1, 179, 36)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return horse.isArmor(stack);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public boolean isEnabled()
			{
				return horse.func_230276_fq_();
			}

			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});

		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 9; ++l)
			{
				this.addSlot(new Slot(enderChestInventory, l + k * 9, 8 + l * 18, 18 + k * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));

		for (int y = 0; y < 3; ++y)
			for (int x = 0; x < 9; ++x)
				this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));

	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.horseInventory.isUsableByPlayer(playerIn) && this.horse.isAlive() && this.horse.getDistance(playerIn) < 8.0F;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			int i = this.horseInventory.getSizeInventory();

			if (index < i)
			{
				if (!this.mergeItemStack(itemstack1, i, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
			{
				if (!this.mergeItemStack(itemstack1, 1, 2, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(0).isItemValid(itemstack1))
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (i <= 2 || !this.mergeItemStack(itemstack1, 2, i, false))
			{
				int j = i + 27;
				int k = j + 9 + 27;
				if (index >= j && index < k)
				{
					if (!this.mergeItemStack(itemstack1, i, j, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= i && index < j)
				{
					if (!this.mergeItemStack(itemstack1, j, k, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.mergeItemStack(itemstack1, j, j, false))
				{
					return ItemStack.EMPTY;
				}

				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn)
	{
		super.onContainerClosed(playerIn);
		this.horseInventory.closeInventory(playerIn);
		this.enderChestInventory.closeInventory(playerIn);
		this.playerInventory.closeInventory(playerIn);
	}
}