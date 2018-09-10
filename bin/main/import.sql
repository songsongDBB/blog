INSERT INTO user (id, username, password, name, email) VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '段松松', '547674051@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'dss', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '游客1', '547674051@163.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_管理员');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_博主');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
