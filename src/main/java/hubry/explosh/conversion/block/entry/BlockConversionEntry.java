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

package hubry.explosh.conversion.block.entry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public abstract class BlockConversionEntry {
	private final int weight;

	protected BlockConversionEntry(int weight) {
		if (weight <= 0)
			this.weight = 1;
		else
			this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * Processes the block at the provided location.
	 *
	 * @param world   World containing the block
	 * @param pos     Position of the block
	 * @param context Loot context of the conversion, provided for loot table drops.
	 * @return Whether the position should be removed from vanilla explosion's handling
	 */
	public abstract boolean process(World world, BlockPos pos, LootContext context);

}