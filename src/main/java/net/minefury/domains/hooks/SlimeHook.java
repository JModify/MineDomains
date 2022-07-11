package net.minefury.domains.hooks;

import com.grinderwolf.swm.api.SlimePlugin;
import com.modify.fundamentum.hook.PlugHook;
import com.modify.fundamentum.text.PlugLogger;
import org.bukkit.Bukkit;

public class SlimeHook extends PlugHook {

    public SlimeHook() {
        super("SlimeWorldManager", true);
    }

    public SlimePlugin getAPI() {
        if (isHooked()) {
            return (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        }
        PlugLogger.logError("Failed to retrieve api for SlimeWorldManager.");
        return null;
    }
}
