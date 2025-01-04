-- 创建数据库
DROP DATABASE IF EXISTS school_association;
CREATE DATABASE school_association;
USE school_association;

-- 用户表（社长和指导老师）
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    real_name VARCHAR(100) NOT NULL COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '联系电话',
    role ENUM('club_president', 'admin') NOT NULL COMMENT '用户角色',
    club_id INT COMMENT '所属社团ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_login DATETIME COMMENT '最后登录时间'
);

-- 社团信息表
CREATE TABLE clubs (
    club_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '社团唯一标识',
    club_name VARCHAR(100) NOT NULL COMMENT '社团名称',
    description TEXT COMMENT '社团简介',
    creation_date DATE NOT NULL COMMENT '成立日期',
    president_id INT COMMENT '社长ID',
    unit VARCHAR(100) COMMENT '指导单位',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT '社团状态'
);

-- 社团成员表
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '成员唯一标识',
    club_id INT NOT NULL COMMENT '所属社团ID',
    name VARCHAR(100) NOT NULL COMMENT '成员姓名',
    student_id VARCHAR(50) NOT NULL COMMENT '学号',
    role ENUM('社长', '副社长', '普通成员') DEFAULT '普通成员' COMMENT '成员角色',
    join_date DATE NOT NULL COMMENT '加入日期',
    contact_info VARCHAR(255) COMMENT '联系方式',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT '状态'
);

-- 活动信息表
CREATE TABLE activities (
    activity_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '活动唯一标识',
    club_id INT NOT NULL COMMENT '所属社团ID',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    description TEXT COMMENT '活动描述',
    start_date DATETIME NOT NULL COMMENT '活动开始时间',
    end_date DATETIME COMMENT '活动结束时间',
    location VARCHAR(255) COMMENT '活动地点',
    max_participants INT COMMENT '最大参与人数',
    status ENUM('draft', 'pending', 'approved', 'ongoing', 'completed', 'cancelled') DEFAULT 'draft' COMMENT '活动状态',
    created_by INT NOT NULL COMMENT '创建者ID'
);

-- 活动审核表
CREATE TABLE activity_approvals (
    approval_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '审核唯一标识',
    activity_id INT NOT NULL COMMENT '活动ID',
    submitted_by INT NOT NULL COMMENT '提交人ID',
    approved_by INT COMMENT '审核人ID',
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending' COMMENT '审核状态',
    comments TEXT COMMENT '审核意见',
    submission_date DATETIME NOT NULL COMMENT '提交日期',
    approval_date DATETIME COMMENT '审核日期'
);

-- 财务记录表
CREATE TABLE finances (
    record_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '财务记录唯一标识',
    club_id INT NOT NULL COMMENT '所属社团ID',
    amount DECIMAL(10, 2) NOT NULL COMMENT '金额',
    type ENUM('income', 'expense') NOT NULL COMMENT '类型',
    description TEXT COMMENT '描述',
    transaction_date DATETIME NOT NULL COMMENT '交易日期',
    recorded_by INT NOT NULL COMMENT '记录人ID'
);

-- 社团荣誉表
CREATE TABLE club_honors (
    honor_id INT PRIMARY KEY AUTO_INCREMENT,
    honor_name VARCHAR(100) NOT NULL COMMENT '荣誉名称',
    club_id INT NOT NULL COMMENT '获奖社团ID',
    honor_level ENUM('SCHOOL', 'CITY', 'PROVINCE', 'NATIONAL') NOT NULL COMMENT '荣誉级别：校级、市级、省级、国家级',
    award_time DATETIME NOT NULL COMMENT '获奖时间',
    issuing_authority VARCHAR(100) NOT NULL COMMENT '颁发单位',
    description TEXT COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (club_id) REFERENCES clubs(club_id)
) ;
