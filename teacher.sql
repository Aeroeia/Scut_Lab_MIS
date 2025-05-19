create table teacher
(
    id          int auto_increment
        primary key,
    teacher_id  char(5)                             not null comment '教师编号',
    name        varchar(50)                         not null comment '姓名',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    title       varchar(10)                         null comment '职称',
    department  varchar(10)                         null comment '所属院系',
    email       varchar(20)                         null comment '邮箱',
    hire_date   varchar(20)                         null comment '入职日期',
    phone       varchar(15)                         null comment '手机号',
    gender      enum ('Male', 'Female')             null comment '性别',
    deleted     int       default 0                 null
)
    comment '教师表';

create index idx_name
    on teacher (name);

INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (230, 'T0001', 'YI ROU GU', '2025-05-19 05:26:00', '2025-05-19 05:31:57', 'Professor', 'COMP', 'jsmith@uni.edu', '2010-09-01', '13828825624', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (231, 'T0002', 'Sarah Johnson', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'COMP', 'sjohnson@uni.edu', '2012-07-15', '2345678901', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (232, 'T0003', 'Michael Brown', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'COMP', 'mbrown@uni.edu', '2015-01-10', '3456789012', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (233, 'T0004', 'Emily Davis', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'COMP', 'edavis@uni.edu', '2018-08-20', '4567890123', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (234, 'T0005', 'David Wilson', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'MATH', 'dwilson@uni.edu', '2008-06-05', '5678901234', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (235, 'T0006', 'Jennifer Lee', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'MATH', 'jlee@uni.edu', '2014-03-12', '6789012345', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (236, 'T0007', 'Robert Taylor', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'MATH', 'rtaylor@uni.edu', '2019-05-22', '7890123456', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (237, 'T0008', 'Lisa Anderson', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'PHYS', 'landerson@uni.edu', '2009-11-30', '8901234567', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (238, 'T0009', 'James Martin', '2025-05-19 05:26:00', '2025-05-19 07:50:13', 'Assoc Prof', 'PHYS', 'jmartin@uni.edu', '2013-09-14', '9012345678', 'Male', 1);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (239, 'T0010', 'Daniel Thompson', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'CHEM', 'dthompson@uni.edu', '2007-08-25', '0123456789', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (240, 'T0011', 'Karen White', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'CHEM', 'kwhite@uni.edu', '2011-02-18', '1234509876', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (241, 'T0012', 'Thomas Harris', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'BIOL', 'tharris@uni.edu', '2005-07-07', '2345609876', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (242, 'T0013', 'Nancy Clark', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'BIOL', 'nclark@uni.edu', '2010-04-30', '3456709876', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (243, 'T0014', 'Steven Wright', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'ECON', 'swright@uni.edu', '2006-12-11', '4567809876', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (244, 'T0015', 'Michelle Young', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'ECON', 'myoung@uni.edu', '2013-01-08', '5678909876', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (245, 'T0016', 'Edward King', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'LANG', 'eking@uni.edu', '2004-09-23', '6789009876', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (246, 'T0017', 'Patricia Lewis', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'LANG', 'plewis@uni.edu', '2012-10-15', '7890109876', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (247, 'T0018', 'Charles Walker', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'HIST', 'cwalker@uni.edu', '2008-03-22', '8901209876', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (248, 'T0019', 'Laura Hall', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'PHIL', 'lhall@uni.edu', '2015-06-07', '9012309876', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (249, 'T0020', 'Kevin Adams', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'PSYC', 'kadams@uni.edu', '2009-05-19', '0123409876', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (250, 'T0021', 'Julie Baker', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'PSYC', 'jbaker@uni.edu', '2014-07-25', '1234569870', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (251, 'T0022', 'Mark Evans', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'ENGI', 'mevans@uni.edu', '2007-10-14', '2345679870', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (252, 'T0023', 'Susan Garcia', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'ENGI', 'sgarcia@uni.edu', '2013-04-05', '3456789870', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (253, 'T0024', 'Richard Turner', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Professor', 'ARTS', 'rturner@uni.edu', '2006-01-29', '4567899870', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (254, 'T0025', 'Catherine Parker', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Assoc Prof', 'BUSI', 'cparker@uni.edu', '2011-11-17', '5678909870', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (255, 'T0026', 'Paul Foster', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'COMP', 'pfoster@uni.edu', '2020-01-15', '6789019870', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (256, 'T0027', 'Rachel Green', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'MATH', 'rgreen@uni.edu', '2019-09-28', '7890129870', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (257, 'T0028', 'Andrew Scott', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'PHYS', 'ascott@uni.edu', '2020-06-10', '8901239870', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (258, 'T0029', 'Olivia Reed', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'CHEM', 'oreed@uni.edu', '2018-11-03', '9012349870', 'Female', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (259, 'T0030', 'William Cooper', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 'Asst Prof', 'BIOL', 'wcooper@uni.edu', '2019-03-18', '0123459870', 'Male', 0);
INSERT INTO mis.teacher (id, teacher_id, name, create_time, update_time, title, department, email, hire_date, phone, gender, deleted) VALUES (260, 'T1021', 'Jimi', '2025-05-19 07:40:02', '2025-05-19 07:40:02', 'Lecturer', 'Computet ', '12312312@a.edu', '2025-05-06', '13241235123', 'Male', 0);
