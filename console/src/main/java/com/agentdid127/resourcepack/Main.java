package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.bedrock.BedrockPackConverter;

import joptsimple.OptionSet;

import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws IOException {
        OptionSet optionSet = BOptions.PARSER.parse(args);
        if (optionSet.has(Options.HELP)) {
            Options.PARSER.printHelpOn(System.out);
            return;
        }

        boolean minify = optionSet.has(Options.MINIFY);
        PrintStream out = System.out;
        new BedrockPackConverter(optionSet.valueOf(BOptions.INPUT_DIR), minify, true, out).runDir();
    }
}
