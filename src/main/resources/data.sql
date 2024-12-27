ALTER DATABASE pan_ttegi CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

INSERT INTO user (email, password, name, profile_url, role, deleted_at, created_at, updated_at)
VALUES ('email1@email.com', 'password', 'name1', 'https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512',
        'ADMIN', null, CURRENT_DATE, CURRENT_DATE);
INSERT INTO user (email, password, name, profile_url, role, deleted_at, created_at, updated_at)
VALUES ('email2@email.com', 'password', 'name2', 'https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512',
        'USER', null, CURRENT_DATE, CURRENT_DATE);
INSERT INTO user (email, password, name, profile_url, role, deleted_at, created_at, updated_at)
VALUES ('email3@email.com', 'password', 'name3', 'https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512',
        'USER', null, CURRENT_DATE, CURRENT_DATE);
INSERT INTO user (email, password, name, profile_url, role, deleted_at, created_at, updated_at)
VALUES ('email4@email.com', 'password', 'name4', 'https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512',
        'USER', null, CURRENT_DATE, CURRENT_DATE);

INSERT INTO workspace (user_id, name, description, created_at, updated_at)
VALUES (1, 'name', 'description', CURRENT_DATE, CURRENT_DATE);

INSERT INTO member (user_id, workspace_id, role, created_at, updated_at)
VALUES (1, 1, 'WORKSPACE', CURRENT_DATE, CURRENT_DATE);
INSERT INTO member (user_id, workspace_id, role, created_at, updated_at)
VALUES (2, 1, 'WORKSPACE', CURRENT_DATE, CURRENT_DATE);
INSERT INTO member (user_id, workspace_id, role, created_at, updated_at)
VALUES (3, 1, 'BOARD', CURRENT_DATE, CURRENT_DATE);
INSERT INTO member (user_id, workspace_id, role, created_at, updated_at)
VALUES (4, 1, 'READONLY', CURRENT_DATE, CURRENT_DATE);

INSERT INTO board (user_id, workspace_id, color, image_url, name, created_at, updated_at)
VALUES (2, 1, 'red', null, 'name', CURRENT_DATE, CURRENT_DATE);

INSERT INTO list (user_id, board_id, title, position, created_at, updated_at)
VALUES (2, 1, 'title', 1, CURRENT_DATE, CURRENT_DATE);
INSERT INTO list (user_id, board_id, title, position, created_at, updated_at)
VALUES (1, 1, 'title2', 2, CURRENT_DATE, CURRENT_DATE);

INSERT INTO card (user_id, manager_id, list_id, title, description, position, end_at, created_at, updated_at)
VALUES (2, 2, 1, 'title1', 'description1', 1, CURRENT_DATE, CURRENT_DATE, CURRENT_DATE);
INSERT INTO card (user_id, manager_id, list_id, title, description, position, end_at, created_at, updated_at)
VALUES (2, 1, 1, 'title2', 'description1', 2, CURRENT_DATE, CURRENT_DATE, CURRENT_DATE);
INSERT INTO card (user_id, manager_id, list_id, title, description, position, end_at, created_at, updated_at)
VALUES (1, 1, 2, 'title3', 'description3', 3, CURRENT_DATE, CURRENT_DATE, CURRENT_DATE);

INSERT INTO comment (user_id, card_id, content, created_at, updated_at)
VALUES (3, 1, 'content1', CURRENT_DATE, CURRENT_DATE);
INSERT INTO comment (user_id, card_id, content, created_at, updated_at)
VALUES (3, 2, 'content2', CURRENT_DATE, CURRENT_DATE);
