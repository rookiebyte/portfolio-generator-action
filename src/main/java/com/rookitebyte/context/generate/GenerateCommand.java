package com.rookitebyte.context.generate;

import com.google.common.base.Preconditions;
import com.rookitebyte.tools.Strings;

import java.nio.file.Files;
import java.nio.file.Path;

record GenerateCommand(

        Path index,
        Path output,
        String name
) {

    GenerateCommand {
        Preconditions.checkArgument(Strings.isNotBlank(name), "Name is mandatory");
        Preconditions.checkArgument(Files.isRegularFile(index), "Index must be existing file");
        Preconditions.checkArgument(Files.isDirectory(output), "Output must be existing directory");
    }
}
