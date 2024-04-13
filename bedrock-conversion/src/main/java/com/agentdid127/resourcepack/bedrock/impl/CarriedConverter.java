package com.agentdid127.resourcepack.bedrock.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;

public class CarriedConverter extends Converter {
    public CarriedConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("textures");
        if (!texturesPath.toFile().exists())
            return;

        Path blocksFolderPath = texturesPath.resolve("blocks");
        if (blocksFolderPath.toFile().exists()) {
            Path lilyPath = blocksFolderPath.resolve("waterlily.png");
            if (lilyPath.toFile().exists())
                colorize(lilyPath, blocksFolderPath.resolve("carried_waterlily.png"), new Color(101, 155, 54));

            Path vinePath = blocksFolderPath.resolve("vine.png");
            if (vinePath.toFile().exists())
                colorize(vinePath, blocksFolderPath.resolve("vine_carried.png"), new Color(101, 155, 54));

            Path grassBlockSideOverlay = blocksFolderPath.resolve("grass_block_side_overlay.png");
            if (grassBlockSideOverlay.toFile().exists())
                background(blocksFolderPath, grassBlockSideOverlay, blocksFolderPath.resolve("grass_side_carried.png"),
                        0,
                        "grass_side.tga", false);
        }

        Path itemsFolderPath = texturesPath.resolve("items");
        if (itemsFolderPath.toFile().exists()) {
            Path fireworksChargePath = itemsFolderPath.resolve("fireworks_charge.png");
            if (fireworksChargePath.toFile().exists())
                background(itemsFolderPath, fireworksChargePath, itemsFolderPath.resolve("firework_star.png"), 4,
                        "fireworks_charge.tga", true);

            Path leatherHelmet = itemsFolderPath.resolve("leather_helmet.png");
            if (leatherHelmet.toFile().exists())
                background(itemsFolderPath, leatherHelmet, itemsFolderPath.resolve("leather_helmet_overlay.png"), 3,
                        "leather_helmet.tga", true);

            Path leatherChestplate = itemsFolderPath.resolve("leather_chestplate_overlay.png");
            if (leatherChestplate.toFile().exists())
                leatherChestplate.toFile().delete();

            Path leatherLeggings = itemsFolderPath.resolve("leather_leggings.png");
            if (leatherLeggings.toFile().exists())
                background(itemsFolderPath, leatherLeggings, itemsFolderPath.resolve("leather_leggings_overlay.png"), 3,
                        "leather_leggings.tga", true);

            Path leatherBoots = itemsFolderPath.resolve("leather_boots.png");
            if (leatherBoots.toFile().exists())
                background(itemsFolderPath, leatherBoots, itemsFolderPath.resolve("leather_boots_overlay.png"), 3,
                        "leather_boots.tga", true);
        }
    }

    private void background(Path src, Path front, Path background, int alpha, String outName,
            boolean shouldDeleteBackgroundImage)
            throws IOException {
        if (!src.toFile().exists())
            return;
        int defaultWIn = 16, defaultHIn = 16;
        ImageConverter frontImage = new ImageConverter(defaultWIn, defaultHIn, front);
        if (background.toFile().exists())
            frontImage.backgroundImage(background, alpha);
        else {
            BufferedImage background_internal = Util.readImageResource("/background/" + background.getFileName());
            frontImage.backgroundImage(background_internal, alpha);
        }
        frontImage.store(front, "tga");
        FileUtil.renameFile(front, outName);
        if (shouldDeleteBackgroundImage)
            background.toFile().delete();
    }

    private void colorize(Path path, Path outPath, Color color) throws IOException {
        if (!path.toFile().exists())
            return;
        if (!outPath.getParent().toFile().exists())
            outPath.toFile().mkdirs();
        int defaultWIn = 16, defaultHIn = 16;
        Files.copy(path, outPath);
        ImageConverter converter = new ImageConverter(defaultWIn, defaultHIn, outPath);
        converter.colorizeClipped(color);
        converter.store();
    }
}
