package com.mod.mozaik.blocks;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.mixin.ServerPlayerAccessor;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
public class MortarBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.box(0.0D, 0.0D, 0.5D, 16.0D, 16.0D, 16.0D));
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;

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
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, ModBlockEntities.MORTAR.get(), (tickerLevel, tickerPos, tickerState, blockEntity) ->
				blockEntity.tick(tickerLevel, tickerPos, tickerState)
		);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());

		Direction clickedFace = context.getClickedFace();
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(clickedFace.getOpposite()));
		return blockState.is(this) && blockState.getValue(FACING) == clickedFace ?
				this.defaultBlockState().setValue(FACING, clickedFace.getOpposite()).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER)) :
				this.defaultBlockState().setValue(FACING, clickedFace).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
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
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof MortarBlockEntity blockEntity) {
			if (serverPlayer.containerMenu != serverPlayer.inventoryMenu) {
				serverPlayer.closeContainer();
			}

			((ServerPlayerAccessor)serverPlayer).setContainerCounter(((ServerPlayerAccessor)serverPlayer).getContainerCounter() % 100 + 1);

			MortarMenu menu = new MortarMenu(
					((ServerPlayerAccessor)serverPlayer).getContainerCounter(),
					serverPlayer.getInventory(),
					blockEntity
			);

			Services.NETWORK.sendToClient(serverPlayer, new OpenGlueMenuClientbound(pos, ((ServerPlayerAccessor)serverPlayer).getContainerCounter()));

			menu.addSlotListener(((ServerPlayerAccessor)serverPlayer).getContainerListener());
			menu.setSynchronizer(((ServerPlayerAccessor)serverPlayer).getContainerSynchronizer());

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
