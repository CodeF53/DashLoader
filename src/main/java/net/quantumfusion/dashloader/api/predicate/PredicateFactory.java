package net.quantumfusion.dashloader.api.predicate;

import net.minecraft.client.render.model.json.MultipartModelSelector;
import net.quantumfusion.dashloader.api.Factory;
import net.quantumfusion.dashloader.api.FactoryType;
import net.quantumfusion.dashloader.model.predicates.DashPredicate;

public interface PredicateFactory extends Factory<MultipartModelSelector, DashPredicate> {
    default FactoryType getFactoryType() {
        return FactoryType.PREDICATE;
    }
}
