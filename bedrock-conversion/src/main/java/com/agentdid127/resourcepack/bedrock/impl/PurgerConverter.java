package com.agentdid127.resourcepack.bedrock.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;

public class PurgerConverter extends Converter {
    public PurgerConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        File[] files = pack.getWorkingPath().toFile().listFiles();
        for (File file : files) {
            Logger.log(file.getPath());
        }
    }
}
