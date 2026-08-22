package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public abstract class ModLangProvider extends LanguageProvider {

	public static final Map<String, String> AUTO_GENERATOR = new HashMap<>();

	public ModLangProvider(PackOutput output) {
		super(output, Constants.MOD_ID, "en_us");
	}

	@Override
	protected final void addTranslations() {
		AUTO_GENERATOR.forEach(this::add);
		this.addCustomTranslations();
	}

	protected abstract void addCustomTranslations();

	public void addBlockAndDesc(Supplier<? extends Block> key, String name, String... desc) {
		this.add(key.get(), name);
		for (int i = 0; i < desc.length; i++) {
			this.add(key.get().getDescriptionId() + ".description_" + i, desc[i]);
		}
	}

	public void addItemAndDesc(Supplier<? extends Item> key, String name, String... desc) {
		this.add(key.get(), name);
		for (int i = 0; i < desc.length; i++) {
			this.add(key.get().getDescriptionId() + ".description_" + i, desc[i]);
		}
	}

	public void addCreativeTab(ResourceSupplier<CreativeModeTab> tab, String name) {
		this.add("item_group." + Constants.MOD_ID + "." + Objects.requireNonNull(tab.id()).getPath(), name);
	}

	public void addKeyCategory(KeyMapping.Category key, String name) {
		this.add("key.category." + key.id().getNamespace() + "." + key.id().getPath(), name);
	}

	public void addKeyMapping(KeyMapping key, String name) {
		this.add(key.getName(), name);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("advancement." + Constants.MOD_ID + "." + key, title);
		this.add("advancement." + Constants.MOD_ID + "." + key + ".desc", desc);
	}

	public void addEntityAndEgg(ResourceSupplier<? extends EntityType<?>> entity, String name) {
		this.addEntityType(entity, name);
		this.add("item." + Constants.MOD_ID + "." + Objects.requireNonNull(entity.id()).getPath() + "_spawn_egg", name + " Spawn Egg");
	}
}
