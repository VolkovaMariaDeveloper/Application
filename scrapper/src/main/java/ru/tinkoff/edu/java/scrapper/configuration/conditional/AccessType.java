package ru.tinkoff.edu.java.scrapper.configuration.conditional;

public enum AccessType {
    JDBC("jdbc"),
    JPA("jpa"),
    JOOQ("jooq");

    AccessType(String implementation) {
    }
}
