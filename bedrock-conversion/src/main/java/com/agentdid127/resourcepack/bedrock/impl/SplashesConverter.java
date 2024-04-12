package com.agentdid127.resourcepack.bedrock.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SplashesConverter extends Converter {
    public SplashesConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path splashesPath = pack.getWorkingPath().resolve("splashes.txt");
        if (!splashesPath.toFile().exists())
            return;
        List<String> contents = Files.readAllLines(splashesPath);
        JsonObject object = new JsonObject();
        JsonArray splashes = new JsonArray();
        for (String line : contents)
            splashes.add(new JsonPrimitive(line));
        object.add("splashes", splashes);
        JsonUtil.writeJson(packConverter.getGson(), pack.getWorkingPath().resolve("splashes.json"), object);
        splashesPath.toFile().delete();
    }
}
