package net.minefury.domains.timers.queue;

import lombok.Getter;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.DomainLoader;

/**
 * Represents an entry which may be added to the DomainLoadQueue.
 * Comprised of two objects:
 * - the domain which needs to be loaded.
 * - the player loading the domain (and the actions which should be performed post load).
 *
 */
public class DomainLoadQueueEntry {

    @Getter private Domain domain;
    @Getter private DomainLoader loader;

    public DomainLoadQueueEntry(Domain domain, DomainLoader loader) {
        this.domain = domain;
        this.loader = loader;
    }
}
