package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.bedrock.Util;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;

public class PackMetaConverter extends Converter {
	public PackMetaConverter(PackConverter packConverter) {
		super(packConverter);
	}

	@Override
	public void convert(Pack pack) throws IOException {
		Path packMetaPath = pack.getWorkingPath().resolve("pack.mcmeta");
		if (!packMetaPath.toFile().exists())
			return;

		Path manifestPath = pack.getWorkingPath().resolve("manifest.json");

		JsonObject json = Util.readJson(packConverter.getGson(), packMetaPath);
		JsonObject out = new JsonObject();

		{
			// format_version
			out.addProperty("format_version", 2);

			// header
			JsonObject header = new JsonObject();

			// header properties
			header.addProperty("description", json.get("pack").getAsJsonObject().get("description").getAsString());
			header.addProperty("name", pack.getFileName());
			header.addProperty("uuid", UUID.randomUUID().toString());

			// version
			JsonArray version = new JsonArray();
			version.add(1);
			version.add(0);
			version.add(0);
			header.add("version", version);

			// min_engine_version
			JsonArray minVersion = new JsonArray();
			minVersion.add(1);
			minVersion.add(16);
			minVersion.add(0);
			header.add("min_engine_version", minVersion);

			out.add("header", header);

			// modules
			JsonArray modules = new JsonArray();
			JsonObject module1 = new JsonObject();
			module1.addProperty("description", json.get("pack").getAsJsonObject().get("description").getAsString());
			module1.addProperty("type", "resources");
			module1.addProperty("uuid", UUID.randomUUID().toString());
			module1.add("version", version);
			modules.add(module1);

			out.add("modules", modules);
		}

		Files.write(manifestPath, Collections.singleton(packConverter.getGson().toJson(out)), StandardCharsets.UTF_8);

		packMetaPath.toFile().delete();
	}
}
