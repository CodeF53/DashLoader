package net.quantumfusion.dashloader.mixin.accessor;

import net.minecraft.client.gl.Program;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Program.class)
public interface ProgramAccessor {
    @Accessor
    Program.Type getShaderType();

    @Invoker("<init>")
    static Program create(Program.Type shaderType, int shaderRef, String name) {
        throw new AssertionError();
    }

    @Accessor
    int getShaderRef();

    @Mixin(Program.Type.class)
    interface TypeAccessor {
        @Accessor
        int getGlType();
    }
}

