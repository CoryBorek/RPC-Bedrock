package com.agentdid127.resourcepack.bedrock;

import com.agentdid127.resourcepack.bedrock.impl.ArmorConverter;
import com.agentdid127.resourcepack.bedrock.impl.BitDepthConverter;
import com.agentdid127.resourcepack.bedrock.impl.EntityConverter;
import com.agentdid127.resourcepack.bedrock.impl.LangConverter;
import com.agentdid127.resourcepack.bedrock.impl.CarriedConverter;
import com.agentdid127.resourcepack.bedrock.impl.MoveFilesConverter;
import com.agentdid127.resourcepack.bedrock.impl.PackMetaConverter;
import com.agentdid127.resourcepack.bedrock.impl.NameConverter;
import com.agentdid127.resourcepack.bedrock.impl.PackPngConverter;
import com.agentdid127.resourcepack.bedrock.impl.PurgerConverter;
import com.agentdid127.resourcepack.bedrock.impl.SplashesConverter;
import com.agentdid127.resourcepack.bedrock.pack.BPack;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BedrockPackConverter extends PackConverter {
	private Path INPUT_DIR;

	public BedrockPackConverter(Path input, boolean minify, boolean debug, PrintStream out) {
		GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setLenient();
		if (!minify)
			gsonBuilder.setPrettyPrinting();
		gson = gsonBuilder.create();
		DEBUG = debug;
		Logger.setStream(out);
		Logger.log("Converting from Java -> Bedrock");
		this.INPUT_DIR = input;
		converterRunner();
	}

	protected void converterRunner() {
		this.registerConverter(new com.agentdid127.resourcepack.backwards.impl.textures.ChestConverter(this));
		this.registerConverter(new PackMetaConverter(this));
		this.registerConverter(new PackPngConverter(this));
		this.registerConverter(new MoveFilesConverter(this));
		this.registerConverter(new BitDepthConverter(this));
		this.registerConverter(new NameConverter(this));
		this.registerConverter(new LangConverter(this));
		this.registerConverter(new SplashesConverter(this));
		this.registerConverter(new CarriedConverter(this));
		this.registerConverter(new ArmorConverter(this));
		this.registerConverter(new EntityConverter(this));
		this.registerConverter(new PurgerConverter(this));
	}

	public void runPack(Pack pack) {
		try {
			Logger.log("Converting " + pack);
			pack.getHandler().setup();
			Logger.log("  Running Converters");
			for (Converter converter : converters.values()) {
				if (DEBUG)
					Logger.log("    Running " + converter.getClass().getSimpleName());
				converter.convert(pack);
			}
			pack.getHandler().finish();
		} catch (Throwable t) {
			Logger.log("Failed to convert!");
			Util.propagate(t);
		}
	}

	public void runDir() throws IOException {
		Files.list(INPUT_DIR)
				.map(BPack::parse)
				.filter(Objects::nonNull)
				.forEach(pack -> runPack(pack));
	}
}
