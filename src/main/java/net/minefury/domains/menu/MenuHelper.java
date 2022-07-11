package net.minefury.domains.menu;

import lombok.NonNull;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.GuiFile;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.objects.MenuIcon;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuHelper {

    public static @NonNull ItemStack getFiller(String menuPath) {
        GuiFile file = MineDomains.getInstance().getDataManager().getGui();
        Material material = file.getMenuFiller(menuPath);

        ItemStack filler = new ItemStack(material, 1);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);
        return filler;
    }

    public static void addFillers(Inventory inv, ItemStack fillerItem){
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack is = inv.getItem(i);
            if(is == null || is.getType() == Material.AIR){
                inv.setItem(i, fillerItem);
            }
        }
    }

    public static void addFillers(Inventory inv, ItemStack fillerItem, int rangeMin, int rangeMax){
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack is = inv.getItem(i);
            if (i >= rangeMin && i <= rangeMax) {
                if(is == null || is.getType() == Material.AIR){
                    inv.setItem(i, fillerItem);
                }
            }
        }
    }
    
    public static int getMenuSlots(String menuPath) {
        GuiFile f = MineDomains.getInstance().getDataManager().getGui();
        return f.getMenuSlots(menuPath);
    }
    
    public static String getMenuName(String menuPath) {
        GuiFile f = MineDomains.getInstance().getDataManager().getGui();
        return f.getMenuOrIconDisplayName(menuPath);
    }

    /**
     * Set menu items based on configuration section for this menu path.
     * Only able to used if the config section has no default replacements.
     * @param inventory inventory to set items.
     * @param menuPath menu path in config.
     */
    public static void setMenuItemsNoReplacements(Inventory inventory, String menuPath) throws ConfigMenuDefinitionException {
        GuiFile file = MineDomains.getInstance().getDataManager().getGui();

        for (String iconName : file.getConfigSection(menuPath).getKeys(false)) {
            if (iconName.equals("filler") || iconName.equals("name") || iconName.equals("slots")) {
                continue;
            }

            MenuIcon icon = MenuIcon.fromConfig(menuPath + "." + iconName);
            inventory.setItem(icon.getSlot(), icon.getItem());
        }
    }

    public static boolean isValidClick(int slot, String menuPath) {
        GuiFile f = MineDomains.getInstance().getDataManager().getGui();
        for (String s : f.getConfigSection(menuPath).getKeys(false)) {
            int validSlot = f.getInteger(menuPath + "." + s + ".slot");
            if (validSlot == slot) {
                return true;
            }
        }
        return false;
    }
}
