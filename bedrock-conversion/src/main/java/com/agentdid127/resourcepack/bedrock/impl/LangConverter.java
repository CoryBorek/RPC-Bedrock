package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.StringBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Collections;
import java.util.Map;

// REMAP ITEM/BLOCK NAMES & OTHER STUFF
// THIS JUST DOES BASIC
public class LangConverter extends Converter {
    public LangConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path textsPath = pack.getWorkingPath().resolve("texts");
        if (!textsPath.toFile().exists())
            return;
        Files.list(textsPath)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        String fileName = path.getFileName().toString().substring(0,
                                path.getFileName().toString().length() - 5);
                        fileName = remapFileName(fileName);
                        StringBuilder builder = new StringBuilder();
                        JsonObject object = JsonUtil.readJson(packConverter.getGson(), path);
                        for (Map.Entry<String, JsonElement> entry : object.entrySet())
                            builder.append(entry.getKey() + "=" + entry.getValue().getAsString() + "\n");
                        Files.write(textsPath.resolve(fileName + ".lang"),
                                Collections.singleton(builder.toString()),
                                Charset.forName("UTF-8"));
                        path.toFile().delete();
                    } catch (Exception exception) {
                        Util.propagate(exception);
                    }
                });
    }

    private String remapFileName(String fileName) {
        if (!fileName.contains("_"))
            return fileName;
        if (fileName.length() < 5)
            return fileName;
        String language = fileName.substring(0, 2);
        String region = fileName.substring(3, 5).toUpperCase();
        return language + "_" + region;
    }
}
