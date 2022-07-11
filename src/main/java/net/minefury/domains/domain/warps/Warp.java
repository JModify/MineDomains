package net.minefury.domains.domain.warps;

import lombok.Getter;
import net.minefury.domains.exceptions.DocumentParseException;
import org.bson.Document;

/**
 * Represents an individual warp a player may have in their domain.
 */
public class Warp {

    /** Name of the warp, visible to users in the UI */
    @Getter
    private String name;

    /** X-axis location of the warp */
    @Getter private int x;

    /** X-axis location of the warp */
    @Getter private int y;

    /** Z-axis location of the warp */
    @Getter private int z;

    /**
     * Constructs a new warp using the given parameters.
     * @param name name of the warp
     * @param x x-axis location of the warp
     * @param y y-axis location of the warp
     * @param z z-axis location of the warp
     */
    public Warp(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Document toDocument() {
        Document warp = new Document();
        warp.append("name", name);
        warp.append("x", x);
        warp.append("y", y);
        warp.append("z", z);

        return warp;
    }

    public static Warp fromDocument(Document document) throws DocumentParseException {
        String warpName;
        int x, y, z;

        try {
            warpName = document.getString("name");
            x = document.getInteger("x");
            y = document.getInteger("y");
            z = document.getInteger("z");
        } catch (NullPointerException e) {
            throw new DocumentParseException("Failed to retrieve domain warp from mongo document.");
        }

        return new Warp(warpName, x, y, z);
    }
}
