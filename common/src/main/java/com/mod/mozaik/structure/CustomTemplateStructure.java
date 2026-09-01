package com.mod.mozaik.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CustomTemplateStructure extends GenericTemplateStructure {
	public static final MapCodec<CustomTemplateStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
							settingsCodec(instance),
							ExtraCodecs.nonEmptyList(Setup.CODEC.listOf()).fieldOf("setups").forGetter((structure) -> structure.setups)
					)
					.apply(instance, CustomTemplateStructure::new)
	);

	public CustomTemplateStructure(StructureSettings settings, List<GenericTemplateStructure.Setup> setups) {
		super(settings, setups);
	}

	public CustomTemplateStructure(StructureSettings settings, GenericTemplateStructure.Setup setup) {
		super(settings, setup);
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.CUSTOM_TEMPLATE.get();
	}
}