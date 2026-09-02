package com.mod.mozaik.polyomino;

import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ShardStack {
	public static final Codec<ShardStack> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).fieldOf("material").forGetter(ShardStack::material),
			Codec.INT.fieldOf("count").forGetter(ShardStack::count)
	).apply(recordCodecBuilder, ShardStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ShardStack> STREAM_CODEC = StreamCodec.ofMember(
			(stack, byteBuf) -> byteBuf.writeJsonWithCodec(CODEC, stack),
			byteBuf -> byteBuf.readJsonWithCodec(CODEC)
	);

	private final ResourceKey<ShardMaterial> material;
	private int count;

	public ShardStack(ResourceKey<ShardMaterial> material, int count) {
		this.material = material;
		this.count = count;
	}

	public ResourceKey<ShardMaterial> material() {
		return this.material;
	}

	public int count() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ItemStack create() {
		return new ItemStack(ShardItem.SHARDS.get(this.material), this.count);
	}

	public static ShardStack fromNonEmptyStack(ItemStack itemStack) {
		if (itemStack.isEmpty()) throw new IllegalStateException("Stack must be non-empty");
		else return fromStack(itemStack);
	}

	public static ShardStack fromStack(ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof ShardItem shardItem)) {
			throw new IllegalStateException("Stack must be a shard item!");
		}
		return new ShardStack(shardItem.getMaterial(), itemStack.getCount());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ShardStack) obj;
		return Objects.equals(this.material, that.material) &&
				this.count == that.count;
	}
}
