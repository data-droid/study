# Generated Columns
* [참고](https://gabi.dev/2016/10/17/understanding-generated-columns/)

### Generated Columns 란?
* MySQL 5.7부터 추가된 VIEW TABLE과 비슷한 기능!
* 2가지 옵션
    * VIRTUAL(DEFAULT) 
        * INSERT, UPDATE 때마다 데이터를 변경/삽입하지 않고 SELECT 할때 만 반영.
        * 해당 컬럼의 데이터를 저장하지 않기 때문에 저장공간 X
    * STORED
        * INSERT, UPDATE 때마다 해당 컬럼 값 변경/삽입
        * 해당 컬럼의 데이터를 실제 저장하기 때문에 저장공간 O
* 사용 가능한 표현식
    * Virtual, Stored 두 가지 유형
    * 수학적 표현 (price * quantity)
    * 내장함수 (RIGHT(), CONCAT(), FROM_UNIXTIME(), JSON_EXTRACT())
    * literals ('2', 'new', 3)
* 그 외 특징
    * index 생성 가능
    * subquery X -> View를 써야함
* 사용법
    * `COLUMN_NAME TYPE(X) AS (표현식)`
    

### 어디서 사용?
* 특정 컬럼들의 연산을 통해 생성되는 컬럼
    * 총계
    * 특정 값 추출
    * SCALA 값
* 데이터의 메타가 자주 변경되는 경우


### CRREATE TABLE
* VIRTUAL COLUMN
```SQL
CREATE TABLE `orders_items` (
`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
`order_id` int(10) unsigned NOT NULL,
`product_id` int(10) unsigned NOT NULL,
`product_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
`quantity` int(10) unsigned NOT NULL DEFAULT 1,
`total_item_price` decimal(10,2) AS (`quantity` * `product_price`),
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_at` varchar(45) NOT NULL DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```
* STORED COLUMN
```SQL
CREATE TABLE `orders_items` (
`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
`order_id` int(10) unsigned NOT NULL,
`product_id` int(10) unsigned NOT NULL,
`product_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
`quantity` int(10) unsigned NOT NULL DEFAULT 1,
`total_item_price` decimal(10,2) AS (`quantity` * `product_price`) STORED,
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_at` varchar(45) NOT NULL DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```
* VIRTUAL이 DEFAULT이며 STORED는 컬럼 마지막에 STORED 표기 필요.

### ALTER
* VIRTUAL COLUMN
```SQL
-- `full_name` as VIRTUAL COLUMN
ALTER TABLE users
ADD COLUMN `full_name` VARCHAR(500)
AS (CONCAT_WS(" ", `first_name`, `last_name`));
```
* STORED COLUMN
```SQL
-- `total_item_price` as STORED COLUMN
ALTER TABLE orders_items
ADD COLUMN `total_item_price` DECIMAL(10, 2)
AS (`quantity` * `product_price`) STORED;
```

## JSON FIELDS
```SQL
-- Stored Columns
ALTER TABLE `twitter_users`
ADD COLUMN `location` VARCHAR(255)
AS (response->>"$.location") STORED;
```

## INDEX 
* VIRTUAL, STORED 두가지 모두 INDEX를 가질 수 있음.
```SQL
ALTER TABLE users
ADD INDEX `ix_full_name` (`full_name`);
```