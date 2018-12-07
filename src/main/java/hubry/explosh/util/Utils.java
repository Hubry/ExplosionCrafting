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

package hubry.explosh.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import crafttweaker.api.block.IBlockStateMatcher;
import crafttweaker.api.minecraft.CraftTweakerMC;

import java.util.Collections;
import java.util.List;

/**
 * Various utilities kept here.
 */
public final class Utils {
	/**
	 * Unpacks the CT blockstate matcher into a list of vanilla blockstates.
	 *
	 * @param stateMatcher Matcher to unpack
	 * @return List of vanilla blockstates matched by the matcher.
	 */
	public static ImmutableList<IBlockState> unpackMatcher(IBlockStateMatcher stateMatcher) {
		ImmutableList.Builder<IBlockState> builder = ImmutableList.builder();
		for (crafttweaker.api.block.IBlockState iBlockState : stateMatcher.getMatchingBlockStates()) {
			builder.add(CraftTweakerMC.getBlockState(iBlockState));
		}
		return builder.build();
	}

	/**
	 * Triggers drops from the specified loot table and drops them at the specified location.
	 * <p>
	 * If the generated list of drops would be longer than maxDrops, it will drop only as many items as maxDrops.
	 *
	 * @param world     World to drop items in
	 * @param x         x coord
	 * @param y         y coord
	 * @param z         z coord
	 * @param maxDrops  Maximum amount of items to be dropped from the table. Ignored if if 0 or lower.
	 * @param context   Context of the dropping event
	 * @param lootTable Location of the loot table
	 */
	public static void dropLootTable(World world, double x, double y, double z, int maxDrops, LootContext context, ResourceLocation lootTable) {
		LootTable table = world.getLootTableManager().getLootTableFromLocation(lootTable);
		List<ItemStack> stacks = table.generateLootForPools(world.rand, context);
		int toDrop = stacks.size();

		if (maxDrops > 0 && maxDrops < toDrop) {
			toDrop = maxDrops;
			Collections.shuffle(stacks, world.rand); //Shuffle to not just pick the first X items from the table. Kinda wasteful but whatever
		}

		for (int i = 0; i < toDrop; i++) {
			ItemStack stack = stacks.get(i);
			EntityItem item = new EntityItem(world, x, y, z, stack);
			item.setDefaultPickupDelay();
			world.spawnEntity(item);
		}
	}

	/**
	 * Generates a LootContext from the specified explosion, containing player info if explosion was triggered by a player.
	 * Assumes that the world is a server world!
	 *
	 * @param world     world where the explosion happened
	 * @param explosion explosion triggering the drop
	 * @return context in world, with the triggering player and his luck specified if explosion was triggered by a player (eg. manually ignited TNT)
	 */
	public static LootContext createExplosionLootContext(World world, Explosion explosion) {
		LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
		EntityLivingBase entity = explosion.getExplosivePlacedBy();

		if (entity instanceof EntityPlayer) {
			builder.withPlayer((EntityPlayer) entity)
					.withLuck(((EntityPlayer) entity).getLuck());
		}

		return builder.build();
	}

	private Utils() {
	}
}
