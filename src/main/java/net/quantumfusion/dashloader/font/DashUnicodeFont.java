package net.quantumfusion.dashloader.font;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import net.gudenau.lib.unsafe.Unsafe;
import net.minecraft.client.font.UnicodeTextureFont;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import net.quantumfusion.dashloader.DashRegistry;
import net.quantumfusion.dashloader.mixin.accessor.UnicodeTextureFontAccessor;
import net.quantumfusion.dashloader.util.PairMap;

import java.util.HashMap;
import java.util.Map;

public class DashUnicodeFont implements DashFont {
    @Serialize(order = 0)
    @SerializeNullable()
    @SerializeNullable(path = {0})
    @SerializeNullable(path = {1})
    public final PairMap<Long, Long> images;

    @Serialize(order = 1)
    public final byte[] sizes;

    @Serialize(order = 2)
    public final String template;


    public DashUnicodeFont(@Deserialize("images") PairMap<Long, Long> images,
                           @Deserialize("sizes") byte[] sizes,
                           @Deserialize("template") String template) {
        this.images = images;
        this.sizes = sizes;
        this.template = template;
    }

    public DashUnicodeFont(UnicodeTextureFont rawFont, DashRegistry registry) {
        images = new PairMap<>();
        UnicodeTextureFontAccessor font = ((UnicodeTextureFontAccessor) rawFont);
        font.getImages().forEach((identifier, nativeImage) -> images.put(registry.createIdentifierPointer(identifier), registry.createImagePointer(nativeImage)));
        this.sizes = font.getSizes();
        this.template = font.getTemplate();
    }


    public UnicodeTextureFont toUndash(DashRegistry registry) {
        Map<Identifier, NativeImage> out = new HashMap<>(images.size());
        images.forEach((key, value) -> out.put(registry.getIdentifier(key), registry.getImage(value)));
        UnicodeTextureFont font = Unsafe.allocateInstance(UnicodeTextureFont.class);
        UnicodeTextureFontAccessor accessor = ((UnicodeTextureFontAccessor) font);
        accessor.setSizes(sizes);
        accessor.setImages(out);
        accessor.setTemplate(template);
        return font;
    }
}
