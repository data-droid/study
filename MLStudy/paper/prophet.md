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
* 최대한 많은 사람이 쓰고, 여러 feature에 제공가능한 도구를 만들겠다
* ![](https://t1.daumcdn.net/cfile/tistory/99C52C4E5B16A17227)
* 분석가들은 필요한 모델링과 그 결과만 살펴보도록. 나머지는 알아서.

### 시계열 분석이란?
* 시계열 분석에 필요한 요소
* ![](https://t1.daumcdn.net/cfile/tistory/99BD67345B16A21932)
* 위는 페이스북의 이벤트인 친구신청이나 여러 상호작용을 점으로 나타낸 그래프인데 어떤 효과들이 있을까?
    * 연도, 요일, 연말, 시즌 효과.
    * 트렌드가 완전히 바뀌는 경우
* 기존 방법들!
* ![](https://t1.daumcdn.net/cfile/tistory/99FAE63B5B16A55D03)
    * arima
        * 계절성을 잘 포착하지 못함.
        * 최대차분개수, 자기회귀, 이동평균 값을 조정해야하지만 잘 모르는 사람들에게는 어려움.
    * ets, snaive
        * 직선만 찍찍

### Prophet
* Harvey & Peters 1990의 기본 적인 3개의 요소를 따름
* ![](https://t1.daumcdn.net/cfile/tistory/991B253B5B1714BF25)
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
    * ![](https://t1.daumcdn.net/cfile/tistory/99FD93385B17284916)
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
        * ![](https://t1.daumcdn.net/cfile/tistory/992BF1335B175E6915)
        * ![](https://t1.daumcdn.net/cfile/tistory/99CAAE335B175E6A0D)
    * 이를 풀기 위해 행렬식으로 표현
        * β = [a1, b1, . . . , aN , bN ]
        * ![](https://t1.daumcdn.net/cfile/tistory/99697F3D5B175DC110)에 대해 
        * ![](https://t1.daumcdn.net/cfile/tistory/990BFE3A5B175DF31E)

### h(t) : Holiday
* 휴일이 영향력을 가지고 있기 때문에 처리하기 위함.
* Prophet은 나라별 휴일을 가지고 있어 활용 가능하나, 한국은 없기 때문에 만들어 줘야함. (hdays.py에 추가하면 됨!)
```python
# ------------ Holidays in South Korea ---------------------
class Korea(HolidayBase):
    """
    Implement public holidays in South Korea
    Reference:
    https://en.wikipedia.org/wiki/Public_holidays_in_South_Korea
    """

    def __init__(self, **kwargs):
        self.country = "KR"
        HolidayBase.__init__(self, **kwargs)

    def _populate(self, year):
        # Sinjeong
        name = "New Year's Day"
        self[date(year, 1, 1)] = name

        # Seolnal
        name = "Lunar New Year"
        for offset in range(-1, 2, 1):
            ds = LunarDate(year + offset, 1, 1).toSolarDate()
            if ds.year == year:
                self[ds-timedelta(days=1)] = name
                self[ds] = name
                self[ds+timedelta(days=1)] = name

        # Samiljeol
        name = "Independence Movement Day"
        self[date(year, 3, 1)] = name


        # Children's Day
        name = "Children's Day"
        self[date(year, 5, 5)] = name

        # Buddha's Birthday
        name = "Buddha's Birthday"
        for offset in range(-1, 2, 1):
            ds = LunarDate(year + offset, 4, 8).toSolarDate()
            if ds.year == year:
                self[ds] = name

        # Memorial Day
        name = "Memorial Day"
        self[date(year, 6, 6)] = name

        # Constitution Day
        name = "Constitution Day"
        self[date(year, 7, 17)] = name

        # Liberation Day
        name = "Liberation Day"
        self[date(year, 8, 15)] = name
        
        # Chuseok
        name = "Chuseok"
        for offset in range(-1, 2, 1):
            ds = LunarDate(year + offset, 8, 15).toSolarDate()
            if ds.year == year:
                self[ds-timedelta(days=1)] = name
                self[ds] = name
                self[ds+timedelta(days=1)] = name

        # National Foundation Day
        name = "National Foundation Day"
        self[date(year, 10, 3)] = name

        # Hangul Day
        name = "Hangul Day"
        self[date(year, 10, 9)] = name

        # Christmas
        name = "Christmas"
        self[date(year, 12, 25)] = name


class KR(Korea):
    pass
```

### Model Fitting
* 주기성과 휴일에 관한 정보가 행렬 X
* change point에 대한 정보가 행렬 A
* ![](https://t1.daumcdn.net/cfile/tistory/995DF2415B17623424)
* 위의 결과는 아래와 같음.
* ![](https://t1.daumcdn.net/cfile/tistory/99C4F6395B17633C2F)
* Prophet은 주단위, 연단위 주기성을 앞에서 본 것들 보다 잘 포착하고 있음. 다만, 2014년 데이터가 2013년을 통해 오버피팅 되어 보임.
* ![](https://t1.daumcdn.net/cfile/tistory/999559455B1763C92B)
* 모든 데이터로 트렌드를 나타낸 그래프로 점선으로 표현된 부분은 예측 부분.
* ![](https://t1.daumcdn.net/cfile/tistory/999BC1495B17648032)
* 각 요소별로 어떤 변화가 있는지 확인 가능함.

* 위를 통해 알수 있는 부분
    * Capacities : 수요, 트래픽
    * ChangePoints : 트렌드가 변경되는 점
    * Holiday and Seasonality : 영향을 많이 미치는 휴일
    * Smoothing Parameter : 주기마다 변동을 얼마나 나타내야 하는지
* τ 를 바꿔서 주기의 크기를 조절, σ 를 바꿔서 주기성을 강/약을 조절.
* Prophet은 **직관**은 극대화시키고, 자동화시키는 부분은 자동화하여 편리함.

### Model Evaluation
* ![](https://t1.daumcdn.net/cfile/tistory/993E194C5B176B5E06)
* T까지의 자료가 있고 h를 예측할때 그 사이 거리.
* 페이스북은 MAPE(Mean Absolute Percentage Error)를 선호함.
* ![](https://t1.daumcdn.net/cfile/tistory/9961C43B5B17739926)
    * 지역적 평활화가 되어있어 에러가 있으면 예측 전구간에서 일정하게 발생해야함.
    * 시간이 지날수록 h 예측은 조금씩 떨어져야한다는 점.
* Simulated Historical Forecasts
    * 윈도우 사이즈가 작은 여러 데이터셋으로 모아 예측하면서 생기는 에러들로 어느 지점으로 수렴할 것.
    * 윈도우가 작으면 에러가 막 바뀌고 너무 크면 다 비슷하기 때문에 대충 전체기간의 절반정도를 대상으로 사이즈를 잡고 나올수 있는 예측치를 측정.
    * ![](https://t1.daumcdn.net/cfile/tistory/99B67A465B1776D530)
* 모델 튜닝
    * Baseline 모델보다 뭔가 떨어질 경우
        * trend, seasonality 수정
    * 특정일자 예측률이 떨어질 경우
        * 아웃라이어 제거
    * 특정 cutoff(연말 등)에 예측률이 떨어질 경우
        * changepoint를 추가