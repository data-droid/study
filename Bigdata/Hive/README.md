# Hive
* 하이브는 하둡 에코시스템
* RDB의 데이터베이스, 테이블과 같은 형태로 HDFS에 저장된 데이터를 정의
* SQL과 유사한 HiveQL를 이용하여 데이터 분석 가능!

## Hive의 구조
* UI
    * CLI, Beeline, JDBC 등을 이용해 사용자가 쿼리 및 기타 작업(DDL,DML)을 하는 인터페이스
* Driver
    * 쿼리를 입력받고 작업을 처리
    * 세션관리 및 JDBC/ODBC 인터페이스 API를 제공
* Compiler
    * 하이브 메타스토어를 참고하여 쿼리 플랜을 짜는 곳!
* Metastore
    * DB, Table, Partition의 정보를 저장!
    * 하이브의 핵심! (다양한 곳에서 해당 메타스토어를 참조 가능함!)
    * 주요 테이블
        * DBS : 데이터베이스 정보
        * TBLS :  테이블 정보
        * PARTITIONS : 파티션 정보
* Execution Engin
    * 쿼리 플랜을 실행

![](http://www.stratapps.net/images/HiveArchitecture.JPG)

## Hive Version History
* Hive 1.0
    * 2015년 2월
        * SQL을 이용한 맵리듀스 처리
        * 파일 데이터를 논리적으로 표현
        * 빅데이터의 배치 처리를 목표
* Hive 2.0
    * 2016년 2월
        * [LLAP](https://cwiki.apache.org/confluence/display/Hive/LLAP#LLAP-ExecutionEngine)(Live Long and Process)구조 추가
            * ![](https://cwiki.apache.org/confluence/download/attachments/62689557/LLAP_diagram.png?version=1&modificationDate=1474327021000&api=v2)
        * Spark 지원 강화
        * CBO(Cost Based Optimizer) 강화
            * 하이브 메타 정보를 이용하여 집계함수의 쿼리 성능 향상!
            * ```set hive.cbo.enable=true;```
            * ```set hive.compute.query.using.stats=true;```
            * ```set hive.stats.fetch.column.stats=true;```
            * ```set hive.stats.fetch.partition.stats=true;```
        * HPLSQL 추가
            * 오라클 PL/SQL과 비슷한 Procedural
            * 재사용 가능한 스크립트
            * FOR를 이용한 루프문이 가능!!
             ```
            FOR i IN 1..10 LOOP
            DBMS_OUTPUT.PUT_LINE(i);
            END LOOP
                ```
* Hive 3.0
    * 2018년 5월
        * 맵리듀스 엔진, Hive CLI를 제거하고 Tez엔진과 Beeline을 이용
        * role, 권한을 이용한 작업 상태 관리(workload management)
            * SQL을 이용하여 워크로드 관리를 위한 role을 생성.
            * 작업의 부하에 따라 쿼리의 성능, 실행 여부 제어 가능.
            * `CREATE RESOURCE PLAN daytime;`
        * 트랜젝션 처리 강화
        * [Materialized View](https://cwiki.apache.org/confluence/display/Hive/Materialized+views) 추가
            * 실제 데이터를 가지는 구체화 뷰
            * 집계 데이터를 개별적으로 보관하여 쿼리 수행시 빠른 속도로 데이터 조회 가능
            ```
            CREATE MATERIALIZED VIEW mv1
            AS
            SELECT empid, deptname, hire_date
            FROM emps JOIN depts
              ON (emps.deptno = depts.deptno)
            WHERE hire_date >= '2016-01-01';
            ```
            
        * 쿼리 결과 캐시
        * 테이블 정보 관리 데이터베이스 추가
            * 하이브 메타스토어에만 확인할 수 있던 정보를 데이터 베이스를 통해 확인가능
            * 전체 테이블 칼럼 정보, 통계 정보 등을 확인 가능하며 커넥터를 이용해 사용자가 직접 확인 가능.
                * ``` use information_schema; use sys; ```

## Hive 서비스
![](https://i.stack.imgur.com/17mZy.png)
* 메타스토어
    * HDFS 데이터 구조를 저장하는 DB
    * 3가지 모드
        * embeded : 더비 DB를 이용한 모드, 다중 사용자 불가
        * local : 별도 데이터베이스를 가지지만, 하이브 드라이버와 같은 JVM에서 동작
        * remote : 별도 데이터베이스를 가지며, 별도의 JVM에서 단독으로 동작
* HiveServer2
    * 다양한 언어로 개발된 클라이언트와 연동
    * 인증 및 다중 사용자 지원
    * Thrift, JDBC, ODBC 연결을 사용하는 어플리케이션과 통신
* Beeline
    * CLI처럼 내장형 모드로 동작하거나 JDBC로 HiveServer2 프로세스에 접근
    * CLI와 달리 원격 하이브 서비스에 접속 가능.
    
## Hive CLI
* 하이브 쿼리를 실행하는 가장 기본적인 도구
    ```
    ## 사용법
     -e <quoted-query-string>         커맨드 라인으로 실행할 쿼리 
     -f <filename>                    쿼리가 작성된 파일을 이용하여 실행할 경우 
     --hiveconf <property=value>      하이브 설정값 입력 
                                      예) --hiveconf tez.queue.name=queue
     --hivevar <key=value>            쿼리에서 사용할 변수 입력 
                                      예) --hivevar targetDate=20180101
    ex) $ hive -e 'SELECT * FROM table WHERE yymmdd=${hivevar:targetDate}' \
       --hiveconf hie.execution.engine=tez \
       --hiveconf tez.queue.name=queue_name \
       --hivevar targetDate=20180101
    ```

## Beeline
* HiveServer2에 접속하여 쿼리를 실행하기 위한 도구.
```
## 사용법
$ beeline
beeline> !connect jdbc:hive2://localhost:10000 scott tiger
```

## Hive Metastore
* Embeded Metastore

```
## hive-site.xml
<property>
   <name>javax.jdo.option.ConnectionURL</name>
   <value>jdbc:derby:metastore_db;create=true </value>
   <description>JDBC connect string for a JDBC metastore </description>
</property>
```

* Local Metastore

```
## hive-site.xml
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://[IP]:[port]/[데이터베이스 이름]</value>
    <description>username to use against metastore database</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>org.mariadb.jdbc.Driver</value>
    <description>username to use against metastore database</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>[사용자명]</value>
    <description>username to use against metastore database</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>[암호]</value>
    <description>password to use against metastore database</description>
</property>
```

* Remote Metastore

```
## hive-site.xml
<property>
    <name>hive.metastore.uris</name>
    <value>thrift://[메타스토어 IP]:[메타스토어 Port]</value>
    <description>JDBC connect string for a JDBC metastore</description>
</property>
```

## Database
* 기본 위치
    * ```hive.metastore.warehouse.dir = hdfs:///user/hive/```
* CREATE
```
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
  [COMMENT database_comment]
  [LOCATION hdfs_path]
  [WITH DBPROPERTIES (property_name=property_value, ...)];
```
* ALTER
```
ALTER (DATABASE|SCHEMA) database_name SET DBPROPERTIES (property_name=property_value, ...);   -- (Note: SCHEMA added in Hive 0.14.0)
ALTER (DATABASE|SCHEMA) database_name SET OWNER [USER|ROLE] user_or_role;   -- (Note: Hive 0.13.0 and later; SCHEMA added in Hive 0.14.0)
ALTER (DATABASE|SCHEMA) database_name SET LOCATION hdfs_path; -- (Note: Hive 2.2.1, 2.4.0 and later) // 실제 데이터가 이동은 없음.
```
* DROP
    * default는 RESTRICT 옵션이며 테이블이 있으면 삭제가 불가능하다.
    * CASCADE 옵션을 사용하면 테이블이 있어도 삭제 가능.
```
DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
```

## TABLE
* HDFS에 저장된 파일과 디렉토리 구조에 대한 메타 정보
* CREATE
    ```
    CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name // TABLE TYPE 정의
      [(col_name data_type [COMMENT col_comment], ... [constraint_specification])]
      [COMMENT table_comment]
      [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]  // 데이터 폴더 구분을 통한 읽기속도 향상
      [CLUSTERED BY (col_name, col_name, ...) [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
      [SKEWED BY (col_name, col_name, ...)  
         ON ((col_value, col_value, ...), (col_value, col_value, ...), ...)
         [STORED AS DIRECTORIES]
      [
       [ROW FORMAT row_format] 
       [STORED AS file_format]
         | STORED BY 'storage.handler.class.name' [WITH SERDEPROPERTIES (...)] 
      ]
      [LOCATION hdfs_path]  // 데이터 위치
      [TBLPROPERTIES (property_name=property_value, ...)] 
      [AS select_statement]; 
    ```

    * LOCATION
        * 테이블의 데이터 저장위치. (default는 데이터베이스 저장위치 아래)
    * TABLE TYPE
        * MANAGED
            * 옵션을 주지 않으면 Managed
            * 세션이 종료되어도 테이블의 데이터와 파일은 유지됨.
            * 테이블 DROP시 파일도 함께 삭제됨
        * EXTERNAL
            * 테이블 DROP시 파일 유지됨.
            * 사용자 실수로 인한 파일 삭제를 방지하기 위해 EXTERNAL 테이블로 관리하는 것도 좋음.
        * TEMPORARY
            * 현재 세션에서만 사용하는 임시 테이블
            * 세션 종료시 제거됨.
    * PARTITIONED BY
        * 컬럼을 지정하여 폴더 단위로 데이터가 생성되게함.
        * 하이브의 경우 폴더단위로 읽기 때문에 없을 경우 FULL SCAN함.
        * 정보 제공 유무에 따라 Dynamic/Static으로 나눠짐.
        ```
        CREATE TABLE tbl(
        col1 STRING
        ) PARTITIONED BY (yymmdd STRING)
        //위와 같이 선언시 일별로 저장됨.
        hdfs://tbl/yymmddval=20180501/0000_0
        hdfs://tbl/yymmddval=20180502/0000_0
        hdfs://tbl/yymmddval=20180503/0000_0
        ```
    * BUCKET, SKEWED
        * CLUSTERED BY SORTED BY INTO BUCKET
            * 버켓팅은 CLUSTERED BY를 이용하여 설정하며 일반적으로 SORTED BY와 함께 사용됨
            * 설정한 버켓(파일)의 개수에 지정한 컬럼의 데이터를 해쉬처리하여 저장함.
            ```
            -- col2 를 기준으로 버켓팅 하여 20개의 파일에 저장 
            CREATE TABLE tbl(
              col1 STRING,
              col2 STRING
            ) CLUSTERED BY col2 SORTED BY col2  INTO 20 BUCKETS
            ```
        
        * SKEWED BY
            * 값을 분리된 파일에 저장하여 특정한 값이 자주 등장하는 경우 속도 향상.
            
            ```
            -- col1의 col_value 값을 스큐로 저장  
            CREATE TABLE tbl (
              col1 STRING,
              col2 STRING
            ) SKEWED BY (col1) on ('col_value');
            ```
    * ROW FORMAT
        * 컬럼.로우의 DELIMETER와 데이터 해석방법을 정의하는 SerDe 지정 및 저장 방식 정의.
        * DELIMITED
        ```
        -- 하이브의 기본 구분자를 이용한 테이블 생성 
        --   입력 데이터
        $ cat sample.txt 
        a,val1^val2^val3,key1:val1^key2:val2
        
        -- ROW FORMAT을 이용한 테이블 생성 
        CREATE TABLE tbl (
         col1 STRING,
         col2 ARRAY<STRING>, 
         col3 MAP<STRING, STRING>
        ) ROW FORMAT DELIMITED
           FIELDS TERMINATED BY ','
           COLLECTION ITEMS TERMINATED BY '^'
           MAP KEYS TERMINATED BY ':';

        -- 데이터 로드 
        LOAD DATA LOCAL INPATH './sample.txt' INTO TABLE tbl;
        
        -- 데이터 조회, 구분자에 따라 데이터가 구분 됨 
        hive> select * from tbl;
        OK
        a   ["val1","val2","val3"]  {"key1":"val1","key2":"val2"}

        -- 지정가능한 구분자 
          FIELDS TERMINATED BY '\t'            -- 칼럼을 구분하는 기준
          COLLECTION ITEMS TERMINATED BY ','   -- 리스트를 구분하는 기준
          MAP KEYS TERMINATED BY '='           -- 맵데이터의 키와 밸류를 구분하는 기준
          LINES TERMINATED BY '\n'             -- 로(row)를 구분하는 기준
          ESCAPED BY '\\'                      -- 값을 입력하지 않음
          NULL DEFINED AS 'null'               -- null 값을 표현(0.13 버전에서 추가)
        ```
      
        * SerDe
            * 파일 포맷을 정의하는 것으로 기본 SerDe, 정규식(RegExSerDe), JSON(JsonSerDe), CSV(OpenCSVSerDe)
          ```
          -- RegEx 서데 
            -- 127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
            CREATE TABLE apachelog (
              host      STRING,
              identity  STRING,
              user      STRING,
              time      STRING,
              request   STRING,
              status    STRING,
              size      STRING,
              referer   STRING,
              agent     STRING )
            ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.RegexSerDe'
            WITH SERDEPROPERTIES (
              "input.regex" = "([^]*) ([^]*) ([^]*) (-|\\[^\\]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\".*\") ([^ \"]*|\".*\"))?"
            );

            -- JSON 서데 
            CREATE TABLE my_table(
              a string, 
              b bigint 
            ) ROW FORMAT SERDE 'org.apache.hive.hcatalog.data.JsonSerDe'
            STORED AS TEXTFILE;

            -- CSV 서데 
            CREATE TABLE my_table(
              a string, 
              b string
            ) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
            WITH SERDEPROPERTIES (
               "separatorChar" = "\t",
               "quoteChar"     = "'",
               "escapeChar"    = "\\"
            )  
            STORED AS TEXTFILE;
           ```
        
        * STORED AS
            * 데이터 저장하는 파일 포맷
            * TEXTFILE, SEQUENCEFILE, ORC, PARQUET
            ```
            -- 저장 포맷을 ORC로 설정하고, ORC 관련 설정정보 전달 
            CREATE TABLE tbl (
              col1 STRING
            ) STORED AS ORC 
            TBLPROPERTIES ("orc.compress"="SNAPPY");

            -- INPUTFORMAT, OUTPUTFORMAT을 따로 지정하는 것도 가능 
            CREATE TABLE tbl1 (
              col1 STRING 
            ) STORED AS INPUTFORMAT  "com.hadoop.mapred.DeprecatedLzoTextInputFormat"
                        OUTPUTFORMAT "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat";
            ```