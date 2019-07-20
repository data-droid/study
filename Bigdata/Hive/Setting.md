# 설정
## 기본 설정값
* 실행 엔진, 큐 설정
    * MR 설정
        * ![](https://msdnshared.blob.core.windows.net/media/MSDNBlogsFS/prod.evol.blogs.msdn.com/CommunityServer.Blogs.Components.WeblogFiles/00/00/01/66/50/7652.HadoopConfigurations_mr.png)
        ```SQL
        set hive.execution.engine=mr;
        set mapred.job.queue.name=queueName;

        -- 맵리듀스 애플리케이션 마스터의 메모리 설정 
        set yarn.app.mapreduce.am.resource.mb=2048;  
        set yarn.app.mapreduce.am.command-opts=-Xmx1600m;

        -- 매퍼의 메모리 설정 
        set mapreduce.map.memory.mb=2048;  
        set mapreduce.map.java.opts=-Xmx1600m;

        -- 리듀서의 메모리 설정 
        set mapreduce.reduce.memory.mb=2048;  
        set mapreduce.reduce.java.opts=-Xmx1600m;
        ```
    * TEZ 설정
        * ![](https://docs.microsoft.com/ja-jp/azure/hdinsight/media/hdinsight-hadoop-hive-out-of-memory-error-oom/hive-out-of-memory-error-oom-tez-container-memory.png)
        ```SQL
        set hive.execution.engine=tez;
        set tez.queue.name=queueName;
        
        -- TEZ잡을 실행하는 애플리케이션 마스터의 메모리 설정은 다음과 같다. 
        set tez.am.resource.memory.mb=2048;  
        set tez.am.java.opts=-Xmx1600m;

        -- TEZ엔진을 처리하는 컨테이너의 메모리 설정은 다음과 같다. 
        set hive.tez.container.size=2048;  
        set hive.tez.java.opts=-Xmx1600m;  // container의 80%

        -- 출력결과를 소팅해야 할 때 사용하는 메모리 
        set tez.runtime.io.sort.mb=800;   // container의 40%

        -- 맵조인에 사용하는 메모리 
        set hive.auto.convert.join.noconditionaltask.size=600;  // container의 33%  
        
        -- TEZ 엔진 실행결과 출력
        set hive.tez.exec.print.summary=true;
        ```
* MAPPER 설정
    * DATA SIZE에 따른 MAPPER 개수 설정
        * 입력데이터의 사이즈가 1GB일때 최대 MAPPER SIZE가 256MB라면 MAPPER는 4개 생성.
        ```SQL
        -- MR 엔진의 매퍼당 최대 처리 사이즈 
        set mapreduce.input.fileinputformat.split.maxsize=268435456;
        set mapreduce.input.fileinputformat.split.minsize=134217728;

        -- TEZ 엔진의 매퍼당 최대 처리 사이즈 
        set tez.grouping.max-size=268435456;
        set tez.grouping.min-size=134217728;        
        ```
    * MAPPER 개수 고정
        * MAPPER ONLY잡에서 최종 생성되는 파일 개수 설정.
        * 리소스의 효율적인 사용을 위한 MAPPER 개수 고정.
        ```SQL
        -- MR 엔진의 매퍼 개수 설정
        set mapreduce.job.maps=1;

        -- TEZ 엔진의 매퍼 개수 설정 
        set tez.grouping.split-count=1;
        ```
* REDUCER 설정
    * 입력사이즈가 1GB일때 최대 처리사이즈가 256MB이면 REDUCER 4개를 이용.
    * REDUCER별 최대 처리 SIZE > 최대 REDUCER 개수 > REDUCER 개수
    ```SQL
    -- 리듀서 사용 개수 지정
    set mapreduce.job.reduces=100;
    -- 최대 리듀서 사용개수 
    set hive.exec.reducers.max=100;
    -- 리듀서별 최대 처리 사이즈 
    set hive.exec.reducers.bytes.per.reducer=268435456;    
    ```
* 압축 설정
    * HIVE 처리 결과를 압축하여 네트워크 및 저장에 이점.
    ```SQL
    -- 하이브 처리 결과를 압축할 것인지 설정 
    set hive.exec.compress.output=true;
    set hive.exec.compress.intermediate=true;
    set mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;    
    ```
* FILE MERGE 설정
    * avgsize 이하의 파일을 모아서 task 사이즈의 파일로 머지.
    ```SQL
    set hive.merge.mapfiles=true;   // 매퍼 only 결과 머지
    set hive.merge.mapredfiles=true;    // 맵리듀스 결과 머지 
    set hive.merge.tezfiles=true;       // tez 결과 머지 
    set hive.merge.size.per.task=256000000;
    set hive.merge.smallfiles.avgsize=32000000;    
    ```
* tmp 파일 위치 설정
    * 하이브 실행중 발생하는 임시 파일 위치.
    ```SQL
    set hive.exec.scratchdir=/tmp;
    set hive.exec.local.scratchdir=/tmp;
    set hive.exec.stagingdir=/tmp/hive-staging;    
    ```
* DINAMIC PARTITION 설정
    ```SQL
    set hive.exec.dynamic.partition=true;   -- 다이나믹 파티션 사용 여부 설정
    set hive.exec.dynamic.partition.mode=nonstrict; -- 스태틱 파티션과의 혼합 사용 여부 
    set hive.exec.max.dynamic.partitions.pernode=500;   -- 노드별 다이나믹 파티션 개수 설정 
    set hive.exec.max.dynamic.partitions=10000;     -- 전체 다이나믹 파티션 개수 설정    
    ```
* MSCK 처리 설정
    * MSCK 처리를 위한 데이터가 많을 경우 배치 사이즈를 조절하여 부하를 줄임.
    ```SQL
    -- 0으로 설정하면 모든 파티션을 복구한다. 설정한 값 만큼처리 
    set hive.msck.repair.batch.size=0;
    -- 파티션에 허용되지 않는 문자가 있으면 오류가 발생하는데, ignore 로 설정하면 무시하고 넘어간다. 
    set hive.msck.path.validation=ignore;    
    ```
* SUB DIRECTORY 조회 설정
    * 테이블 LOCATION의 데이터와 하위 모든 데이터 조회시.
    ```SQL
    set hive.supports.subdirectories=true;
    set mapred.input.dir.recursive=true;
    ```
* 그 외
    ```SQL
    -- 테이블 풀 스캔같은 복잡한 쿼리는 nonstrict 모드에서만 동작 
    -- msck repair 처리시 경우에 따라 nonstrict 모드에서만 동작 
    set hive.mapred.mode=strict | nonstrict;

    -- 프롬프트에 현재 데이터베이스명 표시 
    set hive.cli.print.current.db=true

    -- 조회 결과 출력시에 칼럼이름 출력 
    set hive.cli.print.header=true;    
    ```