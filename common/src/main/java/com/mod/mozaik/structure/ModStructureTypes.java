package com.mod.mozaik.structure;

import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModStructureTypes {
	public static final ResourceSupplier<StructureType<CustomTemplateStructure>> CUSTOM_TEMPLATE = Services.REGISTRY.registerStructureType("custom_template", () -> () -> CustomTemplateStructure.CODEC);

	public static void init() {

	}
}
