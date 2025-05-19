create table teacher_course
(
    id          int auto_increment comment '主键ID'
        primary key,
    teacher_id  char(5)                             not null comment '教师编号',
    course_id   char(10)                            not null comment '课程编号',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     int       default 0                 null
)
    comment '教师课程关联表';

create index idx_course_id
    on teacher_course (course_id);

create index idx_teacher_id
    on teacher_course (teacher_id);

INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (363, 'T0001', 'COMP10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (364, 'T0002', 'COMP10002', '2025-05-19 05:26:00', '2025-05-19 07:58:54', 1);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (365, 'T0003', 'COMP20003', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (366, 'T0003', 'COMP20005', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (367, 'T0001', 'COMP20007', '2025-05-19 05:26:00', '2025-05-19 07:49:53', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (368, 'T0001', 'COMP30010', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (369, 'T0002', 'COMP30020', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (370, 'T0003', 'COMP30023', '2025-05-19 05:26:00', '2025-05-19 07:50:06', 1);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (371, 'T0004', 'COMP40001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (372, 'T0001', 'COMP40005', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (373, 'T0002', 'COMP10003', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (374, 'T0004', 'COMP20006', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (375, 'T0003', 'COMP30021', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (376, 'T0001', 'COMP30024', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (377, 'T0002', 'COMP40003', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (378, 'T0005', 'MATH10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (379, 'T0006', 'MATH10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (380, 'T0007', 'MATH20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (381, 'T0005', 'MATH20003', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (382, 'T0006', 'MATH30001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (383, 'T0007', 'MATH10003', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (384, 'T0005', 'MATH20002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (385, 'T0006', 'MATH30002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (386, 'T0008', 'PHYS10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (387, 'T0009', 'PHYS10002', '2025-05-19 05:26:00', '2025-05-19 07:50:13', 1);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (388, 'T0008', 'PHYS20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (389, 'T0009', 'PHYS20002', '2025-05-19 05:26:00', '2025-05-19 07:50:13', 1);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (390, 'T0010', 'CHEM10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (391, 'T0011', 'CHEM20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (392, 'T0010', 'CHEM10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (393, 'T0012', 'BIOL10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (394, 'T0013', 'BIOL20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (395, 'T0012', 'BIOL10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (396, 'T0014', 'ECON10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (397, 'T0015', 'ECON20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (398, 'T0014', 'ECON20002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (399, 'T0015', 'ECON10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (400, 'T0016', 'LANG10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (401, 'T0017', 'LANG10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (402, 'T0016', 'LANG20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (403, 'T0018', 'HIST10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (404, 'T0018', 'HIST20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (405, 'T0019', 'PHIL10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (406, 'T0020', 'PSYC10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (407, 'T0021', 'PSYC20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (408, 'T0022', 'ENGI10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (409, 'T0023', 'ENGI20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (410, 'T0024', 'ARTS10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (411, 'T0025', 'BUSI10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (412, 'T0025', 'BUSI20001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (413, 'T0001', 'COMP10001', '2025-05-19 05:26:00', '2025-05-19 07:47:33', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (414, 'T0026', 'COMP10002', '2025-05-19 05:26:00', '2025-05-19 07:58:54', 1);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (415, 'T0027', 'MATH10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (416, 'T0027', 'MATH10002', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (417, 'T0028', 'PHYS10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (418, 'T0029', 'CHEM10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (419, 'T0030', 'BIOL10001', '2025-05-19 05:26:00', '2025-05-19 05:26:00', 0);
INSERT INTO mis.teacher_course (id, teacher_id, course_id, create_time, update_time, deleted) VALUES (420, 'T0003', 'COMP90001', '2025-05-19 07:39:02', '2025-05-19 07:39:02', 0);
