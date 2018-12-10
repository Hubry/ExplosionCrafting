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

package hubry.explosh.conversion.item.output.list;

import hubry.explosh.conversion.item.output.ItemConversionOutput;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class WeightedOutputList implements IOutputList {
	private final ItemConversionOutput[] outputs;
	private final int[] weights;
	private final int sumOfWeights;

	public WeightedOutputList(List<Pair<ItemConversionOutput, Float>> pairs) {
		this.outputs = new ItemConversionOutput[pairs.size()];
		this.weights = new int[pairs.size()];
		int sum = 0;
		for (int i = 0; i < pairs.size(); i++) {
			Pair<ItemConversionOutput, Float> pair = pairs.get(i);
			outputs[i] = pair.getLeft();
			int weight = Math.max(1, Math.round(pair.getRight()));
			sum += weight;
			weights[i] = weight;
		}
		this.sumOfWeights = sum;
	}

	@Override
	public void processResults(World world, double x, double y, double z, int processes, LootContext context) {
		int r = world.rand.nextInt(sumOfWeights);
		for (int i = 0; i < outputs.length; i++) {
			r -= weights[i];
			if (r < 0) {
				outputs[i].processResult(world, x, y, z, processes, context);
			}
		}
	}
}
