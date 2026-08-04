package com.mod.mozaik;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public class Constants {
	public static final String MOD_ID = "mozaik";
	public static final String MOD_NAME = "Mozaik";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier prefix(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}