## ItemConversionBuilder

The `ItemConversionBuilder` allows you to cause item entities destroyed by explosions to transform into other items defined directly or through a loot table.

The class can be imported with:  
```
import mods.explosioncrafting.ItemConversionBuilder;
```

### Quick list of methods
```
// Creating a builder and adding inputs
static ItemConversionBuilder create(IIngredient input) // Chance mode - all results are ran
static ItemConversionBuilder createWeighted(IIngredient input) // Experimental: weighted mode - chances are turned into integer weights, only one is picked
ItemConversionBuilder addInput(IIngredient input) 

// Adding outputs. Builder methods - can be chained (and it is encouraged).
ItemConversionBuilder addOutput(WeightedItemStack output, @Optional float chance) 
ItemConversionBuilder addLootTableOutput(String name, @Optional float chance, @Optional int maxDrops)

// Building and registering the conversion 
void build() 
```

### Creating a conversion

Conversions are created with IIngredients. The first ingredient must have a list of matching items to work - `<*>` will not work as it doesn't have a list of associated items, for example. Additional inputs have no such restriction.

**Note:** Currently, if multiple recipes would match the first primary item, only the first match will be attempted. Additionally, if one item matches multiple ingredients in a recipe, it will be only taken into account for the first matching one.   

```
var example = ItemConversionBuilder.create(<minecraft:diamond>); // Start a recipe that takes a diamond.
example.addInput(<ore:cobblestone>); // Add the oredict entry for cobblestone to the recipe
```

### Adding results and building the conversion
These are the types of outputs currently present:
* Standard drop - drops a single item with a chance.
* Loot table drop - drops from a loot table. You can set maximum amount of dropped items, by default it will drop all the generated loot.

In normal mode, unlike block conversions, all results are ran. 
If using the weighted mode, the chance is converted to an integer by rounding into a weight, and only one result is picked.

```
ItemConversionBuilder.create(<ore:gemDiamond>)
    .addOutput(<minecraft:cobblestone> % 20) // Drops cobblestone 20% of the time. 
    .addLootTableOutput("minecraft:chests/simple_dungeon", 0.3, 3) // Drops 3 items from the dungeon loot table 30% of the time.
    .build(); // Builds and registers the conversion.
```
