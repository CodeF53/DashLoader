package net.oskarstrom.dashloader.api;

import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashObject;

@DashObject
public class EmptyData implements DataClass {
    @Override
    public void reload(DashRegistry registry) {

    }

    @Override
    public void apply(DashRegistry registry) {

    }

    @Override
    public void serialize(DashRegistry registry) {

    }
}
