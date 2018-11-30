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

import hubry.explosh.conversion.item.output.ItemConversionOutput;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple item converter for single inputs.
 */
public class SingleItemConversion extends ItemConversion {
	private final IIngredient input;

	public SingleItemConversion(IIngredient input, List<ItemConversionOutput> outputs) {
		super(outputs);
		this.input = input;
	}

	@Override
	protected int processInputs(EntityItem item) {
		ItemStack itemStack = item.getItem();
		int inputAmount = input.getAmount();
		int processAmount = itemStack.getCount() / inputAmount;
		itemStack.shrink(processAmount * inputAmount);
		item.setItem(itemStack);

		return processAmount;
	}

	@Override
	public boolean matches(IItemStack itemStack) {
		return input.matches(itemStack);
	}

	@Override
	public Collection<Item> getMatchingItems() {
		return input.getItems().stream()
				.map(CraftTweakerMC::getItemStack)
				.map(ItemStack::getItem).distinct()
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return getClass().getName() + '{' + input + '}';
	}
}
