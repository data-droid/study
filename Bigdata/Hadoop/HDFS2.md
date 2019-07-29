# HDFS Federation
* 하둡 V2부터 지원
* 네임스페이스(디렉토리) 단위로 네임노드를 등록하여 사용하는 것.

![](https://hadoop.apache.org/docs/r2.7.2/hadoop-project-dist/hadoop-hdfs/images/federation.gif)

* 파일, 디렉토리의 정보를 가지는 네임스페이스와 블록 정보를 가지는 블록 풀을 각각의 네임노드가 독립적으로 관리
* 하나의 네임노드가 문제가 생겨도 다른 네임노드에 영향을 주지 않음.

# HDFS HA
* 하둡 V2부터 지원
* Why?
    * HDFS Namenode는 단일 실패 지점(SFP)
        * Namenode 장애시 모든 작업은 중지되며, 파일을 읽고 쓸수 없음.
* 이중화된 두서버에 하나는 Active 네임노드, 하나는 Standby 네임노드로 사용
* 두 서버 모두 동일한 메타데이터를 유지하고, 공유 스토리지를 이용하여 Edits 파일을 공유함.
* Active 네임노드가 네임노드 역할을 수행하며, Standby 노드는 동일한 메타데이터 정보를 유지하다가, Active 장애시 Standby가 Active로 변경되어 작동됨.
* Active 네임노드의 장애 확인은 주키퍼를 이용함.

### QJM (Quorum Journal Manager)
![](https://hadoopabcd.files.wordpress.com/2015/02/quorum-journal-with-zk.png)
* HDFS 전용 구현체
* HA edits 로그를 지원하기 위해 설계 (HDFS의 권장 옵션)
* Journal Node에서 동작하며, 각 edits 로그는 전체 Journal Node에 동시에 쓰임.
* Journal Node는 보통 3개로 구성하며, 1개가 손상되어도 문제가 없음.
* 주키퍼 방식과 유사하지만 QJM은 주키퍼를 사용하지 않고 해당 기능을 구현함.
    * NameNode의 active 선출은 주키퍼를 이용함.
* Stanby Namenode를 활성화 시키는 전환 작업은 장애복구 컨트롤러(Failover Controller)라는 객체로 관리됨.
    * 주키퍼는 단하나의 네임노드만 Active 상태에 있다는 것을 보장하기 위해 사용
    
### NFS (Network File System)
![](https://hadoopabcd.files.wordpress.com/2015/02/sharededit-with-zk.png)
* Edits 파일을 공유 스토리지를 이용하여 공유하는 방법
* Edits 로그를 공유하고 펜싱을 이용하여 하나의 네임노드만 edits로그를 기록함.

# HDFS Safe Mode
* 데이터노드를 수정할 수 없는 상태.
* 읽기 전용상태가 되고, 데이터 추가/수정이 불가능하며 데이터 복제도 일어나지 않음.
* 관리자가 직접 세이프모드로 설정 할수도 있고, 네임노드의 장애로 자동으로 세이프 모드로 전환되기도 함.
```bash
  $ hadoop fs -put ./sample.txt /user/sample.txt
  put: Cannot create file/user/sample2.txt._COPYING_. Name node is in safe mode.
```

### Safe Mode command
```
# 세이프 모드 상태 확인 
$ hdfs dfsadmin -safemode get
Safe mode is OFF

# 세이프 모드 진입 
$ hdfs dfsadmin -safemode enter
Safe mode is ON

# 세이프 모드 해제 
$ hdfs dfsadmin -safemode leave
Safe mode is OFF
```

### Safe Mode Recovery
* `fsck` 명령어로 무결성을 체크
* `hdfs dfsadmin -report` 명령어로 각 데이터 노드의 상태를 확인하여 해결한후 세이프 모드 해제

# HDFS 데이터 블록 관리
* CORRUPT 블록
    * HDFS는 Heartbeat를 통해 데이터블록에 문제를 감지하고 자동으로 복구를 진행
    * 다른 데이터 노드에 복제된 데이터를 가져와 복구함.
    * 모든 복제 블록에 문제가 생겨서 복구하지 못하는 경우 "Corrupt" 상태가 됨.
    * corrupt 상태파일을 지워주고 원본을 다시 올려줘야함.
* CORRUPT 상태확인
    * `fsck`를 이용해 체크 가능.
    
```
   # 루트의 상태 체크 
   $ hdfs fsck /

   # /user/hadoop/ 디렉토리의 상태 체크 
   $ hdfs fsck /user/hadoop/

    Status: HEALTHY
    Total size:    1378743129 B
    Total dirs:    612
    Total files:   2039
    Total symlinks:        0
    Total blocks (validated):  2039 (avg. block size 676185 B)
    Minimally replicated blocks:   2039 (100.0 %)
    Over-replicated blocks:    0 (0.0 %)
    Under-replicated blocks:   2039 (100.0 %)
    Mis-replicated blocks:     0 (0.0 %)
    Default replication factor:    2
    Average block replication: 1.0
    Corrupt blocks:        0
    Missing replicas:      4004 (66.258484 %)
    Number of data-nodes:      1
    Number of racks:       1
    FSCK ended at Thu Dec 06 05:31:42 UTC 2018 in 37 milliseconds

    The filesystem under path '/user/hadoop' is HEALTHY
```
    * 상태 확인 후 CORRUPT 상태이면 `hdfs fsck -delete` 명령어를 통해 courrupt 블록 삭제
    * corrupt 상태를 방지하기 위해 복제 개수를 미리 늘려놓는 것도 방법.
```
    The filesystem under path '/user/hadoop' is CORRUPT

    # 커럽트 상태의 파일 삭제 
    $ hdfs fsck -delete

    # /user/hadoop/ 의 복제 개수를 5로 조정 
    $ hadoop fs -setrep 5 /user/hadoop/
    # /user/hadoop/ 하위의 모든 파일의 복제 개수를 조정 
    $ hadoop fs -setrep 5 -R /user/hadoop/
```
* 복제 개수 부족 상태
    * 파일에 지정된 복제 개수만큼 데이터 블록 복제가 되지 않았을 때
        * 데이터 노드 개수보다 복제 개수가 많은 경우
    * `fsck` 명령어로 복제 개수 부족한 파일 확인 가능.
    * `hadoop fs -setrep` 명령어로 처리
```
    # /user/hadoop/ 의 복제 개수를 5로 조정 
    $ hadoop fs -setrep 5 /user/hadoop/

    # /user/hadoop/ 하위의 모든 파일의 복제 개수를 조정 
    $ hadoop fs -setrep 5 -R /user/hadoop/
```

# HDFS 휴지통
* `/user/유저명/.Trash`로 삭제한 파일이 이동되며, 해당 디렉토리에 있는 파일은 복구 가능함.
* 설정
    * fs.trash.interval
        * 체크포인트를 삭제하는 시간 간격(분)
        * 0이면 휴지통 기능 OFF
    * fs.trash.checkpoint.interval
        * 체크포인트를 확인하는 시간 간격(분)
        * fs.trash.interval보다 같거나 작음.
        * 체크포인터가 실행될 때마다 체크포인트를 생성하고, 유효기간이 지난 체크포인트 삭제
    * core-site.xml
    ```xml
        <property>
        <name>fs.trash.interval</name>
        <value>1440</value>
        </property>
        <property>
            <name>fs.trash.checkpoint.interval</name>
            <value>120</value>
        </property>
    ```
    * 휴지통 명령
    ```
        # 휴지통을 비움. 
        $ hadoop fs -expunge

        # 휴지통을 이용하지 않고 삭제 
        $ hadoop fs -rm -skipTrash /user/data/file
    ```