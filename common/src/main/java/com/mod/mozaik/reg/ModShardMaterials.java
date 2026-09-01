package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.util.ColorCollection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModShardMaterials {
	public static final ResourceSupplier<ShardMaterial> STONE = Services.REGISTRY.registerShardMaterial("stone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> BLACKSTONE = Services.REGISTRY.registerShardMaterial("blackstone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> GRANITE = Services.REGISTRY.registerShardMaterial("granite", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> DIORITE = Services.REGISTRY.registerShardMaterial("diorite", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> ANDESITE = Services.REGISTRY.registerShardMaterial("andesite", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> DEEPSLATE = Services.REGISTRY.registerShardMaterial("deepslate", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 4));
	public static final ResourceSupplier<ShardMaterial> TUFF = Services.REGISTRY.registerShardMaterial("tuff", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> CALCITE = Services.REGISTRY.registerShardMaterial("calcite", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> DRIPSTONE = Services.REGISTRY.registerShardMaterial("dripstone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> MOSSY = Services.REGISTRY.registerShardMaterial("mossy", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RESIN = Services.REGISTRY.registerShardMaterial("resin", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> AMETHYST = Services.REGISTRY.registerShardMaterial("amethyst", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> BRICK = Services.REGISTRY.registerShardMaterial("brick", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 2));
	public static final ResourceSupplier<ShardMaterial> PACKED_MUD = Services.REGISTRY.registerShardMaterial("packed_mud", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> SANDSTONE = Services.REGISTRY.registerShardMaterial("sandstone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RED_SANDSTONE = Services.REGISTRY.registerShardMaterial("red_sandstone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> BONE = Services.REGISTRY.registerShardMaterial("bone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> NETHERRACK = Services.REGISTRY.registerShardMaterial("netherrack", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> NETHER_BRICK = Services.REGISTRY.registerShardMaterial("nether_brick", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RED_NETHER_BRICK = Services.REGISTRY.registerShardMaterial("red_nether_brick", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> QUARTZ = Services.REGISTRY.registerShardMaterial("quartz", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 2));
	public static final ResourceSupplier<ShardMaterial> GLOWSTONE = Services.REGISTRY.registerShardMaterial("glowstone", () -> new ShardMaterial(ShardMaterial.Type.GLOW, 3));
	public static final ResourceSupplier<ShardMaterial> ANCIENT_DEBRIS = Services.REGISTRY.registerShardMaterial("ancient_debris", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> BASALT = Services.REGISTRY.registerShardMaterial("basalt", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> OBSIDIAN = Services.REGISTRY.registerShardMaterial("obsidian", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> CRYING_OBSIDIAN = Services.REGISTRY.registerShardMaterial("crying_obsidian", () -> new ShardMaterial(ShardMaterial.Type.GLOW, 3));
	public static final ResourceSupplier<ShardMaterial> END_STONE = Services.REGISTRY.registerShardMaterial("end_stone", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> PURPUR = Services.REGISTRY.registerShardMaterial("purpur", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RAW_IRON = Services.REGISTRY.registerShardMaterial("raw_iron", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RAW_COPPER = Services.REGISTRY.registerShardMaterial("raw_copper", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> RAW_GOLD = Services.REGISTRY.registerShardMaterial("raw_gold", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> DARK_PRISMARINE = Services.REGISTRY.registerShardMaterial("dark_prismarine", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> PRISMARINE = Services.REGISTRY.registerShardMaterial("prismarine", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 3));
	public static final ResourceSupplier<ShardMaterial> SEA_LANTERN = Services.REGISTRY.registerShardMaterial("sea_lantern", () -> new ShardMaterial(ShardMaterial.Type.GLOW, 3));

	public static final ResourceSupplier<ShardMaterial> TERRACOTTA = Services.REGISTRY.registerShardMaterial("terracotta", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 2));
	public static final ColorCollection<ResourceSupplier<ShardMaterial>> DYED_TERRACOTTA = ColorCollection.zipMap(ColorCollection.VALUES, ColorCollection.NAMES, (color, name) -> Services.REGISTRY.registerShardMaterial(name + "_terracotta", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 2)));
	public static final ColorCollection<ResourceSupplier<ShardMaterial>> GLAZED_TERRACOTTA = ColorCollection.zipMap(ColorCollection.VALUES, ColorCollection.NAMES, (color, name) -> Services.REGISTRY.registerShardMaterial(name + "_glazed_terracotta", () -> new ShardMaterial(ShardMaterial.Type.NORMAL, 2)));
	public static final ColorCollection<ResourceSupplier<ShardMaterial>> STAINED_GLASS = ColorCollection.zipMap(ColorCollection.VALUES, ColorCollection.NAMES, (color, name) -> Services.REGISTRY.registerShardMaterial(name + "_stained_glass", () -> new ShardMaterial(ShardMaterial.Type.GLASS, 1)));

	public static void init() {

	}

	public static ResourceKey<ShardMaterial> ofMaterial(ResourceSupplier<ShardMaterial> material) {
		return ResourceKey.create(ModRegistries.ModKeys.SHARD_MATERIAL, material.id());
	}
}
