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

package hubry.explosh.conversion.block;

import hubry.explosh.conversion.block.entry.BlockConversionEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;

public class WeightedBlockConversion extends BlockConversion {
	private final int sumOfWeights;
	private final List<BlockConversionEntry> converters;

	public WeightedBlockConversion(List<IBlockState> inputs, List<BlockConversionEntry> converters) {
		super(inputs);
		this.converters = converters;
		this.sumOfWeights = converters.stream().mapToInt(BlockConversionEntry::getWeight).sum();
	}

	@Override
	public boolean processResult(World world, BlockPos pos, LootContext context) {
		int i = world.rand.nextInt(sumOfWeights);
		for (BlockConversionEntry c : converters) {
			i -= c.getWeight();
			if (i < 0) {
				return c.process(world, pos, context);
			}
		}
		return false;
	}
}
