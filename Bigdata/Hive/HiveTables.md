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
        ```SQL
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
* Directory/File To Table
    * 파일을 읽어서 테이블의 location에 쓰는 방법
    * Load
        ```SQL
        LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]
        ex) LOAD DATA INPATH '/user/data/sample.csv' INTO TABLE tbl PARTITION(yymmdd='20180510');
        ```
    * 테이블 location 정보 지정
        ```SQL
        CREATE TABLE employee (
          id         String,
          name       String 
        ) LOCATION 'hdfs://127.0.0.1/user/data/';
        ```
        ```SQL
        ALTER TABLE employee SET LOCATION 'hdfs://127.0.0.1/user/data/';
        ```
        ```SQL
        // 서브 디렉토리도 조회할 경우
        set hive.supports.subdirectories=true;
        set mapred.input.dir.recursive=true;
        ```
    * 테이블 파티션 추가/수정 location 정보 지정
        ```SQL
        // 신규 파티션 추가
        ALTER TABLE employee ADD PARTITION (yymmdd='20180510') LOCATION 'hdfs://127.0.0.1/user/';
        // 기존 파티션 수정
        ALTER TABLE employee PARTITION (yymmdd='20180510') SET LOCATION 'hdfs://127.0.0.1/user/';
        ```
    * MSCK
        * 신규 테이블 생성시 하위 디렉토리까지 자동으로 파티션 생성.
        * 테이블 정보가 삭제되어 테이블을 신규로 생성하고 파티션 정보를 새로 생성할 때 사용.
        * 외부에서 대량 데이터가 추가되어 파티션 정보를 신규로 생성할 때 사용.
        ```SQL
        MSCK REPAIR TABLE employee;
        ```
* Table To Table
    * Insert 문
        ```SQL
        // 원래 있던 내용 삭제 됨.
        INSERT OVERWRITE TABLE tablename1 [PARTITION (partcol1=val1, partcol2=val2 ...) [IF NOT EXISTS]] select_statement1 FROM from_statement;
        // table1에 from_statement 내용 추가됨.
        INSERT INTO TABLE tablename1 [PARTITION (partcol1=val1, partcol2=val2 ...)] select_statement1 FROM from_statement;
        ```
    * From Insert 문
        * 여러 테이블에 한 번에 입력할 때 사용.
        * From 절에 원천 데이터를 조회하여 뷰처럼 사용 가능.
        ```SQL
        // source1, source2 테이블을 읽어서 target1, target2 테이블에 입력.
        FROM (
          SELECT *
            FROM source1
          UNION
          SELECT *
            FROM source2
        ) R
        INSERT INTO TABLE target1
        SELECT R.name,
               R.age

        INSERT OVERWRITE TABLE target2 PARTITION(col1 = 'a', col2)
        SELECT R.name,
               R.age;
        ```
    * Create Table As Select 문
        * 테이블을 생성하면서 다른 테이블의 데이터를 입력.
        ```SQL
        CREATE TABLE new_key_value_store
           ROW FORMAT SERDE "org.apache.hadoop.hive.serde2.columnar.ColumnarSerDe"
           STORED AS RCFile
           AS
        SELECT (key % 1024) new_key, concat(key, value) key_value_pair
          FROM key_value_store
          SORT BY new_key, key_value_pair;
        ```
    * INSERT, UPDATE, DELETE, MERGE
        * UPDATE, DELETE는 트랜젝션 설정해야 사용가능.
        ```SQL
        CREATE TABLE students (name VARCHAR(64), age INT, gpa DECIMAL(3, 2))
          CLUSTERED BY (age) INTO 2 BUCKETS STORED AS ORC;
        
        -- INSERT 
        INSERT INTO TABLE students
          VALUES ('fred flintstone', 35, 1.28), ('barney rubble', 32, 2.32);
        
        -- UPDATE
        UPDATE students SET age = 10 WHERE name = 'fred flintstone';
        
        -- DELETE
        DELETE FROM students WHERE name = 'fred flintstone'; 
        
        -- MERGE
        MERGE INTO <target_table> AS T USING <source expression/table> AS S
        ON <boolean expression1>
            WHEN MATCHED [AND <boolean expression2>] THEN UPDATE SET <set clause list>
            WHEN MATCHED [AND <boolean expression3>] THEN DELETE
            WHEN NOT MATCHED [AND <boolean expression4>] THEN INSERT VALUES<value list> 
        ```

* Table To Directory
    * 테이블 데이터를 조회하여 디렉토리에 파일로 저장.
    * Insert Directory 문
        ```SQL
        INSERT OVERWRITE [LOCAL] DIRECTORY directory1
          [ROW FORMAT row_format] [STORED AS file_format] (Note: Only available starting with Hive 0.11.0)
          SELECT ... FROM ...

        Hive extension (multiple inserts):
        FROM from_statement
        INSERT OVERWRITE [LOCAL] DIRECTORY directory1 select_statement1
        [INSERT OVERWRITE [LOCAL] DIRECTORY directory2 select_statement2] ...

        row_format
          : DELIMITED [FIELDS TERMINATED BY char [ESCAPED BY char]] [COLLECTION ITEMS TERMINATED BY char]
                [MAP KEYS TERMINATED BY char] [LINES TERMINATED BY char]
                [NULL DEFINED AS char] (Note: Only available starting with Hive 0.13)
        ```
        ```SQL
        -- /user/ 디렉토리에 source 테이블을 읽어서 저장 
        INSERT OVERWRITE DIRECTORY 'hdfs://1.0.0.1:8020/user/'
        SELECT *
          FROM source;

        -- /user/ 디렉토리에 source 테이블을 읽어서 칼럼 구분을 탭으로 저장 
        INSERT OVERWRITE DIRECsTORY 'hdfs://1.0.0.1:8020/user/'
           ROW FORMAT DELIMITED
           FIELDS TERMINATED BY '\t'
        SELECT *
          FROM source;

        -- /user/ 디렉토리에 source 테이블을 읽어서 칼럼 구분을 콤마으로 저장하면서 Gzip으로 압축 
        -- 파일을 CSV 형태로 압축하여 저장 
        set hive.exec.compress.output=true;
        set mapred.output.compression.codec=org.apache.hadoop.io.compress.GzipCodec;

        INSERT OVERWRITE DIRECTORY 'hdfs://1.0.0.1:8020/user/'
           ROW FORMAT DELIMITED
           FIELDS TERMINATED BY ','
        SELECT *
          FROM source;
        ```