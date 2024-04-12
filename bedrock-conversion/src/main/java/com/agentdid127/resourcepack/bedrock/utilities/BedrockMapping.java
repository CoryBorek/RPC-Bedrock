package com.agentdid127.resourcepack.bedrock.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// TODO: BedrockMapping shouldn't exist, make RPC Mapping Class more open &
// configurable
public class BedrockMapping {
    protected final Map<String, String> mapping = new HashMap<>();

    public BedrockMapping(Gson gson, String path, String key) {
        load(gson, path, key);
    }

    protected void load(Gson gson, String path, String key) {
        JsonObject object = JsonUtil.readJsonResource(gson, path.replace("/", File.separator))
                .getAsJsonObject(key);
        if (object == null)
            return;
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
            this.mapping.put(entry.getKey(), entry.getValue().getAsString());
    }

    /**
     * @return remapped or in if not present
     */
    public String remap(String in) {
        return mapping.getOrDefault(in, in);
    }
}
