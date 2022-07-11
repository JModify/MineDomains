package net.minefury.domains.commands;

import net.minefury.domains.MineDomains;
import net.minefury.domains.menu.DomainMainMenu;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class TestCommand extends BukkitCommand {

    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(String.valueOf(FuryMessage.INVALID_SENDER));
        }

        int length = args.length;
        Player player = (Player) sender;

        if (length > 1) {
            if (args[0].equalsIgnoreCase("debug")) {

                if (args[1].equalsIgnoreCase("on")) {
                    MineDomains.getInstance().getDebugger().setDebugMode(true);
                    player.sendMessage("Debug mode enabled");
                } else if (args[1].equalsIgnoreCase("off")) {
                    MineDomains.getInstance().getDebugger().setDebugMode(false);
                    player.sendMessage("Debug mode disabled");
                }

            } else if (args[0].equalsIgnoreCase("open")) {
                DomainMainMenu domainMainMenu = new DomainMainMenu(player);
                domainMainMenu.open();
            }
        }

        return false;
    }

}
