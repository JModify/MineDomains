package net.minefury.domains.manager;

import lombok.Getter;
import net.minefury.domains.hooks.PAPIHook;
import net.minefury.domains.hooks.SlimeHook;

public class HookManager {

    @Getter private PAPIHook papiHook;
    @Getter private SlimeHook slimeHook;

    public HookManager () {
        this.papiHook = new PAPIHook();
        this.slimeHook = new SlimeHook();
    }

    public void init() {
        papiHook.check();
        slimeHook.check();
    }

}
