create table course
(
    id                int auto_increment comment '主键'
        primary key,
    course_id         char(10)                            not null comment '课程编号',
    name              varchar(100)                        not null comment '课程名称',
    credit            decimal(3, 1)                       not null comment '学分',
    grade_requirement int                                 not null comment '开课年级',
    discontinue_year  int                                 null comment '停课年份',
    create_time       timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '课程表';

create index idx_name
    on course (name);

create table course_selection
(
    id             int auto_increment comment '主键ID'
        primary key,
    student_id     char(10)                            not null comment '学号',
    course_id      char(10)                            not null comment '课程编号',
    teacher_id     char(5)                             not null comment '教师编号',
    selection_year int                                 not null comment '选课年份',
    score          decimal(5, 2)                       null comment '成绩',
    create_time    timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_selection
        unique (student_id, course_id, selection_year)
)
    comment '选课表';

create index idx_course_id
    on course_selection (course_id);

create index idx_student_id
    on course_selection (student_id);

create index idx_teacher_id
    on course_selection (teacher_id);

create table student
(
    id                int auto_increment comment '主键'
        primary key,
    student_id        char(10)                            not null comment '学号',
    name              varchar(50)                         not null comment '姓名',
    gender            enum ('男', '女')                   not null comment '性别',
    age_at_enrollment int                                 not null comment '入学年龄',
    enrollment_year   int                                 not null comment '入学年份',
    class             varchar(20)                         not null comment '班级',
    create_time       timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint chk_age
        check (`age_at_enrollment` between 10 and 50)
)
    comment '学生表';

create index idx_class
    on student (class);

create index idx_name
    on student (name);

create table teacher
(
    id          int auto_increment
        primary key,
    teacher_id  char(5)                             not null comment '教师编号',
    name        varchar(50)                         not null comment '姓名',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    gender      enum ('男', '女')                   null comment '性别',
    title       varchar(10)                         null comment '职称',
    department  varchar(10)                         null comment '所属院系',
    email       varchar(20)                         null comment '邮箱',
    hire_date   varchar(20)                         null comment '入职日期',
    phone       varchar(15)                         null comment '手机号'
)
    comment '教师表';

create index idx_name
    on teacher (name);

create table teacher_course
(
    id          int auto_increment comment '主键ID'
        primary key,
    teacher_id  char(5)                             not null comment '教师编号',
    course_id   char(7)                             not null comment '课程编号',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_teacher_course
        unique (teacher_id, course_id)
)
    comment '教师课程关联表';

create index idx_course_id
    on teacher_course (course_id);

create index idx_teacher_id
    on teacher_course (teacher_id);

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

