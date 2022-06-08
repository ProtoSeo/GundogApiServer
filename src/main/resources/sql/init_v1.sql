DROP TABLE IF EXISTS member_stage;
DROP TABLE IF EXISTS member_character;
DROP TABLE IF EXISTS member_item;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS `characters`;
DROP TABLE IF EXISTS stage;

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
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`  VARCHAR(100) NOT NULL,
    `level` INT          NOT NULL
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
    best_score BIGINT  DEFAULT 0     NOT NULL,
    CONSTRAINT fk_member_stage_member_id__id FOREIGN KEY (member_id) REFERENCES `member` (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_member_stage_stage_id__id FOREIGN KEY (stage_id) REFERENCES stage (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- TODO 스테이지, 아이템, 캐릭터 확정되면 수정하기
INSERT INTO stage
VALUES (1, '첫 번째 스테이지', 1);
INSERT INTO stage
VALUES (2, '두 번째 스테이지', 2);
INSERT INTO stage
VALUES (3, '세 번째 스테이지', 3);
INSERT INTO stage
VALUES (4, '네 번째 스테이지', 4);

INSERT INTO item
VALUES (1, '개껌', '강아지들이 가장 좋아하는 개껌이다. 보기만 해도 꼬리를 흔드는 것 같다.', DEFAULT);
INSERT INTO item
VALUES (2, '부리', '독수리를 물리쳐서 획득한 부리이다. 아주 무섭게 생겼다.', 4);
INSERT INTO item
VALUES (3, '이빨', '상어를 물리쳐서 획득한 이빨이다. 조금만 스쳐도 아플정도로 날카롭다.', 4);
INSERT INTO item
VALUES (4, '수염', '사자를 물리쳐서 획득한 수염이다. 잡아 당기고 싶게, 길다.', 4);
INSERT INTO item
VALUES (5, '헤어볼', '고양이를 물리쳐서 획득한 헤어볼이다. 아주 동그랗다.', 4);

INSERT INTO `characters`
VALUES (1, '봄이', DEFAULT, DEFAULT);
INSERT INTO `characters`
VALUES (2, '송이', DEFAULT, DEFAULT);
INSERT INTO `characters`
VALUES (3, '쭈니', DEFAULT, DEFAULT);

select * from member_character;
select * from member_stage;
select * from member_item;