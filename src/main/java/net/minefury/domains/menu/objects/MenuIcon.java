package net.minefury.domains.menu.objects;

import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.GuiFile;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MenuIcon {

    private String displayName;
    private List<String> lore;
    private String materialRaw;
    private boolean enchantGlow;

    private UUID skullOwner;
    @Getter private int slot;

    public MenuIcon(String displayName, List<String> lore, String materialRaw, boolean enchantGlow, int slot) {
        this.displayName = displayName;
        this.lore = lore;
        this.materialRaw = materialRaw;
        this.enchantGlow = enchantGlow;
        this.slot = slot;
        this.skullOwner = null;
    }

    public MenuIcon(String displayName, List<String> lore, String materialRaw, UUID skullOwner, boolean enchantGlow, int slot) {
        this.displayName = displayName;
        this.lore = lore;
        this.materialRaw = materialRaw;
        this.enchantGlow = enchantGlow;
        this.slot = slot;
        this.skullOwner = skullOwner;
    }

    /**
     * Retrieves this menu icon as an item stack.
     * @return menu icon as an ItemStack.
     */
    public ItemStack getItem() {
        Material material = Material.matchMaterial(materialRaw);
        if (material == null) {
            MineDomains.getInstance().getDebugger().sendDebugError("Material is null for item " + materialRaw + ". Replacing with sponge.");
            material = Material.SPONGE;
        }

        ItemStack item = new ItemStack(material, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        if (enchantGlow) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (material == Material.PLAYER_HEAD && skullOwner != null) {
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));

            MineDomains.getInstance().getDebugger().sendDebugInfo("Owning player for meta set to " + skullOwner.toString());
        }

        item.setItemMeta(meta);

        return item;
    }

    public static MenuIcon fromClick(ItemStack clickedItem, int slot) {
        Material material = clickedItem.getType();

        ItemMeta meta = clickedItem.getItemMeta();

        String name = meta.getDisplayName();
        List<String> lore = meta.getLore();
        boolean enchantGlow = meta.hasEnchants();

        return new MenuIcon(name, lore, material.name(), enchantGlow, slot);
    }

    /**
     * Retrieves a MenuIcon from GUI config file.
     *
     * @param iconPath menu path of icon (e.g. main-menu.create-domain)
     * @return menu icon object from gui.yml file.
     *
     * @throws ConfigMenuDefinitionException thrown if menu icon configuration is incorrect.
     */
    public static MenuIcon fromConfig(String iconPath) throws ConfigMenuDefinitionException {
        GuiFile file = MineDomains.getInstance().getDataManager().getGui();

        // Slot is required to be a valid configuration-defined integer so must check.
        int slot = file.getSlot(iconPath);
        if (slot == -1) {
            throw new ConfigMenuDefinitionException("Slot is not set for " + iconPath + " icon");
        }

        // Universal default (empty string) of display name exists, so no need to check further.
        String displayName = file.getMenuOrIconDisplayName(iconPath);

        // Lore does not need to be checked as it can be empty.
        List<String> lore = file.getLore(iconPath);

        // Universal default (false) of enchant glow exists, so no need to check further.
        boolean enchantGlow = file.getEnchantGlow(iconPath);

        // Universal default (GRASS_BLOCK) of material exists, so no need to check further.
        String material = file.getMaterial(iconPath);

        UUID skullUUID = null;
        String uuidRaw = file.getSkullOwner(iconPath);
        if (uuidRaw != null && !uuidRaw.isEmpty()) {
            try {
                skullUUID = UUID.fromString(uuidRaw);
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new ConfigMenuDefinitionException("Skull owner for icon " + iconPath + " is not a valid UUID.");
            }
        }

        return new MenuIcon(displayName, lore, material, skullUUID, enchantGlow, slot);
    }

    /**
     * Retrieves a MenuIcon from GUI config file with a pre-defined slot.
     *
     * @param iconPath menu path of icon (e.g. main-menu.create-domain)
     * @param iconSlot slot to place this icon in.
     * @return menu icon object from gui.yml file.
     *
     * @throws ConfigMenuDefinitionException thrown if menu icon configuration is incorrect.
     */
    public static MenuIcon fromConfigWithPredefinedSlot(String iconPath, int iconSlot) throws ConfigMenuDefinitionException {
        GuiFile file = MineDomains.getInstance().getDataManager().getGui();

        // Universal default (empty string) of display name exists, so no need to check further.
        String displayName = file.getMenuOrIconDisplayName(iconPath);

        // Lore does not need to be checked as it can be empty.
        List<String> lore = file.getLore(iconPath);

        // Universal default (false) of enchant glow exists, so no need to check further.
        boolean enchantGlow = file.getEnchantGlow(iconPath);

        // Universal default (GRASS_BLOCK) of material exists, so no need to check further.
        String material = file.getMaterial(iconPath);

        UUID skullUUID = null;
        String uuidRaw = file.getSkullOwner(iconPath);
        if (uuidRaw != null && !uuidRaw.isEmpty()) {
            try {
                skullUUID = UUID.fromString(uuidRaw);
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new ConfigMenuDefinitionException("Skull owner for icon " + iconPath + " is not a valid UUID.");
            }
        }

        return new MenuIcon(displayName, lore, material, skullUUID, enchantGlow, iconSlot);
    }

    /**
     * Replaces a placeholder present in the menu icon's lore.
     * @param placeholder placeholder to replace
     * @param replacement replacement for placeholder
     */
    public void lorePlaceholder(String placeholder, String replacement) {
        lore.replaceAll(t -> {
            if (t.contains(placeholder)) {
                return t.replace(placeholder, replacement).trim();
            }
            return t;
        });
    }

    /**
     * Replaces a placeholder present in the menu icon's display name.
     * @param placeholder placeholder to replace
     * @param replacement replacement for placeholder
     */
    public void namePlaceholder(String placeholder, String replacement) {
        if (displayName.contains(placeholder)) {
            displayName = displayName.replace(placeholder, replacement).trim();
        }
    }

    /**
     * Replaces a placeholder for the icon's material type.
     * @param placeholder placeholder to replace
     * @param replacement replacement for placeholder
     */
    public void materialPlaceholder(String placeholder, String replacement) {
        if (materialRaw.contains(placeholder)) {
            materialRaw = materialRaw.replace(placeholder, replacement);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuIcon menuIcon = (MenuIcon) o;
        return enchantGlow == menuIcon.enchantGlow
                && slot == menuIcon.slot
                && Objects.equals(ChatColor.stripColor(displayName), ChatColor.stripColor(menuIcon.displayName))
                && Objects.equals(ColorUtil.stripListColor(lore), ColorUtil.stripListColor(menuIcon.lore))
                && Objects.equals(materialRaw, menuIcon.materialRaw);
    }

    public boolean sameSlot(MenuIcon otherIcon) {
        return this.slot == otherIcon.getSlot();
    }
}
