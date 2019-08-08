# Kafka 세미나 
2018-05
* 실시간 비동기 Streaming Solution
* RabbitMQ 대비 약 300배 성능
* message length = 100byte / replication = 3
    * kafka와 RabbitMQ의 차이는 Cluster 관리를 누가하는가?
        * kafka => 주키퍼
        * RabbitMQ => RabbitMQ 직접
        * 즉, Cluster 관리에 대한 리소스가 크게 작용함. 따라서, 우리도 zookeeper서버 분리가 필요함.
* 특징
    * Message length가 작을 수록 성능 good
    * Broker 서버가 많을 수록 성능 good
    * replication이 작을 수록 성능 good / but 손실 가능성 UP
    * 파티션은 많을 수록 성능 good / but 파티션의 수를 줄일 수 없기 때문에 신중.
        * Consumer가 데이터를 받아오면 여러 파티션의 경우 개별 파티션에 순서는 유지되지만 파티션간의 순서는 유지되지 않음.
        * 파티션이 많은 만큼 Consumer도 같거나 작게 하여 성능을 최대로 사용할 수 있어야 함.
    * Producer는 Push / Consumer는 Pull 방식
* Kakao의 Kafka는?
    * 현재 0.11.0.2 버전 사용 중 / 추후 1.1.0으로 버전업 계획 중 
    * 180,000,000,000/day 이벤트 발생
    * in 205TB / out 350TB /day
    * 최대 초당 약 80만 이벤트를 컨슈밍함 -> 30개 파티션일 때
    * Brokers : 110~120대 (랙당 최대 2개씩 분산 설치되어 있음)
        * 12core, 32GB, SATA 사용 중
    * zookeeper : 5대 ( 16GB 웹 서버급이면 됨. 고사양 필요 없음 )
* Tips
    * 성능 이슈가 있을 경우 SSD보다는 Scale-out을 추천함.
    * 장애 공유
    * Shrinking ISR
        * In-Sync Replication 하는 Kafka는 복제본 사이에 Leader가 필요함. 브로커 서버들은 비슷한 양의 leader를 가지고 있어야 하지만 한쪽으로 몰릴 경우 문제가 발생함.
        * Kafka의 Info 로그 중 Shrinking ISR 관련 모니터링을 추가함
    * Rack Power
        * Rack 당 2대 이하의 브로커 서버를 둬 장애시 브로커 서버의 영향을 줄임.
        * 모든 Rack이 죽었을 경우 켜지는 순서에 따라 손실이 발생함
        * option 1. 마지막에 leader였던 아이를 기다렸다가 데이터 받기를 시작할 것인가?
        * option 2. 그냥 손실된 부분은 버리고 먼저 켜진애를 leader로 하여 서비스를 빨리 복구할 것인가?
    * 특정 partition의 메시지만 delay ?
        * Producer에서 key를 설정하여 하나의 partition으로만 들어가게 해서 생긴 문제.
        * 기본적으로 라운드로빈을 이용 중이니 특수한 상황이 아닌 경우 key 사용을 하지 말 것.
    * Producer의 Ack option
        * 0, 1, All이 있으나 상황에 따라 선택 ( 1 추천 )
            * 1의 경우 leader가 Ack를 주고 바로 죽으면 팔로워는 그 값을 쓰지 못하여 유실이 될 수 있음.
            * All의 경우 손실은 없으나 성능이 안좋음
            * 툴을 이용할 경우 default값이 툴마다 다름! 꼭 확인 필요!
                * Nifi = 0 / filebeat,flume = 1 등등..
    * Disk의 공간 부족
        * retention.hours = 168이 (7일) default
        * kakao는 3일 사용 중
        * global 값이기 때문에 rolling 해야함
            * Topic별로 retention.ms를 바꿔버리면 바로 설정 가능
    * Consumer 
        * Commit
        * partition마다 어디까지 읽었는지 저장하는 것
        * auto commit을 보통 사용
            * 중복 로그 발생 가능성이 있음.
            * 장애시 offset의 위치를 강제로 찍어서 읽게 할 수도 있음.
                * --to-offset 281047444
        * Lag
            * producer가 보낸 message양과 consumer가 받은 message 양의 차이를 나타냄.
            * Lag의 값이 계속 증가한다면 파티션을 늘리고 consumer의 수를 늘릴 필요 있음.
            * Burrow로 모니터링 가능.
    * Scale-out
        * 토픽이 추가되는 것이 아니라면 기존 partition들의 leader를 옮겨주는 작업이 필요함. 수작업 추천
    * exactly-once
        * 0.11 이상버전부터 기능 지원이 되나, 아직 사용중이지 않음.

![](http://tech.kakao.com/files/kemi-stats.jpg)
![](http://tech.kakao.com/files/kemi-log.jpg)