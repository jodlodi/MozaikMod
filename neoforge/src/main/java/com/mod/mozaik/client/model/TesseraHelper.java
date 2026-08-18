package com.mod.mozaik.client.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.block.mortar.sub.TesseraModelPart;
import net.minecraft.core.Direction;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class TesseraHelper {

	public static TesseraModelPart bakeTessera(TesseraMaterial material, Direction facing, int x, int y, long seed, int index, TesseraShape shape) {
		return new TesseraModelPart.Unbaked(Constants.prefix( "mozaik/" + material.getSerializedName() + "/" + material.getBlockId(seed, index) + "/" + shape.getModel().reference().getSerializedName()), new TesseraModelPart.MyModelState(shape, facing, x, y)).bake(GraphicsRenderHelper.BAKER);
	}
}
