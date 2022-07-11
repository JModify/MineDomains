package net.minefury.domains.menu.objects;

import net.minefury.domains.MineDomains;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.MenuHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class StandardMenu implements InventoryHolder {

    private Inventory inventory;

    protected Player player;
    protected String menuPath;

    private MineDomains plugin;

    public StandardMenu(Player player, String menuPath) {
        this.plugin = MineDomains.getInstance();
        this.player = player;
        this.menuPath = menuPath;
    }

    public void open() {
        String menuName = getMenuName();
        int menuSlots = getMenuSlots();

        if (menuName != null && menuSlots != -1) {
            inventory = Bukkit.createInventory(this, menuSlots, menuName);
            try {
                this.setMenuItems();
                MenuHelper.addFillers(inventory, MenuHelper.getFiller(menuPath));

            } catch (ConfigMenuDefinitionException e) {
                e.printStackTrace();
            }
            player.openInventory(inventory);
        } else {
            plugin.getDebugger().sendDebugError("Failed to open menu for player " + player.getName() + ". " +
                    "Menu name or slots configured incorrectly for menu " + menuPath);
        }
    }

    public String getMenuName() {
        return MenuHelper.getMenuName(menuPath);
    }

    public int getMenuSlots(){
        return MenuHelper.getMenuSlots(menuPath);
    }

    public abstract void setMenuItems() throws ConfigMenuDefinitionException;

    public abstract void handleMenuClick(InventoryClickEvent e);
    public abstract void handleMenuClose(InventoryCloseEvent e);

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
