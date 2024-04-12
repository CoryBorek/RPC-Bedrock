package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.bedrock.utilities.BedrockMapping;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NameConverter extends Converter {
	protected BedrockMapping blockMapping;
	protected BedrockMapping blockTGAMapping;
	protected BedrockMapping itemMapping;

	public NameConverter(PackConverter packConverter) {
		super(packConverter);
		Gson gson = packConverter.getGson();
		// TODO: BedrockMapping shouldn't exist, make RPC Mapping Class more open &
		// configurable
		String namesPath = "/names.json";
		blockMapping = new BedrockMapping(gson, namesPath, "blocks");
		blockTGAMapping = new BedrockMapping(gson, namesPath, "tga");
		itemMapping = new BedrockMapping(gson, namesPath, "items");
	}

	@Override
	public void convert(Pack pack) throws IOException {
		Path texturesPath = pack.getWorkingPath().resolve("textures");
		if (!texturesPath.toFile().exists())
			return;

		Logger.log("      Renaming Textures");

		Path blocksFolderPath = texturesPath.resolve("blocks");
		if (blocksFolderPath.toFile().exists()) {
			renameAll(blockMapping, ".png", blocksFolderPath);
			renameAll(blockTGAMapping, ".png", ".tga", blocksFolderPath);
			// renameAll(blockMapping, ".png.mcmeta", blocksFolderPath);
			// TODO ^: Map mcmeta files for flipbook_textures.json

			move_candles(blocksFolderPath, true);
			move_deepslate(blocksFolderPath);
			move_huge_fungus(blocksFolderPath);
		}

		Path itemsFolderPath = texturesPath.resolve("items");
		if (itemsFolderPath.toFile().exists()) {
			renameAll(itemMapping, ".png", itemsFolderPath);
			// renameAll(itemMapping, ".png.mcmeta", itemsFolderPath);
			// TODO ^: Map mcmeta files for flipbook_textures.json
			move_candles(itemsFolderPath, false);
		}
	}

	private void move_candles(Path sourcePath, boolean has_lit) throws IOException {
		Path candlesPath = sourcePath.resolve("candles");
		String[] candlePaths = new String[] {
				"black_candle",
				"blue_candle",
				"brown_candle",
				"candle",
				"cyan_candle",
				"gray_candle",
				"green_candle",
				"light_blue_candle",
				"light_gray_candle",
				"lime_candle",
				"magenta_candle",
				"orange_candle",
				"pink_candle",
				"purple_candle",
				"red_candle",
				"white_candle",
				"yellow_candle"
		};
		for (String name : candlePaths) {
			String candleName = name + ".png";
			Path candlePath = sourcePath.resolve(candleName);
			if (candlePath.toFile().exists() && !candlesPath.toFile().exists())
				candlesPath.toFile().mkdirs();
			FileUtil.moveIfExists(candlePath, candlesPath.resolve(candleName));
			if (has_lit) {
				String candleLitName = candleName + "_lit.png";
				Path candleLitPath = sourcePath.resolve(candleLitName);
				FileUtil.moveIfExists(candleLitPath, candlesPath.resolve(candleLitName));
			}
		}
	}

	private void move_deepslate(Path sourcePath) throws IOException {
		Path deepslateFolderPath = sourcePath.resolve("deepslate");
		String[] deepslatePaths = new String[] {
				"chiseled_deepslate",
				"cobbled_deepslate",
				"cracked_deepslate_bricks",
				"cracked_deepslate_tiles",
				"deepslate",
				"deepslate_bricks",
				"deepslate_coal_ore",
				"deepslate_copper_ore",
				"deepslate_diamond_ore",
				"deepslate_emerald_ore",
				"deepslate_gold_ore",
				"deepslate_iron_ore",
				"deepslate_lapis_ore",
				"deepslate_redstone_ore",
				"deepslate_tiles",
				"deepslate_top",
				"polished_deepslate"
		};
		for (String name : deepslatePaths) {
			String deepslateName = name + ".png";
			Path deepslatePath = sourcePath.resolve(deepslateName);
			if (deepslatePath.toFile().exists() && !deepslateFolderPath.toFile().exists())
				deepslateFolderPath.toFile().mkdirs();
			FileUtil.moveIfExists(deepslatePath, deepslateFolderPath.resolve(deepslateName));
		}
	}

	private void move_huge_fungus(Path sourcePath) throws IOException {
		Path hugeFungusFolderPath = sourcePath.resolve("huge_fungus");
		String[] hugeFungusPaths = new String[] {
				"crimson_door_lower",
				"crimson_door_top",
				"crimson_log_side",
				"crimson_log_top",
				"crimson_planks",
				"crimson_trapdoor",
				"stripped_crimson_stem_side",
				"stripped_crimson_stem_top",
				"stripped_warped_stem_side",
				"stripped_warped_stem_top",
				"warped_door_lower",
				"warped_door_top",
				"warped_planks",
				"warped_stem_side",
				"warped_stem_top",
				"warped_trapdoor"
		};
		for (String name : hugeFungusPaths) {
			String hugeFungusName = name + ".png";
			Path hugeFungusPath = sourcePath.resolve(hugeFungusName);
			if (hugeFungusPath.toFile().exists() && !hugeFungusFolderPath.toFile().exists())
				hugeFungusFolderPath.toFile().mkdirs();
			FileUtil.moveIfExists(hugeFungusPath, hugeFungusFolderPath.resolve(hugeFungusName));
		}
	}

	protected void renameAll(BedrockMapping mapping, String extension, String newExtension, Path path)
			throws IOException {
		Files.list(path).forEach(path1 -> {
			if (!path1.toString().endsWith(extension))
				return;

			String baseName = path1.getFileName().toString().substring(0,
					path1.getFileName().toString().length() - extension.length());

			String newName = mapping.remap(baseName);
			if (newName != null && !newName.equals(baseName)) {
				try {
					System.out.println(baseName + "->" + newName + ", " + extension + "->" + newExtension);
					if (newExtension.equals(".tga")) {
						ImageConverter i = new ImageConverter(16, 16, path1);
						i.store(path1, "tga");

						Boolean ret = FileUtil.renameFile(path1, newName + newExtension);
						if (ret == null)
							return;
						if (ret && PackConverter.DEBUG) {
							Logger.log(
									"      Renamed: " + path1.getFileName().toString() + "->" + newName + newExtension);
						} else if (!ret) {
							Logger.log("      Failed to Rename: " + path1.getFileName().toString() + "->"
									+ newName + newExtension);
						}
					} else {
						Boolean ret = FileUtil.renameFile(path1, newName + newExtension);
						if (ret == null)
							return;
						if (ret && PackConverter.DEBUG) {
							Logger.log(
									"      Renamed: " + path1.getFileName().toString() + "->" + newName + newExtension);
						} else if (!ret) {
							Logger.log("      Failed to Rename: " + path1.getFileName().toString() + "->"
									+ newName + newExtension);
						}
					}
				} catch (IOException e) {
					Logger.log("      Failed to Rename: " + path1.getFileName().toString() + "->" + newName
							+ newExtension);
					e.printStackTrace();
				}
			}
		});
	}

	protected void renameAll(BedrockMapping mapping, String extension, Path path) throws IOException {
		Files.list(path).forEach(path1 -> {
			if (!path1.toString().endsWith(extension))
				return;

			String baseName = path1.getFileName().toString().substring(0,
					path1.getFileName().toString().length() - extension.length());

			String newName = mapping.remap(baseName);
			if (newName == null || newName.equals(baseName))
				return;

			Boolean ret = FileUtil.renameFile(path1, newName + extension);
			if (ret == null)
				return;
			if (ret && PackConverter.DEBUG) {
				Logger.log("      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
			} else if (!ret) {
				Logger.log(
						"      Failed to Rename: " + path1.getFileName().toString() + "->" + newName + extension);
			}
		});
	}
}