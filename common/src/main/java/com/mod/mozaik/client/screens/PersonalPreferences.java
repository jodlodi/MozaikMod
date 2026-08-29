package com.mod.mozaik.client.screens;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModPolyominoShapes;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ModShardMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@NullMarked
public class PersonalPreferences {
	public static final Codec<PersonalPreferences> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).fieldOf("primary_color").forGetter(pref -> pref.primaryColor),
			ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).fieldOf("secondary_color").forGetter(pref -> pref.secondaryColor),
			ResourceKey.codec(ModRegistries.ModKeys.POLYOMINO_SHAPE).fieldOf("polyomino_shape").forGetter(pref -> pref.polyominoShape),
			Favourite.CODEC.listOf().fieldOf("faves").forGetter(pref -> pref.faves),
			Codec.BOOL.fieldOf("shard_bar_tooltip_name").forGetter(pref -> pref.shardBarTooltipName.get()),
			Codec.BOOL.fieldOf("shard_bar_tooltip_count").forGetter(pref -> pref.shardBarTooltipCount.get()),
			Codec.BOOL.fieldOf("shard_bar_display_count").forGetter(pref -> pref.shardBarDisplayCount.get()),
			Codec.BOOL.fieldOf("tool_button_hotkey").forGetter(pref -> pref.toolButtonHotkey.get()),
			Codec.BOOL.fieldOf("reverse_scroll_direction_bars").forGetter(pref -> pref.reverseScrollDirectionBars.get()),
			Codec.BOOL.fieldOf("picker_tool_tooltip").forGetter(pref -> pref.pickerToolTooltip.get()),
			Codec.BOOL.fieldOf("wand_tool_tooltip").forGetter(pref -> pref.wandToolTooltip.get()),
			Codec.BOOL.fieldOf("shape_tooltip").forGetter(pref -> pref.shapeTooltip.get())
	).apply(recordCodecBuilder, PersonalPreferences::new));

	private static final PersonalPreferences INSTANCE = getOrCreate();

	private ResourceKey<ShardMaterial> primaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.STONE);
	private ResourceKey<ShardMaterial> secondaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.BLACKSTONE);
	private ResourceKey<PolyominoShape> polyominoShape = ModPolyominoShapes.ofShape(ModPolyominoShapes.SMASHBOY);
	private final List<Favourite> faves = new ArrayList<>();

	private final ToggleOption shardBarTooltipName = new ToggleOption("shard_bar_tooltip_name", true);
	private final ToggleOption shardBarTooltipCount = new ToggleOption("shard_bar_tooltip_count", true);
	private final ToggleOption shardBarDisplayCount = new ToggleOption("shard_bar_display_count", false);
	private final SettingCategory shardBar = new SettingCategory("tooltip.mozaik.setting.category.shard_bar", List.of(
			this.shardBarTooltipName,
			this.shardBarTooltipCount,
			this.shardBarDisplayCount
	));

	private final ToggleOption toolButtonHotkey = new ToggleOption("tool_button_hotkey", false);
	private final ToggleOption pickerToolTooltip = new ToggleOption("picker_tool_tooltip", true);
	private final ToggleOption wandToolTooltip = new ToggleOption("wand_tool_tooltip", true);
	private final SettingCategory tools = new SettingCategory("tooltip.mozaik.setting.category.tools", List.of(
			this.toolButtonHotkey,
			this.pickerToolTooltip,
			this.wandToolTooltip
	));

	private final ToggleOption reverseScrollDirectionBars = new ToggleOption("reverse_scroll_direction_bars", false);
	private final ToggleOption shapeTooltip = new ToggleOption("shape_tooltip", false);
	private final SettingCategory misc = new SettingCategory("tooltip.mozaik.setting.category.misc", List.of(
			this.reverseScrollDirectionBars,
			this.shapeTooltip
	));

	private Polyomino shape = Polyomino.EMPTY;

	public PersonalPreferences() {
		for (int i = 0; i < 9; i++) {
			this.faves.add(new Favourite(Optional.empty(), Optional.empty()));
		}
	}

	public PersonalPreferences(
			ResourceKey<ShardMaterial> primaryColor,
			ResourceKey<ShardMaterial> secondaryColor,
			ResourceKey<PolyominoShape> polyominoShape,
			List<Favourite> faves,
			boolean shardBarTooltipName,
			boolean shardBarTooltipCount,
			boolean shardBarDisplayCount,
			boolean toolButtonHotkey,
			boolean reverseScrollDirectionBars,
			boolean pickerToolTooltip,
			boolean wandToolTooltip,
			boolean shapeTooltip
	) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.polyominoShape = polyominoShape;
		this.faves.addAll(faves);

		this.shardBarTooltipName.set(shardBarTooltipName);
		this.shardBarTooltipCount.set(shardBarTooltipCount);
		this.shardBarDisplayCount.set(shardBarDisplayCount);
		this.toolButtonHotkey.set(toolButtonHotkey);
		this.reverseScrollDirectionBars.set(reverseScrollDirectionBars);
		this.pickerToolTooltip.set(pickerToolTooltip);
		this.wandToolTooltip.set(wandToolTooltip);
		this.shapeTooltip.set(shapeTooltip);
	}

	@Contract(value = " -> new", pure = true)
	public static @Unmodifiable List<SettingCategory> getOptions() {
		return List.of(
				INSTANCE.shardBar,
				INSTANCE.tools,
				INSTANCE.misc
		);
	}

	public static SettingCategory getShardBar() {
		return INSTANCE.shardBar;
	}

	public static SettingCategory getTools() {
		return INSTANCE.tools;
	}

	public static SettingCategory getMisc() {
		return INSTANCE.misc;
	}

	public static ToggleOption getShardBarTooltipName() {
		return INSTANCE.shardBarTooltipName;
	}

	public static ToggleOption getShardBarTooltipCount() {
		return INSTANCE.shardBarTooltipCount;
	}

	public static ToggleOption getShardBarDisplayCount() {
		return INSTANCE.shardBarDisplayCount;
	}

	public static ToggleOption getToolButtonHotkey() {
		return INSTANCE.toolButtonHotkey;
	}

	public static ToggleOption getReverseScrollDirectionBars() {
		return INSTANCE.reverseScrollDirectionBars;
	}

	public static ToggleOption getPickerToolTooltip() {
		return INSTANCE.pickerToolTooltip;
	}

	public static ToggleOption getWandToolTooltip() {
		return INSTANCE.wandToolTooltip;
	}

	public static ToggleOption getShapeTooltip() {
		return INSTANCE.shapeTooltip;
	}

	private static final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();

	private static PersonalPreferences getOrCreate() {
		try {
			Path filePath = Services.PLATFORM.getConfigDir().resolve(Constants.MOD_ID).resolve("personal_preferences.json");
			if (Files.exists(filePath)) {
				JsonObject json = new Gson().newBuilder().setPrettyPrinting().create().fromJson(Files.readString(filePath), JsonObject.class);
				return CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
			}
		} catch (Exception _) {

		}

		return new PersonalPreferences();
	}

	private void save() {
		try {
			JsonElement encoded = CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();

			Path configDir = Services.PLATFORM.getConfigDir().resolve(Constants.MOD_ID);
			Files.createDirectories(configDir);
			Path filePath = configDir.resolve("personal_preferences.json");

			try (BufferedWriter writer = com.google.common.io.Files.newWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
				GSON.toJson(encoded, GSON.newJsonWriter(writer));
			}
		} catch (Exception _) {

		}
	}

	public static Favourite getFavourite(int i) {
		return INSTANCE.faves.get(i);
	}

	public static void setFavouriteMaterial(int i, @Nullable ResourceKey<ShardMaterial> material) {
		Favourite favourite = INSTANCE.faves.get(i);
		INSTANCE.faves.set(i, new Favourite(Optional.ofNullable(material), favourite.polyomino()));
		INSTANCE.save();
	}

	public static void setFavouriteShape(int i, @Nullable ResourceKey<PolyominoShape> template) {
		Favourite favourite = INSTANCE.faves.get(i);
		INSTANCE.faves.set(i, new Favourite(favourite.material(), Optional.ofNullable(template)));
		INSTANCE.save();
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
		INSTANCE.save();
	}

	public static ResourceKey<ShardMaterial> getSecondaryColor() {
		return INSTANCE.secondaryColor;
	}

	public static void setSecondaryColor(ResourceKey<ShardMaterial> secondaryColor) {
		INSTANCE.secondaryColor = secondaryColor;
		INSTANCE.save();
	}

	public static Polyomino getShape() {
		return INSTANCE.shape;
	}

	public static void setShape(Polyomino shape) {
		INSTANCE.shape = shape;
		INSTANCE.save();
	}

	public static ResourceKey<PolyominoShape> getPolyominoShape() {
		return INSTANCE.polyominoShape;
	}

	public static void setPolyominoShape(ResourceKey<PolyominoShape> polyominoShape) {
		INSTANCE.polyominoShape = polyominoShape;
		INSTANCE.save();
	}

	public static int minMaterial(MortarScreen screen) {
		return Mth.clamp(screen.getSortedMaterials().indexOf(INSTANCE.primaryColor) - 4, 0, screen.getSortedMaterials().size() - 9);
	}

	public static int minTemplate(MortarScreen screen) {
		return Mth.clamp(screen.getSortedShapes().indexOf(INSTANCE.polyominoShape) - 4, 0, screen.getSortedShapes().size() - 9);
	}

	public record Favourite(Optional<ResourceKey<ShardMaterial>> material, Optional<ResourceKey<PolyominoShape>> polyomino) {
		public static final Codec<Favourite> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).optionalFieldOf("material").forGetter(Favourite::material),
				ResourceKey.codec(ModRegistries.ModKeys.POLYOMINO_SHAPE).optionalFieldOf("polyomino").forGetter(Favourite::polyomino)
		).apply(recordCodecBuilder, Favourite::new));
	}

	public record SettingCategory(String name, List<ToggleOption> options) {

	}

	public record ToggleOption(String name, AtomicBoolean setting) {
		public ToggleOption(String string, boolean setting) {
			this("name.mozaik.setting." + string, new AtomicBoolean(setting));
		}

		public boolean get() {
			return this.setting.get();
		}

		public void set(boolean b) {
			this.setting.set(b);
		}
	}
}
