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

package hubry.explosh;

import hubry.explosh.conversion.block.IBlockConv;
import hubry.explosh.conversion.item.IItemConv;
import hubry.explosh.util.LootTableChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = ExplosionCrafting.MODID,
		name = ExplosionCrafting.NAME,
		version = ExplosionCrafting.VERSION,
		dependencies = "required-after:crafttweaker@[4.1.12,)"
)
public class ExplosionCrafting {
	public static final String MODID = "explosioncrafting";
	public static final String NAME = "Explosion Crafting";
	public static final String VERSION = "0.5-alpha";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		LootTableChecker.validateTables();
		ExplosionEventHandler.buildConversionMap();
	}

	public static void addItemConversion(IItemConv itemConv) {
		if (ExplosionEventHandler.itemConverters == null)
			throw new IllegalStateException("Too late to add conversions!");
		ExplosionEventHandler.itemConverters.add(itemConv);
	}

	public static void addBlockConversion(IBlockConv blockConv) {
		if (ExplosionEventHandler.blockConverters == null)
			throw new IllegalStateException("Too late to add conversions!");
		ExplosionEventHandler.blockConverters.add(blockConv);
	}
}
