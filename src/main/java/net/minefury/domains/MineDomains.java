package net.minefury.domains;

import com.grinderwolf.swm.api.SlimePlugin;
import com.modify.fundamentum.Fundamentum;
import com.modify.fundamentum.util.PlugDebugger;
import lombok.Getter;
import lombok.Setter;
import net.minefury.domains.commands.DomainAdminCommand;
import net.minefury.domains.commands.DomainCommand;
import net.minefury.domains.commands.TestCommand;
import net.minefury.domains.listeners.InventoryListener;
import net.minefury.domains.listeners.PlayerListener;
import net.minefury.domains.manager.DataManager;
import net.minefury.domains.manager.DomainManager;
import net.minefury.domains.manager.HookManager;
import net.minefury.domains.manager.TaskManager;
import net.minefury.domains.slime.SlimeTemplateLoader;
import net.minefury.domains.util.FuryUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MineDomains extends JavaPlugin {

    /** Holds the instance for this JavaPlugin */
    @Getter @Setter private static MineDomains instance;

    @Getter private HookManager hookManager;

    /** The data manager the plugin uses to make connections to external data sources */
    @Getter private DataManager dataManager;

    @Getter private DomainManager domainManager;

    @Getter private PlugDebugger debugger;

    @Getter private TaskManager taskManager;

    @Override
    public void onEnable() {
        setInstance(this);
        Fundamentum.setPlugin(this);

        initialize();

        registerCommands();
        registerListeners();

        registerTemplateLoader();

        //registerMenuItemGlow();
    }

    @Override
    public void onDisable() {
        // Unload all loaded domains stored in the domain registry
        domainManager.getDomainRegistry().unloadAllDomains();
    }

    private void initialize() {
        hookManager = new HookManager();
        hookManager.init();

        domainManager = new DomainManager();

        dataManager = new DataManager();
        dataManager.init();

        taskManager = new TaskManager();
        taskManager.init();

        debugger = new PlugDebugger();
        debugger.setDebugMode(true);
    }

    /**
     * Registers all listeners the plugin uses, with the server.
     */
    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryListener(), this);
        pm.registerEvents(new PlayerListener(), this);
    }

    /**
     * Registers all commands with the server.
     * Command registry uses special util function to avoid having to
     * list all commands in the plugin yml.
     */
    private void registerCommands() {
        FuryUtil.registerCommand(new TestCommand());
        FuryUtil.registerCommand(new DomainCommand());
        FuryUtil.registerCommand(new DomainAdminCommand());
    }

    /**
     * Registers the template loader with SlimeWorldManager.
     * Template loader used for loading theme template worlds for domains.
     */
    private void registerTemplateLoader() {
        if (hookManager.getSlimeHook().isHooked()) {
            SlimePlugin slime = hookManager.getSlimeHook().getAPI();
            if (slime != null) {
                slime.registerLoader("template", new SlimeTemplateLoader());
            }
        }
    }
}
