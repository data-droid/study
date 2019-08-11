# 보조 도구
* Counter
    * 맵리듀스 잡의 진행 상황을 확인
    * 입출력 상황을 확인할 수 있는 카운터 제공.
    ```TEXT
    18/10/19 08:23:02 INFO mapreduce.Job: Counters: 13
    Job Counters 
        Failed map tasks=4
        Killed reduce tasks=7
        Launched map tasks=4
        Other local map tasks=3
        Data-local map tasks=1
        Total time spent by all maps in occupied slots (ms)=327375
        Total time spent by all reduces in occupied slots (ms)=0
        Total time spent by all map tasks (ms)=7275
        Total time spent by all reduce tasks (ms)=0
        Total vcore-milliseconds taken by all map tasks=7275
        Total vcore-milliseconds taken by all reduce tasks=0
        Total megabyte-milliseconds taken by all map tasks=10476000
        Total megabyte-milliseconds taken by all reduce tasks=0
    ```
    
* 분산 캐시
    * job에서 공유되는 데이터를 이용해야 할 경우
    * 데이터를 조인해야 할 경우
    ```JAVA
    // 드라이버에 등록 
    Job job = new Job();
    ...
    job.addCacheFile(new Path(filename).toUri());

    // 맵퍼에서 사용
    Path[] localPaths = context.getLocalCacheFiles();
    ```
    
# 메모리 설정
* mapred-site.xml 파일을 통한 메모리 설정.

* Mapper와 Reducer 메모리 설정
    * yarn.app.mapreduce.am.resource.mb
        * 노드에서 애플리케이션 마스터를 실행할 때 할당하는 메모리
    * yarn.app.mapreduce.am.command-opts
        * 애플리케이션 마스터의 힙사이즈
    * mapreduce.map.memory.mb
        * 맵 컨테이너를 생성할 때 설정하는 메모리
    * mapreduce.map.java.opts
        * 맵 컨테이너를 생성할 때 설정하는 자바 옵션
        * Xmx 옵션을 이용하여 힙사이즈를 설정
        * 맵 컨테이너 메모리(mapreduce.map.momory.mb)의 80%로 설정
    * mapreduce.map.cpu.vcores
        * 맵 컨테이너에서 사용 가능한 가상 코어 개수
        * 기본값은 1
    * mapreduce.reduce.memory.mb
        * 리듀스 컨테이너를 생성할 때 설정하는 메모리
        * 맵 컨테이너 메모리(mapreduce.map.memory.mb)의 2배로 설정하는 것이 일반적
    * mapreduce.reduce.java.opts
        * 리듀스 컨테이너를 생성할 때 설정하는 자바 옵션
        * Xmx 옵션을 이용하여 힙사이즈를 설정
        * 리듀스 컨테이너 메모리의 80%로 설정
    * mapreduce.reduce.cpu.vcores
        * 리듀스 컨테이너의 코어 개수
    * mapred.child.java.opts
        * 맵과 리듀스 태스크의 JVM 실행 옵션, Heap 사이즈 설정
        * mapreduce.map.java.opts, mapreduce.reduce.java.opts 설정이 이 설정을 오버라이드 하여 설정
        * 기본 설정은 -Xmx200m
      
* MR 엔진 메모리 설정
![](https://msdnshared.blob.core.windows.net/media/MSDNBlogsFS/prod.evol.blogs.msdn.com/CommunityServer.Blogs.Components.WeblogFiles/00/00/01/66/50/7652.HadoopConfigurations_mr.png)

* Tez엔젠 메모리 설정
![](https://docs.microsoft.com/ja-jp/azure/hdinsight/media/hdinsight-hadoop-hive-out-of-memory-error-oom/hive-out-of-memory-error-oom-tez-container-memory.png)

* Mapper 설정
    * 매퍼 처리중 발생하는 임시 데이터 처리를 위한 설정
    * mapreduce.task.io.sort.mb
        * 맵의 출력 데이터를 저장할 환형 버퍼의 메모리 크기
        * 맵의 처리 결과를 설정한 메모리에 저장하고 있다가, io.sort.spill.percent 이상에 도달하면 임시 파일로 출력
            * split/sort 작업을 위한 예약 메모리
            * 매퍼가 소팅에 사용하는 버퍼 사이즈를 설정
            * 디스크에 쓰는 횟수가 줄어듬
    * mapreduce.map.sort.spill.percent
        * 맵의 출력데이터를 저장하는 버퍼(mapreduce.task.io.sort.mb)가 설정한 비율에 도달하면 로컬 디스크에 임시 파일 출력
    * mapreduce.task.io.sort.factor
        * 하나의 정렬된 출력 파일로 병합하기 위한 임시 파일의 개수
    * mapreduce.cluster.local.dir
        * 임시 파일이 저장되는 위치
```xml
  <property>
    <name>mapreduce.task.io.sort.mb</name>
    <value>200</value>
  </property>
  <property>
    <name>mapreduce.map.sort.spill.percent</name>
    <value>0.80</value>
  </property>
  <property>
    <name>mapreduce.task.io.sort.factor</name>
    <value>100</value>
  </property>
  <property>
    <name>mapreduce.cluster.local.dir</name>
    <value>${hadoop.tmp.dir}/mapred/temp</value>
  </property>
```
* 셔플 설정
    * mapreduce.reduce.shuffle.parallelcopies
        * 셔플단계에서 맵의 결과를 리듀서로 전달하는 스레드 개수
    * mapreduce.reduce.memory.total.bytes
        * 셔플단계에ㅓ 전달된 맵 데이터를 복사하는 메모리 버퍼 크기
        * default : 1024MB
    * mapreduce.reduce.shuffle.input.buffer.percent
        * 메모리 버퍼 크기의 비율이 넘어가면 파일로 저장하는 비율
        * default : 0.7
    * mapreduce.reduce.shuffle.memory.limit.percent
        * 메모리 버퍼 크기에 비해 파일의 비율이 값이 넘어서면 바로 디스크에 쓰여짐
        * default : 0.25
```xml
  <property>
    <name>mapreduce.reduce.shuffle.parallelcopies</name>
    <value>20</value>
  </property>
  <property>
    <name>mapreduce.reduce.memory.total.bytes</name>
    <value>1024MB</value>
  </property>
  <property>
    <name>mapreduce.reduce.shuffle.input.buffer.percent</name>
    <value>0.7</value>
  </property>
  <property>
    <name>mapreduce.reduce.shuffle.memory.limit.percent/name>
    <value>0.25</value>
  </property>
```

* 매퍼, 리듀서 개수 설정
    * mapreduce.job.maps
        * 매퍼 개수
    * mapreduce.job.reduces
        * 리듀서 개수
    * mapreduce.input.fileinputformat.split.maxsize
        * 매퍼에 입력가능한 최대 사이즈
        * 처리하려고 하는 총 size/mapreduce.input.fileinputformat.split.maxsize 매퍼 개수
    * mapreduce.input.fileinputformat.split.minsize
        * 매퍼에 입력가능한 최소 사이즈
        
* 단계별 메모리 설정
![](https://0x0fff.com/wp-content/uploads/2014/12/MapReduce-v3.png)

# 성능 최적화
* 매퍼, 리듀서 수 설정
    * 매퍼 하나에 많은 파일이 몰리거나, 메모리를 많이 사용하는 작업이어서 GC에 많은 시간이 걸려 느려짐.
    * 원천데이터 사이즈에 따라 매퍼, 리듀서 개수 조절 필요!
* 정렬 속성 튜닝(io.sort.* 튜닝)
    * 임시 결과 파일 개수를 줄이는 것으로 성능개선!
    * 맵 출력 데이터 병합, 네트워크 트래픽, 리듀서 병합 작업 시간을 줄임.
    * io.sort.mb를 늘리면 로컬에 저장될 데이터가 줄어듬!
```xml
  <property>
    <name>mapreduce.task.io.sort.mb</name>
    <value>200</value>
  </property>
  <property>
    <name>mapreduce.map.sort.spill.percent</name>
    <value>0.80</value>
  </property>
  <property>
    <name>mapreduce.task.io.sort.factor</name>
    <value>100</value>
  </property>
```
* 컴바이너 클래스
    * 리듀서 전송전 데이터를 줄여 네트워크 사용량을 줄이고 리듀서 작업 속도 향상.
    * `job.setCombinerClass(IntSumReducer.class);`
* 맵출력 데이터 압축
    * 데이터 압축을 통해 네트워크 트래픽 낮춤
```xml
  <property>
    <name>mapreduce.map.output.compress</name>
    <value>true</value>
  </property>
  <property>
    <name>mapreduce.map.output.compress.codec</name>
    <value>org.apache.hadoop.io.compress.SnappyCodec</value>
  </property>
```
* small file problem
    * 네임노드는 파일 메타데이터, 블록 관리에 많은 메모리 사용.
    * 작은 사이즈 파일이 여러개 존재하면 관리하는데 많은 메모리 사용 되고 맵리듀스 작업 처리중 많은 요청을 처리하게되어 병목발생
    * 작은 파일을 묶어 하나의 압축 파일로 생성
    * har 파일을 생성
    