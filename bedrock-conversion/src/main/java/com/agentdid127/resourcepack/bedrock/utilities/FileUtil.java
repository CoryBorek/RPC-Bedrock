package com.agentdid127.resourcepack.bedrock.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static void moveIfExists(Path in, Path out) throws IOException {
        if (!in.toFile().exists())
            return;
        if (!out.getParent().toFile().exists())
            out.toFile().mkdirs();
        Files.move(in, out);
    }

    public static void copyIfExists(Path in, Path out) throws IOException {
        if (!in.toFile().exists())
            return;
        if (!out.getParent().toFile().exists())
            out.toFile().mkdirs();
        Files.copy(in, out);
    }
}
