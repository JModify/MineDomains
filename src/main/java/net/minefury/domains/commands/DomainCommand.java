package net.minefury.domains.commands;

import net.minefury.domains.menu.DomainMainMenu;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class DomainCommand extends BukkitCommand {

    public DomainCommand() {
        super("domain");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(FuryMessage.INVALID_SENDER.toString());
        }

        Player player = (Player) sender;
        int length = args.length;

        if (length == 0) {
            DomainMainMenu domainMainMenu = new DomainMainMenu(player);
            domainMainMenu.open();
        }

        return false;
    }
}
