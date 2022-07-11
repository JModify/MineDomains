package net.minefury.domains.message;

import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.MessagesFile;

public enum MessagePrefix {

    ERROR("error"),
    GENERAL("general"),
    NOTIFICATION("notification"),
    ADMIN("admin"),
    CUSTOM("custom"),
    QUEUE("queue");

    private String key;

    MessagePrefix(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        MessagesFile file = MineDomains.getInstance().getDataManager().getMessages();
        return file.getPrefix(key);
    }

    public static MessagePrefix fromString(String string) {
        for (MessagePrefix mp : MessagePrefix.values()) {
            if (mp.key.equalsIgnoreCase(string)) {
                return mp;
            }
        }
        return null;
    }

}
