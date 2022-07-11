package net.minefury.domains.data.flatfile;

import com.modify.fundamentum.config.PlugFile;
import lombok.NonNull;
import net.minefury.domains.util.ColorUtil;

public class MessagesFile extends PlugFile {

    public MessagesFile() {
        super("messages");
    }

    private @NonNull String getString(@NonNull String path) {
        return get().getString(path, "");
    }

    public @NonNull String getPrefix(String prefixKey) {
        return ColorUtil.format(getString("prefixes." + prefixKey));
    }

    public @NonNull String getMessage(String messageKey) {
        return ColorUtil.format(getString("messages." + messageKey + ".message"));
    }

    public @NonNull String getMessagePrefix(String messageKey) {
        return getString("messages." + messageKey + ".type");
    }
}
