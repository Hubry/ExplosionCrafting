## BlockConversionBuilder

The `BlockConversionBuilder` allows you to cause blocks destroyed by explosions to drop items, turn into other blocks, or even drop loot from the specified loot table.

The class can be imported with:  
```
import mods.explosioncrafting.BlockConversionBuilder;
```

### Quick list of methods
```
// Creating a builder
static BlockConversionBuilder of(IBlockStateMatcher block) 

// Adding outputs. Builder methods - can be chained (and it is encouraged).
BlockConversionBuilder addConversion(IBlockState block, @Optional int weight) 
BlockConversionBuilder addDrop(IItemStack item, @Optional int weight) 
BlockConversionBuilder addConversionDrop(IBlockState block, @Optional int weight) 
BlockConversionBuilder addLootTableDrop(String lootTable, @Optional int weight, @Optional int maxDrops) 
BlockConversionBuilder addOutputGroup(WeightedItemStack[] items, @Optional int weight) 
BlockConversionBuilder addPass(int weight) 
BlockConversionBuilder convertToAir(int weight) 

// Creates and registers the conversion
void build()

// Shortcut for making a conversion that only sets the block to air
static void removeDrop(IBlockStateMatcher block) 
```

### Creating a conversion

Creating a conversion requires providing a blockstate matcher. The resulting conversion will accept any blockstate this matcher accepts. For a blockstate matcher guide, see the next section.

```
var conv  = BlockConversionBuilder.of(<blockstate:minecraft:cobblestone>); // Accepts default state of cobblestone
var conv2 = BlockConversionBuilder.of(<blockstate:minecraft:dirt>.matchBlock()); // Accepts any state of dirt
var conv3 = BlockConversionBuilder.of(<blockstate:minecraft:stone_stairs>.withMatchedValuesForProperty("half", "top"); // Accepts any upside down stone stairs
```

#### Quick blockstate matcher guide

The simplest matcher is a blockstate, obtained through CraftTweaker's blockstate bracket handler, like: `<blockstate:minecraft:dirt>` or `<blockstate:minecraft:log:variant=spruce>`. Note that this only matches a single block state - the dirt state won't match podzol or coarse dirt - only plain dirt. In addition, blocks in world with states that are not saved (like fence connections or snowy dirt) will be treated as if they had the default value of that property (freestanding fences, non-snowy dirt).

**Note:** you can view all properties of a certain block by looking at it with the F3 debug view enabled - the blockstate info is displayed on the right side of the screen.

Using `IBlockState#withMatchedValuesForProperty(String property, String... values)` allows to match one property with specified values and ignore others, for example, `<blockstate:minecraft:piston>.withMatchedValuesForProperty("extended", "false")` will match pistons of any direction, as long as it's not extended.

Using `IBlockState#matchBlock()` will match any state of that block, eg. `<blockstate:minecraft:dirt>.matchBlock()` will ignore the dirt type.

Lastly, blockstate matchers can be combined by ORing (`|`) them - eg. `<blockstate:minecraft:dirt> | <blockstate:minecraft:log>.matchBlock()` will match normal dirt and the basic 4 log types.

### Adding results and building the conversion

Types of results: 
* Conversion - the block turns into an another block. 
    * Air conversion - replaces the block to air.
* Drop item - replaces the block with air and drops an item.
    * Single drop - drops the specified stack and replaces the block with air. 
    * Group drop - drops an array of items. Each item can have a different chance to drop.
    * Loot table drop - outputs from a defined loot table. You can set maximum amount of dropped items, by default it will drop all the generated loot.
    * Conversion drop - turns the block into a different one and hands it off to vanilla explosion mechanics. This respects the vanilla mechanics of stronger explosions reducing the drop chance (for example, blocks broken by a creeper's explosion have a 1/3 chance to drop items).
* Pass - do nothing to the block. Note that this *will not* prevent damage to blocks behind blocks set to pass. If you want explosion-proof blocks, see `IBlockDefinition#setResistance`.

Block parameters only accept blockstates and not blockstate matchers like in the builder creation.

Each result has a weight - only one result can be picked and the weight determines how often it will happen compared to other ones. Weight parameters are optional - if the weight is omitted, it defaults to 1.

After all the results were created, call `build()` on the conversion object to register it.

### Example converter
```
BlockConversionBuilder.of(<blockstate:minecraft:dirt>.matchBlock()) // Converter for dirt in any state
    .addConversion(<blockstate:minecraft:cobblestone>, 6) // Turns the block to cobblestone, in the default state, at weight 6.
    .addConversion(<blockstate:minecraft:stone:variant=granite>, 3) // Turns the block to granite, at weight 3.
    .addDrop(<minecraft:diamond>) // Drops a diamond. Since weight was skipped, it defaults to 1.
    .addGroupDrop([<minecraft:diamond> % 80, <minecraft:dirt> % 99]) // Drops a diamond at an 80% chance and dirt at a 99% chance.
    .addConversionDrop(<blockstate:minecraft:coal_ore>, 1) // Turns the block into coal ore and then breaks it, dropping coal and experience with the default explosion drop chance.
    .addLootTableDrop("minecraft:chests/spawn_bonus_chest", 1, 2) // Drops 2 items from a randomly generated bonus chest.
    .build() // Builds and registers the converter. 
```
