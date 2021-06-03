package net.quantumfusion.dashloader.misc;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.quantumfusion.dashloader.DashLoader;
import net.quantumfusion.dashloader.DashRegistry;
import net.quantumfusion.dashloader.image.DashSpriteAtlasTexture;
import net.quantumfusion.dashloader.mixin.accessor.ParticleManagerSimpleSpriteProviderAccessor;
import net.quantumfusion.dashloader.util.PairMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashParticleData {

    @Serialize(order = 0)
    @SerializeNullable(path = {0})
    @SerializeNullable(path = {1})
    public PairMap<Long, List<Long>> particles;

    @Serialize(order = 1)
    public DashSpriteAtlasTexture spriteAtlasTexture;

    public DashParticleData(@Deserialize("particles") PairMap<Long, List<Long>> particles) {
        this.particles = particles;
    }

    public DashParticleData(Map<Identifier, ParticleManager.SimpleSpriteProvider> particles, SpriteAtlasTexture texture, DashRegistry registry) {
        this.particles = new PairMap<>();
        spriteAtlasTexture = new DashSpriteAtlasTexture(texture, DashLoader.getInstance().atlasData.get(texture), registry);
        particles.forEach((identifier, simpleSpriteProvider) -> {
            List<Long> out = new ArrayList<>();
            ((ParticleManagerSimpleSpriteProviderAccessor) simpleSpriteProvider).getSprites().forEach(sprite -> out.add(registry.createSpritePointer(sprite)));
            this.particles.put(registry.createIdentifierPointer(identifier), out);
        });
    }


    public Pair<Map<Identifier, List<Sprite>>, SpriteAtlasTexture> toUndash(DashRegistry registry) {
        Map<Identifier, List<Sprite>> out = new HashMap<>();
        particles.forEach((integer, dashSimpleSpriteProvider) -> {
            List<Sprite> outInner = new ArrayList<>();
            dashSimpleSpriteProvider.forEach(integer1 -> outInner.add(registry.getSprite(integer1)));
            out.put(registry.getIdentifier(integer), outInner);
        });
        return Pair.of(out, spriteAtlasTexture.toUndash(registry));
    }

}
