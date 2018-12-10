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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OutputListBuilder {
	private final List<Pair<ItemConversionOutput, Float>> outputs = new ArrayList<>();
	private final Function<List<Pair<ItemConversionOutput, Float>>, IOutputList> factory;

	public OutputListBuilder(Function<List<Pair<ItemConversionOutput, Float>>, IOutputList> factory) {
		this.factory = factory;
	}

	public static OutputListBuilder weighted() {
		return new OutputListBuilder(WeightedOutputList::new);
	}

	public static OutputListBuilder chance() {
		return new OutputListBuilder(ChanceOutputList::new);
	}

	public void add(ItemConversionOutput output, float chance) {
		outputs.add(Pair.of(output, chance));
	}

	public IOutputList build() {
		if (outputs.isEmpty()) {
			return new EmptyOutputList();
		}
		IOutputList list = factory.apply(outputs);
		outputs.clear();
		return list;
	}

	private static class EmptyOutputList implements IOutputList {
		@Override public void processResults(World world, double x, double y, double z, int processes, LootContext context) {}
	}
}
