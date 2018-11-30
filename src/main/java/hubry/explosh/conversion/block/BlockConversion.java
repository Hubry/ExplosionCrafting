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

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public abstract class BlockConversion implements IBlockConv {

	private final ImmutableSet<IBlockState> inputs;

	public BlockConversion(List<IBlockState> inputs) {
		this.inputs = ImmutableSet.copyOf(inputs);
	}

	@Override
	public boolean matches(IBlockState state) {
		return inputs.contains(state);
	}

	@Override
	public Collection<IBlockState> getMatchingStates() {
		return inputs;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", getClass().getName() + "{", "}");
		for (IBlockState input : inputs) {
			joiner.add(input.toString());
		}
		return joiner.toString();
	}
}
