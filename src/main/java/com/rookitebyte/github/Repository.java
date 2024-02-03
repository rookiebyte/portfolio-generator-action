package com.rookitebyte.github;

public record Repository(

        String name,
        String defaultBranch
) {

    String buildLinkToReadme(String owner) {
        return "https://raw.githubusercontent.com/" + owner
                + "/" + name
                + "/" + defaultBranch
                + "/README.md";
    }
}
