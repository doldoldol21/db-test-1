package com.example.awsneptune;


import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.Iterator;
import java.util.Objects;

public class Person {
    private GraphTraversal<Vertex, Vertex> traversal;

    private final String AGE_KEY = "age";
    private final String NAME_KEY = "name";

    public Person(GraphTraversal<Vertex, Vertex> traversal) {
        this.traversal = traversal;
    }

    public GraphTraversal<Vertex, Vertex> getName() {
        return traversal.properties(this.NAME_KEY).value();
    }

    public void setName(String name) {
        traversal.property(this.NAME_KEY, name);
    }

    public GraphTraversal<Vertex, Vertex> getAge() {
        return traversal.properties(this.AGE_KEY).value();
    }

    public void setAge(int age) {
        traversal.property(this.AGE_KEY, age);
    }
}