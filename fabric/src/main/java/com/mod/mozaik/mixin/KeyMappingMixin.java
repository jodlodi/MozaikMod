package com.mod.mozaik.mixin;

import com.mod.mozaik.util.IFabricKeyMapping;
import com.mod.mozaik.util.IMozaikKeyMapping;
import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@NullMarked
@Mixin(KeyMapping.class)
public class KeyMappingMixin implements IMozaikKeyMapping, IFabricKeyMapping {

    @Unique
    private Modifier mozaik$modifier = Modifier.NONE;

    @Override
    public void mozaik$setModifier(Modifier modifier) {
        this.mozaik$modifier = modifier;
    }

    @Override
    public Modifier multiLoader_Template$getModifier() {
        return this.mozaik$modifier;
    }
}