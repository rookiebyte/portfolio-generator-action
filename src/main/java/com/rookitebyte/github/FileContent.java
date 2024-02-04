package com.rookitebyte.github;

import java.nio.charset.Charset;
import java.util.Base64;

public record FileContent(

        String content
) {

    public String decodeContent() {
        var b64 = Base64.getDecoder();
        if (content == null) {
            return null;
        }
        StringBuilder output = new StringBuilder();
        var lines = content.split("\n");
        for (var line : lines) {
            output.append(new String(b64.decode(line), Charset.defaultCharset()));
        }
        return output.toString();
    }
}
