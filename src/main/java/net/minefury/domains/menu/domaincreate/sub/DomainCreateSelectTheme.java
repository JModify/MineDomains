package net.minefury.domains.menu.domaincreate.sub;

import net.minefury.domains.domain.theme.ThemeImpl;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.MenuHelper;
import net.minefury.domains.menu.domaincreate.DomainCreateMenu;
import net.minefury.domains.menu.objects.MenuIcon;
import net.minefury.domains.menu.objects.StandardMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class DomainCreateSelectTheme extends StandardMenu {

    private DomainCreateMenu domainCreateMenu;

    public DomainCreateSelectTheme(Player player, DomainCreateMenu domainCreateMenu) {
        super(player, "domain-select-theme");
        this.domainCreateMenu = domainCreateMenu;
    }

    @Override
    public void setMenuItems() throws ConfigMenuDefinitionException {
        Inventory inventory = getInventory();
        MenuHelper.setMenuItemsNoReplacements(inventory, menuPath);
    }

    @Override
    public void handleMenuClick(InventoryClickEvent e) {

        int slot = e.getSlot();

        try {
            MenuIcon jungleLand = MenuIcon.fromConfig(menuPath + ".jungle-land");
            MenuIcon oasis = MenuIcon.fromConfig(menuPath + ".oasis");
            MenuIcon greenLand = MenuIcon.fromConfig(menuPath + ".green-land");
            MenuIcon grassLand = MenuIcon.fromConfig(menuPath + ".grass-land");

            // Current item can never be null, handled in inventory listener
            MenuIcon clicked = MenuIcon.fromClick(e.getCurrentItem(), e.getSlot());

            if (clicked.sameSlot(jungleLand)) {
                domainCreateMenu.setTheme(ThemeImpl.JUNGLE_LAND);
                domainCreateMenu.open();
            } else if (clicked.sameSlot(oasis)) {
                domainCreateMenu.setTheme(ThemeImpl.OASIS);
                domainCreateMenu.open();
            } else if (clicked.sameSlot(greenLand)) {
                domainCreateMenu.setTheme(ThemeImpl.GREEN_LAND);
                domainCreateMenu.open();
            } else if (clicked.sameSlot(grassLand)) {
                domainCreateMenu.setTheme(ThemeImpl.GRASS_LAND);
                domainCreateMenu.open();
            }

        } catch (ConfigMenuDefinitionException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void handleMenuClose(InventoryCloseEvent e) {
    }

}
