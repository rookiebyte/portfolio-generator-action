package com.rookitebyte.context.generate;

import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;

import java.util.List;

class ProjectVisitor extends YamlFrontMatterVisitor {

    private Heading h1;
    private Node firstNode;
    private List<String> tags;
    private String type;
    private String portfolioId;

    @Override
    public void visit(Heading heading) {
        if (h1 == null) {
            h1 = heading;
        }
    }

    @Override
    public void visit(Paragraph paragraph) {
        if (firstNode == null) {
            firstNode = paragraph;
        }
    }

    public Heading getH1() {
        return h1;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public List<String> getTags() {
        if (tags == null) {
            var tagsList = getData().get("tags");
            tags = tagsList == null ? List.of() : tags;
        }
        return tags;
    }

    public String getType() {
        if (type == null) {
            type = firstOrNull(getData().get("name"));
        }
        return type;
    }

    private String getPortfolioId() {
        if (portfolioId == null) {
            portfolioId = firstOrNull(getData().get("portfolio_id"));
        }
        return portfolioId;
    }

    private String firstOrNull(List<String> list) {
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    public boolean validate(String portfolioId) {
        return h1 != null
                && firstNode != null
                && portfolioId.equals(getPortfolioId());
    }
}
