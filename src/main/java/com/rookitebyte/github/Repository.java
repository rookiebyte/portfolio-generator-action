package com.rookitebyte.github;

public record Repository(

        String name,
        String url
) {

    String buildLinkToReadme() {
        return url + "/contents/README.md";
    }
}
