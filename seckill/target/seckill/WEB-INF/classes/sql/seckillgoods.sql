CREATE TABLE seckill_goods(
id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品ID',
goods_id BIGINT(20) DEFAULT NULL COMMENT '商品ID',
seckill_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '秒杀的商品价格',
stock_count INT(11) DEFAULT NULL COMMENT '库存数量',
start_date DATETIME DEFAULT NULL COMMENT '秒杀开始时间',
end_date DATETIME DEFAULT NULL COMMENT '秒杀结束时间',
PRIMARY KEY(id)
)ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO seckill_goods VALUES(1,1,0.01,4,'2018-09-13 15:18:00','2018-09-15 14:00:18'),(2,2,0.01,9,'2018-09-17 14:00:14','2018-09-19 14:00:24')