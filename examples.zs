import mods.explosioncrafting.BlockConversionBuilder;
import mods.explosioncrafting.ItemConversionBuilder;

import crafttweaker.item.IItemStack;
import crafttweaker.item.WeightedItemStack;

// Block conversions:

// static BlockConversionBuilder of(IItemStack stack) 
// static BlockConversionBuilder of(String block, String propertyPredicate) 
// BlockConversionBuilder addConversion(String block, String properties, @Optional int weight) 
// BlockConversionBuilder addConversion(IItemStack block, @Optional int weight) 
// BlockConversionBuilder addDrop(IItemStack item, @Optional int weight) 
// BlockConversionBuilder addConversionDrop(String block, String properties, @Optional int weight) 
// BlockConversionBuilder addConversionDrop(IItemStack block, @Optional int weight) 
// BlockConversionBuilder addLootTableDrop(String lootTable, @Optional int weight, @Optional int maxDrops) 
// BlockConversionBuilder addOutputGroup(WeightedItemStack[] items, @Optional int weight) 
// BlockConversionBuilder addPass(int weight) 
// BlockConversionBuilder convertToAir(int weight) 
// void build()
// static void removeDrop(String block, String properties) 


// Item conversions:

// static ItemConversionBuilder create(IIngredient input) 
// ItemConversionBuilder addInput(IIngredient input) 
// ItemConversionBuilder addOutput(WeightedItemStack output) 
// ItemConversionBuilder addLootTableOutput(String name, @Optional float chance, @Optional int maxDrops)
// void build() 


val leavesToSticks = BlockConversionBuilder.of(<blockstate:minecraft:leaves>.matchBlock());
leavesToSticks.addDrop(<minecraft:stick> * 3);
leavesToSticks.addPass(5);
leavesToSticks.build();

val outputs = [<minecraft:diamond> % 80, <minecraft:dirt> % 99, <minecraft:gold_ingot> % 50] as WeightedItemStack[];

BlockConversionBuilder.of(<blockstate:minecraft:stone:variant=granite>)
    .addDrop(<minecraft:cobblestone> * 7, 3)
    .addLootTableDrop("minecraft:chests/simple_dungeon", 2)
    .addOutputGroup(outputs)
    .addOutputGroup([<minecraft:emerald> % 80, <minecraft:stick> % 99])
    .build();

BlockConversionBuilder.of(<blockstate:minecraft:coal_ore>)
    .addConversion(<blockstate:minecraft:coal_block>)
    .build();

ItemConversionBuilder.create(<ore:ingotIron>)
    .addOutput(<minecraft:iron_nugget> * 9 % 80)
    .build();

ItemConversionBuilder.create(<ore:ingotGold>)
    .addInput(<ore:cobblestone>)
    .addInput(<ore:ingotBrick>)
    .addOutput(<minecraft:nether_wart> % 100)
    .build();

ItemConversionBuilder.create(<minecraft:brick> * 4)
    .addOutput(<minecraft:brick_block> % 98)
    .addLootTableOutput("minecraft:chests/spawn_bonus_chest", 0.2)
    .build();

BlockConversionBuilder.of(<blockstate:minecraft:brick_block>)
    .addLootTableDrop("minecraft:chests/spawn_bonus_chest", 1, 2)
    .build();

ItemConversionBuilder.create(<minecraft:coal_block>)
    .addOutput(<minecraft:coal:1> % 90)
    .build();

BlockConversionBuilder.removeDrop(<blockstate:minecraft:diamond_ore>);
BlockConversionBuilder.removeDrop(<blockstate:minecraft:stone_stairs>.withMatchedValuesForProperty("half", "top"));
BlockConversionBuilder.removeDrop(<blockstate:minecraft:oak_stairs>.matchBlock()); 
