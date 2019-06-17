# Spark?
* 인메모리기반 대용량 데이터 고속처리 엔진
* 범용 분산 클러스터 컴퓨팅 프레임워크

### 특징
* Speed
    * 인메모리 기반의 빠른 처리
* Ease of Use
    * 다양한 언어(Java, **Scala**, Python, R, SQL) 지원
* Generality
    * SQL, Streaming, ML, Graph 연산 등 다양한 컴포넌트 제공
* Run Everywhere
    * Yarn, Mesos, Kubernetes 등 다양한 클러스터에서 동작 가능
    * HDFS, Casandra, HBase 등 다양한 파일 시스템 지원
    * TXT, Json, ORC, Parquet 등 다양한 파일 포맷 지원
    
### 컴포넌트 구성
![](http://cfile25.uf.tistory.com/image/2140BE3C555DFB51305898)
* 스파크 코어
    * 스케쥴링, 메모리 관리, 장애 복구 지원
    * RDD, Dataset, DataFrame을 이용한 스파크 연산 처리
* 스파크 라이브러리
    * SparkSQL
        * SQL을 이용하여 RDD, Dataset, DataFrame 작업을 생성 및 처리함.
        * 하이브 메타스토어를 이용하여 SQL 작업 처리 가능.
    * Spark Streaming
        * 실시간 데이터 스트림 처리
        * 작은 사이즈로 쪼개어 RDD처럼 처리
    * MLib
        * Classification, Regression, Clustering, Collaborative Filtering 등 ML 알고리즘 지원
        * 모델 평가, 외부 데이터 Load 기능 지원.
    * GraphX
        * 분산 그래프 프로세싱 지원
* 클러스터 매니저
    * 스파크 작업을 운영하는 클러스터 관리자
        * Standalone, Mesos, Yarn, Kubernetes 등

# 스파크 구조
* 스파크 어플리케이션은 마스터-슬레이브 구조로 실행.
![](https://spark.apache.org/docs/latest/img/cluster-overview.png)

### 스파크 어플리케이션
* 스파크 실행 프로그램으로 드라이버와 익스큐터 프로세스로 실행되는 프로그램.
* 클러스터 매니저가 스파크 어플리케이션의 리소스를 배분
* 드라이버
    * 스파크 어플리케이션을 실행
    * main함수 실행하고 스파크 컨텍스트(SparkContext) 객체 생성
    * 스파크 어플리케이션 라이프사이클 관리
    * 사용자로 부터 입력을 받아 어플리케이션에 전달.
    * 사용자에게 작업 처리 결과를 리턴.
* 익스큐터
    * 실제 작업을 진행 (Yarn의 컨테이너와 비슷)
    * 태스크 단위로 작업 후 결과를 드라이버에 리턴
    * 오류 발생시 재작업 진행.
    
### 스파크 잡의 구성
![](https://camo.qiitausercontent.com/269cd805ef77a86e30c13e31000b099fd97d47a1/68747470733a2f2f71696974612d696d6167652d73746f72652e73332e616d617a6f6e6177732e636f6d2f302f373138352f61636661356538392d333461622d393336342d373135362d3232353239303063613830612e706e67)
* 잡
    * 스파크 어플리케이션으로 제출된 작업
* 스테이지
    * 잡을 작업의 단위에 따라 구분한 것
* 태스크
    * 익스큐터에서 실행되는 실제 작업.
    * 데이터를 읽거나, 필터링하는 작업.
    