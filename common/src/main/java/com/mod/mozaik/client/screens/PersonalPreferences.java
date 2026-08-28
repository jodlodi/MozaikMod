package com.mod.mozaik.client.screens;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.ShardMaterial;
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
			Codec.INT.fieldOf("template").forGetter(pref -> pref.template),
			Favourite.CODEC.listOf().fieldOf("faves").forGetter(pref -> pref.faves),
			Codec.BOOL.fieldOf("shard_bar_tooltip_name").forGetter(pref -> pref.shardBarDisplayCount.get()),
			Codec.BOOL.fieldOf("shard_bar_tooltip_count").forGetter(pref -> pref.shardBarTooltipCount.get()),
			Codec.BOOL.fieldOf("shard_bar_display_count").forGetter(pref -> pref.shardBarDisplayCount.get()),
			Codec.BOOL.fieldOf("show_tool_hotkey").forGetter(pref -> pref.toolHotkey.get())
	).apply(recordCodecBuilder, PersonalPreferences::new));

	private static final PersonalPreferences INSTANCE = getOrCreate();

	private ResourceKey<ShardMaterial> primaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.STONE);
	private ResourceKey<ShardMaterial> secondaryColor = ModShardMaterials.ofMaterial(ModShardMaterials.BLACKSTONE);
	private int template = 0;
	private final List<Favourite> faves = new ArrayList<>();

	private final ToggleOption shardBarTooltipName = new ToggleOption("shard_bar_tooltip_name", true);
	private final ToggleOption shardBarTooltipCount = new ToggleOption("shard_bar_tooltip_count", true);
	private final ToggleOption shardBarDisplayCount = new ToggleOption("shard_bar_display_count", false);
	private final ToggleOption toolHotkey = new ToggleOption("show_tool_hotkey", false);

	private Polyomino shape = Polyomino.EMPTY;

	public PersonalPreferences() {
		for (int i = 0; i < 9; i++) {
			this.faves.add(new Favourite(Optional.empty(), Optional.empty()));
		}
	}

	public PersonalPreferences(
			ResourceKey<ShardMaterial> primaryColor,
			ResourceKey<ShardMaterial> secondaryColor,
			int template,
			List<Favourite> faves,
			boolean shardBarTooltipName,
			boolean shardBarTooltipCount,
			boolean shardBarDisplayCount,
			boolean toolHotkey
			) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.template = template;
		this.faves.addAll(faves);
		this.shardBarTooltipName.set(shardBarTooltipName);
		this.shardBarTooltipCount.set(shardBarTooltipCount);
		this.shardBarDisplayCount.set(shardBarDisplayCount);
		this.toolHotkey.set(toolHotkey);
	}

	@Contract(value = " -> new", pure = true)
	public static @Unmodifiable List<ToggleOption> getOptions() {
		return List.of(
				INSTANCE.shardBarTooltipName,
				INSTANCE.shardBarTooltipCount,
				INSTANCE.shardBarDisplayCount,
				INSTANCE.toolHotkey
		);
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

	public static ToggleOption getToolHotkey() {
		return INSTANCE.toolHotkey;
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
		INSTANCE.faves.set(i, new Favourite(Optional.ofNullable(material), favourite.template()));
		INSTANCE.save();
	}

	public static void setFavouriteShape(int i, @Nullable Integer template) {
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

	public static int getTemplate() {
		return INSTANCE.template;
	}

	public static void setTemplate(int template) {
		INSTANCE.template = template;
		INSTANCE.save();
	}

	public static int minMaterial(MortarScreen screen) {
		return Mth.clamp(screen.getSortedList().indexOf(INSTANCE.primaryColor) - 4, 0, screen.getSortedList().size() - 9);
	}

	public static int minTemplate() {
		return Mth.clamp(INSTANCE.template - 4, 0, PrePolyominoShapes.values().length - 9);
	}

	public record Favourite(Optional<ResourceKey<ShardMaterial>> material, Optional<Integer> template) {
		public static final Codec<Favourite> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).optionalFieldOf("material").forGetter(Favourite::material),
				Codec.INT.optionalFieldOf("template").forGetter(Favourite::template)
		).apply(recordCodecBuilder, Favourite::new));
	}

	public record ToggleOption(String name, String tooltip, AtomicBoolean setting) {
		public ToggleOption(String string, boolean setting) {
			this("name.mozaik.setting." + string, "tooltip.mozaik.setting." + string, new AtomicBoolean(setting));
		}

		public boolean get() {
			return this.setting.get();
		}

		public void set(boolean b) {
			this.setting.set(b);
		}
	}
}
