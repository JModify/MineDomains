package net.minefury.domains.domain;

import lombok.Getter;

import java.util.UUID;

/**
 * A domain loader represents any user/command sender who is attempting to load a domain.
 */
public class DomainLoader {

    @Getter private UUID requester;
    private boolean teleport;

    public DomainLoader(UUID requester, boolean teleport) {
        this.requester = requester;
        this.teleport = teleport;
    }

    public boolean shouldTeleport() {
        return this.teleport;
    }
}
