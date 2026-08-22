package com.mod.mozaik.blocks.entities;

import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.reg.ModBlockEntities;
import com.mod.mozaik.util.MortarContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class MortarBlockEntity extends BlockEntity implements Nameable {
	private static final Component DEFAULT_NAME = Component.literal("Glu");
	private static final String CUSTOM_NAME = "CustomName";
	private static final String POLYOMINOS = "polyominos";
	private final List<Polyomino.PlacedPolyomino> polyominos = new ArrayList<>();
	private @Nullable Component name;
	private LockCode lockKey = LockCode.NO_LOCK;
	public final MortarContainerData dataAccess;

	public MortarBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MORTAR.get(), pos, blockState);
		this.dataAccess = new MortarContainerData(this);
	}

	public List<Polyomino.PlacedPolyomino> getPolyomino() {
		return this.polyominos;
	}

	public void setPolyominos(List<Polyomino.PlacedPolyomino> polyominos) {
		this.polyominos.clear();
		this.polyominos.addAll(polyominos);
		if (this.level instanceof ServerLevel serverLevel) {
			Services.NETWORK.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(this.getBlockPos()), new UpdateGlueBidirectional(this.polyominos, this.getBlockPos()));
		}
	}

	public void setCustomName(@Nullable Component name) {
		this.name = name;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : DEFAULT_NAME;
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	@Nullable
	public Component getCustomName() {
		return this.getName();
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.name = components.get(DataComponents.CUSTOM_NAME);
		this.lockKey = components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.name);
		if (!this.lockKey.equals(LockCode.NO_LOCK)) components.set(DataComponents.LOCK, this.lockKey);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.name = parseCustomNameSafe(input, CUSTOM_NAME);
		this.lockKey = LockCode.fromTag(input);
		input.read(POLYOMINOS, Polyomino.PlacedPolyomino.CODEC.listOf()).ifPresent(this::setPolyominos);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		output.storeNullable(CUSTOM_NAME, ComponentSerialization.CODEC, this.name);
		output.store(POLYOMINOS, Polyomino.PlacedPolyomino.CODEC.listOf(), this.getPolyomino());
		this.lockKey.addToTag(output);
	}
}
