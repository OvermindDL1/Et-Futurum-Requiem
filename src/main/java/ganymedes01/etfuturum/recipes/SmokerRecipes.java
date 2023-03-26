package ganymedes01.etfuturum.recipes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ganymedes01.etfuturum.configuration.configs.ConfigFunctions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmokerRecipes
{
	private static final SmokerRecipes smeltingBase = new SmokerRecipes();
	/** The list of smelting results. */
	public Map<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();
	public Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	/**
	 * Used to call methods addSmelting and getSmeltingResult.
	 */
	public static SmokerRecipes smelting()
	{
		return smeltingBase;
	}

	@SuppressWarnings("unchecked")
	public static void seekRecipes() {
		if (ConfigFunctions.enableAutoAddSmoker) {
			Iterator<Entry<ItemStack, ItemStack>> iterator = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<ItemStack, ItemStack> entry = iterator.next();
				ItemStack input = entry.getKey(), result = entry.getValue();
				// Make sure there is no Nullpointers in there, yes there can be invalid Recipes in the Furnace List.
				// That was why DragonAPI somehow fixed a Bug in here, because Reika removes nulls from the List!
				if (input != null && result != null) {
					// If either the Input or the Result are Food, add a Smoker Recipe.
					if (input.getItem() instanceof ItemFood || result.getItem() instanceof ItemFood) {
						smeltingBase.addRecipe(input, result, result.getItem().getSmeltingExperience(result));
					}
				}
			}
		}
	}
	
	public void addRecipe(Block p_151393_1_, ItemStack p_151393_2_, float p_151393_3_)
	{
		this.addRecipe(Item.getItemFromBlock(p_151393_1_), p_151393_2_, p_151393_3_);
	}

	public void addRecipe(Item p_151396_1_, ItemStack p_151396_2_, float p_151396_3_)
	{
		this.addRecipe(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, p_151396_3_);
	}

	public void addRecipe(ItemStack p_151394_1_, ItemStack p_151394_2_, float p_151394_3_)
	{
		this.smeltingList.put(p_151394_1_, p_151394_2_);
		this.experienceList.put(p_151394_2_, Float.valueOf(p_151394_3_));
	}

	/**
	 * Returns the smelting result of an item.
	 */
	public ItemStack getSmeltingResult(ItemStack p_151395_1_)
	{
		Iterator<Entry<ItemStack, ItemStack>> iterator = this.smeltingList.entrySet().iterator();
		Entry<ItemStack, ItemStack> entry;

		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}

			entry = iterator.next();
		}
		while (!this.func_151397_a(p_151395_1_, entry.getKey()));

		return entry.getValue();
	}

	private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_)
	{
		return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
	}

	public Map<ItemStack, ItemStack> getSmeltingList()
	{
		return this.smeltingList;
	}

	public float func_151398_b(ItemStack p_151398_1_)
	{
		float ret = p_151398_1_.getItem().getSmeltingExperience(p_151398_1_);
		if (ret != -1) return ret;

		Iterator<Entry<ItemStack, Float>> iterator = this.experienceList.entrySet().iterator();
		Entry<ItemStack, Float> entry;

		do
		{
			if (!iterator.hasNext())
			{
				return 0.0F;
			}

			entry = iterator.next();
		}
		while (!this.func_151397_a(p_151398_1_, entry.getKey()));

		return entry.getValue().floatValue();
	}
}
