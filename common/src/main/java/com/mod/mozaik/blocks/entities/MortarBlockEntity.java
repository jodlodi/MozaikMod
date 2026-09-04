package com.mod.mozaik.blocks.entities;

import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Mozaik;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.reg.ModBlockEntities;
import com.mod.mozaik.reg.ModDataComponents;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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

	public static final StreamCodec<RegistryFriendlyByteBuf, String> STREAM_STRING_CODEC = StreamCodec.ofMember(
			(stack, byteBuf) -> byteBuf.writeJsonWithCodec(Codec.STRING, stack),
			byteBuf -> byteBuf.readJsonWithCodec(Codec.STRING)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, Boolean> STREAM_BOOL_CODEC = StreamCodec.ofMember(
			(stack, byteBuf) -> byteBuf.writeJsonWithCodec(Codec.BOOL, stack),
			byteBuf -> byteBuf.readJsonWithCodec(Codec.BOOL)
	);

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
			Services.NETWORK.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.getBlockPos()), new UpdateMozaikBidirectional(this.polyomino, this.getBlockPos(), this.signed));
			serverLevel.getChunkAt(this.getBlockPos()).setUnsaved(true);
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
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		this.title = componentInput.get(DataComponents.CUSTOM_NAME);
		this.lockKey = componentInput.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);

		Mozaik mozaik = componentInput.get(ModDataComponents.MOZAIK.get());
		if (mozaik != null) {
			this.polyomino.clear();
			this.polyomino.addAll(mozaik.placedPolyomino());
		}

		String author = componentInput.get(ModDataComponents.AUTHOR.get());
		if (author != null) this.setAuthorName(author);

		this.signed = componentInput.getOrDefault(ModDataComponents.SIGNED.get(), false);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.title);
		if (!this.lockKey.equals(LockCode.NO_LOCK)) components.set(DataComponents.LOCK, this.lockKey);
		components.set(ModDataComponents.MOZAIK.get(), new Mozaik(this.polyomino));
		components.set(ModDataComponents.AUTHOR.get(), this.getAuthorName());
		components.set(ModDataComponents.SIGNED.get(), this.signed);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		if (tag.contains(CUSTOM_NAME, CompoundTag.TAG_STRING)) {
			this.title = parseCustomNameSafe(tag.getString(CUSTOM_NAME), registries);
		}
		this.name = tag.contains(AUTHOR_NAME, CompoundTag.TAG_STRING) ? tag.getString(AUTHOR_NAME) : null;
		this.signed = tag.contains(SIGNED_ID) && tag.getBoolean(SIGNED_ID);
		this.lockKey = LockCode.fromTag(tag);
		if (tag.contains(CUSTOM_NAME, CompoundTag.TAG_STRING)) {
			this.title = parseCustomNameSafe(tag.getString(CUSTOM_NAME), registries);
		}

		if (tag.contains(POLYOMINO_ID)) {
			Polyomino.PlacedPolyomino.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get(POLYOMINO_ID)).result().ifPresent(this::setPolyomino);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		if (this.title != null) {
			tag.putString(CUSTOM_NAME, Component.Serializer.toJson(this.title, registries));
		}
		if (this.name != null) tag.putString(AUTHOR_NAME, this.name);
		tag.putBoolean(SIGNED_ID, this.signed);

		if (!this.polyomino.isEmpty()) {
			tag.put(POLYOMINO_ID, Polyomino.PlacedPolyomino.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.polyomino).getOrThrow());
		}

		this.lockKey.addToTag(tag);
	}
}
