## 통계 정보
* 테이블의 ROW 수, 파일 개수, 사이즈의 통계정보를 이용하여 빠른 데이터처리를 지원.
* CBO를 이용한 실행계획 최적화, 단순 카운트 쿼리등에 사용되어 처리속도를 높여줌.
```SQL
    set hive.stats.autogather=true;
    set hive.stats.column.autogather=true;
```
* 통계정보 매뉴얼 수집
    * DML을 이용한 처리시 통계정보를 자동 수집하지만 파일 시스템 상 정보가 변경된경우 반영안됨.
    * analyze 명령으로 통계정보 수집하도록 설정.
    * analyze 커맨드는 테이블 단위, 파티션 단위로 실행 가능.(but, mapreduce 작업으로 다른 작업에 영향을 끼칠 수 있음.)
    ```SQL
    ANALYZE TABLE [db_name.]tablename [PARTITION(partcol1[=val1], partcol2[=val2], ...)]  -- (Note: Fully support qualified table name since Hive 1.2.0, see HIVE-10007.)
      COMPUTE STATISTICS 
      [FOR COLUMNS]          -- (Note: Hive 0.10.0 and later.)
      [CACHE METADATA]       -- (Note: Hive 2.1.0 and later.)
      [NOSCAN];

    # tbl 테이블 통계정보 수집
    hive> ANALYZE TABLE tbl COMPUTE STATISTICS;
    # tbl 테이블의 yymmdd가 '2018-01-01'인 파티션의 통계정보 수집 
    hive> ANALYZE TABLE tbl PARTITION(yymmdd='2018-01-01') COMPUTE STATISTICS;
    # 칼럼 통계정보 수집 
    hive> ANALYZE TABLE tbl PARTITION(yymmdd='2018-01-01') COMPUTE STATISTICS FOR COLUMNS;    
    ```
    * 통계정보는 desc extended|formatted 커맨드로 확인 가능.
    ```SQL
    hive> desc formatted tbl partition(yymmddval='20180101');
    OK
    # col_name              data_type               comment             

    col1        string                                      
    Partition Parameters:        
        COLUMN_STATS_ACCURATE   {BASIC_STATS:true}
        numFiles                6                   
        numRows                 618048              
        rawDataSize             2230248184          
        totalSize               8546118             
        transient_lastDdlTime   1544059910   
    ```
    
## 파일 머지
* size가 작은 파일이 많이 생길 경우 파일을 묶어 줌.
```SQL
    -- 맵퍼 단독 작업일 때 머지 
    set hive.merge.mapfiles=true;
    -- 맵리듀스 작업일 때 머지 
    set hive.merge.mapredfiles=true;
    -- 테즈 작업일 때 머지 
    set hive.merge.tezfiles=true;
    -- 머지 작업의 대상이 되는 파일 사이즈(32MB이하)
    set hive.merge.smallfiles.avgsize=32000000;
    -- 머지 파일을 묶을때 기준(256MB)
    set hive.merge.size.per.task=256000000;    
```

## 파일 업축
* Insert Directory, CTAS 문으로 파일을 생성할 때 압축 가능.
```xml
    <property>
        <name>io.compression.codecs</name>
        <value>org.apache.hadoop.io.compress.GzipCodec,
               org.apache.hadoop.io.compress.DefaultCodec,
               org.apache.hadoop.io.compress.BZip2Codec,
               org.apache.hadoop.io.compress.SnappyCodec,
               com.hadoop.compression.lzo.LzoCodec,
               com.hadoop.compression.lzo.LzopCodec
        </value>
    </property>
```
* Query 결과를 압축하기 위해서는 설정 필요.
```SQL
    # 압축 여부 설정 
    set hive.exec.compress.output=true;
    # 압축 코덱 설정
    set mapred.output.compression.codec=org.apache.hadoop.io.compress.GzipCodec;
```
* 사용법
```SQL
    set hive.exec.compress.output=true;
    set mapred.output.compression.codec=org.apache.hadoop.io.compress.GzipCodec;

    # table을 읽어서 /user/tables/에  CSV 형태로 압축하여 저장 
    INSERT OVERWRITE DIRECTORY 'hdfs:///user/tables/'
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
    SELECT *
      FROM table
     WHERE name = 'csv';

    # table을 읽어서 csvsample 테이블러 저장 
    CREATE TABLE csvsample
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
    LOCATION '/user/csv/'
    AS 
    SELECT *
      FROM table
     WHERE name = 'csv';
```