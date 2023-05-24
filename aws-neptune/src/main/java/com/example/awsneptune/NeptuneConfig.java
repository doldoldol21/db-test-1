package com.example.awsneptune;

import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

@Configuration
public class NeptuneConfig {

    private final NeptuneProperty neptuneProperty;

    public NeptuneConfig(NeptuneProperty neptuneProperty) {
        this.neptuneProperty = neptuneProperty;
    }

    @Bean
    public Cluster cluster() {
        return Cluster.build()
                .addContactPoint(neptuneProperty.getEndpoint())
                .port(neptuneProperty.getPort())
                .enableSsl(true)
                .maxConnectionPoolSize(5)
                .maxInProcessPerConnection(5)
                .maxSimultaneousUsagePerConnection(2)
                .minSimultaneousUsagePerConnection(1)
                .create();
    }

    @Bean
    public GraphTraversalSource g(Cluster cluster) {
        return AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster));
    }
}
