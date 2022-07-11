package net.minefury.domains.domain.theme;

public enum ThemeImpl implements Theme {


    OASIS {

        @Override
        public String getDescription() {
            return "Hybrid landscape between a desert and plains biome.";
        }

        @Override
        public String getNameRaw() {
            return "oasis";
        }

        @Override
        public String getDisplayBlock() {
            return "SAND";
        }

        @Override
        public String getDisplayName() {
            return "Oasis";
        }
    },

    JUNGLE_LAND {

        @Override
        public String getDescription() {
            return "Tropical rainforest with dense canopies and vegetation.";
        }

        @Override
        public String getNameRaw() {
            return "jungle_land";
        }

        @Override
        public String getDisplayBlock() {
            return "JUNGLE_LEAVES";
        }

        @Override
        public String getDisplayName() {
            return "Jungle Land";
        }
    },

    GRASS_LAND {

        @Override
        public String toString() {
            return "Grass Land";
        }

        @Override
        public String getDescription() {
            return "Flat, grassy landscape consisting of very little vegetation.";
        }

        @Override
        public String getNameRaw() {
            return "grass_land";
        }

        @Override
        public String getDisplayBlock() {
            return "GRASS";
        }

        @Override
        public String getDisplayName() {
            return "Grass Land";
        }


    },

    GREEN_LAND {
        @Override
        public String toString() {
            return "Green Land";
        }

        @Override
        public String getDescription() {
            return "Default Minecraft plains biome.";
        }

        @Override
        public String getNameRaw() {
            return "green_land";
        }

        @Override
        public String getDisplayBlock() {
            return "GRASS_BLOCK";
        }

        @Override
        public String getDisplayName() {
            return "Green Land";
        }
    }

}
