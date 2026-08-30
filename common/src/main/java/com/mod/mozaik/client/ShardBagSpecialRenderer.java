package com.mod.mozaik.client;

import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.polyomino.ShardStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ShardBagSpecialRenderer implements ItemModel {
	private static final ItemModel INSTANCE = new ShardBagSpecialRenderer();

	@Override
	public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		output.appendModelIdentityElement(this);
		ShardStack selectedShard = ShardBagItem.getSelectedItem(item);
		if (selectedShard != null) {
			resolver.appendItemLayers(output, selectedShard.create(), displayContext, level, owner, seed);
		}
	}

	public record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ShardBagSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new ShardBagSpecialRenderer.Unbaked());

		@Override
		public MapCodec<ShardBagSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			return ShardBagSpecialRenderer.INSTANCE;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
		}
	}
}
