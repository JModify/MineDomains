package net.minefury.domains.util;

import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.properties.Role;
import net.minefury.domains.exceptions.UUIDFormatException;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class FuryUtil {

    /**
     * Register command in the command map to avoid listing commands in the plugin.yml
     * @param command command to register
     */
    public static void registerCommand(BukkitCommand command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static boolean anyOnline(Domain domain) {
        boolean online = false;

        for (UUID uuid : domain.getMembers().keySet()) {
            if (Bukkit.getPlayer(uuid) != null) {
                online = true;
            }
        }

        if (Bukkit.getPlayer(domain.getOwner()) != null) {
            online = true;
        }

        return online;
    }

    /**
     * Notify all members of the given domain that the domain has been loaded
     * @param domain domain to notify it's members.
     * @param time time it took to load domain
     */
    public static void notifyDomainLoad(Domain domain, long time) {
        String message = FuryMessage.DOMAIN_LOADED.toString()
                .replace("{TIME}", String.valueOf(time));

        Map<UUID, Role> members = domain.getMembers();
        for (UUID member : members.keySet()) {
            Player player = Bukkit.getPlayer(member);

            if (player != null) {
                player.sendMessage(message);
            }
        }

        Player owner = Bukkit.getPlayer(domain.getOwner());
        if(owner != null) {
            owner.sendMessage(message);
        }
    }

    public static boolean isUUID(String uuid) {
        String regex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
        Pattern pattern = Pattern.compile(regex);

        if (pattern.matcher(uuid).matches()) {
            return true;
        }
        return false;
    }

    /**
     * Splits string using the given delimiter, then capitalizes
     * the first letter of each word. Space is inserted where delimiter is found.
     * @param string string to split and capitalize.
     * @return the split string with the first letter of each word capitalized
     */
    public static String splitAndCapitalize(String string, String delimiter) {
        String[] parts = string.split(delimiter);
        String joined = String.join(" ", parts);

        return toTitleCase(joined);
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }

        return sb.toString().trim();
    }

    /**
     * Retrieves the username for the player with this UUID.
     *
     * @param user UUID of user to get name for.
     *
     * @requires user has joined the server before.
     * @ensures a non-null return value.
     *
     * @return username of this user.
     */
    public static String getUsername(UUID user) {
        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(user);
            return player.getName();
        } catch (NullPointerException e) {
            MineDomains.getInstance().getDebugger().sendDebugError("Failed to retrieve username for "
                    + user.toString() + ". User has never joined this server.");
            return null;
        }
    }

    public static UUID parseUUID(String id) throws UUIDFormatException {
        UUID result;
        try {
            result = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new UUIDFormatException("Failed to parse " + id + " as UUID.");
        }

        return result;
    }

    public static UUID findUUID(List<String> list) {

        UUID uuid = null;
        for (String loreEntry : list) {
            String strippedEntry = ChatColor.stripColor(loreEntry);
            if (isUUID(strippedEntry)) {
                uuid = UUID.fromString(strippedEntry);
            }
        }

        return uuid;
    }



}
