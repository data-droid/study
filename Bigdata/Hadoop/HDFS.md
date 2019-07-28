# HDFS(Hadoop Distributed File System)
범용 하드웨어에서 동작하고, 장애 복구 가능한 분산 파일 시스템. 실시간 처리보다는 배치처리를 위해 설계됨.

### 특징
* 블록 단위 저장
    * 데이터를 특정 블록사이즈로 나눠 저장함.
        * 블록사이즈보다 작은 파일은 그대로 저장.
        * 블록사이즈보다 큰 파일은 블록 단위로 나눠 저장하여 단일 디스크보다 큰 파일도 저장 가능.
* 블록 복제
    * 장애 복구를 위해 블록을 복제하여 저장함. (default 3)
    * 같은 Rack의 서버와 다른 Rack의 서버로 복제함.
    * 블록에 문제 발생시 다른 복제본을 이용하여 데이터를 복구함.
* 읽기 중심
    * HDFS는 데이터를 한번쓰고 여러번 읽는것을 목적
    * 파일 수정을 지원하지 않아 읽을 때 속도를 높임.
* 데이터 지역성
    * 네트워크 비용을 줄이기위해 HDFS(https://wikidocs.net/images/page/23582/hdfsarchitecture.png)

* 네임노드
    * 메타데이터 관리
        * 파일이름, 파일크기, 생성시간, 접근권한, 파일/그룹 소유자, 블록정보 등.
        * 각 데이터노드에서 전달한 메타데이터를 받아 전체 노드의 메타데이터 정보와 파일정보를 묶어서 관리.
        * dfs.name.dir에 보관되며 네임노드가 실행 될 때 읽어 메모리에 보관함.
        * 운영 중 발생한 수정사항은 메모리에 바로 적용되며, 다음 구동시 적용을 위해 주기적으로 Edits 파일로 저장함.
    * 메타데이터 파일 종류
        * Fsimage : 네임스페이스와 블록 정보
        * Edits : 파일 생성,삭제에 대한 트랜잭션 로그. 메모리에 저장하다가 주기적으로 생성함.
        ```
        # 네임노드의 메타 데이터가 다음과 같은 형태로 생성됨  
        -rw-r--r-- 1 hdfs hdfs 439M May 29 05:25 edits_0000000000385503885-0000000000387505111
        -rw-r--r-- 1 hdfs hdfs 456M May 30 08:25 edits_0000000000387505112-0000000000389525762
        -rw-r--r-- 1 hdfs hdfs  10M May 31 01:02 edits_inprogress_0000000000389525763
        -rw-r--r-- 1 hdfs hdfs  346 Nov 23  2017 fsimage_0000000000000000000
        -rw-r--r-- 1 hdfs hdfs   62 Nov 23  2017 fsimage_0000000000000000000.md5
        ```
    * 데이터 노드 관리
        * HeartBeat
            * Heartbeat(3초, dfs.hedfs.blockreport.intervalMsec)으로 HDFS에 저장된 파일에 대한 최신 정보를 유지함. 데이터 노드에 저장된 블록 목록과 각 블록이 로컬 디스크의 어디에 저장되어 있는지 정보를 가짐.

* 데이터 노드 
    * 파일을 블록단위로 저장하는 역할.
    * 네임노드에 하트비트와 블록리포트를 전달.
    * 블록 파일 저장 형태
        * 위치 : dfs.data.dir
        ```
        ./hdfs/current/BP-11233441/current/finalized/subdir187/subdir191:
        total 676K
        drwxr-xr-x   2 hdfs hdfs 4.0K Sep  8 04:30 .
        drwxr-xr-x 258 hdfs hdfs 8.0K Aug 31 22:21 ..
        -rw-r--r--   1 hdfs hdfs  40K Aug 31 22:46 blk_12345
        -rw-r--r--   1 hdfs hdfs  327 Aug 31 22:46 blk_12345_29082353.meta
        -rw-r--r--   1 hdfs hdfs  19K Aug 31 22:46 blk_12346
        -rw-r--r--   1 hdfs hdfs  155 Aug 31 22:46 blk_12346_29082375.meta
        -rw-r--r--   1 hdfs hdfs 262K Aug 31 22:46 blk_12347
        -rw-r--r--   1 hdfs hdfs 2.1K Aug 31 22:46 blk_12347_29082433.meta
        ```
* 네임노드 구동 과정
    1. Fsimage를 읽어 메모리에 적재
    2. Edits 파일을 읽어 변경내역을 반영
    3. 현재 메모리 상태를 스냅샷으로 생성하여 Fsimage파일 생성
    4. 데이터 노드로부터 블록리포트를 수신하여 매핑정보 생성
    5. 서비스 시작.
* 파일 읽기
    ![](http://www.corejavaguru.com/assets/images/content/hdfs-data-flow-read.png)
    1. 네임노드에 파일이 보관된 블록 위치 요청
    2. 네임노드가 블록 위치 반환
    3. 각 데이터 노드에 파일 블록 요청
        * 블록이 깨져 있으면 네임노드에 알리고 다른 블록 확인.
* 파일 쓰기
    ![](http://www.corejavaguru.com/assets/images/content/hdfs-data-flow-write.png)
    1. 네임노드에 파일 정보를 전송하고 파일의 블록을 써야할 노드 목록 요청
    2. 네임노드가 파일을 저장할 목록 반환
    3. 데이터 노드에 파일 쓰기 요청
        * 데이터 노드간 복제 진행.
        
## 블록
HDFS 파일은 지정한 크기의 블록으로 나눠져 독릭접으로 저장됨. 블록은 128MB와 같이 매우 큰단위이며 탐색 비용을 최소화 할 수 있음. 
* 블록 크기 파일 분할
    * 블록 크기보다 작은 파일은 단일 블록으로 저장됨 (실제 파일 크기의 블록)
    * 블록 크기보다 큰 파일은 블록 단위로 나누어 저장됨. (디스크 사이즈보다 더 큰 파일을 보관가능)
* 블록 스캐너
    * 데이터노드는 주기적으로 블록스캐너를 실행하여 블록의 체크섬을 확인하고 수정함.
* 블록 캐싱
    * 저장된 데이터 중 자주 읽는 블록은 블록 캐시로 메모리에 명시적으로 캐싱 가능.
    * 파일 단위로 캐싱 할 수 있어 조인에 사용되는 데이터들을 등록하여 읽기 성능을 높일 수 있음.
    ```
    $ hdfs cacheadmin
    Usage: bin/hdfs cacheadmin [COMMAND]
              [-addDirective -path <path> -pool <pool-name> [-force] [-replication <replication>] [-ttl <time-to-live>]]
              [-modifyDirective -id <id> [-path <path>] [-force] [-replication <replication>] [-pool <pool-name>] [-ttl <time-to-live>]]
              [-listDirectives [-stats] [-path <path>] [-pool <pool>] [-id <id>]
              [-removeDirective <id>]
              [-removeDirectives -path <path>]
              [-addPool <name> [-owner <owner>] [-group <group>] [-mode <mode>] [-limit <limit>] [-maxTtl <maxTtl>]
              [-modifyPool <name> [-owner <owner>] [-group <group>] [-mode <mode>] [-limit <limit>] [-maxTtl <maxTtl>]]
              [-removePool <name>]
              [-listPools [-stats] [<name>]]
              [-help <command-name>]

    # pool 등록 
    $ hdfs cacheadmin -addPool pool1
    Successfully added cache pool pool1.

    # path 등록
    $ hdfs cacheadmin -addDirective -path /user/hadoop/shs -pool pool1
    Added cache directive 1

    # 캐쉬 확인 
     hdfs cacheadmin -listDirectives
     Found 1 entry
     ID POOL    REPL EXPIRY  PATH             
      1 pool1      1 never   /user/hadoop/shs 
    ```

## Secondary Namenode
* 네임노드가 구동되면 Edits 파일이 주기적으로 생성되며 트랜잭션이 빈번하면 빠른 속도로 Edits 파일이 생성됨. 이는 네임노드의 디스크 부족 문제나 재구동시 느려지게 할 수도 있음.
* 세컨더리 네임노드는 Fsimage와 Edits파일을 주기적으로 머지하여 최신 블록의 상태로 파일 생성하여 Edits파일을 삭제하기 대문에 디스크 문제도 해결함.
![](https://charsyam.files.wordpress.com/2011/04/fsimage.png)