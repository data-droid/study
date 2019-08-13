# Kafka Install

### [KAFKA](https://kafka.apache.org/)?
* [참고](https://blog.voidmainvoid.net/179)
![](https://t1.daumcdn.net/cfile/tistory/99B7A03C5C20888D04)
* 링크드인에서 만들고 apache 프로젝트가 된 확장성이 뛰어난 분산 메시지큐!
* 특징
    * 분산 아키텍쳐 구성, Fault-tolerance한 architecture(with zookeeper), 데이터 유실 방지를 위한 구성이 잘되어 있음
    * AMQP, JMS API를 사용하지 않은 TCP기반 프로토콜 사용
    * Pub / Sub 메시징 모델을 채용
    * 읽기 / 쓰기 성능을 중시
    * Producer가 Batch형태로 broker로 메시지 전송이 가능하여 속도 개선
    * 파일 시스템에 메시지를 저장하므로, 데이터의 영속성 보장
    * Consume된 메시지를 곧바로 삭제하지 않고 offset을 통한 consumer-group별 개별 consume가능
* 구조
    * Broker : Kafka를 구성하는 각 서버 1대 = 1 broker
    * Topic : Data가 저장되는 곳
    * Producer : Broker에 data를 write하는 역할
    * Consumer : Broker에서 data를 read하는 역할
    * Consumer-Group : 메세지 소비자 묶음 단위(n consumers)
    * Zookeeper : Kafka를 운용하기 위한 Coordination service
    * Partition : topic이 복사(replicated)되어 나뉘어지는 단위
* 데이터 쓰기!
    * Producer는 1개이상의 partition에 나뉘어 데이터를 write
    * 각 Topic의 partition은 1개의 Leader Replica + 0개 이상의 follower Replica로 구성
        * Leader Replica에 데이터를 write, 다른 broker에 follower replica로 복제
        * Topic의 데이터(log) 중 replica 데이터는 log segment라는 파일(disk)에 기록
        * 메모리가 남아 있으면 페이지 캐시 사용
        
        ![](https://t1.daumcdn.net/cfile/tistory/996BD43F5C2089EA1C)

* 데이터 읽기!
    * Consumer는 Partition단위로 데이터를 병렬로 읽을 수 있음
        * 복수의 Consumer로 이루어진 Consumer group을 구성하여 1 topic의 데이터를 분산하여 처리 가능
        * Topic partition number >= Consumer Group number 일 경우만 가능
           (Topic partition number < Consumer Group number일 경우 1개 이상의 consumer는 유휴 상태가 됨)
           
        ![](https://t1.daumcdn.net/cfile/tistory/99D8AB4F5C208B1B28)
    
### 설치!
* OS선택
    * Kafka는 Java application 이기 때문에 다양한 OS에서 사용가능.
* Java 설치
    * Zookeeper와 Kafka를 사용하기위해 Java가 필요함.
    * Java version 8
* Zookeeper 설치
    * Kafka 클러스터의 메타데이터 저장하기위해 사용.
        * Broker의 경우 Broker와 Topic Metadata, Consumer(old)에서는 Consumer Metadata Partition Offsets
    * Zookeeper 3.4.6 release 사용.
    * Odd 개수로 설치 
* Version!
    * OS : CentOS 6.5
    * JAVA : jdk 1.8.0_161
    * Zookeeper
        * test1, test2, test3 서버
        * https://archive.apache.org/dist/zookeeper/zookeeper-3.4.6/
    * Scala
        * https://downloads.lightbend.com/scala/2.11.0/scala-2.11.0.tgz
    * Kafka
        * test1, test2, test3, test4, test5 서버
        * http://kafka.apache.org/downloads.html
        * version 0.9.0.1 
* Downloads
    * jdk, kafka, scala, zookeeper binary 파일을 각각 다운로드 하여 /home/programs에 저장.
    * 각각 압축 해제
    * 각각 soft link를 이용하여 jdk, kafka, scala, zookeeper 폴더 링크 만들기!
        * 버전 변경 시 roll back하기 편함
* Path 추가
    * bashrc append
        * JAVA_HOME : /home/programs/jdk
        * ZOOKEEPER_HOME : /home/programs/zookeeper
        * KAFKA_HOME = /home/programs/kafka
        * SCALA_HOME = /home/programs/scala
        * `PATH=$PATH:$ZOOKEEPER_HOME/bin:$KAFKA_HOME/bin:$SCALA_HOME/bin`
* ZOOKEEPER
    * TEST1, TEST2, TEST3 서버에 각각 설정
    * `$ZOOKEEPER_HOME/conf/zoo.cfg`
        * dataDir
            * 트랜잭션 로그 저장
            * 실제 해당 디렉토리에 myid 추가!
                * myid는 서버의 id
                    * 서버별로 uniq한 값을 가지면됨!
                * echo 1 > dataDir/myid (test1서버)
                * echo 2 > dataDir/myid (test2서버)
                * echo 3 > dataDir/myid (test3서버)
        * 서버 설정
            * server.id=host:port:port
                * server.1=test1:2888:3888
                * server.2=test2:2888:3888
                * server.3=test3:2888:3888
    * 실행
        * `ZOOKEEPER_HOME/bin/zkServer.sh start`
    * 테스트
        * `telnet localhost 2181`       
* KAFKA
    * Broker 설치
        * test1, test2, test3, test4, test5에 설치
        * `$KAFKA_HOME/config/server.properties`
            * General Broker
                * broker.id : Zookeeper의 myid와 같이 uniq한 숫자이면 됨. test에서는 1~5으로 설정함.
                * port : listener TCP port 9092
                * zookeeper.connect : 주키퍼 서버 및 포트정보,브로커의 메타데이터 저장을 위함.
                    * test1:2181,test2:2181,test3:2181
                    * kafka 외 다른 application도 zookeeper를 사용중이면 chroot path를 추가해서 사용
                * log.dirs : 메시지 보관 디렉토리. 여러 디렉토리를 설정해도됨. 둘이상의 경로가 지정되면 브로커는 파티션의 개수를 맞춰서 저장함 ( 디스크 사이즈로 나누는 것이 아님)
                    * /data/kafka-logs
                * num.recovery.threads.per.data.dir : 로그를 다루는 쓰레드의 개수. (default 1)
                    * 시작할때 각 파티션 로그를 열기위해, 종료할때 로그 세그먼트를 종료하기위해, 장애 발생후 각 파티션 로그 확인 하기 위해 사용.
                    
                    * log.dirs 별로 설정되기 때문에 전체 쓰레드 개수는 “설정 값 * log.dirs 수”
                * auto.create.topics.enable : topic을 자동으로 생성하게 함 (default true)
                    * producer가 메세지를 전달할 때, consumer가 read할 때, client가 request할 때 생성됨.
            * Topic Defaults
                * num.partitions : 토픽 생성시 몇개의 partition으로 할지. ( 자동생성 때만)
                    * partitions은 증가할수 있지만 감소하면 안됨. ( 그니깐 자동생성으로 생성X)
                    * partitions의 증가는 밸런싱, throughput이 증가하여 좋아 보일 수 있으나 디스크 space, network bandwidth 등을 고려하여 적정선을 찾을 필요는 있음. 또한, 각 파티션 별로 memory를 사용하기 때문에 리소스 및 leader 선출 시간 등도 문제가 됨. 
                    * 책에서는 partition당 하루 6GB 정도가 만족스러웠다 함.
                * log.retention.[ms,minutes,hours] : 저장된 메시지의 수명, 수명이 지나면 메시지 삭제.
                * log.retention.bytes : 삭제 전까지 파티션 별로 보관할 로그 바이트 수.초과하면 메시지 삭제
                * log.segment.bytes : 단일 로그의 최대 크기, 해당 크기만큼 되면 새로운 세그먼트 파일 생성. 토픽은 디렉토리안에 여러 세그먼트 파일로 저장됨. 토픽마다 적용되며 기본 값은 1GB
                * log.segment.[ms,minutes,hours] : 세그먼트 파일 close 시간.
                * message.max.bytes : 브로커가 받을 수 있는 메시지 최대 크기. (default 1M)
                    * consumer의 fetch.message.max.bytes와 연관하여 고려해야함.
    * 브로커 실행
        * `kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties`
    * Topic 생성
        * `kafka-topics.sh --create --zookeeper test1:2181 --replication-factor 1 --partitions 1 --topic test`
    * Topic 확인
        * `kafka-topics.sh--zookeeper test1:2181 --describe --topic test`
    * console-producer TEST
        * `kafka-console-producer.sh --broker-list test1:9092 --topic test`
    * console-consumer TEST
        * `kafka-console-consumer.sh --zookeeper test1:2181 --topic test --from-beginning`
        
### Hardware Selection
* Disk Throughput
    * I/O가 많이 일어나는 broker의 디스크가 중요함.
    * SSD 속도가 빠름/ HDD 경제적이며 용량이 큼 (RAID를 통해 여러 디스크를 동시 접근하여 성능 향상 가능) 
* Disk Capacity
    * 메시지 보관 주기 및 replica등을 설정하기 위해 Capacity 확인이 필요함.
* Memory
    * 컨슈머가 메시지 read를 위해 page cache가 많이 일어나 성능을 위해 memory가 많이 씀
    * kafka server의 경우는 memory heap이 크지 않음. 그외 memory는 os page cache 사용을 위해 유휴 상태로 둘 것.
* Networking
    * producer, consumer 를 고려하여 Network inbound, outbound 고려
* CPU
    * 기본으로 사용할 때 중요하지 않지만, 데이터 압축 및 압축 풀어야 하는 경우 사용.

### Kafka Cluster
* 브로커의 수?
    * 데이터 양의 따른 브로커 서버의 수 설정 및 그외 replication 등을 고려해야함.
    * 네트워크 사용량 고려 (peak 타임에 들어오고 consumer로 보내질 것도 고려해야함)
* 브로커 추가 Configuration
    * zookeeper.connection, broker.id만 수정하면 cluster 가능!
* OS 튜닝
    * virtual memory (write pause time vs memory capacity)
        * vm.swappiness = 1 (kernel 버전 (2.6.32-303)부터 0은 기존 out-of-memory가 되지 않는 한 swap하지 않는다에서 모든 경우에 절대 안한다로 바뀜)
        * vm.dirty_background_ratio lower than 10 (0은 계속 flush page하기 때문에 하면 안됨) 
        * vm.dirty_ratio higher than 20 (60~80이 적당, I/O pause가 될 수 있으므로 replication이 꼭 필요함)
        * 
        ```text
        vm.dirty_background_ratio = 5
        vm.dirty_ratio = 80
        vm.swappiness = 1
        // vmstat 확인!
        cat /proc/vmstat
        ```
    * Disk
        * XFS > EXT4
        * noatime : access time이 지속적으로 쓰여지기 때문에 noatime하는 것이 좋음.
        * 
        ```text
        tune2fs -m 0 -i 0 -c -1 /dev/device (waste space!)
        /dev/device       /mountpoint       ext4    defaults,noatime
        ```
    * Networking
        * Socket buffer
            * net.core.wmem_default, net.core.rmem_default = 128 KiB
            * net.core.wmem_max, net.core.rmem_max = 2 MiB
            * net.ipv4.tcp_rmem = 4096 65536 2048000 ( min, default, max )
            * net.ipv4.tcp_wmem = 4096 65536 2048000 ( min, default, max )
            * net.ipv4.tcp_window_scaling = 1 (clients가 데이터 변환을 효율적으로 함)
            * net.ipv4.tcp_max_syn_backlog > 1024 ( 동시 Connection을 증가함)
            * net.core.netdev_max_backlog > 1000 (멀티 기가비트 네트워크 환경에서 네트워크 트래픽을 더 사용 가능)
* Production Concerns
    * Garbage Collector Options
        * MaxGCPauseMillis : 최대 일시정지 시간 목표값 (default 200 밀리초) (책은 20 밀리초)
        * InitiatingHeapOccupancyPercent : mark 시작하는 임계점. (default 45 ) (책은 35)
    * Datacenter Layout
        * No Rack-awareness
        * 물리적인 서버들의 분리와 replication 등으로 장애 대응이 필요함.
    * Colocating Applications on Zookeeper
        * brokers, topics, partitions의 메타 정보를 저장함.
        * consumer 그룹이나 kafka cluster의 changes만 저장하면 traffic이 적을 것.
        * new consumer에서는 zookeeper에 offset을 commit 하지 않고 kafka에서 관리
            * zookeeper.connect -> bootstrap.server ( consumer config )

