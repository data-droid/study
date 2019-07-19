# 성능 최적화
## TEZ
* Yarn 기반의 비동기 사이클 그래프 프레임워크.
![](https://2xbbhjxc6wk3v21p62t8n4d4-wpengine.netdna-ssl.com/wp-content/uploads/2016/03/H1H2Tez-1024x538.png)
* 맵리듀스의 경우 작업 중간결과를 HDFS에 계속 쓰게 되면서 발생하는 IO 오버헤드가 가장 큰 문제.
![](https://tez.apache.org/images/PigHiveQueryOnMR.png)
* Tez는 작업 처리 결과를 메모리에 저장하며 IO 오버헤드를 줄여 속도를 높인다.
![](https://tez.apache.org/images/PigHiveQueryOnTez.png)
* 설정
```SQL
    -- 테즈 엔진 설정 
    set hive.execution.engine=tez;
    set tez.queue.name=tez_queue_name;

    -- 맵리듀스 엔진 설정 
    set hive.execution.engine=mr;
    set mapred.job.queue.name=mr_queue_name;
```

## ORC
* Optimized Row Columnar은 컬럼 기반의 파일 저장방식.
* Hadoop, Hive, Pig, Spark 등에서 사용 가능.
* 컬럼 단위로 데이터를 찾는게 빠르고, 압축효율이 좋음.
![](https://image.slidesharecdn.com/w-1205p-230a-radhakrishnanv3-140617155040-phpapp02/95/hive-and-apache-tez-benchmarked-at-yahoo-scale-21-638.jpg?cb=1403020414)
* 설정
```SQL
    CREATE TABLE table1 (
        col1 string,
        col2 string
    ) STORED AS ORC
    TBLPROPERTIES (
        "orc.compress"="ZLIB",  
        "orc.compress.size"="262144",
        "orc.create.index"="true",
        "orc.stripe.size"="268435456",
        "orc.row.index.stride"="3000",
        "orc.bloom.filter.columns"="col1,col2"
    );
```
* 설정 값
    * orc.compress
        * default : ZLIB
        * 압축방식 설정 : NONE, ZLIB, SNAPPY
    * orc.compress.size
        * default : 262,144
        * 압축 chunk size : 256 X 1024 = 262,144
    * orc.create.index
        * default : true
        * 인덱스 사용 여부
    * orc.row.index.stride
        * 기본값 : 10,000
        * 설정값 이상의 row일 때 인덱스 생성
    * orc.stripe.size
        * 기본값 : 67,108,864
        * 스트라이프를 생성할 사이즈 (64x1024x1024 = 67,108,864)
        * 설정 사이즈마다 하나씩 생성
    * orc.bloom.filter.columns
        * 기본값 : ""
        * 블룸필터를 생성할 컬럼 정보, ","로 구분하여 입력
            * 블룸필터
                * 원소가 집합에 속하는지 여부를 검사할때 사용하는 확률적 자료구조
                * 긍정 오류는 발생 가능하나, 부정 오류는 발생 X
                    * 긍정 오류 : 원소가 속하지 않지만 속한다 한 경우.
                    * 부정 오류 : 원소가 속하지만 속하지 않는다고 한 경우.
    * orc.bloom.filter.fpp
        * 기본값 : 0.05
        * 오판 확률(ffp=false positive portability) 설정 (0~1)


## Vectorization
* 한번에 1024개 row를 처리하여 속도를 높일 수 있음.
* filter, join, aggregation 처리 속도를 높임.
```SQL
set hive.vectorized.execution.enabled=true;
```

## Partitioning, Bucketing
* 디렉토리 단위로 데이터 처리를 하기 때문에 사용되는 데이터를 줄이기 위함.
* partitioning은 데이터를 폴더단위로 구분하여 저장.
* bucketing 컬럼의 해쉬값을 기준으로 데이터를 저장.
```SQL
CREATE TABLE table1 (
) PARTITIONED BY(part_col STRING);
```

## CBO
* 사용자 쿼리를 분석해 쿼리를 최적화.
```SQL
SELECT sum(v)
  FROM (
    SELECT t1.id,
           t1.value AS v
      FROM t1 JOIN t2
     WHERE t1.id = t2.id
       AND t2.id > 50000) inner
  ) outer
```
![](https://wikidocs.net/images/page/33619/CBO.png)
* 옵션
    * 기본적으로는 true인 상태
   ```SQL
    -- CBO 적용
    set hive.cbo.enable=true;
    -- 새로 생성되는 테이블과 INSERT 처리를 할 때 자동으로 통계정보 수집 
    set hive.stats.autogather=true;
    set hive.stats.fetch.column.stats=true;
    set hive.stats.fetch.partition.stats=true;
   ```
    * 통계정보를 자동으로 수집하지 않으면 ANALYZE을 통해 수동으로 수집.
   ```SQL
    ANALYZE TABLE sample_table PARTITION(yymmdd='20180201') COMPUTE STATISTICS for columns;
    ANALYZE TABLE sample_table PARTITION(yymmdd='20180201') COMPUTE STATISTICS; 
   ```
* EXPLAIN 쿼리 확인
    * EXPLAIN을 통해 CBO 적용 여부 확인 가능. (Plan optimized by CBO)
    ```SQL
        hive> explain INSERT OVERWRITE DIRECTORY 'hdfs:///user/data/location'
            > select name, count(1)
            >   from sample_table
            >  where yymmdd=20180201
            >  group by name
            >    
            > ;
        OK
        Plan optimized by CBO.    
    ```
* CBO 적용 불가
    * "Plan not optimized by CBO" 출력된 경우
    ```log
    2019-04-05T08:08:12,490 INFO  [main([])]: parse.BaseSemanticAnalyzer (:()) - Not invoking CBO because the statement has sort by
    ```
    * Transform은 사용 불가
    * Inline Lateral View Join 만 가능
    * UNIQUE 조인은 불가
    * 서브쿼리 사용 불가
    * Having 절에 select의 alias가 있는경우 사용 불가
    * Sort By 사용 불가