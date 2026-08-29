package com.mod.mozaik.blocks.entities;

import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.reg.ModBlockEntities;
import com.mojang.serialization.Codec;
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

import java.util.*;

@NullMarked
public class MortarBlockEntity extends BlockEntity implements Nameable {
	private static final String CUSTOM_NAME = "custom_name";
	public static final String AUTHOR_NAME = "author_name";
	public static final String SIGNED_ID = "signed";
	private static final String POLYOMINO_ID = "polyomino";
	private final List<Polyomino.PlacedPolyomino> polyomino = new ArrayList<>();
	private LockCode lockKey = LockCode.NO_LOCK;
	private @Nullable Component title;
	private @Nullable String name;
	private boolean signed = false;

	public MortarBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MORTAR.get(), pos, blockState);
	}

	public List<Polyomino.PlacedPolyomino> getPolyomino() {
		return this.polyomino;
	}

	public void setPolyomino(List<Polyomino.PlacedPolyomino> polyomino) {
		this.polyomino.clear();
		this.polyomino.addAll(polyomino);
		this.markChanged();
	}

	public void markChanged() {
		if (this.level instanceof ServerLevel serverLevel) {
			Services.NETWORK.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(this.getBlockPos()), new UpdateMozaikBidirectional(this.polyomino, this.getBlockPos()));
		}

	}

	public void setCustomName(@Nullable Component name) {
		this.title = name;
	}

	@Override
	public Component getName() {
		return this.title != null ? this.title : this.getBlockState().getBlock().getName();
	}

	public void setAuthorName(@Nullable String name) {
		this.name = name;
	}

	@Nullable
	public String getAuthorName() {
		return this.name;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public boolean isSigned() {
		return this.signed;
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
		this.title = components.get(DataComponents.CUSTOM_NAME);
		this.lockKey = components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.title);
		if (!this.lockKey.equals(LockCode.NO_LOCK)) components.set(DataComponents.LOCK, this.lockKey);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.title = parseCustomNameSafe(input, CUSTOM_NAME);
		this.name = input.read(AUTHOR_NAME, Codec.STRING).orElse(null);
		this.signed = input.read(SIGNED_ID, Codec.BOOL).orElse(false);
		this.lockKey = LockCode.fromTag(input);
		input.read(POLYOMINO_ID, Polyomino.PlacedPolyomino.CODEC.listOf()).ifPresent(this::setPolyomino);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		output.storeNullable(CUSTOM_NAME, ComponentSerialization.CODEC, this.title);
		output.storeNullable(AUTHOR_NAME, Codec.STRING, this.name);
		output.store(SIGNED_ID, Codec.BOOL, this.signed);
		output.store(POLYOMINO_ID, Polyomino.PlacedPolyomino.CODEC.listOf(), this.polyomino);
		this.lockKey.addToTag(output);
	}
}
