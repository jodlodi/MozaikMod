package com.mod.mozaik.client.screens;

import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ModShardMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public class PersonalPreferences {
	private static final PersonalPreferences INSTANCE = new PersonalPreferences();

	private ResourceKey<ShardMaterial> primaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.STONE);
	private ResourceKey<ShardMaterial> secondaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.BLACKSTONE);

	private Polyomino shape = Polyomino.EMPTY;
	private int template = 0;

	private final List<Favourite> faves = new ArrayList<>();

	public PersonalPreferences() {
		for (int i = 0; i < 9; i++) {
			this.faves.add(new Favourite(Optional.empty(), Optional.empty()));
		}
	}

	public static Favourite getFavourite(int i) {
		return INSTANCE.faves.get(i);
	}

	public static void setFavouriteMaterial(int i, @Nullable ResourceKey<ShardMaterial> material) {
		Favourite favourite = INSTANCE.faves.get(i);
		INSTANCE.faves.set(i, new Favourite(Optional.ofNullable(material), favourite.template()));
	}

	public static void setFavouriteShape(int i, @Nullable Integer template) {
		Favourite favourite = INSTANCE.faves.get(i);
		INSTANCE.faves.set(i, new Favourite(favourite.material(), Optional.ofNullable(template)));
	}

	public static ResourceKey<ShardMaterial> getPrimaryColor() {
		return INSTANCE.primaryColor;
	}

	public static void setPrimaryColor(MortarScreen screen, ResourceKey<ShardMaterial> primaryColor) {
		INSTANCE.primaryColor = primaryColor;
		INSTANCE.shape = INSTANCE.shape.rebuild(INSTANCE.primaryColor);

		screen.carried.forEach(heldPolyominoWidget ->
				heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(primaryColor))
		);
	}

	public static ResourceKey<ShardMaterial> getSecondaryColor() {
		return INSTANCE.secondaryColor;
	}

	public static void setSecondaryColor(ResourceKey<ShardMaterial> secondaryColor) {
		INSTANCE.secondaryColor = secondaryColor;
	}

	public static Polyomino getShape() {
		return INSTANCE.shape;
	}

	public static void setShape(Polyomino shape) {
		INSTANCE.shape = shape;
	}

	public static int getTemplate() {
		return INSTANCE.template;
	}

	public static void setTemplate(int template) {
		INSTANCE.template = template;
	}

	public static int minMaterial(MortarScreen screen) {
		return Mth.clamp(screen.getSortedList().indexOf(INSTANCE.primaryColor) - 4, 0, screen.getSortedList().size() - 9);
	}

	public static int minTemplate() {
		return Mth.clamp(INSTANCE.template - 4, 0, PrePolyominoShapes.values().length - 9);
	}

	public record Favourite(Optional<ResourceKey<ShardMaterial>> material, Optional<Integer> template) {
		public static final Codec<Favourite> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).optionalFieldOf("material").forGetter(Favourite::material),
				Codec.INT.optionalFieldOf("template").forGetter(Favourite::template)
		).apply(recordCodecBuilder, Favourite::new));
	}
}
