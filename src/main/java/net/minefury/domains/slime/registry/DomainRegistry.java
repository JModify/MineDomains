package net.minefury.domains.slime.registry;

import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Domain registry tracks all domains at a given time which are recognised
 * as loaded by the plugin. Additionally, the domain registry is used to handle
 * unloading of slime worlds associated with loaded domains.
 */
public class DomainRegistry {

    /** All loaded domains the plugin has recognised */
    @Getter private Map<Domain, SlimeWorld> loadedDomains;

    /**
     * Constructs a new domain registry object with
     * an empty map of loaded domain worlds.
     */
    public DomainRegistry() {
        this.loadedDomains = new HashMap<>();
    }

    /**
     * Registers a given domain (and slime world for this domain) into the registry.
     * @param domain domain to add.
     * @param world slimeworld associated with domain.
     */
    public void registerDomain(Domain domain, SlimeWorld world) {
        if (!isDomainLoaded(domain)) {
            loadedDomains.put(domain, world);
        }
    }

    public Domain getDomain(UUID domainId) {
        Domain target = null;
        for (Domain domain : loadedDomains.keySet()) {
            if (domain.getId().equals(domainId)) {
                target = domain;
            }
        }

        return target;
    }

    /**
     * Checks if a domain is recognised as loaded using only a domain ID.
     * @param domainId domain id to check.
     * @return true if domain is loaded, else false.
     */
    public boolean isDomainLoaded(UUID domainId) {
        boolean isLoaded = false;
        for (Domain domain : loadedDomains.keySet()) {
            if (domain.getId().equals(domainId)) {
                isLoaded = true;
            }
        }

        return isLoaded;
    }

    /**
     * Checks if a domain is recognised as loaded.
     * @param domain domain to check
     * @return true if domain is loaded, false otherwise.
     */
    public boolean isDomainLoaded(Domain domain) {
        return loadedDomains.containsKey(domain);
    }

    public SlimeWorld getWorld(Domain domain) {
        return loadedDomains.get(domain);
    }

    /**
     * Unloads a domain from the server. Encompasses a two-step process
     * whereby the slime world for this domain is first unlocked, then
     * the bukkit world is unloaded from the server.
     * @param domain domain to unload.
     */
    public void unloadDomain(Domain domain) {
        SlimeLoader loader = MineDomains.getInstance().getHookManager().getSlimeHook().getAPI().getLoader("mongodb");
        SlimeWorld world = loadedDomains.get(domain);
        World bWorld = Bukkit.getWorld(world.getName());

        try {
            loader.unlockWorld(world.getName());
        } catch (UnknownWorldException | IOException e) {
            MineDomains.getInstance().getDebugger().sendDebugError("Failed to unload domain " + domain.getId());
            e.printStackTrace();
        }

        Bukkit.unloadWorld(bWorld, true);
        loadedDomains.remove(domain);
    }

    /**
     * Unloads all domains in the domain registry.
     * Used on server shutdown.
     */
    public void unloadAllDomains() {
        for (Domain domain : new ArrayList<>(loadedDomains.keySet())) {
            unloadDomain(domain);
        }
        loadedDomains.clear();
    }

}
