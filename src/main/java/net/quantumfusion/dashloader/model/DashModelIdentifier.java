package net.quantumfusion.dashloader.model;


import io.activej.serializer.StringFormat;
import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeFixedSize;
import io.activej.serializer.annotations.SerializeStringFormat;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.quantumfusion.dashloader.DashRegistry;
import net.quantumfusion.dashloader.data.DashID;
import net.quantumfusion.dashloader.mixin.accessor.ModelIdentifierAccessor;


public class DashModelIdentifier implements DashID {
    @Serialize(order = 0)
    @SerializeStringFormat(value = StringFormat.UTF8, path = {0, 0})
    @SerializeFixedSize(3)
    public String[] strings;


    public DashModelIdentifier(@Deserialize("strings") String[] strings) {
        this.strings = strings;
    }

    public DashModelIdentifier(ModelIdentifier identifier) {
        strings = new String[3];
        strings[0] = identifier.getNamespace();
        strings[1] = identifier.getPath();
        strings[2] = identifier.getVariant();
    }

    @Override
    public Identifier toUndash(DashRegistry registry) {
        return ModelIdentifierAccessor.init(strings);
    }
}
