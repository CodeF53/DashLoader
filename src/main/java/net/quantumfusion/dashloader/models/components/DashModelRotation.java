package net.quantumfusion.dashloader.models.components;


import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.math.Direction;

public class DashModelRotation {

    @Serialize(order = 0)
    public final DashVec3f origin;
    @Serialize(order = 1)
    public final String axis;
    @Serialize(order = 2)
    public final float angle;
    @Serialize(order = 3)
    public final boolean rescale;

    public DashModelRotation(@Deserialize("origin") DashVec3f origin,
                             @Deserialize("axis") String axis,
                             @Deserialize("angle") float angle,
                             @Deserialize("rescale") boolean rescale
    ) {
        this.origin = origin;
        this.axis = axis;
        this.angle = angle;
        this.rescale = rescale;
    }

    public DashModelRotation(ModelRotation modelRotation) {
        origin = new DashVec3f(modelRotation.origin);
        axis = modelRotation.axis.toString();
        angle = modelRotation.angle;
        rescale = modelRotation.rescale;
    }

    public ModelRotation toUndash() {
        return new ModelRotation(origin.toUndash(), Direction.Axis.fromName(axis), angle, rescale);
    }
}
