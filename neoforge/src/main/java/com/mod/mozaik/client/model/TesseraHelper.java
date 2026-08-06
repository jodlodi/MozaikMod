package com.mod.mozaik.client.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.FlatDirection;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.client.model.block.mortar.sub.TesseraModelPart;
import com.mod.mozaik.data.gen.model.ModBlockStateGen;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class TesseraHelper {
	public static ModelBaker BAKER;

	public static TesseraModelPart bakeTessera(TesseraMaterial material, Direction facing, int x, int y, long seed, int index) {
		return new TesseraModelPart.Unbaked(Constants.prefix( material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/tessera"), new TesseraModelPart.MyModelState(facing, x, y)).bake(TesseraHelper.BAKER);
	}

	public static TesseraModelPart bakeBridge(TesseraMaterial material, Direction facing, FlatDirection direction, int x, int y, long seed, int index) {
		return new TesseraModelPart.Unbaked(perFlatDirection(material, direction, seed, index), new TesseraModelPart.MyModelState(facing, x, y)).bake(TesseraHelper.BAKER);
	}

	private static Identifier perFlatDirection(TesseraMaterial material, FlatDirection direction, long seed, int index) {
		return switch (direction) {
			case UP -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_UP);
			case UP_RIGHT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_UP_RIGHT);
			case RIGHT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_RIGHT);
			case DOWN_RIGHT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_DOWN_RIGHT);
			case DOWN -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_DOWN);
			case DOWN_LEFT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_DOWN_LEFT);
			case LEFT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_LEFT);
			case UP_LEFT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_UP_LEFT);
		};
	}

	public static TesseraModelPart bakeNoBridge(TesseraMaterial material, Direction facing, FlatDirection direction, int x, int y, long seed, int index) {
		return new TesseraModelPart.Unbaked(perNoFlatDirection(material, direction, seed, index), new TesseraModelPart.MyModelState(facing, x, y)).bake(TesseraHelper.BAKER);
	}

	private static Identifier perNoFlatDirection(TesseraMaterial material, FlatDirection direction, long seed, int index) {
		return switch (direction) {
			case UP -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_NO_UP);
			case RIGHT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_NO_RIGHT);
			case DOWN -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_NO_DOWN);
			default -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.BRIDGE_NO_LEFT);
		};
	}

	public static TesseraModelPart bakeNoCorner(TesseraMaterial material, Direction facing, FlatDirection direction, int x, int y, long seed, int index) {
		return new TesseraModelPart.Unbaked(perNoCorerDirection(material, direction, seed, index), new TesseraModelPart.MyModelState(facing, x, y)).bake(TesseraHelper.BAKER);
	}

	private static Identifier perNoCorerDirection(TesseraMaterial material, FlatDirection direction, long seed, int index) {
		return switch (direction) {
			case UP -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_UP_NO_RIGHT);
			case RIGHT -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_RIGHT_NO_DOWN);
			case DOWN -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_DOWN_NO_LEFT);
			default -> Constants.prefix(material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + ModBlockStateGen.CORNER_LEFT_NO_UP);
		};
	}
}
