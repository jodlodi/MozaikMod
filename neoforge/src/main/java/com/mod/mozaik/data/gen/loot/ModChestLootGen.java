package com.mod.mozaik.data.gen.loot;

import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModLootTables;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ModChestLootGen(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> register) {
		register.accept(ModLootTables.WARPED_MOSAIC_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.NETHERRACK_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.ANCIENT_DEBRIS_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.red().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.orange().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.yellow().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.BASALT_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.RED_NETHER_BRICK_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
				)
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(4.0F, 8.0F))
						.add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(Items.FLINT).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
						.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(9.0F, 18.0F))))
						.add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(40))
						.add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
						.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 24.0F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(5))
						.add(LootItem.lootTableItem(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5))
						.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
						.add(LootItem.lootTableItem(Items.CLOCK).setWeight(5))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
						.add(LootItem.lootTableItem(Items.BELL).setWeight(1))
						.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
						.add(LootItem.lootTableItem(Items.GOLD_BLOCK).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				)
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(EmptyLootItem.emptyItem().setWeight(1))
						.add(LootItem.lootTableItem(Items.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				)
		);
		register.accept(ModLootTables.LUKEWARM_MOSAIC_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.STONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.cyan().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.GLAZED_TERRACOTTA_SHARDS.black().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.GLAZED_TERRACOTTA_SHARDS.red().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.SANDSTONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.DYED_TERRACOTTA_SHARDS.white().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
				)
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 8.0F))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
						.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.EMERALD))
						.add(LootItem.lootTableItem(Items.STONE_SPEAR).setWeight(2))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
				)
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
						.add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
						.add(LootItem.lootTableItem(Items.GOLDEN_HELMET))
						.add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte) 1).setSkipKnownStructures(false)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME)))
				)
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(EmptyLootItem.emptyItem().setWeight(148))
						.add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
				)
		);

		register.accept(ModLootTables.COLD_MOSAIC_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.STONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.SEA_LANTERN_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.GLAZED_TERRACOTTA_SHARDS.lightBlue().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.GLAZED_TERRACOTTA_SHARDS.green().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.SANDSTONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.DYED_TERRACOTTA_SHARDS.lime().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
				).withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 8.0F))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
						.add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(2))
						.add(LootItem.lootTableItem(Items.STONE_SPEAR).setWeight(2))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5))
						.add(LootItem.lootTableItem(Items.EMERALD))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
				).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
						.add(LootItem.lootTableItem(Items.GOLDEN_HELMET))
						.add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
						.add(LootItem.lootTableItem(Items.MAP).setWeight(5).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte) 1).setSkipKnownStructures(false)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME)))
				).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(EmptyLootItem.emptyItem().setWeight(148))
						.add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
						.add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
				)
		);

		this.generateTemplate(register, ModLootTables.BUTTON_TEMPLATE_CHEST, ModItems.BUTTON_TEMPLATE);
		this.generateTemplate(register, ModLootTables.BONE_TEMPLATE_CHEST, ModItems.BONE_TEMPLATE);
		this.generateTemplate(register, ModLootTables.BUBBLE_TEMPLATE_CHEST, ModItems.BUBBLE_TEMPLATE);
		this.generateTemplate(register, ModLootTables.WORM_TEMPLATE_CHEST, ModItems.WORM_TEMPLATE);
		this.generateTemplate(register, ModLootTables.CANE_TEMPLATE_CHEST, ModItems.CANE_TEMPLATE);
		this.generateTemplate(register, ModLootTables.POINT_TEMPLATE_CHEST, ModItems.POINT_TEMPLATE);
		this.generateTemplate(register, ModLootTables.HORN_TEMPLATE_CHEST, ModItems.HORN_TEMPLATE);
		this.generateTemplate(register, ModLootTables.TREE_TEMPLATE_CHEST, ModItems.TREE_TEMPLATE);
		this.generateTemplate(register, ModLootTables.FORK_TEMPLATE_CHEST, ModItems.FORK_TEMPLATE);
	}

	public void generateTemplate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> register, ResourceKey<LootTable> key, ResourceSupplier<PolyominoItem> supplier) {
		register.accept(key, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(supplier.get()))
				)
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.STONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.SEA_LANTERN_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.SANDSTONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.RESIN_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.BONE_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.QUARTZ_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.MOSSY_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.RAW_GOLD_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
						.add(LootItem.lootTableItem(ModItems.TUFF_SHARDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 24.0F))))
				).withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 8.0F))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
						.add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5))
						.add(LootItem.lootTableItem(Items.EMERALD))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.LEATHER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.STICK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
				).withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.MORTARS.white().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.orange().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.magenta().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.lightBlue().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.yellow().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.lime().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.pink().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.gray().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.lightGray().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.cyan().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.purple().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.blue().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.brown().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.green().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.red().get()))
						.add(LootItem.lootTableItem(ModItems.MORTARS.black().get()))
				)
		);
	}
}
