package net.minefury.domains.domain.properties;

import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.exceptions.DocumentParseException;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.UUID;

public class DomainBorder {

    private WorldBorder border;
    @Getter private int worldSize;

    public DomainBorder(int worldSize) {
        this.worldSize = worldSize;
    }

    public DomainBorder() {
        ConfigFile file = MineDomains.getInstance().getDataManager().getConfig();
        this.worldSize = file.getInteger("defaults.default-domain-size", 250);
    }

    public void setBorder(UUID worldId) {
        World bukkitWorld = Bukkit.getWorld(worldId.toString());
        if (bukkitWorld != null) {
            border = bukkitWorld.getWorldBorder();
            border.setCenter(0.0, 0.0);
            border.setSize(worldSize);
        }
    }

    public void expand(int blocks) {
        this.worldSize = worldSize + blocks;
    }

    public void shrink(int blocks) {
        this.worldSize = worldSize - blocks;
    }

    public int getMaxWorldSize() {
        ConfigFile file = MineDomains.getInstance().getDataManager().getConfig();
        return file.getInteger("limits.max-domain-size", 500);
    }

    public Document toDocument() {
        Document domainBorder = new Document();
        domainBorder.append("worldSize", worldSize);
        return domainBorder;
    }

    public static DomainBorder fromDocument(Document document) throws DocumentParseException {
        int worldSize;
        try {
            Document upgradablesDocument = (Document) document.get("border");
            worldSize = upgradablesDocument.getInteger("worldSize");
        } catch(NullPointerException e) {
            throw new DocumentParseException("Failed to retrieve domain upgradables from mongo document.");
        }

        return new DomainBorder(worldSize);
    }

}
