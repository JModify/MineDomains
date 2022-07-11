package net.minefury.domains.slime;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.modify.fundamentum.text.PlugLogger;
import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.properties.ThemeProperties;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.util.UUID;

/**
 * Helper class used for interaction between the SlimeWorldManager API and the plugin.
 */
public class SlimeHelper {

    /**
     * Asynchronously loads/creates domains. If the domain does not exist in mongo,
     * then this method will recognise that a domain is being created rather than loaded.
     * @param domain domain to load/create
     * @param notifyTime consumer which returns the time taken to load domain world
     * @param postLoadActions consumer which returns the slime world which should be setup.
     */
    public static void asyncLoadWorld(Domain domain, Consumer<Long> notifyTime, Consumer<SlimeWorld> postLoadActions) {
        SlimePlugin slime = MineDomains.getInstance().getHookManager().getSlimeHook().getAPI();

        UUID worldId = domain.getWorldId();
        ThemeProperties themeProperties = new ThemeProperties(domain.getTheme());
        SlimePropertyMap properties = themeProperties.toSlimePropertyMap();

        SlimeLoader mongoLoader = slime.getLoader("mongodb");
        // Asynchronously load and clone world
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();

                    SlimeWorld world;
                    // Check if the world for this domain already exists. If it does, just load it.
                    // If it does, just load it
                    if (mongoLoader.worldExists(domain.getWorldId().toString())) {
                        world = slime.loadWorld(mongoLoader, worldId.toString(), false, properties);
                    } else {
                        //If world for this domain does not exist, load from template and clone it.
                        SlimeLoader templateLoader = slime.getLoader("template");

                        SlimeWorld original = slime.loadWorld(templateLoader, domain.getTheme().getNameRaw(), false, properties);
                        world  = original.clone(worldId.toString(), mongoLoader);
                    }

                    Bukkit.getScheduler().runTask(MineDomains.getInstance(), () -> {
                        slime.generateWorld(world);

                        long time = System.currentTimeMillis() - start;
                        notifyTime.accept(time);
                        postLoadActions.accept(world);

                        MineDomains.getInstance().getDebugger().sendDebugInfo("Successfully loaded domain world "
                                + domain.getId().toString() + " in " + time + "ms!");
                    });

                } catch (IOException | CorruptedWorldException | WorldInUseException
                        | NewerFormatException | UnknownWorldException | WorldAlreadyExistsException e) {
                    PlugLogger.logError("Failed to load domain " + domain.getId().toString() + ".");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(MineDomains.getInstance());
    }

}
