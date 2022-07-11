package net.minefury.domains.menu;

import net.minefury.domains.domain.DomainHelper;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.domaincreate.DomainCreateMenu;
import net.minefury.domains.menu.domaintravel.DomainTravelMenu;
import net.minefury.domains.menu.objects.MenuIcon;
import net.minefury.domains.menu.objects.StandardMenu;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class DomainMainMenu extends StandardMenu {

    public DomainMainMenu(Player player) {
        super(player, "main-menu");
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

            MenuIcon createDomain = MenuIcon.fromConfig(menuPath + ".domain-create");
            MenuIcon travelDomain = MenuIcon.fromConfig(menuPath + ".domain-travel");
            MenuIcon domainInvites = MenuIcon.fromConfig(menuPath + ".domain-invitations");

            MenuIcon clicked = MenuIcon.fromClick(e.getCurrentItem(), slot);

            if (clicked.sameSlot(createDomain)) {
                if (!DomainHelper.hasMaxDomains(player.getUniqueId())) {
                    DomainCreateMenu domainCreateMenu = new DomainCreateMenu(player, this);
                    domainCreateMenu.open();
                } else {
                    player.closeInventory();
                    FuryMessage.MAX_DOMAINS.send(player);
                }
            } else if (clicked.sameSlot(travelDomain)) {
                DomainTravelMenu domainTravelMenu = new DomainTravelMenu(player, this);
                domainTravelMenu.open();
            }

         } catch (ConfigMenuDefinitionException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void handleMenuClose(InventoryCloseEvent e) {

    }
}
