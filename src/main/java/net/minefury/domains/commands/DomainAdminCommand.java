package net.minefury.domains.commands;

import net.minefury.domains.MineDomains;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class DomainAdminCommand extends BukkitCommand {

    public DomainAdminCommand() {
        super("domainadmin");
        setAliases(List.of("da", "domaina", "dadmin"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(FuryMessage.INVALID_SENDER.toString());
        }

        Player player = (Player) sender;
        int length = args.length;

        if (length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                MineDomains.getInstance().getDataManager().reloadConfigurations();
                FuryMessage.RELOAD_SUCCESS.send(player);
            }
        }

        return false;
    }
}
