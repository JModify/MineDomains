package net.minefury.domains.data.flatfile;

import net.minefury.domains.MineDomains;

public class FlatFileHelper {
    public static boolean isQueueEnabled() {
        return MineDomains.getInstance().getDataManager().getConfig().getBoolean("settings.queue.enabled", true);
    }
    
    public static long getQueueInterval() {
        return MineDomains.getInstance().getDataManager().getConfig().getLong("settings.queue.interval", 2L) * 20;
    }

    public static boolean isAutoUnloadEnabled() {
        return MineDomains.getInstance().getDataManager().getConfig().getBoolean("settings.auto-unload.enabled", true);
    }

    public static long getAutoUnloadInterval() {
        return MineDomains.getInstance().getDataManager().getConfig().getLong("settings.auto-unload.interval", 2L) * 20;
    }
}
