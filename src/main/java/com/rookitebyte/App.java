package com.rookitebyte;

import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.IWebApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        Parser parser = Parser.builder()
                .extensions(List.of(YamlFrontMatterExtension.create()))
                .build();
        var yamlVisitor = new ProjectVisitor();
        Reader reader = new InputStreamReader(Files.getAsStream("README.md"));
        Node document = parser.parseReader(reader);
        document.accept(yamlVisitor);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        System.out.println(yamlVisitor);
        System.out.println(renderer.render(yamlVisitor.getH1()));
        buildTemplateEngine();
    }

    private static ITemplateEngine buildTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");

        var context = new Context();
        context.setVariable("name", "Bunny");
        context.setVariable("date", LocalDate.now().toString());

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        var result = templateEngine.process("index", context);
        System.out.println(result);
        return templateEngine;
    }
}
