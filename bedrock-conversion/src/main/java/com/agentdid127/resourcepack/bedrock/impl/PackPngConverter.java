package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackPngConverter extends Converter {
	public PackPngConverter(PackConverter packConverter) {
		super(packConverter);
	}

	@Override
	public void convert(Pack pack) throws IOException {
		Path image = pack.getWorkingPath().resolve("pack.png");
		if (!image.toFile().exists())
			return;
		Files.move(image, pack.getWorkingPath().resolve("pack_icon.png"));
	}
}
