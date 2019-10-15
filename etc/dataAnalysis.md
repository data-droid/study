# 데이터 분석 방법

## ARPU (Average Revenue Per User)

## ARPPU (Average Revenue Per Paid User)

## LTV (Lifetime Value)

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