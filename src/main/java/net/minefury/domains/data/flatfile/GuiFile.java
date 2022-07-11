package net.minefury.domains.data.flatfile;

import com.modify.fundamentum.config.PlugFile;
import lombok.NonNull;
import net.minefury.domains.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class GuiFile extends PlugFile {

    public GuiFile() {
        super("gui");
    }

    private String getString(String path) {
        return get().getString(path, "");
    }

    public String getMenuOrIconDisplayName(String path) {
        return ColorUtil.format(getString(path + ".name"));
    }

    public String getMaterial(String iconPath) {
        return get().getString(iconPath + ".material", "GRASS_BLOCK");
    }

    public int getSlot(String iconPath) {
        return get().getInt(iconPath + ".slot", -1);
    }

    public boolean getEnchantGlow(String iconPath) {
        return get().getBoolean(iconPath + ".enchant-glow", false);
    }

    public String getSkullOwner(String iconPath) {
        return get().getString(iconPath + ".skull-owner", null);
    }

    public List<String> getLore(String iconPath) {
        return ColorUtil.formatList(get().getStringList(iconPath + ".lore"));
    }

    public @NonNull Material getMenuFiller(String menuPath) {
        Material matched = Material.matchMaterial(getString(menuPath + ".filler.material"));
        return matched != null ? matched : Material.WHITE_STAINED_GLASS_PANE;
    }

    public @NonNull int getMenuSlots(String menuPath) {
        return get().getInt(menuPath + ".slots", 54);
    }

    public ConfigurationSection getConfigSection(String path) {
        return get().getConfigurationSection(path);
    }

    public int getInteger(String path) {
        return get().getInt(path, -1);
    }
}
