# HDFS Command
사용자 커맨드, 운영자 커맨드, 디버그 커맨드로 구분됨. ([참고](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HDFSCommands.html))

### User Command
* `hdfs`, `hadoop` 쉘을 이용할 수 있음.
    * classpath : Hadoop jar 파일에 필요한 클래스 패스 출력
    * dfs : 파일 시스템 쉘 명령어
    * fetchdt : 네임노드의 델리게이션 토큰 확인
    * fsck : 파일 시스템 상태 체크
    * getconf : Config 정보 확인
    * groups : 사용자에 설정된 그룹 정보 확인
    * isSnapshottableDir : 스냅샷이 가능한 디렉토리 목록 확인
    * jmxget : JMX info 확인
    * oev : Offline Edits Viewer, Edits 파일의 상태 확인
    * oiv : Offline Image Viewer, 이미지 파일의 상태 확인(2.4 이상)
    * oiv_legacy : oiv 2.4미만의 상태 확인
    * snapshotDiff : HDFS 스냅샷 상태 확인
    * version : 버전 확인
* dfs 커맨드
    * 파일시스템 쉘.
    * `hdfs dfs`, `hadoop fs`, `hadoop dfs` 세가지로 가능.
    * cat / text : 파일 내용 확인 / 파일 내용 확인 (압축 파일도 확인 가능) 
    * appendToFile : 지정한 파일에 내용 append
    * checksum : 파일 체크섬 확인
    * chgrp : 파일 그룹 변경
    * chmod : 파일 모드 변경
    * chown : 파일 소유권 변경
    * count : 디렉토리/파일 개수, 파일사이즈 확인
    * df : 파일 시스템 용량 확인 ( 전체 용량 (복제본 포함) )
    * du / dus : 지정한 경로의 용량 확인(단일 파일 용량) / 지정한 경로의 하위 폴더를 포함한 용량 확인
    * expunge : 휴지통 비움
    * getfacl / setfacl : 디렉토리, 파일 ACL 확인/설정
    * getfattr / setfattr : 디렉토리, 파일 추가적인 속성 확인/설정
    * getmerge : 주어진 경로 파일들을 로컬의 파일로 머지
    * find : 파일 조회
    * ls / lsr : 디렉토리 파일 조회 / 디렉토리 하위 포함하여 조회
    * mkdir : 디렉토리 생성
    * copyToLocal / copyFromLocal : HDFS파일을 로컬에 복사 / 로컬 파일을 HDFS로 복사
    * cp/mv : 주어진 경로로 파일 복사/이동
    * moveToLocal / moveFromLocal : HDFS파일을 로컬로 이동/ 로컬 파일을 HDFS로 이동
    * get / put : copyToLocal / copyFromLocal 과 비슷
    * createSnapshot / deleteSnapshot / renameSnapshot : 스냅샷 생성 / 스냅샷 삭제 / 스냅샷 이름 변경
    * rm / rmdir / rmr : 파일삭제 / 디렉토리삭제 / 하위 경로까지 삭제
    * setrep : replication factor 설정
    * stat : 파일 stat 확인
    * tail : 파일의 마지막 1kbyte 출력
    * test : 파일/디렉토리 상태 확인
    * touchz : 0byte 파일 생성
    * truncate : 주어진 패턴에 따른 파일 삭제
    * usage : 명령어 사용법 확인
    * help : 전체 사용법 확인
* fsck 커맨드
    * path : 체크를 위한 경로
    * -list-corruptfileblocks : 커럽트 상태의 블록 출력
    * -delete : corrupt 파일 삭제
    * -move : 커럽트 블록을 /lost+found 폴더로 이동
    * -files : 체크한 파일 출력
    * -files -blocks : 블록 리포트 출력
    * -files -blocks -locations : 블록의 위치 출력
    * -files -blocks -racks : 블록의 랙정보 출력
    * -files -blocks -replicaDetails : 복제 개수 정보 출력
    * -files -blocks -upgradedomains : 블록의 도메인을 갱신
    * -includeSnapshots : 스냅샷을 포함해서 체크
    * -openforwrite : 쓰기 작업을 위한 열린 파일 출력
    * -storagepolicies : 블록 저장 정책 출력
    * -maintenance : 블록 관리 정보 출력
    * -blockId : 블록 ID 출력
    ```
        $ hdfs fsck <path> [-list-corruptfileblocks 
                     [-move | -delete | -openforwrite] 
                     [-files [-blocks [-locations | -racks]]]] 
                     [-includeSnapshots] [-storagepolicies] [-blockId <blk_Id>]
        $ hdfs fsck /
        $ hdfs fsck /user/hadoop/
        $ hdfs fsck /user -list-corruptfileblocks
        $ hdfs fsck /user -delete
        $ hdfs fsck /user -files
        $ hdfs fsck /user -files -blocks
        $ hdfs fsck /
        Status: HEALTHY
         Total size:    7683823089 B
         Total dirs:    3534
         Total files:   14454
         Total symlinks:        0
         Total blocks (validated):  14334 (avg. block size 536055 B)
         Minimally replicated blocks:   14334 (100.0 %)
         Over-replicated blocks:    0 (0.0 %)
         Under-replicated blocks:   14334 (100.0 %)
         Mis-replicated blocks:     0 (0.0 %)
         Default replication factor:    2
         Average block replication: 1.0
         Corrupt blocks:        0
         Missing replicas:      31288 (68.58095 %)
         Number of data-nodes:      1
         Number of racks:       1
        FSCK ended at Fri Dec 28 04:07:32 UTC 2018 in 172 milliseconds
    ```
    
### 운영자 커맨드
* 주로 실행, 설정 관련 명령어가 많음.
* 목록
    * namenode : 네임노드 실행
    * datanode : 데이터노드 실행
    * secondarynamenode : 세컨더리 네임노드 실행
    * balancer : HDFS 밸런서 실행
    * cacheadmin : 자주 읽은 데이터에 대한 캐시처리
    * crpyto : 암호화 처리
    * dfsadmin : HDFS관리를 위한 Admin 유틸리티 명령
    * dfsrouter : HDFS 연합 라우팅 실행
    * dfsrouteradmin : 데이터노드 라우팅 설정
    * haadmin : HA실행 명령어(QJM or NFS)
    * journalnode : QJM을 이용한 HA, 저널노드용 명령어
    * mover : 데이터 마이그레이션용 명령어
    * nfs3 : NFS3 게이트웨이 명령어
    * portmap : NFS3 게이트웨이 포트맵 명령어
    * storagepolicies : HDFS 저장정책 설정 명령어
    * zkfc : 주키퍼 failover 컨트롤러 실행
* dfsadmin 
    * hdfs의 관리를 위한 정보를 설정 및 변경 가능.
    ```
        $ hdfs dfsadmin 
        Usage: hdfs dfsadmin
        Note: Administrative commands can only be run as the HDFS superuser.
            [-report [-live] [-dead] [-decommissioning]]
            [-safemode <enter | leave | get | wait>]
            [-saveNamespace]
            [-rollEdits]
            [-restoreFailedStorage true|false|check]
            [-refreshNodes]
            [-setQuota <quota> <dirname>...<dirname>]
            [-clrQuota <dirname>...<dirname>]
            [-setSpaceQuota <quota> [-storageType <storagetype>] <dirname>...<dirname>]
            [-clrSpaceQuota [-storageType <storagetype>] <dirname>...<dirname>]
            [-finalizeUpgrade]
            [-rollingUpgrade [<query|prepare|finalize>]]
            [-refreshServiceAcl]
            [-refreshUserToGroupsMappings]
            [-refreshSuperUserGroupsConfiguration]
            [-refreshCallQueue]
            [-refresh <host:ipc_port> <key> [arg1..argn]
            [-reconfig <datanode|...> <host:ipc_port> <start|status>]
            [-printTopology]
            [-refreshNamenodes datanode_host:ipc_port]
            [-deleteBlockPool datanode_host:ipc_port blockpoolId [force]]
            [-setBalancerBandwidth <bandwidth in bytes per second>]
            [-fetchImage <local directory>]
            [-allowSnapshot <snapshotDir>]
            [-disallowSnapshot <snapshotDir>]
            [-shutdownDatanode <datanode_host:ipc_port> [upgrade]]
            [-getDatanodeInfo <datanode_host:ipc_port>]
            [-metasave filename]
            [-triggerBlockReport [-incremental] <datanode_host:ipc_port>]
            [-help [cmd]]
    ```

    * -report
        * Configured Capacity : 각 데이터 노드에서 HDFS에서 사용할 수 있게 할당 된 용량
        * Present Capacity : HDFS에서 사용할 수 있는 용량
            * Configured Capacity에서 Non DFS Used 용량을 뺀 실제 데이터 저장에 이용할 수 있는 용량
        * DFS Remaining : HDFS에서 남은 용량
        * DFS Used : HDFS에서 남은 용량 
        * Non DFS Used : 맵리듀스 임시파일, 작업 로그 등 데이터 노드에 저장된 블록데이터가 아닌 파일의 용량.
        * Xceivers : 현재 작업중인 블록의 개수
        * ```
           $ hdfs dfsadmin -report
           Configured Capacity: 165810782208 (154.42 GB)
           Present Capacity: 152727556096 (142.24 GB)
           DFS Remaining: 140297670656 (130.66 GB)
           DFS Used: 12429885440 (11.58 GB)
           DFS Used%: 8.14%
           Under replicated blocks: 18861
           Blocks with corrupt replicas: 0
           Missing blocks: 0
           Missing blocks (with replication factor 1): 0

           Live datanodes (1):

           Name: x.x.x.x:50010 (data_node)
           Hostname: data_node
           Decommission Status : Normal
           Configured Capacity: 165810782208 (154.42 GB)
           DFS Used: 12429885440 (11.58 GB)
           Non DFS Used: 13083226112 (12.18 GB)
           DFS Remaining: 140297670656 (130.66 GB)
           DFS Used%: 7.50%
           DFS Remaining%: 84.61%
           Configured Cache Capacity: 0 (0 B)
           Cache Used: 0 (0 B)
           Cache Remaining: 0 (0 B)
           Cache Used%: 100.00%
           Cache Remaining%: 0.00%
           Xceivers: 2
           Last contact: Thu Apr 25 08:29:50 UTC 2019
           ```
    * -safemode
        * get : 세이프 모드 상태 확인
        * enter : 세이프 모드 진입
        * leave : 세이프 모드 복구
        * wait : 세이프 모드이면 대기하다가, 세이프모드 끝나면 회복
        ```
        $ hdfs dfsadmin -safemode get
        Safe mode is OFF

        $ hdfs dfsadmin -safemode enter
        Safe mode is ON

        $ hdfs dfsadmin -safemode get
        Safe mode is ON

        $ hdfs dfsadmin -safemode leave
        Safe mode is OFF

        $ hdfs dfsadmin -safemode wait
        Safe mode is OFF
        ```
