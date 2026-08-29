package com.mod.mozaik.items;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.system.ffm.mapping.Mapping;

import java.util.Optional;

@NullMarked
public class PolyominoItem extends Item {
	private final ResourceKey<PolyominoShape> polyominoShape;
	public final Identifier advancement;

	public PolyominoItem(Properties properties, ResourceSupplier<PolyominoShape> polyominoShape) {
		this(properties, ResourceKey.create(ModRegistries.ModKeys.POLYOMINO_SHAPE, polyominoShape.id()));
	}

	public PolyominoItem(Properties properties, ResourceKey<PolyominoShape> polyominoShape) {
		super(properties);
		this.polyominoShape = polyominoShape;
		this.advancement = polyominoShape.identifier();
	}

	public ResourceKey<PolyominoShape> getPolyominoShape() {
		return this.polyominoShape;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
		}

		player.playSound(SoundEvents.BOOK_PAGE_TURN);

		stack.consume(1, player);
		return InteractionResult.CONSUME;
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
		return PolyominoShape.tryBuild(this.polyominoShape, PersonalPreferences.getPrimaryColor(), PersonalPreferences.getShape().uuid()).map(ShapeTooltip::new);
	}

	public record ShapeTooltip(Polyomino polyomino) implements TooltipComponent {

	}
}
