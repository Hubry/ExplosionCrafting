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

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

import java.util.Collection;

/**
 * The interface for handling conversions of blocks affected
 * by explosions into other blocks, dropped items, etc.
 *
 * @see hubry.explosh.conversion.item.IItemConv
 */
public interface IBlockConv {
	/**
	 * Checks if it should attempt processing provided state.
	 *
	 * @param state Checked state
	 * @return true if it matches the first conversion
	 */
	boolean matches(IBlockState state);

	/**
	 * Attempts to process the provided position in the world.
	 *
	 * @param world   World where the process was started
	 * @param pos     Position in the world
	 * @param context LootContext of the conversion, for context-sensitive effects like loot tables
	 * @return If the block should be removed from the explosion affected block list
	 */
	boolean processResult(World world, BlockPos pos, LootContext context);

	/**
	 * Gets a collection of blockstates matching the conversion.
	 * Used only for building the map of all conversions.
	 *
	 * @return Collection of blockstates
	 */
	Collection<IBlockState> getMatchingStates();
}
