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
import hubry.explosh.conversion.block.BlockConversion;
import hubry.explosh.conversion.block.IBlockConv;
import hubry.explosh.conversion.block.SimpleBlockConversion;
import hubry.explosh.conversion.block.WeightedBlockConversion;
import hubry.explosh.conversion.block.entry.BlockChangeEntry;
import hubry.explosh.conversion.block.entry.BlockConversionEntry;
import hubry.explosh.conversion.block.entry.BlockDegradeEntry;
import hubry.explosh.conversion.block.entry.ItemDropEntry;
import hubry.explosh.conversion.block.entry.ItemGroupDropEntry;
import hubry.explosh.conversion.block.entry.LootTablePullEntry;
import hubry.explosh.conversion.block.entry.PassEntry;
import hubry.explosh.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IBlockStateMatcher;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.explosioncrafting.BlockConversionBuilder")
public class BlockConversionBuilder {
	private final List<net.minecraft.block.state.IBlockState> stateInputs = new ArrayList<>();
	private final List<BlockConversionEntry> outputs = new ArrayList<>();
	private final String name;

	public BlockConversionBuilder(List<net.minecraft.block.state.IBlockState> stateInputs, String name) {
		this.stateInputs.addAll(stateInputs);
		this.name = name;
	}

	@ZenMethod
	public static BlockConversionBuilder of(IBlockStateMatcher matcher) {
		return new BlockConversionBuilder(Utils.unpackMatcher(matcher), matcher.toString());
	}

	@ZenMethod
	public BlockConversionBuilder addConversion(IBlockState state, @Optional int weight) {
		outputs.add(new BlockChangeEntry(CraftTweakerMC.getBlockState(state), weight));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder addDrop(IItemStack item, @Optional int weight) {
		outputs.add(new ItemDropEntry(CraftTweakerMC.getItemStack(item), weight));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder addConversionDrop(IBlockState state, @Optional int weight) {
		outputs.add(new BlockDegradeEntry(CraftTweakerMC.getBlockState(state), weight));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder addLootTableDrop(String lootTable, @Optional int weight, @Optional int maxDrops) {
		outputs.add(new LootTablePullEntry(new ResourceLocation(lootTable), weight, maxDrops));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder addOutputGroup(WeightedItemStack[] items, @Optional int weight) {
		outputs.add(new ItemGroupDropEntry(items, weight));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder addPass(int weight) {
		outputs.add(new PassEntry(weight));
		return this;
	}

	@ZenMethod
	public BlockConversionBuilder convertToAir(int weight) {
		outputs.add(new BlockChangeEntry(Blocks.AIR.getDefaultState(), weight));
		return this;
	}

	@ZenMethod
	public void build() {
		if (outputs.isEmpty()) {
			CraftTweakerAPI.logError("Tried to create an empty block converter!", new IllegalArgumentException());
		} else {
			BlockConversion conversion = outputs.size() == 1
					? new SimpleBlockConversion(this.stateInputs, outputs.get(0))
					: new WeightedBlockConversion(this.stateInputs, outputs);
			CraftTweakerAPI.apply(new Add(conversion, name));
		}
	}

	@ZenMethod
	public static void removeDrop(IBlockStateMatcher block) {
		of(block).convertToAir(1).build();
	}

	private static class Add implements IAction {
		private final IBlockConv converter;
		private final String name;

		Add(IBlockConv converter, String name) {
			this.converter = converter;
			this.name = name;
		}

		@Override
		public void apply() {
			ExplosionCrafting.addBlockConversion(converter);
		}

		@Override
		public String describe() {
			return "Adding block conversion for matcher " + name;
		}
	}
}
