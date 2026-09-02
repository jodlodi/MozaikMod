package com.mod.mozaik.util;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.SymmetricGroup3;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector3i;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModUtil {
	public static Vector3i rotated(Rotation rotation, Vector3i vector3i) {
		OctahedralGroup octahedralGroup = rotation.rotation();
		SymmetricGroup3 symmetricalGroup = octahedralGroup.permutation;

		int v0 = vector3i.get(symmetricalGroup.permutation(0));
		int v1 = vector3i.get(symmetricalGroup.permutation(1));
		int v2 = vector3i.get(symmetricalGroup.permutation(2));
		vector3i = vector3i.set(v0, v1, v2);

		vector3i.x *= octahedralGroup.invertX ? -1 : 1;
		vector3i.y *= octahedralGroup.invertY ? -1 : 1;
		vector3i.z *= octahedralGroup.invertZ ? -1 : 1;

		return vector3i;
	}

	public static <T> T getRandom(T[] array, RandomSource random) {
		return (T) array[random.nextInt(array.length)];
	}
}
