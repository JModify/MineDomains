package net.minefury.domains.menu.domaintravel;

import net.minefury.domains.MineDomains;
import net.minefury.domains.data.mongo.MongoHelper;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.DomainHelper;
import net.minefury.domains.domain.DomainLoader;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.DomainMainMenu;
import net.minefury.domains.menu.objects.MenuIcon;
import net.minefury.domains.menu.objects.StandardMenu;
import net.minefury.domains.slime.registry.DomainRegistry;
import net.minefury.domains.util.FuryUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class DomainTravelMenu extends StandardMenu {

    private DomainMainMenu domainMainMenu;

    private final int[] DOMAIN_LIST_SLOTS = {11, 12, 13, 14, 15};

    public DomainTravelMenu(Player player, DomainMainMenu mainMenu) {
        super(player, "domain-travel");
        this.domainMainMenu = mainMenu;
    }

    private MenuIcon getDomainIcon(Domain domain, int predefinedSlot) throws ConfigMenuDefinitionException {
        MenuIcon icon;
        if (domain.getOwner().equals(player.getUniqueId())) {
            icon = MenuIcon.fromConfigWithPredefinedSlot(menuPath + ".domain-is-owner-icon", predefinedSlot);
        } else {
            icon = MenuIcon.fromConfigWithPredefinedSlot(menuPath + ".domain-is-member-icon", predefinedSlot);
        }

        icon.namePlaceholder("{DOMAIN_NAME}", domain.getName());
        icon.lorePlaceholder("{DOMAIN_OWNER}", Bukkit.getOfflinePlayer(domain.getOwner()).getName());

        if (!domain.getMembers().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (UUID uuid : domain.getMembers().keySet()) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);
                builder.append(member.getName());
                builder.append(", ");
            }
            String members = builder.toString().trim().substring(0, builder.length() - 1);
            icon.lorePlaceholder("{DOMAIN_MEMBERS}", members);
        } else {
            icon.lorePlaceholder("{DOMAIN_MEMBERS}", "None");
        }

        icon.lorePlaceholder("{DOMAIN_THEME}", domain.getTheme().getDisplayName());
        icon.lorePlaceholder("{DOMAIN_SIZE}", String.valueOf(domain.getUpgradables().getBorder().getWorldSize()));
        icon.lorePlaceholder("{DOMAIN_ID}", domain.getId().toString());

        return icon;
    }

    @Override
    public void setMenuItems() throws ConfigMenuDefinitionException {

        Inventory inventory = getInventory();

        List<Domain> userDomains = MongoHelper.getDomains(player.getUniqueId());

        for (int i = 0; i < 5; i++) {

            // Magic number "11" converts slot number to index in domainsIsMember list.
            int indexToSlot = i + 11;
            if (i < userDomains.size()) {
                Domain domain = userDomains.get(i);
                MenuIcon icon = getDomainIcon(domain, indexToSlot);
                inventory.setItem(icon.getSlot(), icon.getItem());
            } else {
                MenuIcon icon = MenuIcon.fromConfigWithPredefinedSlot(menuPath + ".empty-domain-slot", indexToSlot);
                inventory.setItem(icon.getSlot(), icon.getItem());
            }
        }

        MenuIcon backButton = MenuIcon.fromConfig(menuPath + ".back-button");
        inventory.setItem(backButton.getSlot(), backButton.getItem());
    }

    @Override
    public void handleMenuClick(InventoryClickEvent e) {

        int slot = e.getSlot();

        try {
            MenuIcon clicked = MenuIcon.fromClick(e.getCurrentItem(), slot);
            MenuIcon backButton = MenuIcon.fromConfig(menuPath + ".back-button");

            if (clicked.sameSlot(backButton)) {
                domainMainMenu.open();
            } else {
                if (clicked.getItem().getItemMeta()!= null) {

                    ItemMeta meta = clicked.getItem().getItemMeta();

                    if (meta.getLore() != null) {
                        List<String> lore = meta.getLore();

                        UUID domainId = FuryUtil.findUUID(lore);

                        DomainRegistry registry = MineDomains.getInstance().getDomainManager().getDomainRegistry();
                        if (!registry.isDomainLoaded(domainId)) {
                            Domain domain = MongoHelper.getDomain(domainId);
                            DomainLoader loader = new DomainLoader(player.getUniqueId(), true);
                            MineDomains.getInstance().getDomainManager().loadOrCreateDomain(domain, loader);
                            player.closeInventory();
                        } else {
                            Domain domain = registry.getDomain(domainId);
                            DomainHelper.attemptDomainTeleport(player, domain);
                        }
                    }
                }
            }

        } catch (ConfigMenuDefinitionException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void handleMenuClose(InventoryCloseEvent e) {

    }
}
