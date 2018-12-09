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

package hubry.explosh.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import crafttweaker.CraftTweakerAPI;

import java.util.ArrayList;
import java.util.List;

public class LootTableChecker {
	private static final List<ResourceLocation> lootTables = new ArrayList<>();

	public static void checkTable(ResourceLocation lootTable) {
		if (!ModConfig.ignoreInvalidLootTableNames && !lootTables.contains(lootTable)) {
			lootTables.add(lootTable);
		}
	}

	// Done on post-init, because many mods register on init. Maybe should be done on load-complete instead?
	public static void validateTables() {
		boolean noneMissing = true;
		for (ResourceLocation lootTable : lootTables) {
			if (!LootTableList.getAll().contains(lootTable)) {
				if (noneMissing) {
					noneMissing = false;
					CraftTweakerAPI.logWarning("Found usage of unregistered loot tables in explosion conversions!");
					CraftTweakerAPI.logWarning("If this is not a mistake, disable this check in mod config options.");
					CraftTweakerAPI.logWarning("The missing table list will be printed in your mod CraftTweaker log.");
					CraftTweakerAPI.logDefault("Missing tables:");
				}
				CraftTweakerAPI.logDefault("    " + lootTable);
			}
		}
		lootTables.clear();
	}

	private LootTableChecker() {
	}
}
