package com.mod.mozaik;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Constants {
	public static final String MOD_ID = "mozaik";
	public static final String MOD_NAME = "mozaik";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation prefix(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}