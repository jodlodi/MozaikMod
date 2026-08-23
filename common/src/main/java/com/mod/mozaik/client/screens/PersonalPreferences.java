package com.mod.mozaik.client.screens;

import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModShardMaterials;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PersonalPreferences {
	private static ResourceKey<ShardMaterial> primaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.STONE);
	private static ResourceKey<ShardMaterial> secondaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.BLACKSTONE);

	private static Polyomino shape = Polyomino.EMPTY;
	private static int template = 0;

	private static int[] faves = new int[9];

	public static ResourceKey<ShardMaterial> getPrimaryColor() {
		return PersonalPreferences.primaryColor;
	}

	public static void setPrimaryColor(MortarScreen screen, ResourceKey<ShardMaterial> primaryColor) {
		PersonalPreferences.primaryColor = primaryColor;
		PersonalPreferences.shape = PersonalPreferences.shape.rebuild(PersonalPreferences.primaryColor);

		screen.carried.forEach(heldPolyominoWidget ->
				heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(primaryColor))
		);
	}

	public static ResourceKey<ShardMaterial> getSecondaryColor() {
		return PersonalPreferences.secondaryColor;
	}

	public static void setSecondaryColor(ResourceKey<ShardMaterial> secondaryColor) {
		PersonalPreferences.secondaryColor = secondaryColor;
	}

	public static Polyomino getShape() {
		return PersonalPreferences.shape;
	}

	public static void setShape(Polyomino shape) {
		PersonalPreferences.shape = shape;
	}

	public static int getTemplate() {
		return PersonalPreferences.template;
	}

	public static void setTemplate(int template) {
		PersonalPreferences.template = template;
	}

	public static int minMaterial(MortarScreen screen) {
		return Mth.clamp(screen.getSortedList().indexOf(PersonalPreferences.primaryColor) - 4, 0, screen.getSortedList().size() - 9);
	}

	public static int minTemplate() {
		return Mth.clamp(PersonalPreferences.template - 4, 0, PrePolyominoShapes.values().length - 9);
	}
}
