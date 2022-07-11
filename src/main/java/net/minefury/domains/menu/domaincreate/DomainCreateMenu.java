package net.minefury.domains.menu.domaincreate;

import lombok.Setter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.DomainLoader;
import net.minefury.domains.domain.theme.Theme;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.DomainMainMenu;
import net.minefury.domains.menu.domaincreate.sub.DomainCreateEnterName;
import net.minefury.domains.menu.domaincreate.sub.DomainCreateSelectTheme;
import net.minefury.domains.menu.objects.MenuIcon;
import net.minefury.domains.menu.objects.StandardMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class DomainCreateMenu extends StandardMenu {

    @Setter private String domainName = null;
    @Setter private Theme theme = null;

    private DomainMainMenu domainMainMenu;

    public DomainCreateMenu(Player player, DomainMainMenu domainMainMenu) {
        super(player, "domain-create");
        this.domainMainMenu = domainMainMenu;
    }

    @Override
    public void setMenuItems() throws ConfigMenuDefinitionException {
        Inventory inventory = getInventory();

        MenuIcon nameEntry = getNameEntryIcon();
        MenuIcon themeSelect = getThemeSelectIcon();
        MenuIcon confirmCreate = getConfirmCreateIcon();

        MenuIcon backButton = MenuIcon.fromConfig(menuPath + ".back-button");

        inventory.setItem(nameEntry.getSlot(), nameEntry.getItem());
        inventory.setItem(themeSelect.getSlot(), themeSelect.getItem());
        inventory.setItem(confirmCreate.getSlot(), confirmCreate.getItem());
        inventory.setItem(backButton.getSlot(), backButton.getItem());
    }

    @Override
    public void handleMenuClick(InventoryClickEvent e) {

        int slot = e.getSlot();

        try {
            MenuIcon nameEntry = getNameEntryIcon();
            MenuIcon themeSelect = getThemeSelectIcon();
            MenuIcon confirmCreate = getConfirmCreateIcon();
            MenuIcon backButton = MenuIcon.fromConfig(menuPath + ".back-button");

            // Current item can never be null, handled in inventory listener
            MenuIcon clicked = MenuIcon.fromClick(e.getCurrentItem(), slot);

            if (clicked.sameSlot(nameEntry)) {
                DomainCreateEnterName nameEntryMenu = new DomainCreateEnterName(player, this);
                nameEntryMenu.open();
            } else if (clicked.sameSlot(themeSelect)) {
                DomainCreateSelectTheme selectThemeMenu = new DomainCreateSelectTheme(player, this);
                selectThemeMenu.open();
            } else if (clicked.sameSlot(confirmCreate)) {
                if (canCreate()) {
                    Domain domain = Domain.createDomain(player.getUniqueId(), domainName, theme);
                    DomainLoader loader = new DomainLoader(player.getUniqueId(), true);
                    MineDomains.getInstance().getDomainManager().loadOrCreateDomain(domain, loader);
                    player.closeInventory();
                }
            } else if (clicked.sameSlot(backButton)) {
                domainMainMenu.open();
            }


        } catch (ConfigMenuDefinitionException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void handleMenuClose(InventoryCloseEvent e) {
    }

    private boolean canCreate() {
        return domainName != null && theme != null;
    }

    private MenuIcon getNameEntryIcon() throws ConfigMenuDefinitionException{
        MenuIcon nameEntry;
        if (domainName == null) {
            nameEntry = MenuIcon.fromConfig(menuPath + ".name-entry-pre-select");
        } else {
            nameEntry = MenuIcon.fromConfig(menuPath + ".name-entry-post-select");
            nameEntry.lorePlaceholder("{NAME}", domainName);
        }
        return nameEntry;
    }

    private MenuIcon getThemeSelectIcon() throws ConfigMenuDefinitionException {
        MenuIcon themeSelect;
        if (theme == null) {
            themeSelect = MenuIcon.fromConfig(menuPath + ".theme-pre-select");
        } else {
            themeSelect = MenuIcon.fromConfig(menuPath + ".theme-post-select");
            themeSelect.materialPlaceholder("{THEME_DISPLAY_BLOCK}", theme.getDisplayBlock());
            themeSelect.lorePlaceholder("{THEME}", theme.getDisplayName());
        }
        return themeSelect;
    }

    private MenuIcon getConfirmCreateIcon() throws ConfigMenuDefinitionException {
        MenuIcon confirmCreate;
        if (!canCreate()) {
            confirmCreate = MenuIcon.fromConfig(menuPath + ".confirm-create-pre-selections");
        } else {
            confirmCreate = MenuIcon.fromConfig(menuPath + ".confirm-create-post-selections");
        }
        return confirmCreate;
    }
}
