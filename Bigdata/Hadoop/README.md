# 하둡
2006년 더그 커팅이 구글의 GFS와 MapReduce 논문을 참고하여 개발함.
큰 데이터를 클러스터에서 병렬로 동시에 처리하여 처리속도를 높이는 것을 목적으로 하는 분산 처리를 위한 오픈소스 프레임워크.

### 하둡의 구성요소
* Hadoop Common
    * 하둡의 다른 모듈을 지원하기 위한 공통 컴포넌트 모듈
* Hadoop HDFS
    * 분산 저장을 위한 모듈
    * 여러 서버를 하나의 서버처럼 묶어서 데이터를 저장
* Hadoop YARN
    * 병렬처리를 위한 클러스터 자원관리 및 스케쥴링
* Hadoop MapReduce
    * 저장된 데이터를 병렬 처리할 때 사용하는 모듈

### 하둡의 장단점
* 장점
    * Scale Out : 시스템 중단 없이, 확장 가능
    * Fault Tolerance : 특정 서버 장애에도 전체 시스템에 영향이 덜함.
    * 오프라인 배치 프로세싱에 최적화
* 단점
    * HDFS에 저장된 데이터를 변경 불가 (삭제 후 수정한 파일을 다시 넣어야 함)
    * 실시간 데이터 분석 같은 작업에는 부적합
    
### 하둡 버전 History
* Hadoop V1
    ![](https://wikidocs.net/images/page/26170/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2019-05-13_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_11.03.30.png)
    * 2011년에 발표.
    * 분산 저장, 병렬 처리 프레임워크를 정의함.
        * 분산 저장 : Namenode, DataNode
            * 네임노드 : 블록정보를 가지고 있는 메타데이터 관리 및 데이터 노드 관리
            * 데이터노드 : 데이터를 블록 단위로 나누워 저장.
            * 블록 데이터 : 복제를 통해 유실 대비.
        * 병렬 처리 : Job Tracker, Task Tracker
            * 잡트래커 : 전체 작업의 진행상황 관리, 자원 관리
            * 태스크 트래커 : 실제 작업 처리
            * Slot : 병렬 처리의 작업 단위로 맵 슬롯, 리듀스 슬롯의 개수가 정해짐.
    * 노트 4000대
* Hadoop V2
    ![](https://hadoop.apache.org/docs/r2.7.1/hadoop-yarn/hadoop-yarn-site/yarn_architecture.gif)
    * 2012년에 발표.
    * 잡트래커의 병목현상을 제거하기 위해 YARN 아키텍처 도입.
        * 클러스터 관리(기존 잡트래커의 기능 분리) : Resource Manager, Node Manager
        * 작업 관리 : Application Master, Container
            * Application Master : 어플리케이션의 라이프 사이클 관리.
            * Container : 작업 처리
            * 작업이 제출되면 어플리케이션 마스터가 생성되고, 어플리케이션 마스터가 리소스 매니저에 자원을 요청. 컨테이너가 해당 작업을 할당받아 처리.
        * MR이 아니여도 Spark, Hbase, Storm 등 다양한 컴포넌트 실행 가능.
    * 노드 1만대
* Hadoop V3
    ![](https://image.slidesharecdn.com/hadoopenhancementsusingnext-geniatechnologies-161214091750/95/hadoop-enhancements-using-next-gen-ia-technologies-7-638.jpg?cb=1481707636)
    * 2017년에 발표
    * Erasure Coding, Yarn Timeline Service V2 등이 도입.
        * Erasure Coding
            * 기존에는 복제 파일 3개를 통해 장애복구에 사용함.
            * parity block을 이용하여 원본대비 1.5배의 디스크만을 사용하여 효율 증가.
    * 스크립트 재작성 및 이해하기 쉬운 형태로 수정, Java8 지원, 네이티브 코드 최적화, 고가용성을 위한 네임노드 2대 이상 지원 등...