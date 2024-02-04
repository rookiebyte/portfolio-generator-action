package com.rookitebyte.context.generate;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "generate", version = "1.0.0-SNAPSHOT")
public class GeneratorEntrypoint implements Callable<Integer> {

    @Option(names = {"-f", "--file"}, required = true, description = "provide index.html")
    private Path index;

    @Option(names = {"-o", "--output"}, required = true, description = "output directory")
    private Path output;

    @Option(names = {"-n", "--name"}, required = true, description = "Github account name")
    private String name;

    private final GeneratorService generatorService = new GeneratorService();

    @Override
    public Integer call() throws Exception {
        Files.createDirectories(output);
        var command = new GenerateCommand(index.toAbsolutePath(), output.toAbsolutePath(), name);
        generatorService.generate(command);
        return 0;
    }
}
