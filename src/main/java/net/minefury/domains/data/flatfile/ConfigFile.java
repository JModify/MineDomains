package net.minefury.domains.data.flatfile;

import com.modify.fundamentum.config.PlugFile;
import lombok.NonNull;

public class ConfigFile extends PlugFile {

    public ConfigFile() {
        super("config");
    }

    public String getString(String path) {
        return get().getString(path, "");
    }

    public String getString(String path, String def) {
        return get().getString(path, def);
    }

    public boolean getBoolean(@NonNull String path, boolean def) {
        return get().getBoolean(path, def);
    }

    public int getInteger(@NonNull String path, int def) {
        return get().getInt(path, def);
    }

    public long getLong(@NonNull String path, long def) {
        return get().getLong(path, def);
    }
}
