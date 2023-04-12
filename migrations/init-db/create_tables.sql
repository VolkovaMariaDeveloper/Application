--liquibase formatted sql

--changeset Volcova:create_table_chat
CREATE table IF NOT EXISTS chat
(
    chat_id INT PRIMARY KEY
);

--changeset Volcova:create_table_links
CREATE table IF NOT EXISTS links
(
    url VARCHAR(255) NOT NULL,
    link_id INT PRIMARY KEY
);

--changeset svytoq:create_table_chat_link
CREATE table IF NOT EXISTS chat_link
(

    chat_id INT,
    link_id INT,
    PRIMARY KEY (chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
    FOREIGN KEY (link_id) REFERENCES links(link_id)
);
