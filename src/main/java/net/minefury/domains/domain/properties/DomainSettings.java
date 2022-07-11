package net.minefury.domains.domain.properties;

import lombok.Getter;
import lombok.Setter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.domain.theme.Theme;
import net.minefury.domains.exceptions.DocumentParseException;
import org.bson.Document;

public class DomainSettings {

    @Setter
    private boolean notifyLeave;

    @Setter
    private boolean notifyJoin;

    @Getter @Setter
    private boolean allowGuests;

    @Getter @Setter
    private int spawnX, spawnY, spawnZ;

    public DomainSettings(Theme theme) {
        ConfigFile file = MineDomains.getInstance().getDataManager().getConfig();
        this.notifyJoin = file.getBoolean("defaults.notify-join", true);
        this.notifyLeave = file.getBoolean("defaults.notify-leave", true);
        this.allowGuests = file.getBoolean("defaults.allow-guests", false);

        ThemeProperties themeProperties = new ThemeProperties(theme);
        this.spawnX = themeProperties.getSpawnX();
        this.spawnY = themeProperties.getSpawnY();
        this.spawnZ = themeProperties.getSpawnZ();
    }

    public DomainSettings(boolean allowGuests, boolean notifyJoin, boolean notifyLeave, int spawnX, int spawnY, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.notifyJoin = notifyJoin;
        this.notifyLeave = notifyLeave;
        this.allowGuests = allowGuests;
    }

    public boolean shouldNotifyLeave() {
        return notifyLeave;
    }

    public boolean shouldNotifyJoin() {
        return notifyJoin;
    }

    public Document toDocument() {
        Document settings = new Document();
        settings.append("allowGuests", allowGuests);
        settings.append("notifyJoin", notifyJoin);
        settings.append("notifyLeave", notifyLeave);
        settings.append("spawnX", spawnX);
        settings.append("spawnY", spawnY);
        settings.append("spawnZ", spawnZ);
        return settings;
    }

    public static DomainSettings fromDocument(Document document) throws DocumentParseException {

        boolean allowGuests, notifyJoin, notifyLeave;
        int spawnX, spawnY, spawnZ;

        try {
            Document settingsDocument = (Document) document.get("settings");

            allowGuests = settingsDocument.getBoolean("allowGuests");
            notifyJoin = settingsDocument.getBoolean("notifyJoin");
            notifyLeave = settingsDocument.getBoolean("notifyLeave");
            spawnX = settingsDocument.getInteger("spawnX");
            spawnY = settingsDocument.getInteger("spawnY");
            spawnZ = settingsDocument.getInteger("spawnZ");
        } catch (NullPointerException e) {
            throw new DocumentParseException("Failed to retrieve domain settings from mongo document.");
        }

        return new DomainSettings(allowGuests, notifyJoin, notifyLeave,
                spawnX, spawnY, spawnZ);
    }
}
