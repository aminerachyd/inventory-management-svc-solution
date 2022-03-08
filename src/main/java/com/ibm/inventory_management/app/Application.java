package com.ibm.inventory_management.app;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;

import com.ibm.inventory_management.repository.StockItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
@ComponentScan({ "com.ibm.inventory_management.*", "com.ibm.cloud_garage.*", "com.ibm.health" })
@EnableMongoRepositories(basePackageClasses = StockItemRepository.class)
public class Application extends SpringBootServletInitializer {
    @Autowired
    Environment environment;
    @Value("${spring.application.name}")
    private String applicationName;

    public static void main(String[] args) {
        SpringApplication.run(com.ibm.inventory_management.app.Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String port = environment.getProperty("local.server.port");

            System.out.println();
            System.out.println("Server started - http://localhost:" + port + "/swagger-ui.html");
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public io.opentracing.Tracer initTracer() {
        Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration()
                .withType("const")
                .withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        return Configuration.fromEnv(this.applicationName)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig).getTracer();
    }

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[] { new ClassPathResource("data.json") });
        return factory;
    }
}
