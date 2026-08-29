package com.mod.mozaik.platform;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.services.IRegistryHelper;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@NullMarked
public class NeoForgeRegistryHelper implements IRegistryHelper {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, Constants.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);
	public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, Constants.MOD_ID);
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
	public static final DeferredRegister<ShardMaterial> SHARD_MATERIALS = DeferredRegister.create(ModRegistries.ModKeys.SHARD_MATERIAL, Constants.MOD_ID);
	public static final DeferredRegister<PolyominoShape> POLYOMINO_SHAPES = DeferredRegister.create(ModRegistries.ModKeys.POLYOMINO_SHAPE, Constants.MOD_ID);
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Constants.MOD_ID);
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);

	public static final Map<Supplier<? extends EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier>> ATTRIBUTES = new HashMap<>();

	@Override
	public ResourceSupplier<SoundEvent> registerSoundEvent(String id, Function<Identifier, SoundEvent> soundEvent) {
		Identifier location = Constants.prefix(id);
		return new ResourceSupplier<>(SOUND_EVENTS.register(id, () -> soundEvent.apply(location)), location);
	}

	@Override
	public <T extends ParticleType<?>> ResourceSupplier<T> registerParticleType(String id, Supplier<T> particle) {
		return new ResourceSupplier<>(PARTICLE_TYPES.register(id, particle), Constants.prefix(id));
	}

	@Override
	public final <T extends BlockEntity, B extends Block> ResourceSupplier<BlockEntityType<T>> registerBlockEntityType(String id, BiFunction<BlockPos, BlockState, T> supplier, List<ResourceSupplier<B>> blocks) {
		Identifier location = Constants.prefix(id);

		Block[] blockArray  = new Block[blocks.size()];
		for (int i = 0; i < blocks.size(); i++) blockArray[i] = blocks.get(i).get();

		return new ResourceSupplier<>(BLOCK_ENTITY_TYPES.register(id, () -> new BlockEntityType<>(supplier::apply, blockArray)), location);
	}

	@Override
	public <T extends Entity> ResourceSupplier<EntityType<T>> registerEntityType(String id, EntityType.Builder<T> builder) {
		Identifier location = Constants.prefix(id);
		return new ResourceSupplier<>(ENTITY_TYPES.register(id, () -> builder.build(ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), location))), location);
	}

	@Override
	public <T extends LivingEntity> void registerEntityAttributes(Supplier<EntityType<T>> entityType, Supplier<AttributeSupplier> attributes) {
		ATTRIBUTES.put(entityType, attributes);
	}

	@Override
	public <T extends Item> ResourceSupplier<T> registerItem(String id, Function<Item.Properties, T> item) {
		Identifier location = Constants.prefix(id);
		return new ResourceSupplier<>(ITEMS.register(id, () -> item.apply(new Item.Properties().setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), location)))), location);
	}

	@Override
	public <T extends Block> ResourceSupplier<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		Identifier location = Constants.prefix(id);
		return new ResourceSupplier<>(BLOCKS.register(id, () -> block.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK, location)))), location);
	}

	@Override
	public <T extends AbstractContainerMenu> ResourceSupplier<MenuType<T>> registerMenu(String id, BiFunction<Integer, Inventory, T> factory) {
		return new ResourceSupplier<>(MENU_TYPES.register(id, () -> new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS)), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<CreativeModeTab> registerCreativeTab(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
		return new ResourceSupplier<>(TABS.register(id, () -> CreativeModeTab.builder()
				.title(Component.translatable("item_group." + Constants.MOD_ID + "." + id))
				.icon(icon)
				.displayItems(displayItems)
				.build()), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<GameEvent> registerGameEvent(String id, int notificationRadius) {
		Identifier location = Constants.prefix(id);
		return new ResourceSupplier<>(GAME_EVENTS.register(id, () -> new GameEvent(notificationRadius)), location);
	}

	@Override
	public ResourceSupplier<MobEffect> registerMobEffect(String id, Supplier<MobEffect> effect) {
		return new ResourceSupplier<>(MOB_EFFECTS.register(id, effect), Constants.prefix(id));
	}

	@Override
	public <T extends Sensor<?>> ResourceSupplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor) {
		return new ResourceSupplier<>(SENSOR_TYPES.register(id, () -> new SensorType<T>(sensor)), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<Activity> registerActivity(String id) {
		return new ResourceSupplier<>(ACTIVITIES.register(id, () -> new Activity(id)), Constants.prefix(id));
	}

	@Override
	public <T> ResourceSupplier<MemoryModuleType<T>> registerMemoryModuleType(String id, @Nullable Codec<T> codec) {
		return new ResourceSupplier<>(MEMORY_MODULE_TYPES.register(id, () -> new MemoryModuleType<>(Optional.ofNullable(codec))), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<StructurePieceType> registerStructurePieceType(String id, StructurePieceType typeSupplier) {
		return new ResourceSupplier<>(STRUCTURE_PIECE_TYPES.register(id, () -> typeSupplier), Constants.prefix(id));
	}

	@Override
	public <T extends StructureType<?>> ResourceSupplier<T> registerStructureType(String id, Supplier<T> structureType) {
		return new ResourceSupplier<>(STRUCTURE_TYPES.register(id, structureType), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<ShardMaterial> registerShardMaterial(String id, Supplier<ShardMaterial> shardMaterial) {
		return new ResourceSupplier<>(SHARD_MATERIALS.register(id, shardMaterial), Constants.prefix(id));
	}

	@Override
	public ResourceSupplier<PolyominoShape> registerPolyominoShape(String id, Supplier<PolyominoShape> shapeSupplier) {
		return new ResourceSupplier<>(POLYOMINO_SHAPES.register(id, shapeSupplier), Constants.prefix(id));
	}

	@Override
	public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey) {
		return new RegistryBuilder<>(resourceKey).sync(true).create();
	}

	@Override
	public <T> ResourceSupplier<DataComponentType<T>> registerDataComponent(String id, final Codec<T> codec, final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return new ResourceSupplier<>(DATA_COMPONENTS.register(id, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build()), Constants.prefix(id));
	}
}
