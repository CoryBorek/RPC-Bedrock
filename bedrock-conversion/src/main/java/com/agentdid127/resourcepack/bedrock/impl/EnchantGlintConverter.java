package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class EnchantGlintConverter extends Converter {
    public EnchantGlintConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path miscPath = pack.getWorkingPath().resolve("textures/misc".replace("/", File.separator));
        if (!miscPath.toFile().exists())
            return;
        Path glintPath = miscPath.resolve("enchanted_item_glint.png");
        if (!glintPath.toFile().exists())
            return;
        ImageConverter converter = new ImageConverter(null, null, glintPath);
        converter.newImage(converter.getWidth(), converter.getHeight());
        converter.grayscale();
        converter.store();
    }
}
