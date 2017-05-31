package net.dyeo.teleporter.tileentity;

import net.dyeo.teleporter.block.BlockTeleporter;
import net.dyeo.teleporter.teleport.TeleporterNetwork;
import net.dyeo.teleporter.teleport.TeleporterNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTeleporter extends TileEntity implements ITickable
{

	private String customName = null;
	private boolean isPowered = false;

	private ItemStackHandler handler = new ItemStackHandler(1)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			TileEntityTeleporter.this.update();
			TileEntityTeleporter.this.markDirty();
		}
	};


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.<T>cast(this.handler);
		return super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setBoolean("powered", this.isPowered());
		if (this.hasCustomName()) compound.setString("CustomName", this.customName);
		compound.setTag("Inventory", this.handler.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("CustomName", NBT.TAG_STRING)) this.customName = compound.getString("CustomName");
		this.setPowered(compound.getBoolean("powered"));
		this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
	}



	public boolean isPowered()
	{
		return this.isPowered;
	}

	public void setPowered(boolean isPowered)
	{
		this.isPowered = isPowered;
	}

	public String getName()
	{
		String unlocalizedName = "tile." + this.getWorld().getBlockState(this.getPos()).getValue(BlockTeleporter.TYPE).getUnlocalizedName() + ".name";
		return this.hasCustomName() ? this.customName : unlocalizedName;
	}

	public boolean hasCustomName()
	{
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
	}



	public boolean canInteractWith(EntityPlayer player)
	{
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(this.pos.getX() + X_CENTRE_OFFSET, this.pos.getY() + Y_CENTRE_OFFSET, this.pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}


	public void removeFromNetwork()
	{
		TeleporterNetwork netWrapper = TeleporterNetwork.get(this.world);
		ItemStack key = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
		netWrapper.removeNode(this.pos, this.world.provider.getDimension(), key);
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			boolean isNewNode = false;

			TeleporterNetwork netWrapper = TeleporterNetwork.get(this.world);

			int tileDim = this.world.provider.getDimension();

			TeleporterNode thisNode = netWrapper.getNode(this.pos, tileDim);
			if (thisNode == null)
			{
				thisNode = new TeleporterNode();
				isNewNode = true;
			}

			thisNode.pos = this.pos;
			thisNode.dimension = tileDim;
			thisNode.type = this.getWorld().getBlockState(this.pos).getValue(BlockTeleporter.TYPE);

			if (isNewNode == true || netWrapper.runtimeRebuild == true)
			{
				netWrapper.addNode(thisNode);
				this.markDirty();
			}
			else
			{
				netWrapper.updateNode(thisNode);
			}
		}
	}

}
