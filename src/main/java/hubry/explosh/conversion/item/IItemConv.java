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

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootContext;
import crafttweaker.api.item.IItemStack;

import java.util.Collection;

/**
 * The main interface for handling conversions of dropped items
 * affected by explosions.
 *
 * @see hubry.explosh.conversion.block.IBlockConv
 */
public interface IItemConv {
	/**
	 * Checks if it should attempt to process the provided item.
	 * This does not mean it will actually do anything to it, if other requirements aren't fulfilled.
	 *
	 * @param itemStack Checked item
	 * @return true if it matches the first ingredient of conversion
	 */
	boolean matches(IItemStack itemStack);

	/**
	 * Attempts to process the provided item entity.
	 *
	 * @param item    Entity to process
	 * @param context LootContext of the conversion, for context-sensitive effects like loot tables
	 */
	void processResult(EntityItem item, LootContext context);

	/**
	 * Gets a collection of items matching the first ingredient.
	 * Used only to build the multimap of all conversions per item type.
	 *
	 * @return collection of matching items
	 */
	Collection<Item> getMatchingItems();
}
