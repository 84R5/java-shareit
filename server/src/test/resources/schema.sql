DROP TABLE IF EXISTS PUBLIC.REQUESTS CASCADE;
DROP TABLE IF EXISTS PUBLIC.BOOKINGS CASCADE;
DROP TABLE IF EXISTS PUBLIC.ITEMS CASCADE;
DROP TABLE IF EXISTS PUBLIC.USERS CASCADE;
DROP TABLE IF EXISTS PUBLIC.COMMENTS CASCADE;

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME  VARCHAR(255),
    EMAIL VARCHAR(127) UNIQUE NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)

);

CREATE TABLE IF NOT EXISTS PUBLIC.REQUESTS
(
    ID           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    DESCRIPTION  VARCHAR(255)                                        NOT NULL,
    REQUESTER_ID BIGINT                                              NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE                        NOT NULL,
    CONSTRAINT FK_REQUESTS_USERS_ID FOREIGN KEY (REQUESTER_ID) REFERENCES PUBLIC.USERS (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.ITEMS
(
    ID          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME        VARCHAR(255)                                        NOT NULL,
    DESCRIPTION VARCHAR(1000)                                       NOT NULL,
    OWNER_ID    BIGINT                                              NOT NULL,
    REQUEST_ID  BIGINT                                                      ,
    AVAILABLE   BOOLEAN                                                     ,
    CONSTRAINT FK_ITEMS_USER_ID FOREIGN KEY (OWNER_ID) REFERENCES PUBLIC.USERS (ID),
    CONSTRAINT FK_ITEMS_REQUESTS_ID FOREIGN KEY (REQUEST_ID) REFERENCES PUBLIC.REQUESTS (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.BOOKINGS
(
    ID         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    START_DATE TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    END_DATE   TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    ITEM_ID    BIGINT                                              NOT NULL,
    BOOKER_ID  BIGINT                                              NOT NULL,
    STATUS     VARCHAR(20)                                         NOT NULL,
    CONSTRAINT FK_BOOKINGS_ITEMS_ID FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS (ID),
    CONSTRAINT FK_BOOKINGS_USERS_ID FOREIGN KEY (BOOKER_ID) REFERENCES PUBLIC.USERS (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.COMMENTS
(
    ID        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    TEXT      VARCHAR(1000)                                       NOT NULL,
    ITEM_ID   BIGINT                                              NOT NULL,
    AUTHOR_ID BIGINT                                              NOT NULL,
    CREATED   TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    CONSTRAINT FK_COMMENTS_ITEMS_ID FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS (ID),
    CONSTRAINT FK_COMMENTS_USERS_ID FOREIGN KEY (AUTHOR_ID) REFERENCES PUBLIC.USERS (ID)
);


