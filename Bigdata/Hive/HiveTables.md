# Hive Table
* 테이블의 구조와 기타 다양한 기능을 알아보자!

## Data Type
* 기본 타입
    * 숫자형
        * TINYINT, SMALLINT, INT, BIGINT, BOOLEAN, FLOAT, DOUBLE, DOUBLE PRECISION, DECIMAL, DECIMAL(precision, scale)
    * 문자형
        * STRING, VARCHAR, CHAR
    * 기타
        * BINARY, TIMESTAMP, DATE
* 복합 타입
    * ARRAY
        * `ARRAY < data_type >`
        * `SELECT col3[0] FROM tbl;`
    * MAP
        * `MAP < primitive_type, data_type >`
        * `SELECT col4['key1'] FROM tbl;`
    * STRUCT
        * 테이블의 ROW와 비슷하며 정해진 개수의 필드와 타입을 가짐.
        * `STRUCT < col_name : data_type [COMMENT col_comment], ...>`
        * `SELECT col5.age FROM tbl;`
    * UNIONTYPE
        * 지정한 타입들 중 아무거나 사용.
        * `UNIONTYPE < data_type, data_type, ... > `
        ```
        CREATE TABLE union_test(
          foo UNIONTYPE<int, double, array<string>, struct<a:int,b:string>>
        );

        SELECT foo FROM union_test;
        {0:1}
        {1:2.0}
        {2:["three","four"]}
        {3:{"a":5,"b":"five"}}
        {2:["six","seven"]}
        {3:{"a":8,"b":"eight"}}
        {0:9}
        {1:10.0}
        ```

## 데이터 입력