-- 创建课程表数据
INSERT INTO mis.course (course_id, name, credit, semester, hours, description) VALUES
('MATH101', 'Calculus I', 4.0, 1, 48, 'Introduction to differential calculus'),
('MATH102', 'Calculus II', 4.0, 2, 48, 'Introduction to integral calculus'),
('COMP101', 'Introduction to Programming', 3.0, 1, 42, 'Fundamentals of programming'),
('COMP201', 'Data Structures', 3.0, 2, 42, 'Basic data structures and algorithms'),
('PHYS101', 'Mechanics', 4.0, 1, 48, 'Newtonian mechanics'),
('CHEM101', 'General Chemistry', 4.0, 1, 48, 'Basic principles of chemistry'),
('ENG101', 'English Composition', 3.0, 1, 36, 'Academic writing skills'),
('HIST101', 'World History', 3.0, 2, 36, 'Overview of world history'),
('ECON101', 'Microeconomics', 3.0, 1, 36, 'Principles of microeconomics'),
('ECON102', 'Macroeconomics', 3.0, 2, 36, 'Principles of macroeconomics');

-- 创建教师表数据
INSERT INTO mis.teacher (teacher_id, name, title, department, email, hire_date, phone, gender) VALUES
('T001', 'John Smith', 'Professor', 'Math', 'jsmith@univ.edu', '2010-08-15', '555-1234', 'Male'),
('T002', 'Sarah Johnson', 'Professor', 'CompSci', 'sjohnson@univ.edu', '2012-01-20', '555-2345', 'Female'),
('T003', 'Robert Chen', 'Associate', 'Physics', 'rchen@univ.edu', '2015-06-10', '555-3456', 'Male'),
('T004', 'Emily Davis', 'Assistant', 'Chemistry', 'edavis@univ.edu', '2018-08-01', '555-4567', 'Female'),
('T005', 'Michael Brown', 'Professor', 'English', 'mbrown@univ.edu', '2008-09-01', '555-5678', 'Male'),
('T006', 'Lisa Wang', 'Associate', 'History', 'lwang@univ.edu', '2014-07-15', '555-6789', 'Female'),
('T007', 'Thomas Lee', 'Professor', 'Economics', 'tlee@univ.edu', '2011-01-10', '555-7890', 'Male'),
('T008', 'Karen Wilson', 'Assistant', 'Math', 'kwilson@univ.edu', '2019-08-20', '555-8901', 'Female');

-- 创建教师课程关联表数据
INSERT INTO mis.teacher_course (teacher_id, course_id) VALUES
('T001', 'MATH101'),
('T001', 'MATH102'),
('T008', 'MATH101'),
('T002', 'COMP101'),
('T002', 'COMP201'),
('T003', 'PHYS101'),
('T004', 'CHEM101'),
('T005', 'ENG101'),
('T006', 'HIST101'),
('T007', 'ECON101'),
('T007', 'ECON102');

-- 创建学生表数据
INSERT INTO mis.student (student_id, name, age_at_enrollment, enrollment_year, class, gender) VALUES
('2020001001', 'Alex Johnson', 18, 2020, 'CS-2020-01', 'Male'),
('2020001002', 'Emma Williams', 19, 2020, 'CS-2020-01', 'Female'),
('2020001003', 'Daniel Brown', 18, 2020, 'CS-2020-01', 'Male'),
('2020001004', 'Olivia Garcia', 18, 2020, 'CS-2020-02', 'Female'),
('2020002001', 'William Martinez', 19, 2020, 'MATH-2020-01', 'Male'),
('2020002002', 'Sophia Rodriguez', 18, 2020, 'MATH-2020-01', 'Female'),
('2021001001', 'James Wilson', 18, 2021, 'CS-2021-01', 'Male'),
('2021001002', 'Charlotte Lee', 19, 2021, 'CS-2021-01', 'Female'),
('2021002001', 'Benjamin Davis', 18, 2021, 'PHYS-2021-01', 'Male'),
('2021002002', 'Amelia Thompson', 18, 2021, 'PHYS-2021-01', 'Female');

-- 创建选课表数据
INSERT INTO mis.course_selection (student_id, course_id, teacher_id, selection_year, score) VALUES
('2020001001', 'MATH101', 'T001', 2020, 85.5),
('2020001001', 'COMP101', 'T002', 2020, 92.0),
('2020001001', 'PHYS101', 'T003', 2020, 78.5),
('2020001002', 'MATH101', 'T001', 2020, 90.0),
('2020001002', 'COMP101', 'T002', 2020, 88.5),
('2020001002', 'ENG101', 'T005', 2020, 94.0),
('2020001003', 'MATH101', 'T001', 2020, 82.0),
('2020001003', 'CHEM101', 'T004', 2020, 79.5),
('2020001004', 'COMP101', 'T002', 2020, 91.5),
('2020001004', 'HIST101', 'T006', 2020, 87.0),
('2020002001', 'MATH101', 'T001', 2020, 95.0),
('2020002001', 'MATH102', 'T001', 2021, 91.0),
('2020002002', 'MATH101', 'T001', 2020, 89.5),
('2021001001', 'COMP101', 'T002', 2021, 86.0),
('2021001001', 'MATH101', 'T008', 2021, 84.5),
('2021001002', 'COMP101', 'T002', 2021, 93.0),
('2021001002', 'ECON101', 'T007', 2021, 88.0),
('2021002001', 'PHYS101', 'T003', 2021, 90.5),
('2021002001', 'MATH101', 'T008', 2021, 87.5),
('2021002002', 'PHYS101', 'T003', 2021, 92.5);

-- 创建用户表数据
INSERT INTO mis.user (username, password, type, real_id) VALUES
-- 管理员用户
('admin', '', 0, NULL),  -- 密码123456的MD5加密
-- 教师用户
('t_jsmith', '', 1, 'T001'),
('t_sjohnson', '', 1, 'T002'),
('t_rchen', '', 1, 'T003'),
('t_edavis', '', 1, 'T004'),
-- 学生用户
('s_johnson', '', 2, '2020001001'),
('s_williams', '', 2, '2020001002'),
('s_brown', '', 2, '2020001003'),
('s_garcia', '', 2, '2020001004'),
('s_martinez', '', 2, '2020002001');
