package com.rookitebyte.context.generate;

import com.rookitebyte.Project;
import com.rookitebyte.github.GithubClient;
import com.rookitebyte.github.Repository;
import com.rookitebyte.github.exception.GithubClientException;
import com.rookitebyte.template.ThymeleafConfiguration;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class GeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorService.class);

    private final GithubClient githubClient;
    private final Parser parser;
    private final HtmlRenderer renderer;
    private final TemplateEngine templateEngine;

    GeneratorService() {
        githubClient = GithubClient.create();
        parser = Parser.builder().extensions(List.of(YamlFrontMatterExtension.create())).build();
        renderer = HtmlRenderer.builder().build();
        templateEngine = ThymeleafConfiguration.templateEngine();
    }

    void generate(GenerateCommand command) throws IOException {
        var repositories = githubClient.fetchUsersRepositories(command.name());
        var projects = repositories.stream()
                .map(repo -> buildProject(repo, command.name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        var context = new Context();
        context.setVariable("projects", projects);
        var output = command.output().resolve(command.index().getFileName());
        try (var writer = new FileWriter(output.toFile())) {
            templateEngine.process(command.index().toFile().getName(), context, writer);
        }
        copyAllRemainingFiles(command);
    }

    private void copyAllRemainingFiles(GenerateCommand command) throws IOException {
        var parent = command.index().getParent();
        try (var children = Files.list(parent)) {
            var iterator = children.filter(p -> isFileToCopy(p, command)).iterator();
            while (iterator.hasNext()) {
                copyTo(iterator.next(), command.output());
            }
        }
    }

    private void copyTo(Path path, Path output) throws IOException {
        var target = output.resolve(path.getFileName());
        Files.copy(path, target);
    }

    private boolean isFileToCopy(Path path, GenerateCommand command) {
        return !command.output().equals(path) && !command.index().equals(path);
    }

    private Optional<Project> buildProject(Repository repository, String portfolioId) {
        var readme = fetchReadme(repository);
        if (readme.isEmpty()) {
            return Optional.empty();
        }
        var document = parser.parse(readme.get());
        var projectVisitor = new ProjectVisitor();
        document.accept(projectVisitor);
        if (!projectVisitor.validate(portfolioId)) {
            return Optional.empty();
        }
        var name = renderer.render(projectVisitor.getH1());
        var content = renderer.render(projectVisitor.getFirstNode());
        return Optional.of(new Project(name, content, repository.url()));
    }

    private Optional<String> fetchReadme(Repository repository) {
        try {
            var readme = githubClient.fetchReadme(repository);
            return Optional.of(readme);
        } catch (GithubClientException exception) {
            LOGGER.error("Could not fetch readme file for " + repository.name() + "repository");
            return Optional.empty();
        }
    }
}
