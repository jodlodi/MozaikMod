package com.mod.mozaik.data.gen.loot;

import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModLootTables;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ModArchaeologyLootGen(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> register) {
		register.accept(ModLootTables.DESERT_ARCHAEOLOGY, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.SANDSTONE_SHARDS.get()).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.RED_SANDSTONE_SHARDS.get()).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.DYED_TERRACOTTA_SHARDS.lime().get()).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.orange().get()).setWeight(2))
						.add(LootItem.lootTableItem(Items.ARMS_UP_POTTERY_SHERD).setWeight(2))
						.add(LootItem.lootTableItem(Items.BREWER_POTTERY_SHERD).setWeight(2))
						.add(LootItem.lootTableItem(Items.BRICK))
						.add(LootItem.lootTableItem(Items.EMERALD))
						.add(LootItem.lootTableItem(Items.STICK))
						.add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F)).withEffect(MobEffects.JUMP, UniformGenerator.between(7.0F, 10.0F)).withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6.0F, 8.0F)).withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5.0F, 7.0F)).withEffect(MobEffects.POISON, UniformGenerator.between(10.0F, 20.0F)).withEffect(MobEffects.SATURATION, UniformGenerator.between(7.0F, 10.0F))))
				)
		);
		register.accept(ModLootTables.LUKEWARM_MOSAIC_ARCHAEOLOGY, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.cyan().get()).setWeight(2))
								.add(LootItem.lootTableItem(ModItems.DYED_TERRACOTTA_SHARDS.red().get()).setWeight(2))
								.add(LootItem.lootTableItem(Items.ANGLER_POTTERY_SHERD))
								.add(LootItem.lootTableItem(Items.SHELTER_POTTERY_SHERD))
								.add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD))
								.add(LootItem.lootTableItem(Items.SNIFFER_EGG))
								.add(LootItem.lootTableItem(Items.IRON_AXE))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
								.add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
								.add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
								.add(LootItem.lootTableItem(Items.COAL).setWeight(2))
								.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))
						)
		);
		register.accept(ModLootTables.COLD_MOSAIC_ARCHAEOLOGY, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.ANCIENT_DEBRIS_SHARDS.get()).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.STAINED_GLASS_SHARDS.gray().get()).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.DYED_TERRACOTTA_SHARDS.gray().get()).setWeight(2))
						.add(LootItem.lootTableItem(Items.BLADE_POTTERY_SHERD))
						.add(LootItem.lootTableItem(Items.EXPLORER_POTTERY_SHERD))
						.add(LootItem.lootTableItem(Items.MOURNER_POTTERY_SHERD))
						.add(LootItem.lootTableItem(Items.PLENTY_POTTERY_SHERD))
						.add(LootItem.lootTableItem(Items.IRON_AXE))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
						.add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(2))
						.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))
				)
		);
	}
}
