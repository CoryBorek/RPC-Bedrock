package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.agentdid127.resourcepack.bedrock.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

public class ChestConverter extends Converter {
    public ChestConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path chestFolderPath = pack.getWorkingPath().resolve("textures/entity/chest".replace("/", File.separator));
        if (!chestFolderPath.toFile().exists())
            return;

        // Double Chest
        convertDoubleChest(chestFolderPath, "normal");
        convertDoubleChest(chestFolderPath, "trapped");

        moveIfExists(chestFolderPath.resolve("normal_double.png"), chestFolderPath.resolve("double_normal.png"));

        // Normal Chest
        convertNormalChest(chestFolderPath, "normal");
        convertNormalChest(chestFolderPath, "trapped");
        convertNormalChest(chestFolderPath, "ender");

        // Christmas Chest
        Files.deleteIfExists(chestFolderPath.resolve("christmas.png"));
        Files.deleteIfExists(chestFolderPath.resolve("christmas_left.png"));
        Files.deleteIfExists(chestFolderPath.resolve("christmas_right.png"));
    }

    private void convertNormalChest(Path imagePath, String name) throws IOException {
        if (!imagePath.resolve(name + ".png").toFile().exists())
            return;

        int defaultW = 64, defaultH = 64;
        ImageConverter normal = new ImageConverter(defaultW, defaultH, imagePath.resolve(name + ".png"));
        if (!normal.fileIsPowerOfTwo())
            return;

        // Create a new Image
        normal.newImage(defaultW, defaultH);

        // Body
        normal.subImage(28, 0, 42, 14, 14, 0, true);
        normal.subImage(14, 0, 28, 14, 28, 0, true);

        normal.subImage(14, 19, 28, 33, 28, 19, true);
        normal.subImage(28, 19, 42, 33, 14, 19, true);

        normal.subImage(28, 14, 42, 19, 28, 14, 1);
        normal.subImage(42, 14, 56, 19, 14, 14, 1);
        normal.subImage(14, 14, 28, 19, 42, 14, 1);

        normal.subImage(0, 14, 14, 19, 0, 14, 1);
        normal.subImage(0, 33, 14, 43, 0, 33, 1);
        normal.subImage(42, 33, 56, 43, 14, 33, 1);
        normal.subImage(28, 33, 42, 43, 28, 33, 1);
        normal.subImage(14, 33, 28, 43, 42, 33, 1);

        // Latch
        normal.subImage(0, 1, 1, 5, 0, 1, true);
        normal.subImage(1, 0, 3, 1, 3, 0);
        normal.subImage(3, 0, 5, 1, 1, 0);
        normal.subImage(4, 1, 6, 5, 1, 1, 1);
        normal.subImage(1, 1, 4, 4, 3, 1, 1);

        // Store new Image
        normal.store();
    }

    private void convertDoubleChest(Path imagePath, String name) throws IOException {
        int defaultW = 128, defaultH = 64;

        if (imagePath.resolve(name + "_left.png").toFile().exists()
                && imagePath.resolve(name + "_right.png").toFile().exists()) {
            ImageConverter setup = new ImageConverter(64, 64, imagePath.resolve(name + "_left.png"));
            setup.newImage(defaultW, defaultH);
            setup.subImage(0, 0, 64, 64, 0, 0);
            setup.addImage(imagePath.resolve(name + "_right.png"), 64, 0);
            setup.store(imagePath.resolve(name + "_double.png"), "png");
        }

        if (imagePath.resolve(name + "_double.png").toFile().exists()) {
            ImageConverter normal = new ImageConverter(defaultW, defaultH, imagePath.resolve(name + "_double.png"));
            if (!normal.fileIsPowerOfTwo())
                return;

            // Left Side
            // Body
            normal.newImage(defaultW, defaultH);

            normal.subImage(14, 19, 29, 33, 59, 19, true);
            normal.subImage(14, 0, 29, 14, 59, 0, true);
            normal.subImage(29, 19, 44, 33, 29, 19, true);
            normal.subImage(43, 14, 58, 19, 29, 14, 1);
            normal.subImage(29, 0, 44, 14, 29, 0, true);
            normal.subImage(29, 33, 43, 43, 44, 33, 1);
            normal.subImage(43, 33, 58, 43, 29, 33, 1);
            normal.subImage(14, 33, 29, 43, 58, 33, 1);
            normal.subImage(29, 14, 43, 19, 44, 14, 1);
            normal.subImage(14, 14, 29, 19, 58, 14, 1);

            // Latch
            normal.subImage(1, 0, 2, 1, 4, 0);
            normal.subImage(2, 0, 3, 1, 2, 0);
            normal.subImage(1, 1, 4, 5, 2, 1, 1);

            // Right Side
            // Body
            normal.subImage(78, 0, 93, 14, 44, 0, true);
            normal.subImage(93, 0, 108, 14, 14, 0, true);
            normal.subImage(43, 19, 58, 33, 14, 19, true);
            normal.subImage(64, 14, 78, 19, 0, 14, 1);
            normal.subImage(78, 14, 93, 19, 73, 14, 1);
            normal.subImage(107, 14, 122, 19, 14, 14, 1);
            normal.subImage(78, 19, 93, 33, 44, 19, true);
            normal.subImage(107, 33, 122, 43, 14, 33, 1);
            normal.subImage(64, 33, 78, 43, 0, 33, 1);
            normal.subImage(78, 33, 93, 43, 73, 33, 1);

            // Latch
            normal.subImage(65, 0, 66, 1, 3, 0);
            normal.subImage(67, 0, 68, 1, 1, 0);
            normal.subImage(64, 1, 65, 5, 0, 1, true);
            normal.subImage(65, 1, 66, 5, 5, 1, true);
            normal.subImage(68, 1, 69, 5, 1, 1, true);
            normal.store();

            imagePath.resolve(name + "_left.png").toFile().delete();
            imagePath.resolve(name + "_right.png").toFile().delete();
        }
    }

    private void moveIfExists(Path in, Path out) throws IOException {
        if (!in.toFile().exists())
            return;
        if (!out.getParent().toFile().exists())
            out.toFile().mkdirs();
        Files.move(in, out);
    }
}