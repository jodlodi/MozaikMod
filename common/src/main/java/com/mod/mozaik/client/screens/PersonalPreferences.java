package com.mod.mozaik.client.screens;

import com.google.gson.Gson;
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
	private static final PersonalPreferences INSTANCE = getOrCreate();

	private ResourceKey<ShardMaterial> primaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.STONE);
	private ResourceKey<ShardMaterial> secondaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.BLACKSTONE);
	private ResourceKey<PolyominoShape> polyominoShape = ModPolyominoShapes.ofShape(ModPolyominoShapes.SUN);
	private final List<Favourite> faves = new ArrayList<>();
	private float volume = 1.0F;

	private final ToggleOption shardBarTooltipName = new ToggleOption("shard_bar_tooltip_name", true);
	private final ToggleOption shardBarTooltipCount = new ToggleOption("shard_bar_tooltip_count", true);
	private final ToggleOption shardBarDisplayCount = new ToggleOption("shard_bar_display_count", false);
	private final SettingCategory shardBar = new SettingCategory("tooltip.mozaik.setting.category.shard_bar", List.of(
			this.shardBarTooltipName,
			this.shardBarTooltipCount,
			this.shardBarDisplayCount
	));

	private final ToggleOption toolButtonHotkey = new ToggleOption("tool_button_hotkey", false);
	private final ToggleOption toolButtonExtraInfo = new ToggleOption("tool_button_extra_info", true);
	private final ToggleOption pickerToolTooltip = new ToggleOption("picker_tool_tooltip", true);
	private final ToggleOption wandToolTooltip = new ToggleOption("wand_tool_tooltip", true);
	private final ToggleOption cursorAltFunction = new ToggleOption("cursor_alt_function", false);
	private final SettingCategory tools = new SettingCategory("tooltip.mozaik.setting.category.tools", List.of(
			this.toolButtonHotkey,
			this.toolButtonExtraInfo,
			this.pickerToolTooltip,
			this.wandToolTooltip,
			this.cursorAltFunction
	));

	private final ToggleOption reverseScrollDirectionBars = new ToggleOption("reverse_scroll_direction_bars", false);
	private final ToggleOption shapeTooltip = new ToggleOption("shape_tooltip", false);
	private final ToggleOption creativeInfinity = new ToggleOption("creative_infinity", true);
	private final SettingCategory misc = new SettingCategory("tooltip.mozaik.setting.category.misc", List.of(
			this.reverseScrollDirectionBars,
			this.shapeTooltip,
			this.creativeInfinity
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
			float volume,
			boolean shardBarTooltipName,
			boolean shardBarTooltipCount,
			boolean shardBarDisplayCount,
			boolean toolButtonHotkey,
			boolean toolButtonExtraInfo,
			boolean reverseScrollDirectionBars,
			boolean pickerToolTooltip,
			boolean wandToolTooltip,
			boolean shapeTooltip,
			boolean creativeInfinity,
			boolean cursorAltFunction
	) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.polyominoShape = polyominoShape;
		this.faves.addAll(faves);
		this.volume = volume;

		this.shardBarTooltipName.setInit(shardBarTooltipName);
		this.shardBarTooltipCount.setInit(shardBarTooltipCount);
		this.shardBarDisplayCount.setInit(shardBarDisplayCount);
		this.toolButtonHotkey.setInit(toolButtonHotkey);
		this.toolButtonExtraInfo.setInit(toolButtonExtraInfo);
		this.reverseScrollDirectionBars.setInit(reverseScrollDirectionBars);
		this.pickerToolTooltip.setInit(pickerToolTooltip);
		this.wandToolTooltip.setInit(wandToolTooltip);
		this.shapeTooltip.setInit(shapeTooltip);
		this.creativeInfinity.setInit(creativeInfinity);
		this.cursorAltFunction.setInit(cursorAltFunction);
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

	public static ToggleOption getToolButtonExtraInfo() {
		return INSTANCE.toolButtonExtraInfo;
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

	public static ToggleOption getCreativeInfinity() {
		return INSTANCE.creativeInfinity;
	}

	public static ToggleOption getCursorAltFunction() {
		return INSTANCE.cursorAltFunction;
	}

	private static final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();

	private static PersonalPreferences getOrCreate() {
		PersonalPreferences standard = new PersonalPreferences();

		try {
			Path filePath = Services.PLATFORM.getConfigDir().resolve(Constants.MOD_ID).resolve("personal_preferences.json");
			if (Files.exists(filePath)) {
				JsonObject json = new Gson().newBuilder().setPrettyPrinting().create().fromJson(Files.readString(filePath), JsonObject.class);

				return new PersonalPreferences(
						read(ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL), json, "primary_color", standard.primaryColor),
						read(ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL), json, "secondary_color", standard.secondaryColor),
						read(ResourceKey.codec(ModRegistries.ModKeys.POLYOMINO_SHAPE), json, "polyomino_shape", standard.polyominoShape),
						read(Favourite.CODEC.listOf(), json, "faves", standard.faves),
						read(Codec.FLOAT, json, "volume", standard.volume),
						read(Codec.BOOL, json, "shard_bar_tooltip_name", standard.shardBarTooltipName.get()),
						read(Codec.BOOL, json, "shard_bar_tooltip_count", standard.shardBarTooltipCount.get()),
						read(Codec.BOOL, json, "shard_bar_display_count", standard.shardBarDisplayCount.get()),
						read(Codec.BOOL, json, "tool_button_hotkey", standard.toolButtonHotkey.get()),
						read(Codec.BOOL, json, "toolButton_extra_info", standard.toolButtonExtraInfo.get()),
						read(Codec.BOOL, json, "reverse_scroll_direction_bars", standard.reverseScrollDirectionBars.get()),
						read(Codec.BOOL, json, "picker_tool_tooltip", standard.pickerToolTooltip.get()),
						read(Codec.BOOL, json, "wand_tool_tooltip", standard.wandToolTooltip.get()),
						read(Codec.BOOL, json, "shape_tooltip", standard.shapeTooltip.get()),
						read(Codec.BOOL, json, "creative_infinity", standard.creativeInfinity.get()),
						read(Codec.BOOL, json, "cursor_alt_function", standard.cursorAltFunction.get())
				);
			}
		} catch (Exception _) {

		}

		return standard;
	}

	private static <T> T read(Codec<T> codec, JsonObject json, String name, T other) {
		try {
			return codec.decode(JsonOps.INSTANCE, json.get(name)).getOrThrow().getFirst();
		} catch (Exception _) {
			return other;
		}
	}

	private void save() {
		try {
			JsonObject jsonObject = new JsonObject();

			jsonObject.add("primary_color", ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).encodeStart(JsonOps.INSTANCE, this.primaryColor).getOrThrow());
			jsonObject.add("secondary_color", ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).encodeStart(JsonOps.INSTANCE, this.secondaryColor).getOrThrow());
			jsonObject.add("polyomino_shape", ResourceKey.codec(ModRegistries.ModKeys.POLYOMINO_SHAPE).encodeStart(JsonOps.INSTANCE, this.polyominoShape).getOrThrow());
			jsonObject.add("faves", Favourite.CODEC.listOf().encodeStart(JsonOps.INSTANCE, this.faves).getOrThrow());
			jsonObject.add("volume", Codec.FLOAT.encodeStart(JsonOps.INSTANCE, this.volume).getOrThrow());
			jsonObject.add("shard_bar_tooltip_name", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.shardBarTooltipName.get()).getOrThrow());
			jsonObject.add("shard_bar_tooltip_count", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.shardBarTooltipCount.get()).getOrThrow());
			jsonObject.add("shard_bar_display_count", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.shardBarDisplayCount.get()).getOrThrow());
			jsonObject.add("tool_button_hotkey", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.toolButtonHotkey.get()).getOrThrow());
			jsonObject.add("toolButton_extra_info", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.toolButtonExtraInfo.get()).getOrThrow());
			jsonObject.add("reverse_scroll_direction_bars", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.reverseScrollDirectionBars.get()).getOrThrow());
			jsonObject.add("picker_tool_tooltip", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.pickerToolTooltip.get()).getOrThrow());
			jsonObject.add("wand_tool_tooltip", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.wandToolTooltip.get()).getOrThrow());
			jsonObject.add("shape_tooltip", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.shapeTooltip.get()).getOrThrow());
			jsonObject.add("creative_infinity", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.creativeInfinity.get()).getOrThrow());
			jsonObject.add("cursor_alt_function", Codec.BOOL.encodeStart(JsonOps.INSTANCE, this.cursorAltFunction.get()).getOrThrow());

			Path configDir = Services.PLATFORM.getConfigDir().resolve(Constants.MOD_ID);
			Files.createDirectories(configDir);
			Path filePath = configDir.resolve("personal_preferences.json");

			try (BufferedWriter writer = com.google.common.io.Files.newWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
				GSON.toJson(jsonObject, GSON.newJsonWriter(writer));
			}
		} catch (Exception _) {

		}
	}

	public static Favourite getFavourite(int i) {
		return INSTANCE.faves.get(i);
	}

	public static float getVolume() {
		return INSTANCE.volume;
	}

	public static void voidSetVolume(float volume) {
		INSTANCE.volume = volume;
		INSTANCE.save();
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

	public static void setShape(MortarScreen screen, Polyomino shape) {
		if (screen.carried.size() == 1) {
			screen.carried.getFirst().setPolyomino(screen.carried.getFirst().getPolyomino().rebuild(shape));
		}
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

	public record Favourite(Optional<ResourceKey<ShardMaterial>> material,
							Optional<ResourceKey<PolyominoShape>> polyomino) {
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
			INSTANCE.save();
		}

		private void setInit(boolean b) {
			this.setting().set(b);
		}
	}
}
