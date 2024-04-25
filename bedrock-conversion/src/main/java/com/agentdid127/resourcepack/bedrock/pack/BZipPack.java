package com.agentdid127.resourcepack.bedrock.pack;

import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.BomDetector;
import com.agentdid127.resourcepack.library.utilities.FileUtil;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.IOException;
import java.nio.file.Path;

public class BZipPack extends Pack {
	public BZipPack(Path path) {
		super(path);
	}

	@Override
	public BZipPack.Handler createHandler() {
		return new BZipPack.Handler(this);
	}

	@Override
	public String getFileName() {
		return path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4);
	}

	public static class Handler extends Pack.Handler {
		public Handler(Pack pack) {
			super(pack);
		}

		public Path getConvertedZipPath() {
			return pack.getWorkingPath().getParent().resolve(pack.getWorkingPath().getFileName() + ".zip");
		}

		/**
		 * Removes Existing Conversions and starts new one
		 * 
		 * @throws IOException
		 */
		@Override
		public void setup() throws IOException {
			if (pack.getWorkingPath().toFile().exists()) {
				Logger.log("  Deleting existing conversion");
				FileUtil.deleteDirectoryAndContents(pack.getWorkingPath());
			}

			Path convertedZipPath = getConvertedZipPath();
			if (convertedZipPath.toFile().exists()) {
				Logger.log("  Deleting existing conversion zip");
				convertedZipPath.toFile().delete();
			}

			pack.getWorkingPath().toFile().mkdir();

			try {
				ZipFile zipFile = new ZipFile(pack.getOriginalPath().toFile());
				zipFile.extractAll(pack.getWorkingPath().toString());
				zipFile.close();
			} catch (ZipException e) {
				Util.propagate(e);
			}

			bomRemover(pack.getWorkingPath());
		}

		/**
		 * Runs after program is finished. Zips directory.
		 * 
		 * @throws IOException
		 */
		@Override
		public void finish() throws IOException {
			try {
				Logger.log("  Zipping working directory");
				ZipFile zipFile = new ZipFile(getConvertedZipPath().toFile());
				ZipParameters parameters = new ZipParameters();
				parameters.setIncludeRootFolder(false);
				zipFile.createSplitZipFileFromFolder(pack.getWorkingPath().toFile(), parameters, false, 65536);
				FileUtil.renameFile(getConvertedZipPath(), pack.getWorkingPath().getFileName() + ".mcpack");
				zipFile.close();
			} catch (ZipException e) {
				Util.propagate(e);
			}

			Logger.log("  Deleting working directory");
			FileUtil.deleteDirectoryAndContents(pack.getWorkingPath());
		}

		static void bomRemover(Path workingPath) throws IOException {
			BomDetector bom = new BomDetector(
					workingPath.toString(),
					".txt", ".json", ".mcmeta", ".properties", ".lang");
			int count = bom.findBOMs().size();
			if (count > 0)
				Logger.log("Removing BOMs from " + count + " files.");
			bom.removeBOMs();
		}

		@Override
		public String toString() {
			return "Handler{} " + super.toString();
		}
	}
}