package net.minefury.domains.domain.theme;

import net.minefury.domains.exceptions.ThemeParseException;

public interface Theme {

    /**
     * Retrieves the description for this theme
     */
    String getDescription();

    /**
     * Retrieves the schematic name for which this theme will be saved under.
     * @return name of schematic excluding file extension
     */
    String getNameRaw();

    /**
     * Display block used in menu's involving this theme.
     * @return display block
     */
    String getDisplayBlock();

    /**
     * Display name used where this theme is displayed to the player.
     * @return display name
     */
    String getDisplayName();

    /**
     * Attempts to parse the given string as a valid domain theme
     * @param s string to parse
     * @return parsed theme
     */
    static Theme parseTheme(String s) throws ThemeParseException {
        Theme parsed = null;
        for (Theme theme : ThemeImpl.values()) {
            if (theme.getNameRaw().equalsIgnoreCase(s)) {
                parsed = theme;
            }
        }

        if (parsed != null) {
            return parsed;
        }
        throw new ThemeParseException("Theme " + s + " not recognised.");
    }
}
