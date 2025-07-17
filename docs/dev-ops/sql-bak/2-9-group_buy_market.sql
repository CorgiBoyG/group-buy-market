# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20050
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 5.6.39)
# 数据库: group_buy_market
# 生成时间: 2025-01-11 23:40:03 +0000
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

# 转储表 crowd_tags
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crowd_tags`;

CREATE TABLE `crowd_tags` (
                              `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                              `tag_id` varchar(32) NOT NULL COMMENT '人群ID',
                              `tag_name` varchar(64) NOT NULL COMMENT '人群名称',
                              `tag_desc` varchar(256) NOT NULL COMMENT '人群描述',
                              `statistics` int(8) NOT NULL COMMENT '人群标签统计量',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uq_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人群标签';

LOCK TABLES `crowd_tags` WRITE;
/*!40000 ALTER TABLE `crowd_tags` DISABLE KEYS */;

INSERT INTO `crowd_tags` (`id`, `tag_id`, `tag_name`, `tag_desc`, `statistics`, `create_time`, `update_time`)
VALUES
    (1,'RQ_KJHKL98UU78H66554GFDV','潜在消费用户','潜在消费用户',6,'2024-12-28 12:53:28','2024-12-28 16:58:45');

/*!40000 ALTER TABLE `crowd_tags` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 crowd_tags_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crowd_tags_detail`;

CREATE TABLE `crowd_tags_detail` (
                                     `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                     `tag_id` varchar(32) NOT NULL COMMENT '人群ID',
                                     `user_id` varchar(16) NOT NULL COMMENT '用户ID',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `uq_tag_user` (`tag_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人群标签明细';

LOCK TABLES `crowd_tags_detail` WRITE;
/*!40000 ALTER TABLE `crowd_tags_detail` DISABLE KEYS */;

INSERT INTO `crowd_tags_detail` (`id`, `tag_id`, `user_id`, `create_time`, `update_time`)
VALUES
    (4,'RQ_KJHKL98UU78H66554GFDV','xiaofuge','2024-12-28 14:42:30','2024-12-28 14:42:30'),
    (5,'RQ_KJHKL98UU78H66554GFDV','liergou','2024-12-28 14:42:30','2024-12-28 14:42:30');

/*!40000 ALTER TABLE `crowd_tags_detail` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 crowd_tags_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crowd_tags_job`;

CREATE TABLE `crowd_tags_job` (
                                  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                  `tag_id` varchar(32) NOT NULL COMMENT '标签ID',
                                  `batch_id` varchar(8) NOT NULL COMMENT '批次ID',
                                  `tag_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '标签类型（参与量、消费金额）',
                                  `tag_rule` varchar(8) NOT NULL COMMENT '标签规则（限定类型 N次）',
                                  `stat_start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计数据，开始时间',
                                  `stat_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计数据，结束时间',
                                  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态；0初始、1计划（进入执行阶段）、2重置、3完成',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uq_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人群标签任务';

LOCK TABLES `crowd_tags_job` WRITE;
/*!40000 ALTER TABLE `crowd_tags_job` DISABLE KEYS */;

INSERT INTO `crowd_tags_job` (`id`, `tag_id`, `batch_id`, `tag_type`, `tag_rule`, `stat_start_time`, `stat_end_time`, `status`, `create_time`, `update_time`)
VALUES
    (1,'RQ_KJHKL98UU78H66554GFDV','10001',0,'100','2024-12-28 12:55:05','2024-12-28 12:55:05',0,'2024-12-28 12:55:05','2024-12-28 12:55:05');

/*!40000 ALTER TABLE `crowd_tags_job` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_activity`;

CREATE TABLE `group_buy_activity` (
                                      `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增',
                                      `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                                      `activity_name` varchar(128) NOT NULL COMMENT '活动名称',
                                      `discount_id` varchar(8) NOT NULL COMMENT '折扣ID',
                                      `group_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '拼团方式（0自动成团、1达成目标拼团）',
                                      `take_limit_count` int(4) NOT NULL DEFAULT '1' COMMENT '拼团次数限制',
                                      `target` int(5) NOT NULL DEFAULT '1' COMMENT '拼团目标',
                                      `valid_time` int(4) NOT NULL DEFAULT '15' COMMENT '拼团时长（分钟）',
                                      `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '活动状态（0创建、1生效、2过期、3废弃）',
                                      `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动开始时间',
                                      `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动结束时间',
                                      `tag_id` varchar(32) DEFAULT NULL COMMENT '人群标签规则标识',
                                      `tag_scope` varchar(4) DEFAULT NULL COMMENT '人群标签规则范围（多选；1可见限制、2参与限制）',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uq_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团活动';

LOCK TABLES `group_buy_activity` WRITE;
/*!40000 ALTER TABLE `group_buy_activity` DISABLE KEYS */;

INSERT INTO `group_buy_activity` (`id`, `activity_id`, `activity_name`, `discount_id`, `group_type`, `take_limit_count`, `target`, `valid_time`, `status`, `start_time`, `end_time`, `tag_id`, `tag_scope`, `create_time`, `update_time`)
VALUES
    (1,100123,'测试活动','25120208',0,1,3,15,1,'2024-12-07 10:19:40','2024-12-07 10:19:40','RQ_KJHKL98UU78H66554GFDV','1,2','2024-12-07 10:19:40','2025-01-11 15:08:42');

/*!40000 ALTER TABLE `group_buy_activity` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_discount
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_discount`;

CREATE TABLE `group_buy_discount` (
                                      `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                      `discount_id` varchar(8) NOT NULL COMMENT '折扣ID',
                                      `discount_name` varchar(64) NOT NULL COMMENT '折扣标题',
                                      `discount_desc` varchar(256) NOT NULL COMMENT '折扣描述',
                                      `discount_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '折扣类型（0:base、1:tag）',
                                      `market_plan` varchar(4) NOT NULL DEFAULT 'ZJ' COMMENT '营销优惠计划（ZJ:直减、MJ:满减、ZK:折扣、N元购）',
                                      `market_expr` varchar(32) NOT NULL COMMENT '营销优惠表达式',
                                      `tag_id` varchar(32) DEFAULT NULL COMMENT '人群标签，特定优惠限定',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uq_discount_id` (`discount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `group_buy_discount` WRITE;
/*!40000 ALTER TABLE `group_buy_discount` DISABLE KEYS */;

INSERT INTO `group_buy_discount` (`id`, `discount_id`, `discount_name`, `discount_desc`, `discount_type`, `market_plan`, `market_expr`, `tag_id`, `create_time`, `update_time`)
VALUES
    (1,'25120207','直减优惠20元','直减优惠20元',0,'ZJ','20',NULL,'2024-12-07 10:20:15','2024-12-22 12:09:45'),
    (2,'25120208','满减优惠100-10元','满减优惠100-10元',0,'MJ','100,10',NULL,'2024-12-07 10:20:15','2024-12-22 12:09:47'),
    (4,'25120209','折扣优惠8折','折扣优惠8折',0,'ZK','0.8',NULL,'2024-12-07 10:20:15','2024-12-22 12:11:36'),
    (5,'25120210','N元购买优惠','N元购买优惠',0,'N','1.99',NULL,'2024-12-07 10:20:15','2024-12-22 12:11:39'),
    (22,'25120288','每满减优惠100-10元','每满减优惠100-10元',0,'MMJ','100,20,-1',NULL,'2024-12-07 10:20:15','2024-12-22 12:12:39');

/*!40000 ALTER TABLE `group_buy_discount` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_order`;

CREATE TABLE `group_buy_order` (
                                   `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                                   `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                                   `source` varchar(8) NOT NULL COMMENT '渠道',
                                   `channel` varchar(8) NOT NULL COMMENT '来源',
                                   `original_price` decimal(8,2) NOT NULL COMMENT '原始价格',
                                   `deduction_price` decimal(8,2) NOT NULL COMMENT '折扣金额',
                                   `pay_price` decimal(8,2) NOT NULL COMMENT '支付价格',
                                   `target_count` int(5) NOT NULL COMMENT '目标数量',
                                   `complete_count` int(5) NOT NULL COMMENT '完成数量',
                                   `lock_count` int(5) NOT NULL COMMENT '锁单数量',
                                   `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0-拼单中、1-完成、2-失败）',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `group_buy_order` WRITE;
/*!40000 ALTER TABLE `group_buy_order` DISABLE KEYS */;

INSERT INTO `group_buy_order` (`id`, `team_id`, `activity_id`, `source`, `channel`, `original_price`, `deduction_price`, `pay_price`, `target_count`, `complete_count`, `lock_count`, `status`, `create_time`, `update_time`)
VALUES
    (7,'64675056',100123,'s01','c01',100.00,90.00,0.00,3,3,0,0,'2025-01-11 15:13:08','2025-01-11 15:13:47'),
    (8,'31724477',100123,'s01','c01',100.00,90.00,0.00,3,0,3,0,'2025-01-11 16:02:23','2025-01-11 16:06:49'),
    (9,'33063446',100123,'s01','c01',100.00,90.00,0.00,3,0,3,0,'2025-01-11 18:32:17','2025-01-11 18:33:35'),
    (10,'RNPZQnlL',100123,'s01','c01',100.00,90.00,0.00,3,0,1,0,'2025-01-11 19:15:54','2025-01-11 19:15:54'),
    (11,'tQvVMQWS',100123,'s01','c01',100.00,90.00,0.00,3,0,1,0,'2025-01-11 19:16:30','2025-01-11 19:16:30'),
    (12,'64451200',100123,'s01','c01',100.00,90.00,0.00,3,0,2,0,'2025-01-11 19:18:01','2025-01-11 19:18:38');

/*!40000 ALTER TABLE `group_buy_order` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_order_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_order_list`;

CREATE TABLE `group_buy_order_list` (
                                        `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                        `user_id` varchar(64) NOT NULL COMMENT '用户ID',
                                        `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                                        `order_id` varchar(12) NOT NULL COMMENT '订单ID',
                                        `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                                        `start_time` datetime NOT NULL COMMENT '活动开始时间',
                                        `end_time` datetime NOT NULL COMMENT '活动结束时间',
                                        `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                                        `source` varchar(8) NOT NULL COMMENT '渠道',
                                        `channel` varchar(8) NOT NULL COMMENT '来源',
                                        `original_price` decimal(8,2) NOT NULL COMMENT '原始价格',
                                        `deduction_price` decimal(8,2) NOT NULL COMMENT '折扣金额',
                                        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态；0初始锁定、1消费完成',
                                        `out_trade_no` varchar(12) NOT NULL COMMENT '外部交易单号-确保外部调用唯一幂等',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uq_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `group_buy_order_list` WRITE;
/*!40000 ALTER TABLE `group_buy_order_list` DISABLE KEYS */;

INSERT INTO `group_buy_order_list` (`id`, `user_id`, `team_id`, `order_id`, `activity_id`, `start_time`, `end_time`, `goods_id`, `source`, `channel`, `original_price`, `deduction_price`, `status`, `out_trade_no`, `create_time`, `update_time`)
VALUES
    (9,'xiaofuge','64675056','394859646148',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'314870877261','2025-01-11 15:13:08','2025-01-11 15:13:08'),
    (10,'xiaofuge','64675056','141036625362',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'485524644458','2025-01-11 15:13:35','2025-01-11 15:13:35'),
    (11,'xiaofuge','64675056','686664095344',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'942065764153','2025-01-11 15:13:47','2025-01-11 15:13:47'),
    (12,'xiaofuge','31724477','755647827138',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'782446346690','2025-01-11 16:02:23','2025-01-11 16:02:23'),
    (13,'xiaofuge','31724477','356055747145',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'648016222817','2025-01-11 16:06:27','2025-01-11 16:06:27'),
    (14,'xiaofuge','31724477','831212748893',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'494004780611','2025-01-11 16:06:49','2025-01-11 16:06:49'),
    (15,'xiaofuge','33063446','391398265383',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'285608029258','2025-01-11 18:32:18','2025-01-11 18:32:18'),
    (16,'xiaofuge','33063446','479801370153',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'561239558595','2025-01-11 18:33:13','2025-01-11 18:33:13'),
    (17,'xiaofuge','33063446','371204389323',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'987071087883','2025-01-11 18:33:35','2025-01-11 18:33:35'),
    (18,'xiaofuge','RNPZQnlL','934890879756',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'628931704712','2025-01-11 19:15:54','2025-01-11 19:15:54'),
    (19,'xiaofuge','tQvVMQWS','321326266333',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'445825049409','2025-01-11 19:16:30','2025-01-11 19:16:30'),
    (20,'xiaofuge','64451200','028993411110',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'911193728147','2025-01-11 19:18:01','2025-01-11 19:18:01'),
    (21,'xiaofuge','64451200','950295523573',100123,'2024-12-07 10:19:40','2024-12-07 10:19:40','9890001','s01','c01',90.00,90.00,0,'054537636261','2025-01-11 19:18:38','2025-01-11 19:18:38');

/*!40000 ALTER TABLE `group_buy_order_list` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sc_sku_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sc_sku_activity`;

CREATE TABLE `sc_sku_activity` (
                                   `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `source` varchar(8) NOT NULL COMMENT '渠道',
                                   `channel` varchar(8) NOT NULL COMMENT '来源',
                                   `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                                   `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uq_sc_goodsid` (`source`,`channel`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道商品活动配置关联表';

LOCK TABLES `sc_sku_activity` WRITE;
/*!40000 ALTER TABLE `sc_sku_activity` DISABLE KEYS */;

INSERT INTO `sc_sku_activity` (`id`, `source`, `channel`, `activity_id`, `goods_id`, `create_time`, `update_time`)
VALUES
    (1,'s01','c01',100123,'9890001','2025-01-01 13:15:54','2025-01-01 13:15:54');

/*!40000 ALTER TABLE `sc_sku_activity` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sku
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sku`;

CREATE TABLE `sku` (
                       `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                       `source` varchar(8) NOT NULL COMMENT '渠道',
                       `channel` varchar(8) NOT NULL COMMENT '来源',
                       `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                       `goods_name` varchar(128) NOT NULL COMMENT '商品名称',
                       `original_price` decimal(10,2) NOT NULL COMMENT '商品价格',
                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `uq_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息';

LOCK TABLES `sku` WRITE;
/*!40000 ALTER TABLE `sku` DISABLE KEYS */;

INSERT INTO `sku` (`id`, `source`, `channel`, `goods_id`, `goods_name`, `original_price`, `create_time`, `update_time`)
VALUES
    (1,'s01','c01','9890001','《手写MyBatis：渐进式源码实践》',100.00,'2024-12-21 11:10:06','2024-12-21 11:10:06');

/*!40000 ALTER TABLE `sku` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
