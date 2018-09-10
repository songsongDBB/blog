INSERT INTO user (id, username, password, name, email) VALUES (1, 'admin', '123456', '段松松', '547674051@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'dss', '123456', '游客1', '547674051@163.com');

INSERT INTO authority (id, name) VALUES (1, '管理员');
INSERT INTO authority (id, name) VALUES (2, '博主');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
