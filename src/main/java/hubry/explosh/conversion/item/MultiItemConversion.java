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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specialized converter for multiple inputs.
 */
public class MultiItemConversion extends ItemConversion {
	private static final AxisAlignedBB SCAN_AREA = new AxisAlignedBB(-1, -0.5, -1, 1, 0.5, 1);

	private final ImmutableList<IIngredient> inputs;

	public MultiItemConversion(List<IIngredient> inputs, List<ItemConversionOutput> outputs) {
		super(outputs);
		this.inputs = ImmutableList.copyOf(inputs);
	}

	@Override
	protected int processInputs(EntityItem item) {
		// Get all items around the input and the amount of processes to do.
		// NOTE: This list includes the entity passed here - and that's fine, as that entity
		// is only directly referenced here for the amount of processes and to find all entities.
		List<EntityItem> foundItems = item.world.getEntitiesWithinAABB(EntityItem.class,
				SCAN_AREA.offset(item.posX, item.posY, item.posZ), e -> !(e.isDead || e.getItem().isEmpty()));

		if (foundItems.size() < inputs.size()) {
			return 0;
		}
		List<List<EntityItem>> itemsPerIngredientType = new ArrayList<>();
		int processAmount = Integer.MAX_VALUE;

		// Gather all item entities to process. If any ingredient cannot be gathered, return with a 0.
		for (int i = 0; i < inputs.size(); i++) {
			List<EntityItem> gatheredInput = getItemsForIngredient(foundItems, inputs.get(i));
			if (gatheredInput.isEmpty()) {
				return 0;
			}
			int totalAmount = 0;
			for (EntityItem entity : gatheredInput) {
				totalAmount += entity.getItem().getCount();
			}
			int processesPossible = totalAmount / inputs.get(i).getAmount();
			if (processesPossible < processAmount) {
				if (processesPossible == 0) {
					return 0; //Return early if nothing can be done.
				}
				processAmount = processesPossible;
			}
			itemsPerIngredientType.add(gatheredInput);
		}

		// Finally, process the entities, shrinking their stacks or killing them if drained entirely.
		for (int i = 0; i < itemsPerIngredientType.size(); i++) {
			reduceStacks(itemsPerIngredientType.get(i), processAmount * inputs.get(i).getAmount());
		}

		// Pass the processes to the caller to output the results.
		return processAmount;
	}

	private void reduceStacks(List<EntityItem> items, int inputAmount) {
		for (EntityItem item : items) {
			ItemStack itemStack = item.getItem();
			if (itemStack.getCount() > inputAmount) {
				itemStack.shrink(inputAmount);
				item.setItem(itemStack);
				return;
			} else {
				inputAmount -= itemStack.getCount();
				item.setItem(ItemStack.EMPTY);
				item.setDead();
				if (inputAmount == 0) {
					return;
				}
			}
		}
	}

	private List<EntityItem> getItemsForIngredient(List<EntityItem> items, IIngredient ingredient) {
		List<EntityItem> gatheredInput = new ArrayList<>();

		for (Iterator<EntityItem> iterator = items.iterator(); iterator.hasNext(); ) {
			EntityItem entity = iterator.next();
			if (ingredient.matches(CraftTweakerMC.getIItemStack(entity.getItem()))) {
				gatheredInput.add(entity);
				iterator.remove();
			}
		}
		return gatheredInput;
	}

	@Override
	public boolean matches(IItemStack itemStack) {
		return inputs.get(0).matches(itemStack);
	}

	@Override
	public Collection<Item> getMatchingItems() {
		return inputs.get(0).getItems().stream()
				.map(CraftTweakerMC::getItemStack)
				.map(ItemStack::getItem).distinct()
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(getClass().getName()).append('{');
		for (Iterator<IIngredient> iterator = inputs.iterator(); iterator.hasNext(); ) {
			b.append(iterator.next());
			if (iterator.hasNext())
				b.append(',');
		}
		return b.append('}').toString();
	}
}
