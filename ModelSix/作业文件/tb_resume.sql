/*
Navicat MySQL Data Transfer

Source Server         : 内网
Source Server Version : 50723
Source Host           : 60.1.1.11:3306
Source Database       : lxr_test

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2020-05-14 20:04:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_resume
-- ----------------------------
DROP TABLE IF EXISTS `tb_resume`;
CREATE TABLE `tb_resume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resume
-- ----------------------------
INSERT INTO `tb_resume` VALUES ('1', '黑龙江哈尔滨', '张三', '13100000011');
INSERT INTO `tb_resume` VALUES ('2', '上海', '李四', '151000000');
INSERT INTO `tb_resume` VALUES ('3', '广州', '王五', '153000000');
INSERT INTO `tb_resume` VALUES ('8', '辽宁', '吕六', '1390452');
