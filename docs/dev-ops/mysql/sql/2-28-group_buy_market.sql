# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20094
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 8.0.42)
# 数据库: group_buy_market
# 生成时间: 2025-07-26 02:38:20 +0000
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
                              `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                              `tag_id` varchar(32) NOT NULL COMMENT '人群ID',
                              `tag_name` varchar(64) NOT NULL COMMENT '人群名称',
                              `tag_desc` varchar(256) NOT NULL COMMENT '人群描述',
                              `statistics` int NOT NULL COMMENT '人群标签统计量',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uq_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='人群标签';

LOCK TABLES `crowd_tags` WRITE;
/*!40000 ALTER TABLE `crowd_tags` DISABLE KEYS */;

INSERT INTO `crowd_tags` (`id`, `tag_id`, `tag_name`, `tag_desc`, `statistics`, `create_time`, `update_time`)
VALUES
    (1,'RQ_KJHKL98UU78H66554GFDV','潜在消费用户','潜在消费用户',33,'2024-12-28 12:53:28','2025-01-28 08:23:57');

/*!40000 ALTER TABLE `crowd_tags` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 crowd_tags_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crowd_tags_detail`;

CREATE TABLE `crowd_tags_detail` (
                                     `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                     `tag_id` varchar(32) NOT NULL COMMENT '人群ID',
                                     `user_id` varchar(16) NOT NULL COMMENT '用户ID',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `uq_tag_user` (`tag_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='人群标签明细';

LOCK TABLES `crowd_tags_detail` WRITE;
/*!40000 ALTER TABLE `crowd_tags_detail` DISABLE KEYS */;

INSERT INTO `crowd_tags_detail` (`id`, `tag_id`, `user_id`, `create_time`, `update_time`)
VALUES
    (4,'RQ_KJHKL98UU78H66554GFDV','xiaofuge','2024-12-28 14:42:30','2024-12-28 14:42:30'),
    (5,'RQ_KJHKL98UU78H66554GFDV','liergou','2024-12-28 14:42:30','2024-12-28 14:42:30'),
    (9,'RQ_KJHKL98UU78H66554GFDV','xfg01','2025-01-25 15:44:55','2025-01-25 15:44:55'),
    (10,'RQ_KJHKL98UU78H66554GFDV','xfg02','2025-01-25 15:44:55','2025-01-25 15:44:55'),
    (11,'RQ_KJHKL98UU78H66554GFDV','xfg03','2025-01-25 15:44:55','2025-01-25 15:44:55'),
    (17,'RQ_KJHKL98UU78H66554GFDV','xfg04','2025-01-26 19:10:36','2025-01-26 19:10:36'),
    (18,'RQ_KJHKL98UU78H66554GFDV','xfg05','2025-01-26 19:10:36','2025-01-26 19:10:36'),
    (19,'RQ_KJHKL98UU78H66554GFDV','xfg06','2025-01-26 19:10:37','2025-01-26 19:10:37'),
    (20,'RQ_KJHKL98UU78H66554GFDV','xfg07','2025-01-26 19:10:37','2025-01-26 19:10:37'),
    (21,'RQ_KJHKL98UU78H66554GFDV','xfg08','2025-01-26 19:10:37','2025-01-26 19:10:37'),
    (22,'RQ_KJHKL98UU78H66554GFDV','xfg09','2025-01-26 19:10:37','2025-01-26 19:10:37');

/*!40000 ALTER TABLE `crowd_tags_detail` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 crowd_tags_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crowd_tags_job`;

CREATE TABLE `crowd_tags_job` (
                                  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='人群标签任务';

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
                                      `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增',
                                      `activity_id` bigint NOT NULL COMMENT '活动ID',
                                      `activity_name` varchar(128) NOT NULL COMMENT '活动名称',
                                      `discount_id` varchar(8) NOT NULL COMMENT '折扣ID',
                                      `group_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '拼团方式（0自动成团、1达成目标拼团）',
                                      `take_limit_count` int NOT NULL DEFAULT '1' COMMENT '拼团次数限制',
                                      `target` int NOT NULL DEFAULT '1' COMMENT '拼团目标',
                                      `valid_time` int NOT NULL DEFAULT '15' COMMENT '拼团时长（分钟）',
                                      `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '活动状态（0创建、1生效、2过期、3废弃）',
                                      `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动开始时间',
                                      `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动结束时间',
                                      `tag_id` varchar(8) DEFAULT NULL COMMENT '人群标签规则标识',
                                      `tag_scope` varchar(4) DEFAULT NULL COMMENT '人群标签规则范围（多选；1可见限制、2参与限制）',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uq_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='拼团活动';

LOCK TABLES `group_buy_activity` WRITE;
/*!40000 ALTER TABLE `group_buy_activity` DISABLE KEYS */;

INSERT INTO `group_buy_activity` (`id`, `activity_id`, `activity_name`, `discount_id`, `group_type`, `take_limit_count`, `target`, `valid_time`, `status`, `start_time`, `end_time`, `tag_id`, `tag_scope`, `create_time`, `update_time`)
VALUES
    (1,100123,'测试活动','25120207',0,1,3,15,1,'2024-12-07 10:19:40','2029-12-07 10:19:40','1','1','2024-12-07 10:19:40','2025-04-05 11:21:03');

/*!40000 ALTER TABLE `group_buy_activity` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_discount
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_discount`;

CREATE TABLE `group_buy_discount` (
                                      `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                      `discount_id` varchar(8) NOT NULL COMMENT '折扣ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `group_buy_discount` WRITE;
/*!40000 ALTER TABLE `group_buy_discount` DISABLE KEYS */;

INSERT INTO `group_buy_discount` (`id`, `discount_id`, `discount_name`, `discount_desc`, `discount_type`, `market_plan`, `market_expr`, `tag_id`, `create_time`, `update_time`)
VALUES
    (1,'25120207','测试优惠','测试优惠',0,'ZJ','20',NULL,'2024-12-07 10:20:15','2024-12-21 11:13:32'),
    (22, '25120288', '每满减优惠100-10元', '每满减优惠100-10元', 0, 'MMJ', '100,20,-1', NULL, '2024-12-07 10:20:15','2024-12-22 12:12:39');

/*!40000 ALTER TABLE `group_buy_discount` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_order`;

CREATE TABLE `group_buy_order` (
                                   `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                                   `activity_id` bigint NOT NULL COMMENT '活动ID',
                                   `source` varchar(8) NOT NULL COMMENT '渠道',
                                   `channel` varchar(8) NOT NULL COMMENT '来源',
                                   `original_price` decimal(8,2) NOT NULL COMMENT '原始价格',
                                   `deduction_price` decimal(8,2) NOT NULL COMMENT '折扣金额',
                                   `pay_price` decimal(8,2) NOT NULL COMMENT '支付价格',
                                   `target_count` int NOT NULL COMMENT '目标数量',
                                   `complete_count` int NOT NULL COMMENT '完成数量',
                                   `lock_count` int NOT NULL COMMENT '锁单数量',
                                   `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0-拼单中、1-完成、2-失败、3-完成-含退单）',
                                   `valid_start_time` datetime NOT NULL COMMENT '拼团开始时间',
                                   `valid_end_time` datetime NOT NULL COMMENT '拼团结束时间',
                                   `notify_type` varchar(8) NOT NULL DEFAULT 'HTTP' COMMENT '回调类型（HTTP、MQ）',
                                   `notify_url` varchar(512) DEFAULT NULL COMMENT '回调地址（HTTP 回调不可为空）',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `group_buy_order` WRITE;
/*!40000 ALTER TABLE `group_buy_order` DISABLE KEYS */;

INSERT INTO `group_buy_order` (`id`, `team_id`, `activity_id`, `source`, `channel`, `original_price`, `deduction_price`, `pay_price`, `target_count`, `complete_count`, `lock_count`, `status`, `valid_start_time`, `valid_end_time`, `notify_type`, `notify_url`, `create_time`, `update_time`)
VALUES
    (23,'29487599',100123,'s01','c01',100.00,20.00,80.00,3,0,2,0,'2025-04-05 14:54:47','2025-04-05 15:09:47','HTTP','http://127.0.0.1:8091/api/v1/test/group_buy_notify','2025-04-05 14:54:46','2025-04-05 14:56:23'),
    (24,'15721600',100123,'s01','c01',100.00,20.00,80.00,3,3,3,1,'2025-04-05 15:18:29','2025-04-05 15:33:29','HTTP','http://127.0.0.1:8091/api/v1/test/group_buy_notify','2025-04-05 15:18:29','2025-07-20 21:05:22');

/*!40000 ALTER TABLE `group_buy_order` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_order_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_order_list`;

CREATE TABLE `group_buy_order_list` (
                                        `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                        `user_id` varchar(64) NOT NULL COMMENT '用户ID',
                                        `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                                        `order_id` varchar(12) NOT NULL COMMENT '订单ID',
                                        `activity_id` bigint NOT NULL COMMENT '活动ID',
                                        `start_time` datetime NOT NULL COMMENT '活动开始时间',
                                        `end_time` datetime NOT NULL COMMENT '活动结束时间',
                                        `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                                        `source` varchar(8) NOT NULL COMMENT '渠道',
                                        `channel` varchar(8) NOT NULL COMMENT '来源',
                                        `original_price` decimal(8,2) NOT NULL COMMENT '原始价格',
                                        `deduction_price` decimal(8,2) NOT NULL COMMENT '折扣金额',
                                        `pay_price` decimal(8,2) NOT NULL COMMENT '支付金额',
                                        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态；0初始锁定、1消费完成、2用户退单',
                                        `out_trade_no` varchar(12) NOT NULL COMMENT '外部交易单号-确保外部调用唯一幂等',
                                        `out_trade_time` datetime DEFAULT NULL COMMENT '外部交易时间',
                                        `biz_id` varchar(64) NOT NULL COMMENT '业务唯一ID',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uq_order_id` (`order_id`),
                                        KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `group_buy_order_list` WRITE;
/*!40000 ALTER TABLE `group_buy_order_list` DISABLE KEYS */;

INSERT INTO `group_buy_order_list` (`id`, `user_id`, `team_id`, `order_id`, `activity_id`, `start_time`, `end_time`, `goods_id`, `source`, `channel`, `original_price`, `deduction_price`, `pay_price`, `status`, `out_trade_no`, `out_trade_time`, `biz_id`, `create_time`, `update_time`)
VALUES
    (25,'xfg03','29487599','069645854815',100123,'2024-12-07 10:19:40','2029-12-07 10:19:40','9890001','s01','c01',100.00,20.00,80.00,0,'769515763172',NULL,'100123_xfg03_1','2025-04-05 14:54:46','2025-04-05 14:54:46'),
    (26,'xfg04','15721600','663374201900',100123,'2024-12-07 10:19:40','2029-12-07 10:19:40','9890001','s01','c01',100.00,20.00,80.00,1,'727869517356','2025-04-05 15:21:51','100123_xfg04_1','2025-04-05 14:56:23','2025-07-26 10:34:44'),
    (27,'xfg01','15721600','146024339576',100123,'2024-12-07 10:19:40','2029-12-07 10:19:40','9890001','s01','c01',100.00,20.00,80.00,1,'441842218120','2025-04-05 15:21:51','100123_xfg01_1','2025-04-05 15:18:29','2025-07-20 21:05:38'),
    (29,'xfg02','15721600','630306594433',100123,'2024-12-07 10:19:40','2029-12-07 10:19:40','9890001','s01','c01',100.00,20.00,80.00,1,'061974054911','2025-04-05 15:21:51','100123_xfg02_1','2025-04-05 15:21:51','2025-07-20 21:05:32');

/*!40000 ALTER TABLE `group_buy_order_list` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 notify_task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notify_task`;

CREATE TABLE `notify_task` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                               `activity_id` bigint NOT NULL COMMENT '活动ID',
                               `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                               `notify_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回调种类',
                               `notify_type` varchar(8) NOT NULL DEFAULT 'HTTP' COMMENT '回调类型（HTTP、MQ）',
                               `notify_mq` varchar(32) DEFAULT NULL COMMENT '回调消息',
                               `notify_url` varchar(128) DEFAULT NULL COMMENT '回调接口',
                               `notify_count` int NOT NULL COMMENT '回调次数',
                               `notify_status` tinyint(1) NOT NULL COMMENT '回调状态【0初始、1完成、2重试、3失败】',
                               `parameter_json` varchar(256) NOT NULL COMMENT '参数对象',
                               `uuid` varchar(128) NOT NULL COMMENT '唯一标识',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               KEY `uq_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `notify_task` WRITE;
/*!40000 ALTER TABLE `notify_task` DISABLE KEYS */;

INSERT INTO `notify_task` (`id`, `activity_id`, `team_id`, `notify_category`, `notify_type`, `notify_mq`, `notify_url`, `notify_count`, `notify_status`, `parameter_json`, `uuid`, `create_time`, `update_time`)
VALUES
    (7,100123,'58693013',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"58693013\",\"outTradeNoList\":[\"214969043474\"]}','','2025-03-16 18:23:05','2025-03-16 18:23:05'),
    (8,100123,'16341565',NULL,'HTTP',NULL,'http://127.0.0.1:8091/api/v1/test/group_buy_notify',1,1,'{\"teamId\":\"16341565\",\"outTradeNoList\":[\"539291175688\"]}','','2025-03-16 18:28:59','2025-03-22 09:53:26'),
    (9,100123,'63403622',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"63403622\",\"outTradeNoList\":[\"904941690333\"]}','','2025-03-17 22:12:04','2025-03-17 22:12:04'),
    (10,100123,'44784629',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"44784629\",\"outTradeNoList\":[\"008981724008\"]}','','2025-03-22 10:50:03','2025-03-22 10:50:03'),
    (11,100123,'17743372',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"17743372\",\"outTradeNoList\":[\"562013701479\"]}','','2025-03-22 11:20:40','2025-03-22 11:20:41'),
    (12,100123,'30797609',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"30797609\",\"outTradeNoList\":[\"482309025222\"]}','','2025-03-22 12:04:08','2025-03-22 12:04:08'),
    (13,100123,'46018603',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"46018603\",\"outTradeNoList\":[\"549111800140\"]}','','2025-03-22 12:06:29','2025-03-22 12:06:29'),
    (14,100123,'09150517',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"09150517\",\"outTradeNoList\":[\"753636951899\"]}','','2025-03-29 10:06:53','2025-03-29 10:06:54'),
    (15,100123,'22810944',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"22810944\",\"outTradeNoList\":[\"147604406597\"]}','','2025-03-29 10:10:17','2025-03-29 10:10:17'),
    (16,100123,'98209369',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"98209369\",\"outTradeNoList\":[\"537455032540\"]}','','2025-03-29 10:30:35','2025-03-29 10:30:35'),
    (17,100123,'45338842',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"45338842\",\"outTradeNoList\":[\"183852291730\"]}','','2025-03-29 12:33:28','2025-03-29 12:33:29'),
    (18,100123,'72304503',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"72304503\",\"outTradeNoList\":[\"303596099292\"]}','','2025-03-29 13:07:41','2025-03-29 13:07:41'),
    (19,100123,'14639651',NULL,'MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"14639651\",\"outTradeNoList\":[\"928263928388\"]}','','2025-03-29 13:10:27','2025-03-29 13:10:27'),
    (23,100123,'15721600',NULL,'MQ','topic.team_refund',NULL,2,1,'{\"activityId\":100123,\"orderId\":\"630306594433\",\"teamId\":\"15721600\",\"type\":\"paid_unformed\",\"userId\":\"xfg02\"}','','2025-07-18 17:52:03','2025-07-18 21:45:05'),
    (24,100123,'15721600','trade_paid_team2refund','MQ','topic.team_refund',NULL,1,1,'{\"activityId\":100123,\"orderId\":\"630306594433\",\"teamId\":\"15721600\",\"type\":\"paid_formed\",\"userId\":\"xfg02\"}','','2025-07-18 21:45:05','2025-07-18 21:45:05');

/*!40000 ALTER TABLE `notify_task` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 sc_sku_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sc_sku_activity`;

CREATE TABLE `sc_sku_activity` (
                                   `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `source` varchar(8) NOT NULL COMMENT '渠道',
                                   `channel` varchar(8) NOT NULL COMMENT '来源',
                                   `activity_id` bigint NOT NULL COMMENT '活动ID',
                                   `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uq_sc_goodsid` (`source`,`channel`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='渠道商品活动配置关联表';

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
                       `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                       `source` varchar(8) NOT NULL COMMENT '渠道',
                       `channel` varchar(8) NOT NULL COMMENT '来源',
                       `goods_id` varchar(16) NOT NULL COMMENT '商品ID',
                       `goods_name` varchar(128) NOT NULL COMMENT '商品名称',
                       `original_price` decimal(10,2) NOT NULL COMMENT '商品价格',
                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `uq_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品信息';

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
