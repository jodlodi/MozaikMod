package com.mod.mozaik.polyomino;

import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class ShardStack implements ItemInstance {
	public static final Codec<ShardStack> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).fieldOf("material").forGetter(ShardStack::material),
			Codec.INT.fieldOf("count").forGetter(ShardStack::count)
	).apply(recordCodecBuilder, ShardStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ShardStack> STREAM_CODEC = StreamCodec.ofMember(
			(stack, byteBuf) -> byteBuf.writeJsonWithCodec(CODEC, stack),
			byteBuf -> byteBuf.readLenientJsonWithCodec(CODEC)
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

	@Override
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

	@Override
	@SuppressWarnings("deprecation")
	public Holder<Item> typeHolder() {
		return ShardItem.SHARDS.get(this.material).builtInRegistryHolder();
	}

	@Override
	public @Nullable <T> T get(DataComponentType<? extends T> dataComponentType) {
		return null;
	}
}
