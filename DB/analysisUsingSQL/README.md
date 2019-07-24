# 데이터 분석 Using SQL
사내 큐레이터, 기획자를 위한 SQL Study 자료입니다. 참고서적([데이터 분석을 위한 SQL 레시피](http://www.hanbit.co.kr/store/books/look.php?p_code=B8585882565))

* 목적 
    * 데이터 기반의 의사결정을 위한 데이터 활용
* 목표 
    * 데이터에 대한 이해 및 올바른 데이터 활용

* 데이터 기반의 의사결정
    * 빅데이터의 발전
        * HW발전으로 DISK는 점점 커지고 가격도 저렴해짐.
        * Cloud / Distribute Computing 환경의 발전
    * 활용
        * 사용자 개인화 행동패턴 분석
        * 추천 ( 광고타겟팅, 컨텐츠 추천 )
        * 예측 모델링
* 데이터 분석 환경
    * 다양한 데이터 저장소 ([DB-Engines Ranking](https://db-engines.com/en/ranking))
        * RDB, Key-value store, Document Store, Graph DBMS, Time Series DBMS, Object Oriented DBMS 등...
* 데이터 종류
    * 서비스 데이터
        * 서비스, 시스템 운용목적의 Application DB
        * 데이터 종류
            * Transaction : 구매, 리뷰 등
            * Master : 상품, 컨텐츠, 회원 정보 등
        * 특징
            * 갱신형 데이터 (삽입, 삭제, 수정)
            * 데이터 정밀도가 높음 (유실 없음)
                * 매출액처럼 정확한 분석 때 사용
            * 추출 시점마다 결과가 변함 (최신 값만 저장하기 때문에)
            * 시스템에 따라 Table 구조가 복잡함. (ER 다이어그램 등으로 테이블간 관계를 표현해야함.)
                * 보통 분석을 위해 Join이 많이 필요함.
            
    * 로그 데이터
        * 통계/분석 용으로 남기는 Time Series 데이터
        * 데이터 종류
            * Action을 JS등으로 추적하기 위한 통계 데이터.
            * 서버/시스템 로그 데이터
        * 특징
            * 누적형 데이터 (삽입, 삭제)
            * 데이터 정밀도가 낮음 (유실, 어뷰징, 중복 발생)
                * 웹사이트의 PV 등을 구할 때 사용
            * 추출 시점마다 항상 같은 결과 (누적 데이터이기 때문에)
            * 사용자IP, End-Point, URL, Referrer, Cookie등의 정보를 기록하며 다른 테이블간의 Relation이 약함.
                * 보통 분석에서 Join이 적게 들어감.
* 데이터 활용
    * KPI
        * 서비스의 목표 설계 및 관리
    * 서비스 기획
        * 사용자 행동기반 Trend 분석을 통한 서비스 개선
    * 예측
        * 과거 Trend 기반으로 앞으로의 Trend 예측
        
### Context
* HiveQL을 기반으로 아래 과정 진행
    * 데이터 가공
        * 데이터 추출을 위한 전처리
    * 데이터 추출
        * 올바른 의사결정을 위한 필요한 데이터 추출
    * 데이터 분석
        * KPI, 가설 검증, 비정상 탐지 등..