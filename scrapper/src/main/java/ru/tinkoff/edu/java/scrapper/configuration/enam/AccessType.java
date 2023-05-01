package ru.tinkoff.edu.java.scrapper.configuration.enam;

public enum AccessType {
    JDBC("jdbc"),
    JPA("jpa"),
    JOOQ("jooq");


    AccessType(String implementation) {
    }
}