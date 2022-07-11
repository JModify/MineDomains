package net.minefury.domains.timers;

import com.modify.fundamentum.timer.SyncRepeatingTask;
import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.slime.registry.DomainRegistry;
import net.minefury.domains.util.FuryUtil;

import java.util.ArrayList;

public class AutoUnloadTimer extends SyncRepeatingTask {

    @Override
    public void run() {
        DomainRegistry domainRegistry = MineDomains.getInstance().getDomainManager().getDomainRegistry();
        if (!domainRegistry.getLoadedDomains().isEmpty()) {
            for (Domain domain : new ArrayList<>(domainRegistry.getLoadedDomains().keySet())) {
                if (!FuryUtil.anyOnline(domain)) {
                    domainRegistry.unloadDomain(domain);
                }
            }
        }
    }



}
