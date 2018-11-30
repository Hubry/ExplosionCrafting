/*
 * Copyright (c) 2018 Hubry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package hubry.explosh;

import com.google.common.collect.ImmutableList;
import hubry.explosh.conversion.block.IBlockConv;
import hubry.explosh.conversion.block.WeightedBlockConversion;
import hubry.explosh.conversion.item.IItemConv;
import hubry.explosh.conversion.item.SingleItemConversion;
import hubry.explosh.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientUnknown;
import crafttweaker.api.minecraft.CraftTweakerMC;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ExplosionCrafting.MODID)
public class ExplosionEventHandler {

	static List<IBlockConv> blockConverters = new ArrayList<>();
	static List<IItemConv> itemConverters = new ArrayList<>();

	private static final Map<IBlockState, IBlockConv> blockConvertersMap = new IdentityHashMap<>();
	private static final Map<Item, List<IItemConv>> itemConvertersMap = new IdentityHashMap<>();

	// Preinitialized to basically a null conversion. Never matching and never doing anything.
	private static IBlockConv lastBlock = new WeightedBlockConversion(ImmutableList.of(), ImmutableList.of());
	private static IItemConv lastItem = new SingleItemConversion(IngredientUnknown.INSTANCE, ImmutableList.of());

	static void buildConversionMap() {
		for (IBlockConv converter : blockConverters) {
			for (IBlockState state : converter.getMatchingStates()) {
				blockConvertersMap.put(state, converter);
			}
		}
		for (IItemConv converter : itemConverters) {
			for (Item item : converter.getMatchingItems()) {
				itemConvertersMap.computeIfAbsent(item, (key) -> new ArrayList<>()).add(converter);
			}
		}
/*
        ExplosionCrafting.LOGGER.info("Listing present converters:");
        for (IItemConv itemConverter : itemConverters)
            ExplosionCrafting.LOGGER.info("  {}", itemConverter);

        for (IBlockConv blockConverter : blockConverters)
            ExplosionCrafting.LOGGER.info("  {}", blockConverter);
*/
		blockConverters = null;
		itemConverters = null;
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate e) {
		World world = e.getWorld();
		if (world.isRemote)
			return;

		world.profiler.startSection(ExplosionCrafting.MODID);

		Explosion explosion = e.getExplosion();
		LootContext context = Utils.createExplosionLootContext(world, explosion);

		if (!blockConvertersMap.isEmpty()) {
			world.profiler.startSection("blocks");
			for (Iterator<BlockPos> iterator = e.getAffectedBlocks().iterator(); iterator.hasNext(); ) {
				BlockPos pos = iterator.next();

				// I had an air block check here but it seems to be faster without it. *shrugs*
				IBlockConv conversion = getConversion(world.getBlockState(pos));
				if (conversion != null) {
					lastBlock = conversion;
					if (conversion.processResult(world, pos, context)) {
						iterator.remove();
					}
				}
			}
			world.profiler.endSection();
		}
		if (!itemConvertersMap.isEmpty() && !e.getAffectedEntities().isEmpty()) {
			world.profiler.startSection("items");
			for (Entity entity : e.getAffectedEntities()) {
				if (entity instanceof EntityItem && !((EntityItem) entity).getItem().isEmpty()) {
					EntityItem item = (EntityItem) entity;
					IItemConv conversion = getConversion(item.getItem());
					if (conversion != null) {
						lastItem = conversion;
						conversion.processResult(item, context);
					}
				}
			}
			world.profiler.endSection();
		}
		world.profiler.endSection();
	}

	private static IBlockConv getConversion(IBlockState state) {
		if (lastBlock.matches(state)) {
			return lastBlock;
		}
		if (blockConvertersMap.containsKey(state)) {
			return blockConvertersMap.get(state);
		}
		return null;
	}

	private static IItemConv getConversion(ItemStack stack) {
		IItemStack item = CraftTweakerMC.getIItemStack(stack);
		if (lastItem.matches(item)) {
			return lastItem;
		}
		if (itemConvertersMap.containsKey(stack.getItem())) {
			for (IItemConv conversion : itemConvertersMap.get(stack.getItem())) {
				if (conversion.matches(item)) {
					return conversion;
				}
			}
		}
		return null;
	}
}
