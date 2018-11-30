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

import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

import java.util.Random;

public abstract class ItemConversionOutput {
	protected final float chance;

	protected ItemConversionOutput(float chance) {
		if (chance <= 0.0f) {
			this.chance = 1.0f;
		} else {
			this.chance = chance;
		}
	}

	protected final boolean checkChance(Random rand) {
		return chance == 1.0f || rand.nextFloat() <= chance;
	}

	/**
	 * Performs the effect of the conversion at the provided location.
	 *
	 * @param world     Location's world
	 * @param x         x-coord of location
	 * @param y         y-coord of location
	 * @param z         z-coord of location
	 * @param processes Amount of processes to perform at the location
	 * @param context   Loot context of the conversion, provided for loot table drops.
	 */
	public abstract void processResult(World world, double x, double y, double z, int processes, LootContext context);
}
