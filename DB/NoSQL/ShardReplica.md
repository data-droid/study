# Sharding?

## 배경
1. DB서버를 싱글로 구성한 상황에서 서비스가 계속 커진다면?
    * 하드웨어를 더 좋은걸로 올린다.
2. 하드웨어의 한계가 왔다면?
    * Partitioning을 이용하여 데이터를 분산하자!

#### Partitioning
* 배경
    * VLDB(Very Large DBMS) 전체 DB가 하나의 DBMS에 못들어가는 경우
    * 하나의 DBMS가 많은 Table을 관리하여 느려짐!
* 큰 Table이나 인덱스를 관리하기 쉬운 단위로 분리하여 저장!
* 프로그래밍, 운영적인 복잡도는 증가하지만 많은 양의 데이터를 저장할 수 있음!
* 장점
    * Availability
        * 물리적인 partitioning으로 데이터 가용성 향상
    * Manageability
        * 큰 테이블들을 줄여 관리가 쉬워짐
    * Performance
        * 쿼리 성능 향상.
        * 대용량 데이터 write 환경에 효율 (하나의 서버에 쓰는게 아니라 여러 서버에 나눠 써서)
* 단점
    * 테이블 간 join 비용 증가
    * 테이블과 인덱스를 별도 파티션 할 수 없음.
* 종류
    * Horizontal Partition
        * 스키마는 유지한 채 데이터를 분산해서 저장!
            * 데이터를 Range, List, Composite, Hash 등으로 나눠 여러 서버에 저장
    * Vertical Partition
        * 테이블의 컬럼 기준으로 나눠 저장
            * id,col1,col2 -> {id,col1},{id,col2}
        * 자주사용하는 컬럼을 분리시킴

#### Sharding?
다시 돌아와서 샤딩이란?
* Horizontal(수평) Partitioning 중 하나
    * 스키마를 유지하고 데이터를 분산하여 저장함.
* 장점
    * 많은 데이터를 저장할 때 
    * write 성능이 중요할 때
* 단점
    * 운영관리 이슈가 upup
        * join 등의 작업이 어려움

#### 샤딩을 고민하기전!
* 하드웨어를 더 늘릴수 없는가? : Scale Up
* Read가 느리다면? : Replication or Cache
* Table의 일부 컬럼만 자주 사용한다면 ? : Vertically Partition or Data를 HOT, WARM, COLD로 분리.

#### +)Replication?
* 장점
    * 장애시 대응 가능!
    * read 성능 upup
* 단점
    * 운영관리 이슈가 upup
        * 복제본과 원본의 무결성을 어떻게??..