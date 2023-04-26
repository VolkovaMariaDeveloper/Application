--liquibase formatted sql

--changeset Volkova:create_table_chat
create table IF NOT EXISTS chat
(
    id BIGSERIAL PRIMARY KEY
);

--changeset Volkova:create_table_links
create table IF NOT EXISTS links
(
    url VARCHAR(255) NOT NULL,
    id BIGSERIAL PRIMARY KEY,
    last_check_time timestamp,
    count INT

);

--changeset Volkova:create_table_chat_link
create table IF NOT EXISTS chat_link
(

    chat_id BIGINT,
    link_id BIGINT,
    PRIMARY KEY (chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(id) ON delete CASCADE,
    FOREIGN KEY (link_id) REFERENCES links(id)
);
