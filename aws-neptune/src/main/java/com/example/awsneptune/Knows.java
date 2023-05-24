//package com.example.awsneptune;
//
//import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
//import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
//import org.apache.tinkerpop.gremlin.structure.Edge;
//import org.apache.tinkerpop.gremlin.structure.Vertex;
//
//public class Knows {
//    private Vertex vertex;
//
//    public Knows(Vertex vertex) {
//        this.vertex = vertex;
//    }
//
//    public Person getPrevPerson() {
//        return new Person(__.outE("knows").inV().next());
//    }
//
//    public Person getNextPerson() {
//        try (GraphTraversal<Vertex, Edge> inVertex = __.inE("knows")) {
//            return new Person(inVertex.inV().next());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
