package com.legacy.ender_chest_horses.container;

import com.legacy.ender_chest_horses.registry.HorseRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderHorseInventoryMenu extends AbstractContainerMenu
{
	private final Container horseInventory, playerInventory;
	private final PlayerEnderChestContainer enderChestInventory;
	public final AbstractHorse horse;

	public EnderHorseInventoryMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(id, playerInventory, (AbstractHorse) playerInventory.player.level.getEntity(buffer.readInt()));
	}

	public EnderHorseInventoryMenu(int id, Inventory playerInventory, final AbstractHorse horse)
	{
		super(HorseRegistry.ENDER_HORSE_INVENTORY.get(), id);
		this.enderChestInventory = playerInventory.player.getEnderChestInventory();
		this.horse = horse;
		this.horseInventory = horse.inventory;
		this.playerInventory = playerInventory;

		playerInventory.startOpen(playerInventory.player);
		this.enderChestInventory.startOpen(playerInventory.player);
		horseInventory.startOpen(playerInventory.player);

		this.addSlot(new Slot(horseInventory, 0, 179, 18)
		{
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return stack.getItem() == Items.SADDLE && !this.hasItem() && horse.isSaddleable();
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public boolean isActive()
			{
				return horse.isSaddleable();
			}
		});
		this.addSlot(new Slot(horseInventory, 1, 179, 36)
		{
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return horse.isArmor(stack);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public boolean isActive()
			{
				return horse.canWearArmor();
			}

			@Override
			public int getMaxStackSize()
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
	public boolean stillValid(Player playerIn)
	{
		return this.horseInventory.stillValid(playerIn) && this.horse.isAlive() && this.horse.distanceTo(playerIn) < 8.0F;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			int i = this.horseInventory.getContainerSize();

			if (index < i)
			{
				if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem())
			{
				if (!this.moveItemStackTo(itemstack1, 1, 2, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(0).mayPlace(itemstack1))
			{
				if (!this.moveItemStackTo(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false))
			{
				int j = i + 27;
				int k = j + 9 + 27;
				if (index >= j && index < k)
				{
					if (!this.moveItemStackTo(itemstack1, i, j, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= i && index < j)
				{
					if (!this.moveItemStackTo(itemstack1, j, k, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (!this.moveItemStackTo(itemstack1, j, j, false))
				{
					return ItemStack.EMPTY;
				}

				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.set(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void removed(Player playerIn)
	{
		super.removed(playerIn);
		this.horseInventory.stopOpen(playerIn);
		this.enderChestInventory.stopOpen(playerIn);
		this.playerInventory.stopOpen(playerIn);
	}
}