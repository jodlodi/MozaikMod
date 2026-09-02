package com.mod.mozaik.platform;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.services.IRegistryHelper;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.flag.FeatureFlags;
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
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@NullMarked
public class FabricRegistryHelper implements IRegistryHelper {

	@Override
	public <T extends Entity> ResourceSupplier<EntityType<T>> registerEntityType(String id, EntityType.Builder<T> builder) {
		Identifier location = Constants.prefix(id);
		EntityType<T> entityType = builder.build(ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), location));
		Registry.register(BuiltInRegistries.ENTITY_TYPE, location, entityType);
		return new ResourceSupplier<>(() -> entityType, location);
	}

	@Override
	public ResourceSupplier<SoundEvent> registerSoundEvent(String id, Function<Identifier, SoundEvent> soundEvent) {
		Identifier location = Constants.prefix(id);
		SoundEvent sound = soundEvent.apply(location);
		Registry.register(BuiltInRegistries.SOUND_EVENT, location, sound);
		return new ResourceSupplier<>(() -> sound, location);
	}

	@Override
	public <T extends ParticleType<?>> ResourceSupplier<T> registerParticleType(String id, Supplier<T> particle) {
		Identifier location = Constants.prefix(id);
		T registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, location, particle.get());
		return new ResourceSupplier<>(() -> registered, location);
	}

	@Override
	public final <T extends BlockEntity, B extends Block> ResourceSupplier<BlockEntityType<T>> registerBlockEntityType(String id, BiFunction<BlockPos, BlockState, T> supplier, List<ResourceSupplier<B>> blocks) {
		Identifier location = Constants.prefix(id);

		FabricBlockEntityTypeBuilder<T> builder = FabricBlockEntityTypeBuilder.create(supplier::apply);
		for (Supplier<B> blockSupplier : blocks) builder.addBlock(blockSupplier.get());

		BlockEntityType<T> type = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location, builder.build());
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public <T extends LivingEntity> void registerEntityAttributes(Supplier<EntityType<T>> entityType, Supplier<AttributeSupplier> attributes) {
		FabricDefaultAttributeRegistry.register(entityType.get(), attributes.get());
	}

	@Override
	public <T extends Item> ResourceSupplier<T> registerItem(String id, Function<Item.Properties, T> item) {
		Identifier location = Constants.prefix(id);
		T registered = Registry.register(BuiltInRegistries.ITEM, location, item.apply(new Item.Properties().setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), location))));
		return new ResourceSupplier<>(() -> registered, location);
	}

	@Override
	public <T extends Block> ResourceSupplier<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		Identifier location = Constants.prefix(id);
		T registered = Registry.register(BuiltInRegistries.BLOCK, location, block.apply(properties.get().setId(ResourceKey.create(BuiltInRegistries.BLOCK.key(), location))));
		return new ResourceSupplier<>(() -> registered, location);
	}

	@Override
	public <T extends AbstractContainerMenu> ResourceSupplier<MenuType<T>> registerMenu(String id, BiFunction<Integer, Inventory, T> factory) {
		Identifier location = Constants.prefix(id);
		MenuType<T> registered = Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS));
		return new ResourceSupplier<>(() -> registered, location);
	}

	@Override
	public ResourceSupplier<CreativeModeTab> registerCreativeTab(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
		Identifier location = Constants.prefix(id);
		CreativeModeTab tab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, location, FabricCreativeModeTab.builder()
				.title(Component.translatable("item_group." + Constants.MOD_ID + "." + id))
				.icon(icon)
				.displayItems(displayItems)
				.build());
		return new ResourceSupplier<>(() -> tab, location);
	}

	@Override
	public ResourceSupplier<GameEvent> registerGameEvent(String id, int notificationRadius) {
		Identifier location = Constants.prefix(id);
		GameEvent gameEvent = Registry.register(BuiltInRegistries.GAME_EVENT, location, new GameEvent(notificationRadius));
		return new ResourceSupplier<>(() -> gameEvent, location);
	}

	@Override
	public ResourceSupplier<MobEffect> registerMobEffect(String id, Supplier<MobEffect> effect) {
		Identifier location = Constants.prefix(id);
		MobEffect mobEffect = Registry.register(BuiltInRegistries.MOB_EFFECT, location, effect.get());
		return new ResourceSupplier<>(() -> mobEffect, location);
	}

	@Override
	public <T extends Sensor<?>> ResourceSupplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor) {
		Identifier location = Constants.prefix(id);
		SensorType<T> type = Registry.register(BuiltInRegistries.SENSOR_TYPE, location, new SensorType<>(sensor));
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public ResourceSupplier<Activity> registerActivity(String id) {
		Identifier location = Constants.prefix(id);
		Activity type = Registry.register(BuiltInRegistries.ACTIVITY, location, new Activity(id));
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public <T> ResourceSupplier<MemoryModuleType<T>> registerMemoryModuleType(String id, @Nullable Codec<T> codec) {
		Identifier location = Constants.prefix(id);
		MemoryModuleType<T> type = Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, location, new MemoryModuleType<>(Optional.ofNullable(codec)));
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public ResourceSupplier<StructurePieceType> registerStructurePieceType(String id, StructurePieceType typeSupplier) {
		Identifier location = Constants.prefix(id);
		StructurePieceType type = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, location, typeSupplier);
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public <T extends StructureType<?>> ResourceSupplier<T> registerStructureType(String id, Supplier<T> structureType) {
		Identifier location = Constants.prefix(id);
		T type = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, location, structureType.get());
		return new ResourceSupplier<>(() -> type, location);
	}

	@Override
	public ResourceSupplier<ShardMaterial> registerShardMaterial(String id, Supplier<ShardMaterial> shardMaterial) {
		Identifier location = Constants.prefix(id);
		ShardMaterial material = Registry.register(ModRegistries.SHARD_MATERIALS, location, shardMaterial.get());
		return new ResourceSupplier<>(() -> material, location);
	}

	@Override
	public ResourceSupplier<PolyominoShape> registerPolyominoShape(String id, Supplier<PolyominoShape> shardMaterial) {
		Identifier location = Constants.prefix(id);
		PolyominoShape shape = Registry.register(ModRegistries.POLYOMINO_SHAPES, location, shardMaterial.get());
		return new ResourceSupplier<>(() -> shape, location);
	}

	@Override
	public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey) {
		return FabricRegistryBuilder.create(resourceKey).buildAndRegister();
	}

	@Override
	public <T> ResourceSupplier<DataComponentType<T>> registerDataComponent(String id, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		Identifier location = Constants.prefix(id);
		DataComponentType<T> type = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, location, DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
		return new ResourceSupplier<>(() -> type, location);
	}
}
