CREATE TABLE `member`
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL
);

ALTER TABLE `member`
    ADD CONSTRAINT member_email_unique UNIQUE (email);

CREATE TABLE item
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`      VARCHAR(255)                       NOT NULL,
    description longtext                           NOT NULL,
    `limit`     BIGINT DEFAULT 9223372036854775807 NOT NULL
);

CREATE TABLE member_item
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT           NOT NULL,
    item_id   BIGINT           NOT NULL,
    `count`   BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_member_item_member_id__id FOREIGN KEY (member_id) REFERENCES `member` (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_member_item_item_id__id FOREIGN KEY (item_id) REFERENCES item (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE `characters`
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`  VARCHAR(100)   NOT NULL,
    health  INT DEFAULT 3  NOT NULL,
    stamina INT DEFAULT 10 NOT NULL
);

ALTER TABLE `characters`
    ADD CONSTRAINT characters_name_unique UNIQUE (`name`);

CREATE TABLE member_character
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT                NOT NULL,
    character_id  BIGINT                NOT NULL,
    health_level  INT     DEFAULT 0     NOT NULL,
    stamina_level INT     DEFAULT 0     NOT NULL,
    is_open       BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_member_character_member_id__id FOREIGN KEY (member_id) REFERENCES `member` (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_member_character_character_id__id FOREIGN KEY (character_id) REFERENCES `characters` (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE stage
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`     VARCHAR(100)      NOT NULL,
    `level`    INT               NOT NULL,
    next_id BIGINT DEFAULT -1 NOT NULL
);

ALTER TABLE stage
    ADD CONSTRAINT stage_name_unique UNIQUE (`name`);

ALTER TABLE stage
    ADD CONSTRAINT stage_level_unique UNIQUE (`level`);

CREATE TABLE member_stage
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT                NOT NULL,
    stage_id   BIGINT                NOT NULL,
    is_clear   BOOLEAN DEFAULT FALSE NOT NULL,
    is_open    BOOLEAN DEFAULT FALSE NOT NULL,
    best_score BIGINT  DEFAULT 0     NOT NULL,
    CONSTRAINT fk_member_stage_member_id__id FOREIGN KEY (member_id) REFERENCES `member` (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_member_stage_stage_id__id FOREIGN KEY (stage_id) REFERENCES stage (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);