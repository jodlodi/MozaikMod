package com.mod.mozaik.blocks;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.mixin.ServerPlayerAccessor;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MortarBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.box(0.0D, 0.0D, 0.5D, 16.0D, 16.0D, 16.0D));
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<DirectionAndRotation> FACING_ROTATED = EnumProperty.create("facing_rotated", DirectionAndRotation.class);

	public static final MapCodec<MortarBlock> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					DyeColor.CODEC.fieldOf("color").forGetter(MortarBlock::getColor),
					Properties.CODEC.fieldOf("properties").forGetter(BlockBehaviour::properties)
			).apply(instance, MortarBlock::new)
	);

	private final DyeColor color;

	public MortarBlock(DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING_ROTATED, DirectionAndRotation.UP_0).setValue(WATERLOGGED, false));
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING_ROTATED).getDirection());
	}

	@Override
	protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		return Shapes.block();
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING_ROTATED).getDirection());
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING_ROTATED, state.getValue(FACING_ROTATED).rotate(rotation));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING_ROTATED, state.getValue(FACING_ROTATED).mirror(mirror));
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return SHAPES.get(state.getValue(FACING_ROTATED).getDirection());
	}

	@Override
	protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPES.get(state.getValue(FACING_ROTATED).getDirection());
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING_ROTATED).getDirection());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());

		DirectionAndRotation directionAndRotation = switch (context.getClickedFace()) {
			case DOWN -> switch (context.getHorizontalDirection()) {
				case Direction.WEST -> DirectionAndRotation.DOWN_90;
				case Direction.SOUTH -> DirectionAndRotation.DOWN_180;
				case Direction.EAST -> DirectionAndRotation.DOWN_270;
				default -> DirectionAndRotation.DOWN_0;
			};
			case UP -> switch (context.getHorizontalDirection()) {
				case Direction.WEST -> DirectionAndRotation.UP_270;
				case Direction.SOUTH -> DirectionAndRotation.UP_180;
				case Direction.EAST -> DirectionAndRotation.UP_90;
				default -> DirectionAndRotation.UP_0;
			};
			case NORTH -> DirectionAndRotation.NORTH;
			case SOUTH -> DirectionAndRotation.SOUTH;
			case WEST -> DirectionAndRotation.WEST;
			case EAST -> DirectionAndRotation.EAST;
		};

		return this.defaultBlockState().setValue(FACING_ROTATED, directionAndRotation).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING_ROTATED, WATERLOGGED);
	}

	@Override
	protected MapCodec<MortarBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
		return Services.MODLOADER.mortarBlockEntity(pos, blockState);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (itemStack.getItem() instanceof BucketItem bucketItem && (
				(bucketItem.getContent() == Fluids.WATER && !state.getValue(WATERLOGGED))
						|| (bucketItem.getContent() == Fluids.EMPTY && state.getValue(WATERLOGGED))
		)) {
			return InteractionResult.PASS;
		}

		if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity blockEntity) || blockEntity.isSigned()) {
			return InteractionResult.PASS;
		}

		DyeColor dye = itemStack.get(DataComponents.DYE);
		if (dye != null) {
			ResourceSupplier<MortarBlock> mortar = ModBlocks.MORTARS.pick(dye);

			if (level instanceof ServerLevel serverLevel && !state.is(mortar.get())) {
				BlockState newState = mortar.get().defaultBlockState()
						.setValue(WATERLOGGED, state.getValue(WATERLOGGED))
						.setValue(FACING_ROTATED, state.getValue(FACING_ROTATED));

				level.setBlock(pos, newState, Block.UPDATE_ALL);

				if (level.getBlockEntity(pos) instanceof MortarBlockEntity newBlockEntity) {
					newBlockEntity.setPolyomino(blockEntity.getPolyomino());

					serverLevel.getServer().schedule(new TickTask(0, () ->
							Services.NETWORK.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(pos), new UpdateMozaikBidirectional(newBlockEntity.getPolyomino(), pos))
					));
				}
				serverLevel.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.PLAYERS);
				if (!player.getAbilities().instabuild) itemStack.shrink(1);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity blockEntity) || blockEntity.isSigned()) {
			return InteractionResult.PASS;
		}

		if (player instanceof ServerPlayer serverPlayer) {
			if (serverPlayer.containerMenu != serverPlayer.inventoryMenu) {
				serverPlayer.closeContainer();
			}

			((ServerPlayerAccessor) serverPlayer).setContainerCounter(((ServerPlayerAccessor) serverPlayer).getContainerCounter() % 100 + 1);

			MortarMenu menu = new MortarMenu(
					((ServerPlayerAccessor) serverPlayer).getContainerCounter(),
					serverPlayer.getInventory(),
					blockEntity,
					Rotation.NONE);

			Services.NETWORK.sendToClient(serverPlayer, new OpenGlueMenuClientbound(pos, ((ServerPlayerAccessor) serverPlayer).getContainerCounter()));

			menu.addSlotListener(((ServerPlayerAccessor) serverPlayer).getContainerListener());
			menu.setSynchronizer(((ServerPlayerAccessor) serverPlayer).getContainerSynchronizer());

			serverPlayer.containerMenu = menu;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}
