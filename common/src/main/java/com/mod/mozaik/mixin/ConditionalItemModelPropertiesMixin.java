package com.mod.mozaik.mixin;

import com.mod.mozaik.Constants;
import com.mod.mozaik.items.ShardBagItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConditionalItemModelProperties.class)
public class ConditionalItemModelPropertiesMixin {
	@Shadow
	@Final
	private static ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends ConditionalItemModelProperty>> ID_MAPPER;

	@Inject(method = "bootstrap", at = @At(value = "TAIL"))
	private static void bootstrap(CallbackInfo ci) {
		ID_MAPPER.put(Constants.prefix("shard_bag/has_selected_item"), ShardBagItem.ShardBagHasSelectedItem.MAP_CODEC);
	}
}
