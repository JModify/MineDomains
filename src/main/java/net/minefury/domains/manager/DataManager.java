package net.minefury.domains.manager;

import com.modify.fundamentum.text.PlugLogger;
import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.data.flatfile.GuiFile;
import net.minefury.domains.data.flatfile.MessagesFile;
import net.minefury.domains.data.mongo.MongoConnection;
import net.minefury.domains.domain.theme.Theme;
import net.minefury.domains.domain.theme.ThemeImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataManager {

    @Getter private MongoConnection connection;

    @Getter private ConfigFile config;
    @Getter private MessagesFile messages;
    @Getter private GuiFile gui;

    /**
     * Initializes all configuration files, copies theme templates, and makes a connection to mongo
     * to plugin data folder on startup.
     */
    public void init() {
        config = new ConfigFile();
        messages = new MessagesFile();
        gui = new GuiFile();

        for (Theme theme : ThemeImpl.values()){
            copyDomainTemplate(theme);
        }

        connection = new MongoConnection();
        connection.connect();
    }

    /**
     * Copies theme template file from resource location to plugin data folder
     * on startup.
     * @param theme theme to copy file for.
     */
    private void copyDomainTemplate(Theme theme) {
        File templateFile = new File(MineDomains.getInstance().getDataFolder() + "/themes/", theme.getNameRaw() + ".slime");

        if (templateFile.exists()) {
            return;
        }

        InputStream resource = MineDomains.getInstance().getResource("themes/" + theme.getNameRaw() + ".slime");
        try {
            FileUtils.copyInputStreamToFile(resource, templateFile);
        } catch (NullPointerException | IOException e) {
            PlugLogger.logError("Failed to copy template resource to file.");
            e.printStackTrace();
        }
    }

    /**
     * Reloads all configuration files
     */
    public void reloadConfigurations() {
        // Initialize config files again
        config.reload();
        messages.reload();
        gui.reload();

        // Initialize task manager again in case of config changes surrounding repeating timers.
        MineDomains.getInstance().getTaskManager().init();
    }



}
