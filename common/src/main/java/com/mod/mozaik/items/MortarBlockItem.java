package com.mod.mozaik.items;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.reg.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

@NullMarked
public class MortarBlockItem extends BlockItem {
	public MortarBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
		String author = itemStack.get(ModDataComponents.AUTHOR.get());
		if (author != null) {
			consumer.accept(Component.translatable("book.byAuthor", author).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack itemStack, BlockState placedState) {
		if (level.isClientSide() && level.getBlockEntity(pos) instanceof MortarBlockEntity block) {
			block.markChanged();
		}
		return super.updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
	}
}
