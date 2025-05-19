create table user
(
    id          int auto_increment comment '用户ID'
        primary key,
    username    varchar(50)                            not null comment '用户名',
    password    varchar(100) default '123456'          not null comment '密码',
    type        int                                    not null comment '角色类型',
    real_id     varchar(10)                            null comment '实际ID (学生学号/教师编号)',
    create_time timestamp    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_username
        unique (username)
)
    comment '用户表';

create index idx_role_id
    on user (type);

INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (496, 'admin', '123456', 1, '0', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (497, 'superadmin', '123456', 1, '1', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (498, 'jsmith', '123456', 2, 'T0001', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (499, 'sjohnson', '123456', 2, 'T0002', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (500, 'mbrown', '123456', 2, 'T0003', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (501, 'edavis', '123456', 2, 'T0004', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (502, 'dwilson', '123456', 2, 'T0005', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (503, 'jlee', '123456', 2, 'T0006', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (504, 'rtaylor', '123456', 2, 'T0007', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (505, 'landerson', '123456', 2, 'T0008', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (507, 'dthompson', '123456', 2, 'T0010', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (508, 'kwhite', '123456', 2, 'T0011', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (509, 'tharris', '123456', 2, 'T0012', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (510, 'nclark', '123456', 2, 'T0013', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (511, 'swright', '123456', 2, 'T0014', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (512, 'myoung', '123456', 2, 'T0015', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (513, 'eking', '123456', 2, 'T0016', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (514, 'plewis', '123456', 2, 'T0017', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (515, 'cwalker', '123456', 2, 'T0018', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (516, 'lhall', '123456', 2, 'T0019', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (517, 'kadams', '123456', 2, 'T0020', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (518, 'jbaker', '123456', 2, 'T0021', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (519, 'mevans', '123456', 2, 'T0022', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (520, 'sgarcia', '123456', 2, 'T0023', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (521, 'rturner', '123456', 2, 'T0024', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (522, 'cparker', '123456', 2, 'T0025', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (523, 'pfoster', '123456', 2, 'T0026', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (524, 'rgreen', '123456', 2, 'T0027', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (525, 'ascott', '123456', 2, 'T0028', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (526, 'oreed', '123456', 2, 'T0029', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (527, 'wcooper', '123456', 2, 'T0030', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (528, 'ajohnson', '123456', 3, '2020010001', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (529, 'edaviss', '123456', 3, '2020010002', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (530, 'rmiller', '123456', 3, '2020010003', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (531, 'sbrown', '123456', 3, '2020010004', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (532, 'ewilson', '123456', 3, '2020010005', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (533, 'otaylor', '123456', 3, '2020010006', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (534, 'nanderson', '123456', 3, '2020010007', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (535, 'amartinez', '123456', 3, '2020010008', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (536, 'wgarcia', '123456', 3, '2020010009', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (537, 'irobinson', '123456', 3, '2020010010', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (538, 'jclark', '123456', 3, '2020020001', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (539, 'clewis', '123456', 3, '2020020002', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (540, 'jreed', '123456', 3, '2021010001', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (541, 'zbrooks', '123456', 3, '2021010002', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (542, 'tgarcia', '123456', 3, '2022010001', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (543, 'mrodriguez', '123456', 3, '2022010002', '2025-05-19 05:26:00', '2025-05-19 05:29:14');
INSERT INTO mis.user (id, username, password, type, real_id, create_time, update_time) VALUES (544, 'Jimi', '123456', 2, 'T1021', '2025-05-19 07:40:02', '2025-05-19 07:40:02');
