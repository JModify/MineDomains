package net.minefury.domains.hooks;

import com.modify.fundamentum.hook.PlugHook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PAPIHook extends PlugHook {

    public PAPIHook() {
        super("PlaceholderAPI", false);
    }

    public String replacePlaceholders(Player player, String s) {
        if (isHooked()) {
            return PlaceholderAPI.setPlaceholders(player, s);
        }
        return s;
    }

}
