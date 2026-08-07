package com.mod.mozaik;

import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@NullMarked
public enum TesseraMaterial implements StringRepresentable {
	STONE(0x7f7f7f, 0x747474),
	COBBLESTONE(0x888788, 0x6e6d6d, 0x616161),
	BLACKSTONE(0x3c3947, 0x312c36, 0x1f121b),
	GRANITE(0xa97764, 0x9f6b58, 0x7f5646),
	DIORITE(0xe9e9e9, 0xcececf, 0xbebfc1),
	ANDESITE(0x8A8A8E, 0x7F7F7F, 0x747474),
	DEEPSLATE(0x515151, 0x5a5a5a, 0x4a4a4f, 0x3f3f45),
	TUFF(0x85837b, 0x6a6e6f, 0x5d5d52),
	BRICKS(0x9b5643, 0x8f503f),
	PACKED_MUD(0x9b775b, 0x957150, 0x89654d),
	CALCITE(0xf0f5f4, 0xedece6, 0xd9dbd7),
	SANDSTONE(0xe3dbb0, 0xdad2a3, 0xd5c496),
	RED_SANDSTONE(0xd2752b, 0xc06822, 0xac5712),
	CINNABAR(0xad6764, 0xaa5553, 0x964b42),
	NETHERRACK(0x723232, 0x652828, 0x501b1b),
	NETHER_BRICKS(0x38181e, 0x30181c, 0x211114),
	RED_NETHER_BRICKS(0x6b1317, 0x560e10, 0x440507),
	QUARTZ(0xeeeae6, 0xeee6de),
	GLOWSTONE(0xfbda74, 0xcc8654, 0x855029),
	ANCIENT_DEBRIS(0x654740, 0x5D342C, 0x4A2C23),
	BASALT(0x5c5c5c, 0x4f4b4f, 0x32333d),
	END_STONE(0xeef6b4, 0xdee6a4, 0xd5da94),
	PURPUR(0xb286b2, 0xac7bac, 0x9d6f9c),
	BLOCK_OF_RAW_IRON(0xe9c8b1, 0xd8af93, 0xaf8e77),
	BLOCK_OF_RAW_COPPER(0xea8770, 0xd67b5b, 0x9d573f),
	BLOCK_OF_RAW_GOLD(0xfaea2e, 0xf7c431, 0xeea41a),
	DARK_PRISMARINE(0x3b8268, 0x386251, 0x345648),
	PRISMARINE(GenericAnimationMetadata.PRISMARINE,
			0x79b7ab, 0x5ea496, 0x468974,
			0x79b794, 0x5ea48e, 0x2c8755,
			0x79b3b7, 0x5e85a4, 0x687396,
			0x79b7ab, 0x5e9ea4, 0x4e86a3
	),

	TERRACOTTA(0x9b6045, 0x965d43),
	BLACK_TERRACOTTA(0x251710, 0x261811),
	BLUE_TERRACOTTA(0x4a3b5b, 0x493a5a),
	BROWN_TERRACOTTA(0x4f3524, 0x4d3324),
	CYAN_TERRACOTTA(0x565b5b, 0x55595a),
	GRAY_TERRACOTTA(0x3b2c24, 0x392923),
	GREEN_TERRACOTTA(0x4c532a, 0x4c5229),
	LIGHT_BLUE_TERRACOTTA(0x726c8a, 0x6d6a88),
	LIGHT_GRAY_TERRACOTTA(0x876b61, 0x83685f),
	LIME_TERRACOTTA(0x687534, 0x647231),
	MAGENTA_TERRACOTTA(0x96586c, 0x905369),
	ORANGE_TERRACOTTA(0xa25325, 0x9c5022),
	PINK_TERRACOTTA(0xa74e52, 0x9f4b4b),
	PURPLE_TERRACOTTA(0x774656, 0x734353),
	RED_TERRACOTTA(0x923f30, 0x8b3a2d),
	WHITE_TERRACOTTA(0xd2b1a1, 0xcfafa0),
	YELLOW_TERRACOTTA(0xba8523, 0xb88322),

	BLACK_GLAZED_TERRACOTTA(0x111111, 0x1d1d21),
	BLUE_GLAZED_TERRACOTTA(0x2c2e8f, 0x343699),
	BROWN_GLAZED_TERRACOTTA(0x603b1f, 0x6a4122),
	CYAN_GLAZED_TERRACOTTA(0x157788, 0x136a79),
	GRAY_GLAZED_TERRACOTTA(0x474f52, 0x434a4e),
	GREEN_GLAZED_TERRACOTTA(0x4f6226, 0x495b24),
	LIGHT_BLUE_GLAZED_TERRACOTTA(0x4db9dd, 0x3ab3da),
	LIGHT_GRAY_GLAZED_TERRACOTTA(0xc1c6c8, 0xccd0d2),
	LIME_GLAZED_TERRACOTTA(0x85cf21, 0x8bd922),
	MAGENTA_GLAZED_TERRACOTTA(0xb333a9, 0xa9309f),
	ORANGE_GLAZED_TERRACOTTA(0xf06600, 0xe16100),
	PINK_GLAZED_TERRACOTTA(0xf497b3, 0xf38baa),
	PURPLE_GLAZED_TERRACOTTA(0x621f98, 0x5a1d8d),
	RED_GLAZED_TERRACOTTA(0xb02e26, 0xa82b24),
	WHITE_GLAZED_TERRACOTTA(0xf9fffe, 0xf4f4f2),
	YELLOW_GLAZED_TERRACOTTA(0xfede5f, 0xfed83d),

	BLACK_STAINED_GLASS(0x66191919),
	BLUE_STAINED_GLASS(0x66334cb2),
	BROWN_STAINED_GLASS(0x66664c33),
	CYAN_STAINED_GLASS(0x664c7f99),
	GRAY_STAINED_GLASS(0x664c4c4c),
	GREEN_STAINED_GLASS(0x66667f33),
	LIGHT_BLUE_STAINED_GLASS(0x666699d8),
	LIGHT_GRAY_STAINED_GLASS(0x66999999),
	LIME_STAINED_GLASS(0x667fcc19),
	MAGENTA_STAINED_GLASS(0x66b24cd8),
	ORANGE_STAINED_GLASS(0x66d87f33),
	PINK_STAINED_GLASS(0x66f27fa5),
	PURPLE_STAINED_GLASS(0x667f3fb2),
	RED_STAINED_GLASS(0x66993333),
	WHITE_STAINED_GLASS(0x66ffffff),
	YELLOW_STAINED_GLASS(0x66e5e533),

	CAN_PLACE(true, GenericAnimationMetadata.INDICATOR,
			0x672C8755,
			0x67349E63,
			0x67239B57,
			0x6713994D
	),
	CANT_PLACE(true, GenericAnimationMetadata.INDICATOR,
			0x67FF7272,
			0x67FF5959,
			0x67FF3F3F,
			0x67FF2626
	);

	private static final RandomSource RANDOM = RandomSource.createThreadLocalInstance();

	private final boolean isFakeMaterial;
	private @Nullable
	final GenericAnimationMetadata metadata;
	private final List<MaterialColor> spriteSheets = new ArrayList<>();

	TesseraMaterial(Integer... colors) {
		this(null, colors);
	}

	TesseraMaterial(@Nullable GenericAnimationMetadata metadata, Integer... colors) {
		this(false, metadata, colors);
	}

	TesseraMaterial(boolean isFakeMaterial, @Nullable GenericAnimationMetadata metadata, Integer... colors) {
		this.isFakeMaterial = isFakeMaterial;
		this.metadata = metadata;
		if (metadata == null) {
			int i = 0;
			for (int color : colors) {
				this.spriteSheets.add(new MaterialColor(i++, this.getSerializedName(), color));
			}
		} else {
			int frameCount = metadata.frameCount; // 4 prismarine example
			int variantCount = colors.length / frameCount; // 3 prismarine example

			int[][] frames = new int[variantCount][frameCount];
			for (int i = 0; i < colors.length; i++) { // 12 prismarine example
				int frame = i % frameCount;
				int variant = i / frameCount;

				frames[variant][frame] = colors[i];
			}

			for (int i = 0; i < variantCount; i++) {
				this.spriteSheets.add(new MaterialColor(i, this.getSerializedName(), frames[i]));
			}
		}
	}

	public boolean isFakeMaterial() {
		return this.isFakeMaterial;
	}

	public @Nullable GenericAnimationMetadata getMetadata() {
		return this.metadata;
	}

	public List<MaterialColor> getSpriteSheets() {
		return this.spriteSheets;
	}

	private static Identifier pathToTessera(String name, int number) {
		return Constants.prefix("textures/block/mural/" + name + "/gui_" + number + ".png");
	}

	public Identifier getGuiSheet(long polySeed, int index) {
		if (this.spriteSheets.size() == 1) return this.spriteSheets.getFirst().gui;
		RANDOM.setSeed(this.ordinal() + polySeed + index);
		return this.spriteSheets.get(RANDOM.nextInt(this.spriteSheets.size())).gui;
	}

	public int getBlockId(long polySeed, int index) {
		if (this.spriteSheets.size() == 1) return 0;
		RANDOM.setSeed(this.ordinal() + polySeed + index);
		return RANDOM.nextInt(this.spriteSheets.size());
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public record MaterialColor(Identifier gui, int... color) {
		public MaterialColor(int number, String type, int... color) {
			this(Constants.prefix(type + "/" + number), color);
		}
	}

	public record GenericAnimationMetadata(Optional<List<AnimationFrame>> frames, int defaultFrameTime,
										   boolean interpolatedFrames, int frameCount) {
		public static final GenericAnimationMetadata PRISMARINE = new GenericAnimationMetadata(
				Optional.of(List.of(
						new AnimationFrame(0),
						new AnimationFrame(1),
						new AnimationFrame(0),
						new AnimationFrame(2),
						new AnimationFrame(0),
						new AnimationFrame(3),
						new AnimationFrame(0),
						new AnimationFrame(1),
						new AnimationFrame(2),
						new AnimationFrame(1),
						new AnimationFrame(3),
						new AnimationFrame(1),
						new AnimationFrame(0),
						new AnimationFrame(2),
						new AnimationFrame(1),
						new AnimationFrame(2),
						new AnimationFrame(3),
						new AnimationFrame(2),
						new AnimationFrame(0),
						new AnimationFrame(3),
						new AnimationFrame(1),
						new AnimationFrame(3)
				)),
				300,
				true,
				4
		);
		public static final GenericAnimationMetadata INDICATOR = new GenericAnimationMetadata(
				Optional.of(List.of(
						new AnimationFrame(0),
						new AnimationFrame(1),
						new AnimationFrame(2),
						new AnimationFrame(3)
				)),
				5,
				true,
				4
		);
	}
}
