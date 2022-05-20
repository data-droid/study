(...ing)
# 그룹간 검정
* 가설검정에서 귀무가설의 진위여부를 판별하기 위해 검정 통계량 사용.

### 단계
1. 대립가설 설정
2. 유의수준 설정
3. 검정 통계량 설정
4. 유의수준에 대한 기각역 구함.
5. 샘플에 대한 검정 통계량 관찰.
6. 기각역에 있는지 판단하여 기각 결정.

### 분산분석
* 정규성, 등분산 가정을 만족하면 수행!
* 그룹간 variation, 그룹내 variation을 이용하여 분석.
* 그룹간 variation이 그룹내 variation보다 크다면 그룹 간 차이가 있다고 검정.
* 함수
    * aov 함수
        * aov(종속변수~그룹변수, data)
    * anova 함수
        * lm함수를 사용해 모형을 먼저 만든 후 aov와 동일.
* 종류 ([wiki](https://ko.wikipedia.org/wiki/%EB%B6%84%EC%82%B0_%EB%B6%84%EC%84%9D))
   * 일원분산분석(one-way ANOVA)
      * 종속변인 1개, 독립변인 집단 1개
      * ex) 가구 소득에 따른 소비정도 차이 (독립변인 : 가구소득집단 / 종속변인 : 소비)
   * 이원분산분석(two-way ANOVA)
      * 독립변인 수가 2개 이상일 경우 차이가 유의한지 검증
      * ex) 학력, 성별에 따른 휴대폰요금 차이 (독립변인 : 학력,성별 / 종속변인 : 휴대폰요금)
         * 주효과 : 학력, 성별
         * 상호작용효과 : 학력*성별
   * 다원변량분산분석(MANOVA)
      * 종속변인 수가 2개 이상일 경우 둘 이상의 집단간 차이검증
   * 공분산분석(ANCOVA)
      * 다원변량분산분석에서 특정 독립변인에 초점을 맞추고 다른 독립변인은 통제변수로 하여 분석하는 방법.
      * 특정한 사항을 제한하여 분산분석하는 방법.
* F분포
   * 분산의 비교를 통해 얻어진 분포 비율
   * 각 잡단의 모집단분산이 차이가 있는지 검정
   * 모집단평균이 차이가 있는지 검정
   * (군간변동)/(군내변동)
      * 군내변동이 크다면 집단간 평균차이를 확인하는 것이 어려움.
   * 분산분석에서는 집단간 분산의 동질성을 가정하기 때문에 분산의 차이가 크다면 차이를 유발한 변인을 찾아 제거 필요.
   * 가정
      * 정규성 가정
         * 각 모집단에서 변인 Y는 정규분포를 따름.
         * 각 모집단에서 Y의 평균은 다를 수 있음.
      * 분산의 동질성 가정
         * Y의 무집단 분산은 각 모집단에서 동일함.
      * 관찰의 독립성 가정
         * 각 모집단에서 크기가 각각 n1, n2인 표본들이 독립적으로 표본
   * 각 표본에서 산출된 모집단 분산 추정치의 비율를 구함. 이를 F 통계치라하며 특정한 확률분포를 따르는데 이것이 F