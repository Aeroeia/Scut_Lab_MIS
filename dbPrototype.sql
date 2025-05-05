
-- 创建用户表
CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `role_id` INT NOT NULL COMMENT '角色ID',
    `real_id` VARCHAR(10) DEFAULT NULL COMMENT '实际ID (学生学号/教师编号)',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建学生表
CREATE TABLE `student` (
    `student_id` CHAR(10) NOT NULL COMMENT '学号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` ENUM('男', '女') NOT NULL COMMENT '性别',
    `age_at_enrollment` INT NOT NULL COMMENT '入学年龄',
    `enrollment_year` INT NOT NULL COMMENT '入学年份',
    `class` VARCHAR(20) NOT NULL COMMENT '班级',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`student_id`),
    INDEX `idx_class` (`class`),
    INDEX `idx_name` (`name`),
    CONSTRAINT `chk_age` CHECK (`age_at_enrollment` BETWEEN 10 AND 50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 创建教师表
CREATE TABLE `teacher` (
    `teacher_id` CHAR(5) NOT NULL COMMENT '教师编号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`teacher_id`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- 创建课程表
CREATE TABLE `course` (
    `course_id` CHAR(7) NOT NULL COMMENT '课程编号',
    `name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `credit` DECIMAL(3,1) NOT NULL COMMENT '学分',
    `grade_requirement` INT NOT NULL COMMENT '开课年级',
    `discontinue_year` INT DEFAULT NULL COMMENT '停课年份',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`course_id`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 创建教师课程关联表
CREATE TABLE `teacher_course` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `teacher_id` CHAR(5) NOT NULL COMMENT '教师编号',
    `course_id` CHAR(7) NOT NULL COMMENT '课程编号',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teacher_course` (`teacher_id`, `course_id`),
    INDEX `idx_teacher_id` (`teacher_id`),
    INDEX `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师课程关联表';

-- 创建选课表
CREATE TABLE `course_selection` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` CHAR(10) NOT NULL COMMENT '学号',
    `course_id` CHAR(7) NOT NULL COMMENT '课程编号',
    `teacher_id` CHAR(5) NOT NULL COMMENT '教师编号',
    `selection_year` INT NOT NULL COMMENT '选课年份',
    `score` DECIMAL(5,2) DEFAULT NULL COMMENT '成绩',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_selection` (`student_id`, `course_id`, `selection_year`),
    INDEX `idx_student_id` (`student_id`),
    INDEX `idx_course_id` (`course_id`),
    INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课表';

-- 插入默认管理员账号
INSERT INTO `user` (`username`, `password`, `role_id`) VALUES 
('admin', 'admin123456', 1); -- 密码: admin123
