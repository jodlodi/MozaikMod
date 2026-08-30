package com.mod.mozaik.mixin;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.ShardBagSpecialRenderer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModels.class)
public class ItemModelsMixin {
	@Shadow
	@Final
	private static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemModel.Unbaked>> ID_MAPPER;

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void bootstrap(CallbackInfo ci) {
		ID_MAPPER.put(Constants.prefix("shard_bag/selected_item"), ShardBagSpecialRenderer.Unbaked.MAP_CODEC);
	}
}
