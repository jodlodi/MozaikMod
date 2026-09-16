package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;


@NullMarked
public class ModAdvancementGen extends AdvancementSubProvider {
	private final HolderGetter<Item> items;

	protected ModAdvancementGen(BootstrapContext<Advancement> output) {
		super(output);
		this.items = output.lookup(Registries.ITEM);
	}

	@Override
	public void generate() {
		AdvancementHolder root = this.prefix(this.output, "root", Advancement.Builder.advancement().rootDisplay(
						ModItems.DARK_PRISMARINE_SHARDS.get(),
						createTranslated("advancement.mozaik.root", "Mozaik"),
						createTranslated("advancement.mozaik.root.desc", "Now what's all this?"),
						Constants.prefix("block/black_mortar"),
						AdvancementType.TASK,
						true, false, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(this.items, ModTags.Items.SHARDS)
				))
				.addCriterion("has_mortar", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(this.items, ModTags.Items.MORTARS)
				))
		);

		this.prefix(this.output, "button", Advancement.Builder.advancement().parent(root).display(
						ModItems.BUTTON_TEMPLATE.get(),
						createTranslated("advancement.mozaik.button", "The Button"),
						createTranslated("advancement.mozaik.button.desc", "Learn the Button Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.BUTTON_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "bone", Advancement.Builder.advancement().parent(root).display(
						ModItems.BONE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.bone", "The Bone"),
						createTranslated("advancement.mozaik.bone.desc", "Learn the Bone Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.BONE_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "bubble", Advancement.Builder.advancement().parent(root).display(
						ModItems.BUBBLE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.bubble", "The Bubble"),
						createTranslated("advancement.mozaik.bubble.desc", "Learn the Bubble Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.BUBBLE_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "worm", Advancement.Builder.advancement().parent(root).display(
						ModItems.WORM_TEMPLATE.get(),
						createTranslated("advancement.mozaik.worm", "The Worm"),
						createTranslated("advancement.mozaik.worm.desc", "Learn the Worm Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.WORM_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "cane", Advancement.Builder.advancement().parent(root).display(
						ModItems.CANE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.cane", "The Cane"),
						createTranslated("advancement.mozaik.cane.desc", "Learn the Cane Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.CANE_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "point", Advancement.Builder.advancement().parent(root).display(
						ModItems.POINT_TEMPLATE.get(),
						createTranslated("advancement.mozaik.point", "The Point"),
						createTranslated("advancement.mozaik.point.desc", "Learn the Point Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.POINT_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "horn", Advancement.Builder.advancement().parent(root).display(
						ModItems.HORN_TEMPLATE.get(),
						createTranslated("advancement.mozaik.horn", "The Horn"),
						createTranslated("advancement.mozaik.horn.desc", "Learn the Horn Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.HORN_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "tree", Advancement.Builder.advancement().parent(root).display(
						ModItems.TREE_TEMPLATE.get(),
						createTranslated("advancement.mozaik.tree", "The Tree"),
						createTranslated("advancement.mozaik.tree.desc", "Learn the Tree Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.TREE_TEMPLATE.get()
				))
		);

		this.prefix(this.output, "fork", Advancement.Builder.advancement().parent(root).display(
						ModItems.FORK_TEMPLATE.get(),
						createTranslated("advancement.mozaik.fork", "The Fork"),
						createTranslated("advancement.mozaik.fork.desc", "Learn the Fork Polyomino."),
						AdvancementType.GOAL,
						true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("has_template", ConsumeItemTrigger.TriggerInstance.usedItem(
						this.items, ModItems.FORK_TEMPLATE.get()
				))
		);
	}

	private static MutableComponent createTranslated(String key, String translated) {
		ModLangGen.SUBTITLE_GENERATOR.put(key, translated);
		return Component.translatable(key);
	}

	private AdvancementHolder prefix(BootstrapContext<Advancement> output, String name, Advancement.Builder builder) {
		return builder.save(output, Constants.prefix(name));
	}
}
