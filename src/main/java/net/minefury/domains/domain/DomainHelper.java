package net.minefury.domains.domain;

import com.grinderwolf.swm.api.world.SlimeWorld;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.data.mongo.MongoHelper;
import net.minefury.domains.domain.properties.DomainSettings;
import net.minefury.domains.manager.DomainManager;
import net.minefury.domains.slime.SlimeHelper;
import net.minefury.domains.slime.registry.DomainRegistry;
import net.minefury.domains.util.FuryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Helper class used to connect many operations contained in the plugin.
 */
public class DomainHelper {

    /**
     * Load/creates domain and registers it with domain registry
     * If domain is being created, algorithm will recognise this and register it into mongodb.
     * @param domain domain to load/create
     * @param loader user loading the domain
     */
    public static void loadDomain(DomainLoader loader, Domain domain) {

        if (!MongoHelper.isRegistered(domain)) {
            MongoHelper.registerDomain(domain);
        }

        DomainRegistry domainRegistry = MineDomains.getInstance().getDomainManager().getDomainRegistry();
        if (domainRegistry.isDomainLoaded(domain)) {
            return;
        }

        Consumer<Long> notifyTime = time -> FuryUtil.notifyDomainLoad(domain, time);
        Consumer<SlimeWorld> postLoadActions = world -> {
            domainRegistry.registerDomain(domain, world);

            // Only time this wouldn't execute is if the load world method returned an exception.
            domain.getUpgradables().getBorder().setBorder(domain.getWorldId());

            if (loader.shouldTeleport()) {
                Player player = Bukkit.getPlayer(loader.getRequester());
                if (player != null) {
                    attemptDomainTeleport(player, domain);
                }
            }
        };

        SlimeHelper.asyncLoadWorld(domain, notifyTime, postLoadActions);;
    }


    /**
     * Attempts to teleport a player to the provided domain IF it is loaded.
     * If it isn't, nothing will happen.
     * @param player player to teleport.
     * @param domain domain to teleport too.
     */
    public static void attemptDomainTeleport(Player player, Domain domain) {
        DomainManager manager = MineDomains.getInstance().getDomainManager();

        if (!manager.getDomainRegistry().isDomainLoaded(domain)) {
            return;
        }

        Location spawnLocation = getSpawnLocation(domain);
        if (spawnLocation == null) {
            return;
        }

        player.teleport(spawnLocation);
    }

    /**
     * Retrieves the bukkit location of the spawn for the given domain.
     *
     * @param domain domain to get spawn location for.
     * @return bukkit location of the domain spawn.
     */
    public static @Nullable Location getSpawnLocation(Domain domain) {
        DomainSettings settings = domain.getDomainSettings();

        int x = settings.getSpawnX();
        int y = settings.getSpawnY();
        int z = settings.getSpawnZ();

        World world = Bukkit.getWorld(domain.getWorldId().toString());
        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z);
    }

    public static boolean hasMaxDomains(UUID user) {
        ConfigFile file = MineDomains.getInstance().getDataManager().getConfig();
        int maxDomains = file.getInteger("limits.max-domains", 5);
        return MongoHelper.countDomains(user) >= maxDomains;
    }

}
