package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

@NullMarked
public class ModAdvancementGen implements AdvancementSubProvider {
	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
		HolderLookup.RegistryLookup<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
		HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

		AdvancementHolder root = this.prefix(consumer, "root", Advancement.Builder.advancement().display(
						ModItems.DARK_PRISMARINE_SHARDS.get(),
						createTranslated("advancement.mozaik.root", "Mozaik"),
						createTranslated("advancement.mozaik.root.desc", "Now what's all this?"),
						Constants.prefix("block/black_mortar"),
						AdvancementType.TASK,
						true, false, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.SHARDS)
				))
				.addCriterion("has_mortar", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.MORTARS)
				))
		);

		this.prefix(consumer, "button", Advancement.Builder.advancement().parent(root).display(
						ModItems.BUTTON_TEMPLATE.get(),
						createTranslated("advancement.mozaik.button", "The Button"),
						createTranslated("advancement.mozaik.button.desc", "Learn the Button Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.BUTTON_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "bone", Advancement.Builder.advancement().parent(root).display(
						ModItems.BONE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.bone", "The Bone"),
						createTranslated("advancement.mozaik.bone.desc", "Learn the Bone Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.BONE_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "bubble", Advancement.Builder.advancement().parent(root).display(
						ModItems.BUBBLE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.bubble", "The Bubble"),
						createTranslated("advancement.mozaik.bubble.desc", "Learn the Bubble Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.BUBBLE_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "worm", Advancement.Builder.advancement().parent(root).display(
						ModItems.WORM_TEMPLATE.get(),
						createTranslated("advancement.mozaik.worm", "The Worm"),
						createTranslated("advancement.mozaik.worm.desc", "Learn the Worm Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.WORM_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "cane", Advancement.Builder.advancement().parent(root).display(
						ModItems.CANE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.cane", "The Cane"),
						createTranslated("advancement.mozaik.cane.desc", "Learn the Cane Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.CANE_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "point", Advancement.Builder.advancement().parent(root).display(
						ModItems.POINT_TEMPLATE.get(),
						createTranslated("advancement.mozaik.point", "The Point"),
						createTranslated("advancement.mozaik.point.desc", "Learn the Point Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.POINT_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "horn", Advancement.Builder.advancement().parent(root).display(
						ModItems.HORN_TEMPLATE.get(),
						createTranslated("advancement.mozaik.horn", "The Horn"),
						createTranslated("advancement.mozaik.horn.desc", "Learn the Horn Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.HORN_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "tree", Advancement.Builder.advancement().parent(root).display(
						ModItems.TREE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.tree", "The Tree"),
						createTranslated("advancement.mozaik.tree.desc", "Learn the Tree Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.TREE_TEMPLATE.get()
				))
		);

		this.prefix(consumer, "fork", Advancement.Builder.advancement().parent(root).display(
						ModItems.FORK_TEMPLATE.get(),
						createTranslated("advancement.mozaik.fork", "The Fork"),
						createTranslated("advancement.mozaik.fork.desc", "Learn the Fork Polyomino."),
						null,
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						registries.lookupOrThrow(Registries.ITEM), ModItems.FORK_TEMPLATE.get()
				))
		);
	}

	private static MutableComponent createTranslated(String key, String translated) {
		ModLangGen.SUBTITLE_GENERATOR.put(key, translated);
		return Component.translatable(key);
	}

	private AdvancementHolder prefix(Consumer<AdvancementHolder> consumer, String name, Advancement.Builder builder) {
		return builder.save(consumer, Constants.prefix(name));
	}
}
