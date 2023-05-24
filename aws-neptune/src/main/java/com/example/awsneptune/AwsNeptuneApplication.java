package com.example.awsneptune;

import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;


@SpringBootApplication
@RequiredArgsConstructor
public class AwsNeptuneApplication implements CommandLineRunner {

    private final GraphTraversalSource g;

    public static void main(String[] args) {
        SpringApplication.run(AwsNeptuneApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        init();
        Person person = new Person(g.V().has("person", "name", "John"));
        System.out.println(person.getName().next());
        g.tx().commit();

        System.exit(0);

    }

    private void init(){
        // 데이터 추가
        g.addV("person").property("name", "John").property("age", 30).next();
        g.addV("person").property("name", "Alice").property("age", 25).next();
        g.V().has("name", "John").as("john").V().has("name", "Alice").addE("knows").from("john").next();
    }
}
