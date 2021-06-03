package net.quantumfusion.dashloader.image;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import net.gudenau.lib.unsafe.Unsafe;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.quantumfusion.dashloader.DashLoader;
import net.quantumfusion.dashloader.DashRegistry;
import net.quantumfusion.dashloader.mixin.accessor.AbstractTextureAccessor;
import net.quantumfusion.dashloader.mixin.accessor.SpriteAccessor;
import net.quantumfusion.dashloader.mixin.accessor.SpriteAtlasTextureAccessor;
import net.quantumfusion.dashloader.util.PairMap;

import java.util.*;

public class DashSpriteAtlasTexture {
    @Serialize(order = 2)
    public final Long id;
    @Serialize(order = 3)
    public final int maxTextureSize;
    @Serialize(order = 0)
    public PairMap<DashSpriteAnimation, Long> animatedSprites; //sprite
    @Serialize(order = 1)
    @SerializeNullable()
    @SerializeNullable(path = {1})
    @SerializeNullable(path = {0})
    public PairMap<Long, Long> sprites;
    @Serialize(order = 4)
    public boolean bilinear;
    @Serialize(order = 5)
    public boolean mipmap;

    @Serialize(order = 6)
    public DashSpriteAtlasTextureData data;


    public DashSpriteAtlasTexture(@Deserialize("animatedSprites") PairMap<DashSpriteAnimation, Long> animatedSprites,
                                  @Deserialize("sprites") PairMap<Long, Long> sprites,
                                  @Deserialize("id") Long id,
                                  @Deserialize("maxTextureSize") int maxTextureSize,
                                  @Deserialize("bilinear") boolean bilinear,
                                  @Deserialize("mipmap") boolean mipmap,
                                  @Deserialize("data") DashSpriteAtlasTextureData data

    ) {
        this.animatedSprites = animatedSprites;
        this.sprites = sprites;
        this.id = id;
        this.maxTextureSize = maxTextureSize;
        this.bilinear = bilinear;
        this.mipmap = mipmap;
        this.data = data;
    }

    public DashSpriteAtlasTexture(SpriteAtlasTexture spriteAtlasTexture, DashSpriteAtlasTextureData data, DashRegistry registry) {
        SpriteAtlasTextureAccessor spriteTextureAccess = ((SpriteAtlasTextureAccessor) spriteAtlasTexture);
        this.data = data;
        animatedSprites = new PairMap<>();
        sprites = new PairMap<>();
        spriteTextureAccess.getSprites().forEach((identifier, sprite) -> {
            final long spritePointer = registry.createSpritePointer(sprite);
            sprites.put(registry.createIdentifierPointer(identifier), spritePointer);
            final Sprite.Animation animation = (Sprite.Animation) sprite.getAnimation();
            if (animation != null) {
                animatedSprites.put(new DashSpriteAnimation(animation, registry), spritePointer);
            }
        });
        id = registry.createIdentifierPointer(spriteAtlasTexture.getId());
        maxTextureSize = spriteTextureAccess.getMaxTextureSize();
        bilinear = ((AbstractTextureAccessor) spriteAtlasTexture).getBilinear();
        mipmap = ((AbstractTextureAccessor) spriteAtlasTexture).getMipmap();
    }

    public SpriteAtlasTexture toUndash(DashRegistry registry) {
        final SpriteAtlasTexture spriteAtlasTexture = Unsafe.allocateInstance(SpriteAtlasTexture.class);
        final AbstractTextureAccessor access = ((AbstractTextureAccessor) spriteAtlasTexture);
        access.setBilinear(bilinear);
        access.setMipmap(mipmap);
        final SpriteAtlasTextureAccessor spriteAtlasTextureAccessor = ((SpriteAtlasTextureAccessor) spriteAtlasTexture);
        final Map<Identifier, Sprite> out = new HashMap<>(sprites.size());
        sprites.forEach((dashIdentifier, spritePointer) -> out.put(registry.getIdentifier(dashIdentifier), loadSprite(spritePointer, registry, spriteAtlasTexture)));
        final Set<Identifier> outLoad = new HashSet<>();
        final List<Sprite.Animation> outAnimatedSprites = new ArrayList<>();
        animatedSprites.forEach((animation, l) -> outAnimatedSprites.add(animation.toUndash(registry.getSprite(l), registry)));
        spriteAtlasTextureAccessor.setAnimatedSprites(outAnimatedSprites);
        spriteAtlasTextureAccessor.setSpritesToLoad(outLoad);
        spriteAtlasTextureAccessor.setSprites(out);
        spriteAtlasTextureAccessor.setId(registry.getIdentifier(id));
        spriteAtlasTextureAccessor.setMaxTextureSize(maxTextureSize);
        DashLoader.getInstance().atlasData.put(spriteAtlasTexture, data);
        return spriteAtlasTexture;
    }

    private Sprite loadSprite(Long spritePointer, DashRegistry registry, SpriteAtlasTexture spriteAtlasTexture) {
        Sprite sprite = registry.getSprite(spritePointer);
        ((SpriteAccessor) sprite).setAtlas(spriteAtlasTexture);
        return sprite;
    }


}
