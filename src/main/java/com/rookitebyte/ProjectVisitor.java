package com.rookitebyte;

import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;

public class ProjectVisitor extends YamlFrontMatterVisitor {

    private Heading h1;
    private Node firstNode;

    @Override
    public void visit(Heading heading) {
        if (h1 == null) {
            h1 = heading;
        }
    }

    @Override
    public void visit(Text text) {
        if (firstNode == null) {
            firstNode = text;
        }
    }

    public Heading getH1() {
        return h1;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    @Override
    public String toString() {
        return "ProjectVisitor{" +
                "h1=" + h1 +
                ", firstNode=" + firstNode +
                ", data=" + getData() +
                '}';
    }
}
