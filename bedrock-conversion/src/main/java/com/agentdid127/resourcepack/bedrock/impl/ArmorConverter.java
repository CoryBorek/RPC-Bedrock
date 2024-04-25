package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class ArmorConverter extends Converter {
    public ArmorConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("textures");
        if (!texturesPath.toFile().exists())
            return;

        Path armorPath = texturesPath.resolve("models/armor".replace("/", File.separator));
        if (!armorPath.toFile().exists())
            return;

        FileUtil.moveIfExists(armorPath.resolve("chainmail_layer1.png"), armorPath.resolve("chain_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("chainmail_layer2.png"), armorPath.resolve("chain_2.png"));

        FileUtil.moveIfExists(armorPath.resolve("diamond_layer_1.png"), armorPath.resolve("diamond_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("diamond_layer_2.png"), armorPath.resolve("diamond_2.png"));

        FileUtil.moveIfExists(armorPath.resolve("gold_layer_1.png"), armorPath.resolve("gold_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("gold_layer_2.png"), armorPath.resolve("gold_2.png"));

        FileUtil.moveIfExists(armorPath.resolve("iron_layer_1.png"), armorPath.resolve("iron_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("iron_layer_2.png"), armorPath.resolve("iron_2.png"));

        // TODO: Fix Leather
        FileUtil.moveIfExists(armorPath.resolve("leather_layer_1.png"), armorPath.resolve("cloth_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("leather_layer_2.png"), armorPath.resolve("cloth_2.png"));

        Path leatherOnePath = armorPath.resolve("leather_1.png");
        Path leatherTwoPath = armorPath.resolve("leather_2.png");
        FileUtil.moveIfExists(armorPath.resolve("leather_layer_1_overlay.png"), leatherOnePath);
        FileUtil.moveIfExists(armorPath.resolve("leather_layer_2_overlay.png"), leatherTwoPath);
        toTgaIfExists(leatherOnePath);
        toTgaIfExists(leatherTwoPath);

        FileUtil.moveIfExists(armorPath.resolve("netherite_layer_1.png"), armorPath.resolve("netherite_1.png"));
        FileUtil.moveIfExists(armorPath.resolve("netherite_layer_2.png"), armorPath.resolve("netherite_2.png"));

        FileUtil.moveIfExists(armorPath.resolve("turtle_layer_1.png"), armorPath.resolve("turtle_1.png"));
    }

    private void toTgaIfExists(Path imagePath) throws IOException {
        if (!imagePath.toFile().exists())
            return;
        String fileName = imagePath.getFileName().toString();
        int defaultWIn = 64, defaultHIn = 32;
        ImageConverter converter = new ImageConverter(defaultWIn, defaultHIn, imagePath);
        converter.store(imagePath, "tga");
        FileUtil.moveIfExists(imagePath,
                imagePath.resolveSibling(fileName.substring(0, fileName.length() - 4) + ".tga"));
    }
}
