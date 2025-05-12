-- Mock Data for School Management System

-- 清空表数据
DELETE FROM course_selection;
DELETE FROM teacher_course;
DELETE FROM user;
DELETE FROM student;
DELETE FROM teacher;
DELETE FROM course;

-- 课程表数据
INSERT INTO course (course_id, name, credit, grade_requirement, discontinue_year) VALUES
('COMP10001', 'Introduction to Programming', 4.0, 1, NULL),
('MATH10002', 'Calculus I', 3.5, 1, NULL),
('PHYS10003', 'Mechanics', 4.0, 1, NULL),
('CHEM10004', 'General Chemistry', 3.5, 1, NULL),
('COMP20001', 'Data Structures', 4.0, 2, NULL),
('COMP20002', 'Algorithms', 4.0, 2, NULL),
('MATH20001', 'Linear Algebra', 3.5, 2, NULL),
('COMP30001', 'Database Systems', 4.0, 3, NULL),
('COMP30002', 'Artificial Intelligence', 4.0, 3, NULL),
('COMP40001', 'Machine Learning', 4.0, 4, NULL),
('HIST10001', 'World History', 3.0, 1, 2022),
('BIOL10001', 'General Biology', 3.5, 1, NULL);

-- 教师表数据
INSERT INTO teacher (teacher_id, name, gender, title, department, email, hire_date, phone) VALUES
('T0001', 'John Smith', 'Male', 'Professor', 'CS', 'john@example.com', '2010-09-01', '13800001111'),
('T0002', 'Mary Johnson', 'Female', 'Professor', 'MATH', 'mary@example.com', '2012-09-01', '13800002222'),
('T0003', 'Robert Davis', 'Male', 'Associate', 'CS', 'robert@example.com', '2015-09-01', '13800003333'),
('T0004', 'Sarah Wilson', 'Female', 'Assistant', 'PHYS', 'sarah@example.com', '2018-09-01', '13800004444'),
('T0005', 'Michael Brown', 'Male', 'Professor', 'CHEM', 'michael@example.com', '2011-09-01', '13800005555'),
('T0006', 'Emily Taylor', 'Female', 'Associate', 'CS', 'emily@example.com', '2016-09-01', '13800006666'),
('T0007', 'David Miller', 'Male', 'Assistant', 'HIST', 'david@example.com', '2019-09-01', '13800007777'),
('T0008', 'Jennifer Lee', 'Female', 'Professor', 'BIOL', 'jennifer@example.com', '2014-09-01', '13800008888');

-- 学生表数据
INSERT INTO student (student_id, name, gender, age_at_enrollment, enrollment_year, class) VALUES
('S202101001', 'Alice Johnson', 'Female', 18, 2021, 'CS2021-1'),
('S202101002', 'Bob Williams', 'Male', 19, 2021, 'CS2021-1'),
('S202101003', 'Charlie Brown', 'Male', 18, 2021, 'CS2021-1'),
('S202101004', 'Diana Smith', 'Female', 18, 2021, 'CS2021-2'),
('S202101005', 'Edward Jones', 'Male', 19, 2021, 'CS2021-2'),
('S202101006', 'Fiona Taylor', 'Female', 18, 2021, 'MATH2021-1'),
('S202101007', 'George Miller', 'Male', 18, 2021, 'MATH2021-1'),
('S202101008', 'Hannah Davis', 'Female', 19, 2021, 'PHYS2021-1'),
('S202101009', 'Ian Wilson', 'Male', 18, 2021, 'PHYS2021-1'),
('S202101010', 'Julia Moore', 'Female', 18, 2021, 'CHEM2021-1'),
('S202001001', 'Kevin Harris', 'Male', 18, 2020, 'CS2020-1'),
('S202001002', 'Laura Martin', 'Female', 19, 2020, 'CS2020-1'),
('S202001003', 'Nathan Clark', 'Male', 18, 2020, 'CS2020-1'),
('S202001004', 'Olivia Lewis', 'Female', 18, 2020, 'MATH2020-1'),
('S201901001', 'Peter Walker', 'Male', 18, 2019, 'CS2019-1'),
('S201901002', 'Quinn Evans', 'Female', 19, 2019, 'CS2019-1');

-- 教师课程关联表数据
INSERT INTO teacher_course (teacher_id, course_id) VALUES
('T0001', 'COMP10001'),
('T0003', 'COMP20001'),
('T0003', 'COMP20002'),
('T0001', 'COMP30001'),
('T0006', 'COMP30002'),
('T0001', 'COMP40001'),
('T0002', 'MATH10002'),
('T0002', 'MATH20001'),
('T0004', 'PHYS10003'),
('T0005', 'CHEM10004'),
('T0007', 'HIST10001'),
('T0008', 'BIOL10001');

-- 选课表数据
INSERT INTO course_selection (student_id, course_id, teacher_id, selection_year, score) VALUES
('S202101001', 'COMP10001', 'T0001', 2021, 85.5),
('S202101001', 'MATH10002', 'T0002', 2021, 78.0),
('S202101001', 'PHYS10003', 'T0004', 2021, 82.5),
('S202101002', 'COMP10001', 'T0001', 2021, 92.0),
('S202101002', 'MATH10002', 'T0002', 2021, 88.5),
('S202101002', 'CHEM10004', 'T0005', 2021, 76.0),
('S202101003', 'COMP10001', 'T0001', 2021, 79.5),
('S202101004', 'COMP10001', 'T0001', 2021, 88.0),
('S202101005', 'COMP10001', 'T0001', 2021, 91.5),
('S202101006', 'MATH10002', 'T0002', 2021, 95.0),
('S202101007', 'MATH10002', 'T0002', 2021, 87.5),
('S202101008', 'PHYS10003', 'T0004', 2021, 83.0),
('S202101009', 'PHYS10003', 'T0004', 2021, 80.5),
('S202101010', 'CHEM10004', 'T0005', 2021, 86.0),
('S202001001', 'COMP20001', 'T0003', 2022, 84.5),
('S202001001', 'COMP20002', 'T0003', 2022, 79.0),
('S202001001', 'MATH20001', 'T0002', 2022, 88.0),
('S202001002', 'COMP20001', 'T0003', 2022, 92.5),
('S202001003', 'COMP20001', 'T0003', 2022, 81.0),
('S202001004', 'MATH20001', 'T0002', 2022, 94.0),
('S201901001', 'COMP30001', 'T0001', 2022, 87.5),
('S201901001', 'COMP30002', 'T0006', 2022, 90.0),
('S201901002', 'COMP30001', 'T0001', 2022, 85.0),
('S201901002', 'COMP30002', 'T0006', 2022, 82.5);

-- 用户表数据
INSERT INTO user (username, password, type, real_id) VALUES
-- 管理员
('admin', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL), -- 密码：123456的MD5加密

-- 学生用户
('alice_j', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202101001'),
('bob_w', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202101002'),
('charlie_b', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202101003'),
('diana_s', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202101004'),
('edward_j', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202101005'),
('kevin_h', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S202001001'),
('peter_w', 'e10adc3949ba59abbe56e057f20f883e', 1, 'S201901001'),

-- 教师用户
('john_s', 'e10adc3949ba59abbe56e057f20f883e', 2, 'T0001'),
('mary_j', 'e10adc3949ba59abbe56e057f20f883e', 2, 'T0002'),
('robert_d', 'e10adc3949ba59abbe56e057f20f883e', 2, 'T0003'),
('sarah_w', 'e10adc3949ba59abbe56e057f20f883e', 2, 'T0004'),
('emily_t', 'e10adc3949ba59abbe56e057f20f883e', 2, 'T0006');
