# Forcasting at Scale
* 페이스북에서 나온 논문 (https://peerj.com/preprints/3190.pdf)
* 참고하기 좋은 [링크](https://gorakgarak.tistory.com/1255)

### Forcasting 이란?
* 웹 회사라면 네트워크, 상가라면 상품 등의 생산 수요 관리.
* 내년의 목표치를 설정.
* 비이상적인 트렌드를 탐지.

### Facebook에서 만들고자 한건?
* 현재 시계열 분석의 한계는?
    * 완전 자동화 되서 분석하는건 어려움.
    * 도메인 지식이 많이 필요함.
* 확장 가능한 시계열을 만들겠다!
* 최대한 많은 사람이 쓰고, 여러 feature에 제공가능한 도구를 만들겠다!
![](https://t1.daumcdn.net/cfile/tistory/99C52C4E5B16A17227)
* 분석가들은 필요한 모델링과 그 결과만 살펴보도록. 나머지는 알아서.

### 시계열 분석이란?
* 시계열 분석에 필요한 요소
![](https://t1.daumcdn.net/cfile/tistory/99BD67345B16A21932)
* 위는 페이스북의 이벤트인 친구신청이나 여러 상호작용을 점으로 나타낸 그래프인데 어떤 효과들이 있을까?
    * 연도, 요일, 연말, 시즌 효과.
    * 트렌드가 완전히 바뀌는 경우
* 기존 방법들!
![](https://t1.daumcdn.net/cfile/tistory/99FAE63B5B16A55D03)
    * arima
        * 계절성을 잘 포착하지 못함.
        * 최대차분개수, 자기회귀, 이동평균 값을 조정해야하지만 잘 모르는 사람들에게는 어려움.
    * ets, snaive
        * 직선만 찍찍

### Prophet
* Harvey & Peters 1990의 기본 적인 3개의 요소를 따름
![](https://t1.daumcdn.net/cfile/tistory/991B253B5B1714BF25)
    * g(t) : 반복적인 요소를 가지지 않는 트렌드.
    * s(t) : 요일, 연, 계절성과 같은 반복적인 변화
    * h(t) : Holiday와 같이 불규칙하게 영향을 미치는 요소
    * e : 정규분포를 따르는 잔차
* GAM 모델(Generalized Additive Model)과 비슷하며, 뭔가 새로운 것이 발견되었을 때 다시 모델을 쉽게 훈련할 수 있음.
* Prophet은 라인 하나를 기가막히게 그리는데 초점. ARIMA와 달리 내부 데이터 구조가 어떻게 생겼는지에 중점을 두지 않음.
* 장점
    * 유연성 : 계절성과 여러 기간들에 대한 예측을 쉽게 모델링 가능.
    * 모델을 차분해서 정규화 시킬 필요가 없음. 즉, 결측치를 안만들어 내도 됨.
    * 훈련이 빠르며 여러가지 상세한 모델을 만들어 낼 수 있음.
    * 회귀분석과 같은 느낌으로 어렵고 생소한 분석보다 적응하기 쉬움.
    
### g(t) : Trend
* Saturtating growth model
    * 특정 지역은 인터넷 접속이 가능한 모든 사용자가 다 페이스북 유저가 아닐까?
    * ![](https://t1.daumcdn.net/cfile/tistory/99236F355B1724CD0E)
        * C : Carrying Capacity
        * K : Growth Rate
        * M : Offset Parameter
    * C는 인터넷 사용자 증가에 따라 바뀔 수 있기 때문에 C(t)
    * 성장률 또한 지역마다 달라지기 때문에 아래와 같이 변함
    ![](https://t1.daumcdn.net/cfile/tistory/99FD93385B17284916)
    * 이를 조합하면 결국 piecewise logistic growth model로 됨.

* Piecewise linear model 
    * ![](https://t1.daumcdn.net/cfile/tistory/999CE5365B17294F15)
        * a(t) : adjustments를 위한 함수
        * m도 시간에 따라 성장률이 바뀌면 그에 맞춰 곡선을 이어지게 바꿔줘야하니 똑같이 조정함.
    * 미래 구간에서는 changepoint가 언제 나올지 알기 어려움.
    * Prophet에는 changepoint가 과거와 같이 미래에도 adjustment ~ Laplace(0,τ)의 주기로 나타난다고 가정함.
        
### s(t) : Seasonality
* 시계열 데이터는 주기성을 가짐.
* 푸리에 급수
    * ![](https://t1.daumcdn.net/cfile/tistory/9932F3395B175B8E0E)
        * P는 목적에 맞게 연단위면 365.25를 주단위면 7을 넣게 됨.
        * N을 얼만큼 넣느냐가 중요함.
            * 연단위면 N은 10, 주단위면 N은 3이 제일 잘 들어 맞는것 같다고 함.
        * N이 크면 패턴이 빠르게 바뀌고, 작으면 느리게 변함.
        ![](https://t1.daumcdn.net/cfile/tistory/992BF1335B175E6915)
        ![](https://t1.daumcdn.net/cfile/tistory/99CAAE335B175E6A0D)
    * 이를 풀기 위해 행렬식으로 표현
        * β = [a1, b1, . . . , aN , bN ]
        ![](https://t1.daumcdn.net/cfile/tistory/99697F3D5B175DC110)에 대해 ![](https://t1.daumcdn.net/cfile/tistory/990BFE3A5B175DF31E)

### h(t) : Holiday
* 휴일이 영향력을 가지고 있기 때문에 처리하기 위함.
* Prophet은 나라별 휴일을 가지고 있어 활용 가능하나, 한국은 없기 때문에 만들어 줘야함.
```python
```

### Model Fitting

### Model Evaluation
