# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20050
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 5.6.39)
# 数据库: group_buy_market
# 生成时间: 2024-12-07 03:48:52 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `group_buy_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `group_buy_market`;

# 转储表 group_buy_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_activity`;

CREATE TABLE `group_buy_activity` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增',
  `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
  `activity_name` varchar(128) NOT NULL COMMENT '活动名称',
  `source` varchar(8) NOT NULL COMMENT '来源',
  `channel` varchar(8) NOT NULL COMMENT '渠道',
  `goods_id` varchar(12) NOT NULL COMMENT '商品ID',
  `discount_id` varchar(8) NOT NULL COMMENT '折扣ID',
  `group_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '拼团方式（0自动成团、1达成目标拼团）',
  `take_limit_count` int(4) NOT NULL DEFAULT '1' COMMENT '拼团次数限制',
  `target` int(5) NOT NULL DEFAULT '1' COMMENT '拼团目标',
  `valid_time` int(4) NOT NULL DEFAULT '15' COMMENT '拼团时长（分钟）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '活动状态（0创建、1生效、2过期、3废弃）',
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动开始时间',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动结束时间',
  `tag_id` varchar(8) DEFAULT NULL COMMENT '人群标签规则标识',
  `tag_scope` varchar(4) DEFAULT NULL COMMENT '人群标签规则范围（多选；1可见限制、2参与限制）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团活动';

LOCK TABLES `group_buy_activity` WRITE;
/*!40000 ALTER TABLE `group_buy_activity` DISABLE KEYS */;

INSERT INTO `group_buy_activity` (`id`, `activity_id`, `activity_name`, `source`, `channel`, `goods_id`, `discount_id`, `group_type`, `take_limit_count`, `target`, `valid_time`, `status`, `start_time`, `end_time`, `tag_id`, `tag_scope`, `create_time`, `update_time`)
VALUES
	(1,100123,'测试活动','s01','c01','9890001','25120207',0,1,1,15,0,'2024-12-07 10:19:40','2024-12-07 10:19:40','1','1','2024-12-07 10:19:40','2024-12-07 11:47:27');

/*!40000 ALTER TABLE `group_buy_activity` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_discount
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_discount`;

CREATE TABLE `group_buy_discount` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `discount_id` int(8) NOT NULL COMMENT '折扣ID',
  `discount_name` varchar(64) NOT NULL COMMENT '折扣标题',
  `discount_desc` varchar(256) NOT NULL COMMENT '折扣描述',
  `discount_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '折扣类型（0:base、1:tag）',
  `market_plan` varchar(4) NOT NULL DEFAULT 'ZJ' COMMENT '营销优惠计划（ZJ:直减、MJ:满减、N元购）',
  `market_expr` varchar(32) NOT NULL COMMENT '营销优惠表达式',
  `tag_id` varchar(8) DEFAULT NULL COMMENT '人群标签，特定优惠限定',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_discount_id` (`discount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `group_buy_discount` WRITE;
/*!40000 ALTER TABLE `group_buy_discount` DISABLE KEYS */;

INSERT INTO `group_buy_discount` (`id`, `discount_id`, `discount_name`, `discount_desc`, `discount_type`, `market_plan`, `market_expr`, `tag_id`, `create_time`, `update_time`)
VALUES
	(1,25120207,'测试优惠','测试优惠',0,'ZJ','20',NULL,'2024-12-07 10:20:15','2024-12-07 10:20:15');

/*!40000 ALTER TABLE `group_buy_discount` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
