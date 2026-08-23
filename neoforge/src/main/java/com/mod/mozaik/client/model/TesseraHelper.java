package com.mod.mozaik.client.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.block.mortar.sub.TesseraModelPart;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class TesseraHelper {
	public static TesseraModelPart bakeTessera(ShardMaterial material, String path, Direction facing, int x, int y, long seed, int index, TesseraShape shape) {
		return new TesseraModelPart.Unbaked(Constants.prefix( "mozaik/" + path + "/" + material.getBlockId(seed, index) + "/" + shape.getModel().reference().getSerializedName()), new TesseraModelPart.MyModelState(shape, facing, x, y)).bake(GraphicsRenderHelper.BAKER);
	}
}
