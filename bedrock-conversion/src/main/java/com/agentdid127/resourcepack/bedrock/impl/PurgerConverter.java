package com.agentdid127.resourcepack.bedrock.impl;

import java.io.IOException;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.nio.file.Files;
import java.util.Comparator;

public class PurgerConverter extends Converter {
    public PurgerConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Files.walk(pack.getWorkingPath()).sorted(Comparator.reverseOrder()).map(path -> path.toFile()).forEach(file -> {
            if (!file.isDirectory())
                return;
            int file_count = file.listFiles().length;
            if (file_count == 0)
                file.delete();
        });
    }
}
