/*
Navicat MySQL Data Transfer

Source Server         : 123
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : dams

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-06-23 17:58:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(20) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(40) DEFAULT NULL COMMENT '加密密码',
  `PASSWORD_VALUE` varchar(40) DEFAULT NULL COMMENT '原密码',
  `ROLE` varchar(10) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', '5efd2fa838f4f8e09780e622e8b42b31', 'admin', 'admin');
INSERT INTO `admin` VALUES ('29', 'user', 'a8f20301b6e57cabed952967607e5350', 'user', 'user');
INSERT INTO `admin` VALUES ('36', '1', 'c92684923ae2247e6ddfdc50b95ed355', '1', null);
INSERT INTO `admin` VALUES ('37', '2', 'a51b84371c9050b34b74c0d2a66eb0f5', '2', null);
INSERT INTO `admin` VALUES ('38', '3', '89cc895594a211b2ba246a20f69ff4f2', '3', null);

-- ----------------------------
-- Table structure for `dormitory`
-- ----------------------------
DROP TABLE IF EXISTS `dormitory`;
CREATE TABLE `dormitory` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `DORMITORY_BUILDING` varchar(10) DEFAULT NULL COMMENT '宿舍楼号',
  `DORMITORY_SEX` varchar(10) DEFAULT NULL COMMENT '男/女宿舍',
  `DORMITORY_NUMBER` varchar(10) DEFAULT NULL COMMENT '宿舍门牌号',
  `DORMITORY_CAPACITY` varchar(10) DEFAULT NULL COMMENT '宿舍可容纳学生人数',
  `DORMITORY_STUDENTS` varchar(40) DEFAULT NULL COMMENT '宿舍学生',
  `IS_FULL` varchar(10) DEFAULT '0' COMMENT '是否满员',
  `REMAIN_NUMBER` varchar(10) DEFAULT NULL COMMENT '剩余床位数',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dormitory
-- ----------------------------
INSERT INTO `dormitory` VALUES ('1', '1', '男', '100', '4', 'a,b,c,d', '1', '0');
INSERT INTO `dormitory` VALUES ('2', '1', '男', '101', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('3', '1', '男', '102', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('4', '1', '男', '103', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('5', '1', '男', '104', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('6', '2', '女', '100', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('7', '2', '女', '101', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('8', '2', '女', '102', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('9', '2', '女', '103', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('10', '3', '女', '100', '4', '', '1', '0');
INSERT INTO `dormitory` VALUES ('11', '3', '女', '101', '4', '9,10', '1', '0');
INSERT INTO `dormitory` VALUES ('12', '3', '女', '102', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('13', '3', '女', '103', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('14', '3', '女', '104', '4', null, '0', '4');
INSERT INTO `dormitory` VALUES ('15', '4', '男', '100', '4', '', '1', '0');
INSERT INTO `dormitory` VALUES ('16', '4', '男', '101', '4', '', '1', '0');
INSERT INTO `dormitory` VALUES ('17', '4', '男', '102', '4', '', '1', '0');
INSERT INTO `dormitory` VALUES ('18', '4', '男', '103', '4', '', '1', '0');
INSERT INTO `dormitory` VALUES ('19', '4', '男', '104', '4', null, '0', '4');

-- ----------------------------
-- Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `QUESTION_ID` varchar(20) DEFAULT NULL COMMENT '问卷ID',
  `QUESTION_NUMBER` varchar(10) DEFAULT NULL COMMENT '题目序号',
  `QUESTION_TITLE` varchar(100) DEFAULT NULL COMMENT '题目标题',
  `IS_MORE_SELECT` varchar(10) DEFAULT '1' COMMENT '是否多选（1，单选；2，多选）',
  `ANSWERS` varchar(400) DEFAULT NULL COMMENT '题目选项',
  `IS_RELEASE` varchar(10) DEFAULT '0' COMMENT '发布状态（0，未发布；1发布中）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES ('1', null, null, null, '1', null, '0');
INSERT INTO `question` VALUES ('2', null, null, null, '1', null, '0');
INSERT INTO `question` VALUES ('3', null, null, null, '1', null, '0');
INSERT INTO `question` VALUES ('4', null, null, null, '1', null, '0');
INSERT INTO `question` VALUES ('5', '20200523232704', '1', '1', '1', '1,1,1', '0');
INSERT INTO `question` VALUES ('6', '20200523232803', '1', '2', '2', '2,2,', '0');
INSERT INTO `question` VALUES ('7', '20200523235012', '1', '1', '1', '1,1', '0');
INSERT INTO `question` VALUES ('8', '20200523235012', '2', '2', '2', '2,2', '0');
INSERT INTO `question` VALUES ('9', '20200524105118', '1', '1', '1', '1,1', '0');
INSERT INTO `question` VALUES ('10', '20200524105118', '2', '2', '1', '222,222', '0');
INSERT INTO `question` VALUES ('11', '20200524151153', '1', '生活习惯', '2', '喜欢在宿舍打牌,喜欢打游戏,热爱运动,经常去图书馆学习', '0');
INSERT INTO `question` VALUES ('12', '20200524151153', '2', '个人性格', '1', '偏内向,偏外向', '0');
INSERT INTO `question` VALUES ('13', '20200524151153', '3', '生源地', '1', '省内,省外', '0');
INSERT INTO `question` VALUES ('14', '20200524151153', '4', '作息习惯', '2', '12点后睡,打呼，说梦话,吸烟,正常', '0');
INSERT INTO `question` VALUES ('15', '20200524151153', '5', '是否曾住校', '1', '住过,没住过', '0');
INSERT INTO `question` VALUES ('16', '20200531001550', '1', '1', '1', '1,1,1,1', '0');
INSERT INTO `question` VALUES ('17', '20200531001550', '2', '2', '2', '2,2,2,2', '0');
INSERT INTO `question` VALUES ('18', '20200531001550', '3', '3', '1', '3,3', '0');
INSERT INTO `question` VALUES ('19', '20200531001550', '4', '4', '2', '4,4', '0');
INSERT INTO `question` VALUES ('20', '20200531001550', '5', '5', '1', '5,5,5', '0');
INSERT INTO `question` VALUES ('21', '20200531001550', '6', '6', '2', '6,6,6', '0');
INSERT INTO `question` VALUES ('22', '20200531001550', '7', '7', '1', '77,7,7,7', '0');
INSERT INTO `question` VALUES ('23', '20200531001550', '8', '8', '2', '8,8,8,8', '0');
INSERT INTO `question` VALUES ('24', '20200606013006', '1', '平常喜欢怎样运动', '2', '打篮球,踢足球,跑步,去健身房', '1');
INSERT INTO `question` VALUES ('25', '20200606013006', '2', '课余时间的最喜欢的娱乐方式？', '1', '看动漫,看电影/追剧,打游戏,其他', '1');
INSERT INTO `question` VALUES ('26', '20200606013006', '3', '最喜欢在哪里学习？', '1', '寝室,教室,图书馆,随意，都可以', '1');
INSERT INTO `question` VALUES ('27', '20200606013006', '4', '晚上作息时间', '1', '10点左右入睡,11点左右入睡,12点左右入睡,1点后入睡', '1');
INSERT INTO `question` VALUES ('28', '20200606013006', '5', '是否吸烟/喝酒？', '1', '是,否', '1');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `STU_NUMBER` varchar(20) DEFAULT NULL COMMENT '学号',
  `STU_NAME` varchar(20) DEFAULT NULL COMMENT '姓名',
  `STU_SEX` varchar(10) DEFAULT NULL COMMENT '性别',
  `PROFESSION_CODE` varchar(20) DEFAULT NULL COMMENT '专业编码',
  `PROFESSION_NAME` varchar(20) DEFAULT NULL COMMENT '专业名称',
  `CLASS_NAME` varchar(20) DEFAULT NULL COMMENT '班级',
  `PASSWORD` varchar(20) DEFAULT NULL COMMENT '密码',
  `QUESTION_ANSWERS` varchar(40) DEFAULT NULL COMMENT '问卷答案',
  `STU_DORMITORY` varchar(10) DEFAULT NULL COMMENT '宿舍',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '541613260101', '曹万剑', '男', '10001', '软件开发', '1601', '541613260101', null, null);
INSERT INTO `student` VALUES ('2', '541613260102', '程湘潭', '男', '10001', '软件开发', '1601', '541613260102', null, null);
INSERT INTO `student` VALUES ('3', '541613260103', '楚瑞博', '男', '10001', '软件开发', '1601', '541613260103', null, null);
INSERT INTO `student` VALUES ('4', '541613260104', '高杏', '女', '10001', '软件开发', '1601', '541613260104', null, null);
INSERT INTO `student` VALUES ('5', '541613260105', '侯泽翔', '男', '10001', '软件开发', '1601', '541613260105', null, null);
INSERT INTO `student` VALUES ('6', '541613260106', '胡立强', '男', '10001', '软件开发', '1601', '541613260106', null, null);
INSERT INTO `student` VALUES ('7', '541613260107', '李冰', '男', '10001', '软件开发', '1601', '541613260107', null, null);
INSERT INTO `student` VALUES ('8', '541613260108', '江仟峰', '男', '10001', '软件开发', '1601', '541613260108', null, null);
INSERT INTO `student` VALUES ('9', '541613260109', '李丹丹', '女', '10001', '软件开发', '1601', '541613260109', null, null);
INSERT INTO `student` VALUES ('10', '541613260110', null, null, '10001', '软件开发', '1601', '541613260110', null, null);
INSERT INTO `student` VALUES ('11', '541613260111', null, null, '10001', '软件开发', '1601', '541613260111', null, null);
INSERT INTO `student` VALUES ('12', '541613260112', null, null, '10001', '软件开发', '1601', '541613260112', null, null);
INSERT INTO `student` VALUES ('13', '541613260113', null, null, '10001', '软件开发', '1601', '541613260113', null, null);
INSERT INTO `student` VALUES ('14', '541613260114', null, null, '10001', '软件开发', '1601', '541613260114', null, null);
INSERT INTO `student` VALUES ('15', '541613260115', null, null, '10001', '软件开发', '1601', '541613260115', null, null);
INSERT INTO `student` VALUES ('16', '20200101001', '张三', '男', '10002', '测试专业1', '2001', '20200101001', null, null);
INSERT INTO `student` VALUES ('17', '20200101002', '李四', '男', '10002', '测试专业1', '2001', '20200101002', null, null);
INSERT INTO `student` VALUES ('18', '20200101003', '王五', '女', '10002', '测试专业1', '2001', '20200101003', null, null);
INSERT INTO `student` VALUES ('19', '20200101004', '刘六', '男', '10002', '测试专业1', '2001', '20200101004', null, null);
INSERT INTO `student` VALUES ('20', '20200102001', '张三', '女', '10003', '测试专业2', '2001', '20200102001', null, null);
INSERT INTO `student` VALUES ('21', '20200102002', '李四', '女', '10003', '测试专业2', '2001', '20200102002', null, null);
INSERT INTO `student` VALUES ('22', '20200102003', '王五', '男', '10003', '测试专业2', '2001', '20200102003', null, null);
INSERT INTO `student` VALUES ('23', '20200102004', '刘六', '女', '10003', '测试专业2', '2001', '20200102004', null, null);
INSERT INTO `student` VALUES ('24', '0', '测试', '男', '10000', '测试专业0', '2020', '0', '', null);
INSERT INTO `student` VALUES ('25', '101', '测试2', '男', '10000', '测试专业0', '2020', '101', null, null);
INSERT INTO `student` VALUES ('26', '1', '男1', '男', '10004', '测试专业3', '2020', '1', 'A,B,D;B;D;C;B', '');
INSERT INTO `student` VALUES ('27', '2', '男2', '男', '10004', '测试专业3', '2020', '2', 'D;C;A;B;B', '');
INSERT INTO `student` VALUES ('28', '3', '男3', '男', '10004', '测试专业3', '2020', '3', 'C;C;A;B;A', '');
INSERT INTO `student` VALUES ('29', '4', '男4', '男', '10004', '测试专业3', '2020', '4', 'A,B,C,D;D;C;C;B', '');
INSERT INTO `student` VALUES ('30', '5', '男5', '男', '10004', '测试专业3', '2020', '5', 'A;C;D;A;A', '');
INSERT INTO `student` VALUES ('31', '6', '男6', '男', '10004', '测试专业3', '2020', '6', 'A,C,D;B;C;C;A', '');
INSERT INTO `student` VALUES ('32', '7', '男7', '男', '10004', '测试专业3', '2020', '7', 'B;A;A;B;A', '');
INSERT INTO `student` VALUES ('33', '8', '男8', '男', '10004', '测试专业3', '2020', '8', 'B,C,D;B;C;D;B', '');
INSERT INTO `student` VALUES ('34', '9', '女1', '女', '10004', '测试专业3', '2020', '9', 'D;A;B;A;B', '3#101');
INSERT INTO `student` VALUES ('35', '10', '女2', '女', '10004', '测试专业3', '2020', '10', 'C;B;C;B;B', '3#101');
INSERT INTO `student` VALUES ('36', '20200100501', '张三', '男', '10005', '测试专业5', '2020', '20200100501', null, null);
INSERT INTO `student` VALUES ('37', '20200100502', '李四', '男', '10005', '测试专业5', '2020', '20200100502', null, null);
INSERT INTO `student` VALUES ('38', '20200100503', '王五', '女', '10005', '测试专业5', '2020', '20200100503', null, null);
INSERT INTO `student` VALUES ('39', '20200100504', '刘六', '男', '10005', '测试专业5', '2020', '20200100504', null, null);
INSERT INTO `student` VALUES ('40', '20200100601', '张三', '女', '10006', '测试专业5', '2020', '20200100601', null, null);
INSERT INTO `student` VALUES ('41', '20200100602', '李四', '女', '10006', '测试专业5', '2020', '20200100602', null, null);
INSERT INTO `student` VALUES ('42', '20200100603', '王五', '男', '10006', '测试专业5', '2020', '20200100603', null, null);
INSERT INTO `student` VALUES ('43', '20200100604', '刘六', '女', '10006', '测试专业5', '2020', '20200100604', null, null);
