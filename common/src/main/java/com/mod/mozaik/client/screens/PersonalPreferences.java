package com.mod.mozaik.client.screens;

import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PersonalPreferences {
	private static TesseraMaterial primaryColor = TesseraMaterial.values()[0];
	private static TesseraMaterial secondaryColor = TesseraMaterial.values()[1];

	private static Polyomino shape = Polyomino.EMPTY;
	private static int template = 0;

	private static int[] faves = new int[9];

	public static TesseraMaterial getPrimaryColor() {
		return PersonalPreferences.primaryColor;
	}

	public static void setPrimaryColor(MortarScreen screen, TesseraMaterial primaryColor) {
		PersonalPreferences.primaryColor = primaryColor;
		PersonalPreferences.shape = PersonalPreferences.shape.rebuild(PersonalPreferences.primaryColor);

		screen.carried.forEach(heldPolyominoWidget ->
				heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(primaryColor))
		);
	}

	public static TesseraMaterial getSecondaryColor() {
		return PersonalPreferences.secondaryColor;
	}

	public static void setSecondaryColor(TesseraMaterial secondaryColor) {
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

	public static int minMaterial() {
		return Mth.clamp(PersonalPreferences.primaryColor.ordinal() - 4, 0, TesseraMaterial.values().length - 9);
	}

	public static int minTemplate() {
		return Mth.clamp(PersonalPreferences.template - 4, 0, PrePolyominoShapes.values().length - 9);
	}
}
