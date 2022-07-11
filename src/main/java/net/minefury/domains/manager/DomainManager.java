package net.minefury.domains.manager;

import lombok.Getter;
import net.minefury.domains.data.flatfile.FlatFileHelper;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.DomainHelper;
import net.minefury.domains.domain.DomainLoader;
import net.minefury.domains.slime.registry.DomainRegistry;
import net.minefury.domains.timers.queue.DomainLoadQueue;
import net.minefury.domains.timers.queue.DomainLoadQueueEntry;

public class DomainManager {

    @Getter private DomainRegistry domainRegistry;
    @Getter private DomainLoadQueue domainLoadQueue;

    public DomainManager() {
        this.domainLoadQueue = new DomainLoadQueue();
        this.domainRegistry = new DomainRegistry();
    }

    public void loadOrCreateDomain(Domain domain, DomainLoader domainLoader) {
        if (FlatFileHelper.isQueueEnabled()) {
            DomainLoadQueueEntry entry = new DomainLoadQueueEntry(domain, domainLoader);
            domainLoadQueue.addEntry(entry);
        } else {
            DomainHelper.loadDomain(domainLoader, domain);
        }
    }

}
