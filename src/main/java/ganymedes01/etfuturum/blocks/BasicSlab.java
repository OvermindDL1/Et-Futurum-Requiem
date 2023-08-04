package ganymedes01.etfuturum.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BasicSlab extends BlockSlab implements ISubBlocksBlock {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	public final String[] types;
	private BasicSlab doubleSlab;
	/**
	 * Used to know the previous slab registered, so the slab knows what the double slab version is without passing it as a constructor argument every time
	 * We store the previous slab we registered, so it can be passed to the double slab that registers after it.
	 */
	private static BasicSlab previousSlab;

	public BasicSlab(boolean isDouble, Material material, String... names) {
		super(isDouble, material);
		if (names.length > 8) {
			throw new IllegalArgumentException("Slabs can't have more than 8 subtypes! Tried to register a slab with " + names.length);
		}
		types = names;
		useNeighborBrightness = !isDouble;
		opaque = isDouble;
		if (isDouble) {
			doubleSlab = this;
			previousSlab.doubleSlab = this;
			previousSlab = null;
		} else {
			previousSlab = this;
		}
	}

	public BasicSlab getDoubleSlab() {
		return doubleSlab;
	}

	@Override
	public IIcon[] getIcons() {
		return icons;
	}

	protected void setIcons(IIcon[] icons) {
		this.icons = icons;
	}

	@Override
	public String[] getTypes() {
		return types;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		setIcons(new IIcon[getTypes().length]);
		for (int i = 0; i < getIcons().length; i++) {
			getIcons()[i] = "".equals(getTypes()[i]) ? reg.registerIcon(getTextureName()) : reg.registerIcon(getTypes()[i]);
		}
		blockIcon = getIcons()[0];
	}

	@Override
	public String getNameFor(ItemStack stack) {
		return func_150002_b(stack.getItemDamage());
	}

	@Override
	public String func_150002_b(int meta) {
		String type = getTypes()[Math.max(0, (meta % 8) % getTypes().length)];
		type = ("".equals(type) ? getUnlocalizedName().replace("tile.", "").replace("etfuturum.", "") : type);
		if (type.toLowerCase().endsWith("bricks") || type.toLowerCase().endsWith("tiles")) {
			type = type.substring(0, type.length() - 1);
		}
		return type + "_slab";
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		if (!field_150004_a) {
			for (int i = 0; i < types.length; i++) {
				p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[(meta % 8) % icons.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}
}
