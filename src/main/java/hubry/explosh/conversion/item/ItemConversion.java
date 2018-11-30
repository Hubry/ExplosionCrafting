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

package hubry.explosh.conversion.item;

import com.google.common.collect.ImmutableList;
import hubry.explosh.conversion.item.output.ItemConversionOutput;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;

public abstract class ItemConversion implements IItemConv { //todo weighted mode?
	private final ImmutableList<ItemConversionOutput> outputs;

	protected ItemConversion(List<ItemConversionOutput> outputs) {
		this.outputs = ImmutableList.copyOf(outputs);
	}

	/**
	 * Processes inputs and returns the amount of output processes to output
	 *
	 * @param item Start point item
	 * @return Amount of processes to do
	 */
	protected abstract int processInputs(EntityItem item);

	@Override
	public void processResult(EntityItem item, LootContext context) {
		int processes = processInputs(item);

		for (ItemConversionOutput output : outputs) {
			output.processResult(item.world, item.posX, item.posY, item.posZ, processes, context);
		}
	}

}
