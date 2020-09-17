# 데이터 분석 방법

* 참고
    * 광고 단가
        * ![](https://blogfiles.pstatic.net/20150911_218/applift_1441965608556rWGly_PNG/Screen_Shot_2015-09-08_at_4.36.49_PM.png?type=w2)
            * CPM (Cost Per Mille) : 광고 1,000번 노출 당 과금
            * CPC (Cost Per Click) : 광고 클릭 당 과금
            * CPI (Cost Per Install) : 설치당 과금
            * CPA/CPE (Cost Per Action, Cost Per Engagement) : 앱/서비스 실행 및 행동에 대한 과금
    * 광고 종류
        * 배너 광고
        * 인터스티셜 : 스크린 가득 채우는 광고
        * 네이티브 광고 : 컨텐츠, 서비스 내에 자연스럽게 결합되어 있는 광고
        * 비디오 광고
        * 리치 미디어 : 형식에 제한 없는 광고 (게임, 동영상, 상호작용)
        * 리타겟팅
    * 그 외 용어
        * Impression : 노출
        * CTR (Click Through Rate) : 노출당 클릭률
        * CR (Conversion Rate) : 전환율
        * eCPM(effective Cost Per Mille) : 유효 1,000회 노출당 비용
        * eCPI(effective Cost Per Install) : 유효 앱 설치당 비용
    * 유저 관련 용어
        * 진성유저 : 지속적으로 사용하는 유저
        * DAU/MAU (Daily Active User / Monthly Active User) : 기간 별 사용자 수
        * ARPU (Average Revenue Per User) : 유저 1인당 평균 매출
        * LTV (Lifetime Value) : 고객 생애 가치. 유저가 사용하는 총 기간동안 지불하는 총액
        * UAC (User Acquisition Cost) : 유저 확보 비용. 유저 한명을 확보하기까지 지불한 총비용

## ARPU (Average Revenue Per User)
* 수익 전체를 가입자로 나눈 단위당 매출.
* 1명의 사용자가 지불한 평균 금액.
* 계산
    * 일간 : 일매출 / DAU
    * 월간 : 월매출 / MAU
    

## ARPPU (Average Revenue Per Paid User)
* ARPU와 달리 결제한 사용자로 나눈 단위당 매출
* 계산
    * 일간 : 일매출 / PU (Paid User) (일간 결제한 순수 사용자수)
    * 월간 : 월매출 / PU (Paid User) (월간 결제한 순수 사용자수)

## LTV (Lifetime Value)
![](https://newapplift-production.s3.amazonaws.com/comfy/cms/files/files/000/002/660/original/LTV_Formula.jpg)
* 유저 생애 가치
* CPI(Cost Per Install), 매출, ROI (Return on investment)와 같이 LTV도 수익성을 평가하는 지표
* 정의
    * 한명의 유저가 서비스에 진입하여 최종 이탈까지 생산해낸 가치
* 계산
    * ![](https://newapplift-production.s3.amazonaws.com/comfy/cms/files/files/000/002/660/original/LTV_Formula.jpg)
        * 기간별 유저당 평균 수익 / 유저 이탈률
        * ex) 한달동안 유저당 평균수익(ARPU)가 10,000이며, 이탈한 유저 비율(Churn)이 60%라면, LTV = 16666이라고 함.
* 위를 통해 CPI 전략 및 예산을 얼마나 사용할지 등에 도움을 줌
    * UA(User Acquistion)에 지출한 비용보다 새롭게 유입된 유저가 더 가치있는지.
    * 장기적으로 ROI가 적정한지
    * 유저 유입보다 리텐션에 집중해야 할 시점인지.
* 해석
    * LTV > CPI
        * 예산을 잘 사용하고 있으며, 마켓 수수료를 제외한 CPI가 LTV의 70% 이하라면 UA를 진행해볼만 함
    * LTV < CPI
        * 서비스 수익 모델 재점검이 필요함.


## AARRR (해적 지표)
![](http://www.wisetracker.co.kr/wp-content/uploads/2017/02/aarrr-1.png)
* 스타트업에 매력적인 분석방법
* 단계별 핵심 지표
    * Acquisition : 어떻게 우리 서비스를 접하고 있는가
        * DAU, MAU, New User
            * 사용자를 획득하는 단계
            * 여러 채널을 통해 얼마나 많은 사용자가 유입됬는지
            * 신규 사용자는 얼마나 되는지
    * Activation : 사용자가 처음 서비스를 이용할 때 긍정적인 경험을 제공하는가
        * Bounce Rate, Avg.PV, Avg.Duration, Signup
            * 서비스 첫페이지에서 이탈하는 비율
            * engagement는 어떻게 되는지
    * Retention : 서비스 재사용률은 어떻게 되는가
        * Retention Rate
            * 서비스 만족도를 가장 잘 대변
            * 푸시, 메일링, 리뉴얼 등으로 Retention을 올려야함.
    * Referral : 사용자가 자발적 공유를 하는가
        * Channel, SNS Share Rate
            * 서비스 공유를 통해 얼마나 많은 사용자를 다시 확보하는지
            * Acquisition과 맞물려 선순환을 만듬
    * Revenue : 매출으로 연결되고 있는가
        * Conversion
            * 수익과 연관된 행동을 하였는지.

* 목표 : Vanity Metrics(트래픽)지표 보다는 Actionable Metrics에 집중하여 개선 방향을 잡고 최적화 할 수 있도록 분석!

## Cohort 
![](http://analyticsmarketing.co.kr/wp-content/uploads/2017/08/%EA%B5%AC%EA%B8%80%EC%95%A0%EB%84%90%EB%A6%AC%ED%8B%B1%EC%8A%A4_%EC%BD%94%ED%98%B8%ED%8A%B8_01.png)
* cohort
    * 특정 기간에 특정 경험을 공유한 사용자의 집합
* cohort 분석
    * 특정 기간에 특정 경험을 공유한 집단 간 행동패턴 비교/분석
* 타겟 분석과 유사해 보이지만, "특정 기간"이 같다는 조건과 "특정 경험"을 기준으로 그룹을 분류 한다는 점이 차이
    * 사용자 그룹을 나눠서 뭘? 분석할 것인가!
        * 가입일 기준
            * 2019년 10월 1일 가입한 사용자들에 대한 Retention 분석
                * 특정 기간 : 2019년 10월 1일
                * 특정 경험 : 가입한 사용자
* 어떤 비교/분석을 할까?
    * 재사용률, 제거률 등
* 왜 필요한가?
    * 사용자 그룹의 차이를 보기 쉽게 나타내줌.
* 주의 할점
    * Cohort에서 많이하는 가입일 기준 retention 분석에서는 가입일 마다 사용자수가 다양하기 때문에 retention을 전체에 대한 비율보다는 바로 전 기간에 대한 비율로 볼 것!
* 목표 : 서비스가 잘 되고 있는지 분석!

## Funnel
![](http://mblogthumb3.phinf.naver.net/MjAxNzAzMjNfMzMg/MDAxNDkwMjYzMDU4NDU1.3C5gLckH2Vz9mwy0Q0uyCl9LAaDjTVt5xVz0bWe4afkg.19q79SKLxuXLzOvBSixHKHYwVYOK0NgegjzxtDM3LcYg.JPEG.applift/Naver_Retargeting1_v3.jpg?type=w800)
* 특정 목표를 달성하기 위한 유저행동을 단계별로 분석하는 방법
    * 각 단계별로 사용자가 이탈하기 때문에 좁아지는 깔떼기 형태 그래프
* 단계
    * 최종 목표 설정 (Referral)
        * 최종 목표부터 거꾸로 단계별 KPI를 잡을 수 있음
            * 10%만 남았으면 목표치 * 10 을 깔때기의 시작 부분의 KKPI
    * 깔떼기 단계 설정 (Retention, Activation, Acquistion)
    * 단계별 이탈률 측정
        * 이탈률이 높은 step부터 개선하면 됨!
            * 어디가 문제인지는 찾음!
            * 왜 문제인지는 따로 분석 필요
* 타사 활용
    * 랜딩페이지 전환률/공유 기능/ 초대기능
    * 성장 로드맵
        * 사용자 획득/ 활성화/ 유지,참여/ 부활
    * 회원가입 절차
* 목표 : 어디에 집중해서 서비스를 개선할지 분석!

## 기여도
* First Touch Point
   * 성과를 발생시킨 시점에 가장 처음 접속한 광고에 성과!
   * 최초 상품/브랜드 알리는 광고
* Last Touch Point
   * 성과를 발생시킨 시점에 가장 마지막 접속한 광고에 성과!
   * 구매할인 이벤트처럼 직접구매 유도시
* Linear
   * 성과를 발생시킨 시점에 접속한 모든 광고에 1/n
   * 정보공유, 바이럴마케팅 등
* Time Decay
   * 시간에 따라 마지막에 접속한 광고에 가중해서 성과!
   * 여행/숙박 상품과 같이 구매결정 소요시간이 긴 상품의 마케팅
* U-Shape
   * 성과를 발생시킨 시점에 처음과 마지막에 접속한 광고에 가중치 UP!
   * 신규고객, 기존고객 모두 타겟하며, 다양한 매체가 섞인경우
