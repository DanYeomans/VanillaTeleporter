package net.dyeo.teleporter.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.dyeo.teleporter.entities.TileEntityTeleporter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockEnderTeleporter extends BlockTeleporterBase
{
	public BlockEnderTeleporter()
	{
		super(true);
	}

	@Override
	public String getBlockName()
	{
		return "enderTeleporterBlock";
	}

	public static Vec3 getBounds()
	{
		return BlockTeleporterBase.getBounds();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityTeleporter result = new TileEntityTeleporter();
		return result;
	}

	@Override
	public boolean onBlockActivated(World worldIn, int posx, int posy, int posz, EntityPlayer playerIn, int side, float hitX, float hitY, float hitZ)
	{
		return super.onBlockActivated(worldIn, posx, posy, posz, playerIn, side, hitX, hitY, hitZ);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, int posx, int posy, int posz, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, posx, posy, posz, entityIn);
	}

	@Override
	public void onNeighborBlockChange(World world, int posx, int posy, int posz, Block neighbourBlock)
	{
		super.onNeighborBlockChange(world, posx, posy, posz, neighbourBlock);
	}

	@Override
	public void breakBlock(World worldIn, int posx, int posy, int posz, Block blockIn, int something)
	{
		super.breakBlock(worldIn, posx, posy, posz, blockIn, something);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockLayer()
	{
		return 2;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return true;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return 0; //return 3;
	}
}
