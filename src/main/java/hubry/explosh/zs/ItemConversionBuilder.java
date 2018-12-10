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

package hubry.explosh.zs;

import hubry.explosh.ExplosionCrafting;
import hubry.explosh.conversion.item.ItemConversion;
import hubry.explosh.conversion.item.output.ItemConversionOutput;
import hubry.explosh.conversion.item.MultiItemConversion;
import hubry.explosh.conversion.item.SingleItemConversion;
import hubry.explosh.conversion.item.output.ItemDropOutput;
import hubry.explosh.conversion.item.output.LootTableDropOutput;
import hubry.explosh.util.LootTableChecker;
import net.minecraft.util.ResourceLocation;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IngredientAny;
import crafttweaker.api.item.WeightedItemStack;

import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.explosioncrafting.ItemConversionBuilder")
public class ItemConversionBuilder {
	private final String name;
	private final List<IIngredient> inputs = new ArrayList<>();
	private final List<ItemConversionOutput> outputs = new ArrayList<>();

	public ItemConversionBuilder(IIngredient input) {
		inputs.add(input);
		name = input.toString();
	}

	@ZenMethod
	public static ItemConversionBuilder create(IIngredient input) {
		return new ItemConversionBuilder(input);
	}

	@ZenMethod
	public ItemConversionBuilder addInput(IIngredient input) {
		// dirty thing to make it have a proper amount for my later checks - IngredientAny has -1 items.
		if (input instanceof IngredientAny)
			input = input.amount(1);
		inputs.add(input);
		return this;
	}

	@ZenMethod
	public ItemConversionBuilder addOutput(WeightedItemStack output) {
		outputs.add(new ItemDropOutput(output));
		return this;
	}

	@ZenMethod
	public ItemConversionBuilder addLootTableOutput(String name, @Optional float chance, @Optional int maxDrops) {
		ResourceLocation table = new ResourceLocation(name);
		LootTableChecker.checkTable(table);
		outputs.add(new LootTableDropOutput(table, chance, maxDrops));
		return this;
	}

	@ZenMethod
	public void build() {
		ItemConversion conversion;
		if (inputs.size() == 1)
			conversion = new SingleItemConversion(inputs.get(0), outputs);
		else
			conversion = new MultiItemConversion(inputs, outputs);
		ExplosionCrafting.ADDITIONS.add(new Add(conversion, name));
	}

	static class Add implements IAction {
		private final ItemConversion converter;
		private final String name;

		Add(ItemConversion converter, String name) {
			this.converter = converter;
			this.name = name;
		}

		@Override
		public void apply() {
			ExplosionCrafting.addItemConversion(converter);
		}

		@Override
		public String describe() {
			return "Adding an item conversion for primary input " + name;
		}
	}


}
