package net.minefury.domains.menu.objects;

import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.GuiFile;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import org.bukkit.entity.Player;

public abstract class AnvilMenu {

    protected Player player;
    protected String menuPath;

    private MineDomains plugin;

    public AnvilMenu(Player player, String menuPath) {
        this.plugin = MineDomains.getInstance();
        this.player = player;
        this.menuPath = menuPath;
    }

    public String getMenuName() {
        GuiFile file = plugin.getDataManager().getGui();
        return file.getMenuOrIconDisplayName(menuPath);
    }

    public abstract void open() throws ConfigMenuDefinitionException;

    public abstract void handleMenuClose();

}
