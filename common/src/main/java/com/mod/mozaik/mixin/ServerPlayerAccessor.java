package com.mod.mozaik.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ContainerSynchronizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {
	@Accessor("containerCounter")
	int getContainerCounter();

	@Accessor("containerCounter")
	void setContainerCounter(int containerCounter);

	@Accessor("containerSynchronizer")
	ContainerSynchronizer getContainerSynchronizer();

	@Accessor("containerListener")
	ContainerListener getContainerListener();
}
