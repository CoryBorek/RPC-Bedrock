package com.agentdid127.resourcepack.bedrock.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FlipBookAtlasConverter extends Converter {
    public FlipBookAtlasConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("textures");
        if (!texturesPath.toFile().exists())
            return;

        JsonArray flipbook = new JsonArray();

        Path blockPath = texturesPath.resolve("block");
        if (blockPath.toFile().exists()) {
            Files.list(blockPath)
                    .filter((path) -> path.toString().endsWith(".mcmeta"))
                    .forEach((path) -> {
                        try {
                            JsonObject metadata = JsonUtil.readJson(packConverter.getGson(), path);
                            path.toFile().delete();
                            if (metadata == null)
                                return;
                            // TODO
                        } catch (Exception exception) {
                            Logger.log("Failed to generate flipbook atlas for " + path + ", skipping...");
                        }
                    });
        }

        Path itemPath = texturesPath.resolve("item");
        if (itemPath.toFile().exists()) {
            Files.list(itemPath)
                    .filter((path) -> path.toString().endsWith(".mcmeta"))
                    .forEach((path) -> {
                        try {
                            JsonObject metadata = JsonUtil.readJson(packConverter.getGson(), path);
                            path.toFile().delete();
                            if (metadata == null)
                                return;
                            // TODO
                        } catch (Exception exception) {
                            Logger.log("Failed to generate flipbook atlas for " + path + ", skipping...");
                        }
                    });
        }

        // TODO: Other

        if (itemPath.toFile().exists() && blockPath.toFile().exists())
            JsonUtil.writeJson(packConverter.getGson(), texturesPath.resolve("flipbook_textures.json"), flipbook);
    }
}
