package com.agentdid127.resourcepack;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BOptions {
        public static final OptionParser PARSER = new OptionParser();

        public static final OptionSpec<Void> HELP = PARSER
                        .acceptsAll(Arrays.asList("?", "h", "help"), "Print this message.").forHelp();

        public static final OptionSpec<Path> INPUT_DIR = PARSER
                        .acceptsAll(Arrays.asList("i", "input", "input-dir"), "Input directory for the packs")
                        .withRequiredArg()
                        .withValuesConvertedBy(new Options.PathConverter()).defaultsTo(Paths.get("./"));

        public static final OptionSpec<Boolean> DEBUG = PARSER.accepts("debug", "Displays other output")
                        .withRequiredArg()
                        .ofType(Boolean.class).defaultsTo(true);

        public static final OptionSpec<Void> MINIFY = PARSER.accepts("minify", "Minify the json files.");
}
