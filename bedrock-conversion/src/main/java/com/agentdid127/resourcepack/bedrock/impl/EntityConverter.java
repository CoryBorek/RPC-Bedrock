package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class EntityConverter extends Converter {
    public EntityConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("textures");
        Path entityPath = texturesPath.resolve("entity");
        if (!entityPath.toFile().exists())
            return;

        {
            // Rename Shulkers
            Path shulkerPath = entityPath.resolve("shulker");
            FileUtil.moveIfExists(shulkerPath.resolve("shulker.png"), shulkerPath.resolve("shulker_undyed.png"));
            FileUtil.moveIfExists(shulkerPath.resolve("shulker_light_gray.png"),
                    shulkerPath.resolve("shulker_silver.png"));
        }

        {
            // Skulls
            Path skullsPath = entityPath.resolve("skulls");
            if (!skullsPath.toFile().exists())
                skullsPath.toFile().mkdirs();
            // Copy Textures
            FileUtil.copyIfExists(entityPath.resolve("creeper/creeper.png".replace("/", File.separator)),
                    skullsPath.resolve("creeper.png"));
            FileUtil.copyIfExists(entityPath.resolve("skeleton/skeleton.png".replace("/", File.separator)),
                    skullsPath.resolve("skeleton.png"));
            FileUtil.copyIfExists(entityPath.resolve("skeleton/wither_skeleton.png".replace("/", File.separator)),
                    skullsPath.resolve("wither_skeleton.png"));
            FileUtil.copyIfExists(entityPath.resolve("zombie/zombie.png".replace("/", File.separator)),
                    skullsPath.resolve("zombie.png"));
        }

        {
            // Convert Beds
            Path bedsPath = entityPath.resolve("bed");
            if (bedsPath.toFile().exists())
                convert_beds(bedsPath);
        }

        // Convert Zombies

        // Convert Conduit

        // Other
    }

    private void convert_beds(Path bedsPath) throws IOException {
        String[] bedNames = new String[] {
                "black.png",
                "blue.png",
                "brown.png",
                "cyan.png",
                "gray.png",
                "green.png",
                "light_blue.png",
                "light_gray.png",
                "lime.png",
                "magenta.png",
                "orange.png",
                "pink.png",
                "purple.png",
                "red.png",
                "white.png",
                "yellow.png"
        };

        for (String bedName : bedNames) {
            Path bedPath = bedsPath.resolve(bedName);
            if (!bedPath.toFile().exists())
                continue;

            ImageConverter converter = new ImageConverter(64, 64, bedPath);
            converter.newImage(64, 64);

            // Bed Left Side Top | Bed Top (Pillow) | Bed Right Side Top | Bed Top Back
            // (Pillow) | Bed Planks Bottom
            converter.subImageSized(0, 0, 44, 22);

            // Bed Footer
            converter.subImageSized(22, 22, 16, 6, 22, 0);

            // Bed Left Side Bottom | Bed Bottom | Bed Right Side Bottom | Bed Planks Bottom
            converter.subImageSized(0, 28, 44, 16, 0, 22);

            converter.store();
        }
    }
}
