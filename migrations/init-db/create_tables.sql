--liquibase formatted sql

--changeset Volkova:create_table_chat
CREATE table IF NOT EXISTS chat
(
    id BIGSERIAL PRIMARY KEY
);

--changeset Volkova:create_table_links
CREATE table IF NOT EXISTS links
(
    url VARCHAR(255) NOT NULL,
    id BIGSERIAL PRIMARY KEY,
    last_check_time timestamp

);

--changeset Volkova:create_table_chat_link
CREATE table IF NOT EXISTS chat_link
(

    chat_id INT,
    link_id INT,
    PRIMARY KEY (chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES links(id)
);
