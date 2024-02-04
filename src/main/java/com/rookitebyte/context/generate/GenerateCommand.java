package com.rookitebyte.context.generate;

import com.google.common.base.Preconditions;
import com.rookitebyte.tools.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

record GenerateCommand(

        Path index,
        Path output,
        String name
) {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateCommand.class);

    GenerateCommand {
        if (!Files.isRegularFile(index)) {
            LOGGER.error("Index is not regualar file wtf " + index.toAbsolutePath());
        }
        Preconditions.checkArgument(Strings.isNotBlank(name), "Name is mandatory");
        Preconditions.checkArgument(Files.isRegularFile(index), "Index must be existing file");
        Preconditions.checkArgument(Files.isDirectory(output), "Output must be existing directory");
    }
}
