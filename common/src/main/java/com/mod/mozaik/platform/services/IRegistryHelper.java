package com.mod.mozaik.platform.services;

import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@NullMarked
public interface IRegistryHelper {

	ResourceSupplier<SoundEvent> registerSoundEvent(String id, Function<Identifier, SoundEvent> soundEvent);

	<T extends ParticleType<?>> ResourceSupplier<T> registerParticleType(String id, Supplier<T> particle);

	<T extends BlockEntity, B extends Block> ResourceSupplier<BlockEntityType<T>> registerBlockEntityType(String id, BiFunction<BlockPos, BlockState, T> supplier, List<ResourceSupplier<B>> blocks);

	<T extends Entity> ResourceSupplier<EntityType<T>> registerEntityType(String id, EntityType.Builder<T> builder);

	<T extends LivingEntity> void registerEntityAttributes(Supplier<EntityType<T>> entityType, Supplier<AttributeSupplier> attributes);

	<T extends Item> ResourceSupplier<T> registerItem(String id, Function<Item.Properties, T> item);

	<T extends Block> ResourceSupplier<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties);

	<T extends AbstractContainerMenu> ResourceSupplier<MenuType<T>> registerMenu(String id, BiFunction<Integer, Inventory, T> factory);

	ResourceSupplier<CreativeModeTab> registerCreativeTab(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems);

	ResourceSupplier<GameEvent> registerGameEvent(String id, int notificationRadius);

	ResourceSupplier<MobEffect> registerMobEffect(String id, Supplier<MobEffect> effect);

	<T extends Sensor<?>> ResourceSupplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor);

	ResourceSupplier<Activity> registerActivity(String id);

	<T> ResourceSupplier<MemoryModuleType<T>> registerMemoryModuleType(String id, @Nullable Codec<T> codec);

	ResourceSupplier<StructurePieceType> registerStructurePieceType(String id, StructurePieceType typeSupplier);

	<T extends StructureType<?>> ResourceSupplier<T> registerStructureType(String id, Supplier<T> structureType);

	ResourceSupplier<ShardMaterial> registerShardMaterial(String id, Supplier<ShardMaterial> shardMaterial);

	<T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey);

	<T> ResourceSupplier<DataComponentType<T>> registerDataComponent(String id, final Codec<T> codec, final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec);
}
