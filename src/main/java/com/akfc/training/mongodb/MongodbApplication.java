package com.akfc.training.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class MongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }
    
    @Configuration
    public static class MongoConfig extends AbstractMongoClientConfiguration {
        
        @Override
        protected String getDatabaseName() {
            return "airbnb";
        }
    }
}