# Transaction
* 가능한 기능
    * BEGIN, COMMIT, ROLLBACK은 지원하지 않음. AUTO-COMMIT만 지원
    * ORC 포맷, BUCKETING 설정이 된 MANAGED TABLE에서만 지원
    * NON-ACID 세션에서는 ACID 테이블 접근 불가.
* 처리 순서
    * HDFS 파일의 변경/수정 지원 X
    * 데이터를 BASE파일에 기록하고, 트랜잭션(생성/수정/삭제) 발생할 때마다 델타(DELTA)파일에 내용을 기록
    * 파일을 읽을 때 베이스 파일에 델타 파일의 내요을 적용하여 수정된 내용 반환.
* COMPACTION
    * DELTA 파일을 정리하는 작업.
    * 트랜잭션이 수행되면 DELTA 파일이 커지면서 네임노드 관리포인트 증가.
    * DELTA 파일이 많아지면 MINOR COMPACTION 발생하여 DELTA 파일을 하나로 합침.
    * DELTA 파일이 점점 커지면 MAJOR COMPACTION이 발생하여 BASE 파일내용 수정.
    * 트랜잭션이 발생할 때 처리되지 않고, 조기적으로 수행되는 스케쥴에 따라 MR 잡으로 실행
* BASE 파일과 DIRECTORY 구조
    * base_ : 기본파일
    * delta_ : 델타 파일
    ```bash
    hive> dfs -ls -R /user/hive/warehouse/t;
    drwxr-xr-x   - ekoifman staff          0 2016-06-09 17:03 /user/hive/warehouse/t/base_0000022
    -rw-r--r--   1 ekoifman staff        602 2016-06-09 17:03 /user/hive/warehouse/t/base_0000022/bucket_00000
    drwxr-xr-x   - ekoifman staff          0 2016-06-09 17:06 /user/hive/warehouse/t/delta_0000023_0000023_0000
    -rw-r--r--   1 ekoifman staff        611 2016-06-09 17:06 /user/hive/warehouse/t/delta_0000023_0000023_0000/bucket_00000
    drwxr-xr-x   - ekoifman staff          0 2016-06-09 17:07 /user/hive/warehouse/t/delta_0000024_0000024_0000
    -rw-r--r--   1 ekoifman staff        610 2016-06-09 17:07 /user/hive/warehouse/t/delta_0000024_0000024_0000/bucket_00000    
    ```
* 설정
    * 테이블의 bucketing, transaction manager 설정 필요
    ```SQL
    set hive.support.concurrency=true;
    set hive.enforce.bucketing=true;
    set hive.exec.dynamic.partition.mode=nonstrict;
    set hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager;    
    ```
    * compaction 설정
    ```xml
    <property>
        <name>hive.compactor.initiator.on</name>
        <value>true</value>
    </property>
        <property>
        <name>hive.compactor.worker.threads</name>
        <value>3</value>
    </property>
    ```
* 테이블 생성
    * bucketing 설정, transaction 설정, orc 포맷
    ```SQL
    CREATE TABLE table_name (
      id                int,
      name              string
    )
    CLUSTERED BY (id) INTO 2 BUCKETS STORED AS ORC
    TBLPROPERTIES ("transactional"="true",
      "compactor.mapreduce.map.memory.mb"="2048",     -- specify compaction map job properties
      "compactorthreshold.hive.compactor.delta.num.threshold"="4",  -- trigger minor compaction if there are more than 4 delta directories
      "compactorthreshold.hive.compactor.delta.pct.threshold"="0.5" -- trigger major compaction if the ratio of size of delta files to
                                                                       -- size of base files is greater than 50%
    );
    ```
* 테이블의 COMPACTION 설정
```SQL
    ALTER TABLE table_name COMPACT 'minor' 
       WITH OVERWRITE TBLPROPERTIES ("compactor.mapreduce.map.memory.mb"="3072");  -- specify compaction map job properties
    ALTER TABLE table_name COMPACT 'major'
       WITH OVERWRITE TBLPROPERTIES ("tblprops.orc.compress.size"="8192");         -- change any other Hive table properties
```
* 트랜잭션 처리상황 확인
```SQL
    show transactions;
    show compactions;
    show locks;

    -- 테이블별, 파티션별 확인 
    SHOW LOCKS <TABLE_NAME>;
    SHOW LOCKS <TABLE_NAME> EXTENDED;
    SHOW LOCKS <TABLE_NAME> PARTITION (<PARTITION_DESC>);
    SHOW LOCKS <TABLE_NAME> PARTITION (<PARTITION_DESC>) EXTENDED;
```