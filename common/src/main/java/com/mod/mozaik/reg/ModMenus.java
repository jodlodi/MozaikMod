package com.mod.mozaik.reg;

import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.platform.Services;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.BiFunction;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModMenus {
	public static final ResourceSupplier<MenuType<MortarMenu>> GLUE = registerMenu("glue", MortarMenu::new);

	public static void init() {

	}

	private static <T extends AbstractContainerMenu> ResourceSupplier<MenuType<T>> registerMenu(String key, BiFunction<Integer, Inventory, T> factory) {
		return Services.REGISTRY.registerMenu(key, factory);
	}
}
