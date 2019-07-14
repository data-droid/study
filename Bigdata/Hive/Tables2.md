## 파티션
* 데이터를 디렉토리로 분리하여 파티션을 컬럼으로 처리하여 읽어 들이는 데이터 양을 줄여 속도를 향상시킴. 
    ```SQL 
    CREATE TABLE tbl(
      col1 STRING
    ) PARTITIONED BY (yymmdd STRING); 
    ```

* 파티션 종류
    * 고정 파티션
        * Insert 문에 파티션 정보를 고정된 값으로 전달하여 입력함.
        ```SQL
        INSERT INTO TABLE tbl(yymmdd='20190713')
        SELECT name
          FROM temp;
        > hdfs://[tbl 테이블 로케이션]/yymmdd=20190713/  
        ```
    * 동적 파티션
        * Insert 문에 파티션 정보를 조회하는 컬럼을 전달하여 입력함.
        ```SQL
        INSERT INTO TABLE tbl(yymmdd)
        SELECT name,
               yymmdd
          FROM temp;      
        -- temp 테이블의 yymmdd 컬럼에 20190713 데이터가 있으면
        > hdfs://[tbl 테이블 로케이션]/yymmdd=20190713/
        ```
        * 동적 파티션만 이용하는 것을 권하지 않음. 만약 통적 파티션만 이용하기 위해서는 따로 설정 필요함.
        ```SQL 
        set hive.exec.dynamic.partition.mode=nonstrict;```
        * 동적 파티션은 속도가 느려지기 때문에 개수가 제한됨.
        ```SQL
        -- 동적 파티션 개수 
        set hive.exec.max.dynamic.partitions=1000;
        -- 노드별 동적 파티션 생성 개수 
        set hive.exec.max.dynamic.partitions.pernode=100;
        ```
        * 동적 파티션에 NULL값이 들어가는 경우 기본 파티션에 들어감.
        ```SQL
        -- NULL 값의 기본 파티션 명 
        set hive.exec.default.partition.name=__HIVE_DEFAULT_PARTITION__;

        -- 아래와 같은 형태로 파티션이 생성 
        hdfs://temp/yymmdd=20190713/hh=00/
        hdfs://temp/yymmdd=20190713/hh=__HIVE_DEFAULT_PARTITION__/

        -- 해당 파티션을 조회할 때는 다음과 같이 사용 
        SELECT *
          FROM temp
         WHERE hh = '__HIVE_DEFAULT_PARTITION__';        
        ```
* 파티션 수정/삭제
    * alter 문을 사용.
    * External Table이 아닌 경우. 즉, Managed Table인 경우 파티션 위치의 데이터도 함께 삭제됨!
    ```SQL
    -- 신규 파티션 추가 
    ALTER TABLE employee ADD PARTITION (yymmdd='20190713');

    -- 파티션의 LOCATION 수정  
    ALTER TABLE employee PARTITION (yymmdd='20190713') SET LOCATION 'hdfs://127.0.0.1/user/';

    -- 파티션 삭제 
    ALTER TABLE employee DROP PARTITION (yymmdd='20190713');

    -- 파티션 범위 삭제, 비교연산자를 이용해 범위 삭제 가능 
    ALTER TABLE employee DROP PARTITION (yymmdd < '20190713');
    ALTER TABLE employee DROP PARTITION (yymmdd >= '20190713');
    ```
* 파티션 복구
    * MSCK 명령어를 이용함.
    * 많은 파티션을 한번에 복구시 시간초과 등의 오류 발생함.
        * 한번에 처리할 파티션 개수를 설정하여 명령어를 여러번 돌리게 함.
        ```SQL
        -- 0으로 설정하면 모든 파티션을 복구한다. 
        set hive.msck.repair.batch.size=0;        
        ```
        * 파티션 규칙이 맞지 않을때 오류가 발생할 경우
        ```SQL
        -- 파티션에 허용되지 않는 문자가 있으면 오류가 발생하는데, ignore 로 설정하면 무시하고 넘어간다. 
        set hive.msck.path.validation=ignore;
        ```
    * 파티션 복구
    ```SQL
    MSCK REPAIR TABLE employee;
    ```
    
## 버켓팅
* 지정한 컬럼의 값을 해쉬 처리하고 지정한 수의 파일로 나눠 저장.
* 조인에 사용되는 키로 버켓 컬럼을 생성하면, 소트 머지 버켓(SMB) 조인으로 처리되어 빠름!
* 파티션은 데이터를 티렉토리로 나눴다면, 버켓팅은 데이터를 파일별로 나눠 저장함.
```SQL
    -- col2 를 기준으로 버켓팅 하여 20개의 파일에 저장 
    CREATE TABLE tbl1(
      col1 STRING,
      col2 STRING
    ) CLUSTERED BY (col2) INTO 20 BUCKETS

    -- col2 를 기준으로 버켓팅 하고, col1 기준으로 정렬하여 20개의 파일에 저장 
    CREATE TABLE tbl2(
      col1 STRING,
      col2 STRING
    ) CLUSTERED BY (col2) SORTED BY (col1) INTO 20 BUCKETS
```

## 스큐
* 컬럼에 특정 데이터가 주로 들어오는 경우 분리하여 저장.
* 파티션과 유사하나 파티션은 주로 데이터를 크게 구분하는 용도이며, 스큐는 컬럼의 데이터를 구분할 때 사용.
* 1~1000 값이 들어오는 컬럼에 1,2의 값이 대부분이라면 파티션의 경우 1000개를 만들지만, 스큐는 1,2와 나머지 디렉토리나 파일로 구별되어 저장됨.
```text
    # 파티션은 데이터를 크게 구분할 때 사용
    /year=2018/month=07/day=01
    /year=2018/month=07/day=02

    # 스큐는 칼럼의 데이터를 구분할 때 사용 
    /year=2018/month=07/day=01/code=1
    /year=2018/month=07/day=01/code=2
    /year=2018/month=07/day=01/code=HIVE_DEFAULT_LIST_BUCKETING_DIR_NAME/
```
* 스큐의 기본 문법
```SQL
    CREATE TABLE tbl (
      col1 STRING,
      col2 STRING
    ) SKEWED BY (col1) on ('value1', 'value2' ) [STORED as DIRECTORIES];
```

## 정렬
* Order By
    * 모든 데이터를 정렬하여 하나의 파일로 생성.
    * 데이터가 클 수록 오래걸리고, Out Of Memory 오류가 발생할 수 있음.
    ```SQL
    -- 테이블 풀 스캔 같은 성능에 영향을 미치는 쿼리는 nonstrict 모드에서만 동작 
    set hive.mapred.mode=nonstrict;

    SELECT *
      FROM tbl
     ORDER BY number;

    -- strict 모드 일때는 LIMIT 가 있어야만 처리 가능 
    set hive.mapred.mode=strict;

    SELECT *
      FROM tbl
     ORDER BY number
     LIMIT 100;
    ```
    * [1,4,3,2,5] -> [1,2,3,4,5]
* Sort By
    * 리듀서 별로 입력된 데이터를 정렬하여 출력.
    * 리듀서 개수만큼 결과가 정렬되어 출력.
    ```SQL
    SELECT *
      FROM tbl
      SORT BY number;
    ```
    * [1,4,3,2,5] -> [1,4,3][2,5]
* Distribute By
    * 정렬하지는 않지만, 리듀서 기반으로 출력.
    ```SQL
    SELECT *
      FROM tbl
      DISTRIBUTE BY number;    
    ```
    * [1,4,1,3,2] -> [1,2,1][4,3]
* Cluster By
    * sort by와 distribute by를 동시에 수행.
    ```SQL
    SELECT *
      FROM tbl
      CLUSTER BY age;    
    ```
    * [1,4,1,3,2] -> [1,1,2][3,4]