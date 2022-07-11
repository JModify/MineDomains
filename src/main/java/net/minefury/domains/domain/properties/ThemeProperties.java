package net.minefury.domains.domain.properties;

import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.domain.theme.Theme;

public class ThemeProperties {

    @Getter
    private int spawnX;

    @Getter
    private int spawnY;

    @Getter
    private int spawnZ;

    @Getter
    private String difficulty;

    @Getter
    private String environment;

    @Getter
    private boolean allowAnimals;

    @Getter
    private boolean allowMonsters;

    @Getter
    private String defaultBiome;

    @Getter
    private String worldType;

    @Getter
    private boolean allowPvp;

    public ThemeProperties(Theme theme) {
        ConfigFile file = MineDomains.getInstance().getDataManager().getConfig();
        String themeName = theme.getNameRaw();

        this.spawnX = file.getInteger("properties." + themeName + ".domain-spawn.x", 0);
        this.spawnY = file.getInteger("properties." + themeName + ".domain-spawn.y", 100);
        this.spawnZ = file.getInteger("properties." + themeName + ".domain-spawn.z", 0);
        this.difficulty = file.getString("properties." + themeName + ".difficulty", "normal");
        this.environment = file.getString("properties." + themeName + ".environment", "NORMAL");
        this.allowAnimals = file.getBoolean("properties." + themeName + ".allow-animals", true);
        this.allowMonsters = file.getBoolean("properties." + themeName + ".allow-monsters", true);
        this.defaultBiome = file.getString("properties." + themeName + ".default-biome", "minecraft:plains");
        this.worldType = file.getString("properties." + themeName + ".world-type", "DEFAULT");
        this.allowPvp = file.getBoolean("properties." + themeName + ".pvp", true);
    }

    public SlimePropertyMap toSlimePropertyMap() {
        SlimePropertyMap propertyMap = new SlimePropertyMap();
        propertyMap.setValue(SlimeProperties.SPAWN_X, spawnX);
        propertyMap.setValue(SlimeProperties.SPAWN_Y, spawnY);
        propertyMap.setValue(SlimeProperties.SPAWN_Z, spawnZ);
        propertyMap.setValue(SlimeProperties.DIFFICULTY, difficulty);
        propertyMap.setValue(SlimeProperties.ENVIRONMENT, environment);
        propertyMap.setValue(SlimeProperties.ALLOW_ANIMALS, allowAnimals);
        propertyMap.setValue(SlimeProperties.ALLOW_MONSTERS, allowMonsters);
        propertyMap.setValue(SlimeProperties.DEFAULT_BIOME, defaultBiome);
        propertyMap.setValue(SlimeProperties.WORLD_TYPE, worldType);
        propertyMap.setValue(SlimeProperties.PVP, allowPvp);

        return propertyMap;
    }


}
