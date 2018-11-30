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

package hubry.explosh.conversion.item.output;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

public final class ItemDropOutput extends ItemConversionOutput {
	private final ItemStack stack;

	public ItemDropOutput(WeightedItemStack stack) {
		super(stack.getChance());
		this.stack = CraftTweakerMC.getItemStack(stack.getStack());
	}

	@Override
	public void processResult(World world, double x, double y, double z, int processes, LootContext context) {
		int resultAmount = stack.getCount() * processes;
		if (chance < 1.0f) {                            // If you input 50 items and chance is 80%, you'll always get 40 items.
			float resultAvg = resultAmount * chance;    // If you input 51 items, you have an 80% chance to get 41 and 20% chance to get 40.
			resultAmount = (int) resultAvg;             // This is less of a "chance" and more of "% you get."
			if (world.rand.nextFloat() < resultAvg - resultAmount) {
				resultAmount++;
			}
		}
		int maxStackSize = stack.getMaxStackSize();
		ItemStack drop = stack.copy();
		drop.setCount(maxStackSize);
		while (resultAmount > maxStackSize) {
			EntityItem item = new EntityItem(world, x, y, z, drop.copy());
			item.setDefaultPickupDelay();
			world.spawnEntity(item);

			resultAmount -= maxStackSize;
		}
		drop.setCount(resultAmount);
		EntityItem item = new EntityItem(world, x, y, z, drop);
		item.setDefaultPickupDelay();
		world.spawnEntity(item);
	}
}
