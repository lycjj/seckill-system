CREATE TABLE goods(
id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
goods_name VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
goods_title VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
goods_img VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
goods_detail LONGTEXT COMMENT '商品的详细介绍',
goods_price DECIMAL(10,2)  DEFAULT 0.00 COMMENT '商品单价',
goods_stock INT(11) DEFAULT 0 COMMENT '商品库存,-1表示没有限制',
PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO goods VALUES(1,'iphoneX','Apple iPhone X (A1865) 64G 银色 移动联通电信4G手机','/img/iphonex.png','Apple iPhone X (A1865) 64G 银色 移动联通电信4G手机',
8765.00,10000),(2,'华为Mate9','华为Mate9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待','/img/mate10.png','华为Mate9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待',
3212.00,-1)