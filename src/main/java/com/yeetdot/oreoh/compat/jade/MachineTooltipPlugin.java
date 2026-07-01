package com.yeetdot.oreoh.compat.jade;

import org.jspecify.annotations.NonNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MachineTooltipPlugin implements IWailaPlugin {
    @Override
    public void register(@NonNull IWailaCommonRegistration registration) {
        IWailaPlugin.super.register(registration);
    }

    @Override
    public void registerClient(@NonNull IWailaClientRegistration registration) {
        IWailaPlugin.super.registerClient(registration);
    }
}
