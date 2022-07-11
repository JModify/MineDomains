package net.minefury.domains.message;

import com.modify.fundamentum.text.PlugLogger;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.MessagesFile;
import org.bukkit.command.CommandSender;

public enum FuryMessage {

    NO_PERMISSION("no_permission"),
    INVALID_USAGE("invalid_usage"),
    INVALID_SENDER("invalid_sender"),
    PLAYER_NOT_FOUND("player_not_found"),
    INVALID_THEME("invalid_theme"),
    RELOAD_SUCCESS("reload_success"),
    DOMAIN_LOADED("domain_loaded"),
    DOMAIN_QUEUE_UPDATE("domain_queue_update"),
    DOMAIN_QUEUE_JOINED("domain_queue_joined"),
    DOMAIN_CREATE_CANCELLED("domain_create_cancelled"),
    MAX_DOMAINS("max_domains"),
    DOMAIN_NOT_LOADED("domain_not_loaded");

    private String key;

    FuryMessage(String key) {
        this.key = key;
    }

    @Override
    public String toString(){
        MessagesFile file = MineDomains.getInstance().getDataManager().getMessages();

        String prefix = file.getMessagePrefix(key);
        String message = file.getMessage(key);

        if (prefix.equalsIgnoreCase("raw")) {
            return message;
        }

        MessagePrefix messagePrefix = MessagePrefix.fromString(prefix);
        if (messagePrefix == null) {
            PlugLogger.logError("Failed to send message " + key + ". Prefix " + prefix + " not found.");
            return null;
        }

        return messagePrefix + message;
    }

    public void send(CommandSender sender) {
        String message = toString();
        if (message != null) {
            sender.sendMessage(message);
        }
    }



}
