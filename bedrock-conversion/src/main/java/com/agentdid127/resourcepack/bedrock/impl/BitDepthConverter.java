package com.agentdid127.resourcepack.bedrock.impl;

import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.awt.image.BufferedImage;

public class BitDepthConverter extends Converter {
    public BitDepthConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("textures");
        if (!texturesPath.toFile().exists())
            return;

        Path blocksFolderPath = texturesPath.resolve("blocks");
        if (blocksFolderPath.toFile().exists())
            bitdepthify(blocksFolderPath,
                    new String[] { "bedrock", "grass_top", "observer_top", "smooth_stone", "stone",
                            "stone_slab_top" });

        Path uiPath = texturesPath.resolve("ui");
        if (uiPath.toFile().exists())
            bitdepthify(uiPath, new String[] { "hotbar_0", "hotbar_1", "hotbar_2", "hotbar_3", "hotbar_4", "hotbar_5",
                    "hotbar_6", "hotbar_7", "hotbar_8", "selected_hotbar_slot",
                    "experiencebarempty", "experiencebarfull" });
    }

    // POSSIBLE_TODO/: Support other than PNG or have other function
    // for different type
    private void bitdepthify(Path sourcePath, String[] paths) throws IOException {
        for (String pathName : paths) {
            Path path = sourcePath.resolve(pathName + ".png");
            if (!path.toFile().exists())
                continue;
            BufferedImage image = ImageIO.read(path.toFile());
            int w = image.getWidth();
            int h = image.getHeight();
            ImageConverter converter = new ImageConverter(w, h, path);
            converter.newImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
            converter.subImageSized(0, 0, w, h);
            converter.store();
        }
    }
}
