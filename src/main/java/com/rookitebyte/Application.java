package com.rookitebyte;

import com.rookitebyte.context.generate.GeneratorEntrypoint;
import picocli.CommandLine;

@SuppressWarnings("all")
public class Application {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GeneratorEntrypoint())
                .execute(args);
        System.exit(exitCode);
    }
}
