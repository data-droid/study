## 한눈에 보는 머신러닝
### 1. 머신러닝이란?
```
명시적인 프로그래밍 없이 컴퓨터가 학습하는 능력을 갖추게하는 분야.

어떤 작업 T에 대한 컴퓨터 프로그램의 성능을 P로 측정했을 때, 경험 E로 인해 성능이 향상됐다면,
이 프로그램은 작업T와 성능 측정P에 대해 경험 E로 학습한 것이다.
```
### 2. 왜 머신러닝을 사용하는가?
* 기존 솔루션으로 많은 조정과 규칙이 필요한 문제 : 코드를 더 간단하게 만들며 전통적인 방법보다 더 잘 수행되도록 함.
* 전통적인 방식으로 해결방법이 없는 복잡한 문제
* 유동적인 환경 : 새로운 데이터에 적응
* 복잡한 문제와 대량의 데이터에서 Insight를 얻기 위해

### 3. 사례
* 생산 라인에서 제품 이미지를 통한 자동 분류 : 이미지 분류 - CNN(합성곱 신경망)
* 뇌종양 진단 : 시맨틱 분할 작업, CNN
* 뉴스기사 분류 : NLP(자연어처리), 텍스트분류 - RNN(순환 신경망), CNN, 트랜스포머
* 요약/부정적 코멘트 분류 : NLP
* 챗봇 : NLU(자연어 이해), Question-answering
* 예측 : regression(linear, polynomial), regression SVM, regression random forest, articifical neural network(인공신경망), 시퀀스분석(RNN, CNN, 트랜스포머)
* 부정거래 감지 : 이상치 탐지
* 군집 : Clustering
* 고차원을 의미있는 그래프로 : 차원축소
* 과거 이력 기반 추천 : Artificial neural network
* 지능형 게임 봇 : 강화학습(reinforcement learning)

### 4. 종류
* 지도/비지도/준지도/강화 학습
  * 지도 학습 : 레이블 제공 - 분류, 회귀 / K-nearest, linear regression, logistic regression, SVM(서포트 벡터 머신), decision tree, random forest, neural networks
  * 비지도 학습 : 레이블 미제공 - clustering(k-means, DBSCAN, hierarchial cluster analysis, outlier detection, novelty detection, one-class SVM, isolation forest), visualization and dimensionality reduction(PCA(주성분분석), kernel PCA, locally-linear embedding, t-SNE), association rule learning(Apriori, Eclat)
  * 준지도 학습 : 지도학습과 비지도학습의 조합 - deep belief network(심층 신뢰 신경망), restricted Boltzmann machine(RBM 제한된 볼츠만 머신)
  * 강화 학습 : 에이전트는 환경을 관찰해 행동을 하고, 결과에 따라 보상이나 벌점을 받는다. 가장 큰 보상을 받기위한 전략을 스스로 학습
* 배치/온라인 학습
  * 배치 학습 : 오프라인 학습
  * 온라인 학습 : 작은 단위로 학습 (미니배치)
* 사례기반/모델기반 학습
  * 사례 기반 : 스팸 필터링과 같이 유사도를 측정해서 새로운 데이터와 학습한 샘플을 비교하는 방식
  * 모델 기반 : 샘플들의 모델을 만들어 예측에 활용하는 것

### 5. 주요 도전 과제
* 충분하지 않은 양의 훈련 데이터
* 대표성 없는 훈련 데이터 : 
* 낮은 품질의 데이터 : error, noise, outlier
* 관련 없는 특성 : Feature engineering, 특성 선택/추출
* 훈련데이터의 과대 적합 : 하이퍼파라미터 - 학습 알고리즘의 파라미터으로 클 수록 평평한 모델을 얻어 과대 적합 확률은 낮아지나 좋은 모델이 될 수 없음
* 훈련데이터의 과소 적합 : 더 강력한 모델 필요/ 더 좋은 특성을 찾아야함 /규제를 낮춰야함

### 6. 테스트와 검증
* 훈련세트와 테스트세트로 나눠 훈련세트로 모델을 훈련하고 테스트세트로 테스트
* 하이퍼파라미터 튜닝 및 모델 선택
  * holdout validation : 새로운 데이터에 잘 동작하는지
    * train 세트에서 validation 세트를 빼고 다양한 하이퍼 파라미터 값을 가진 여러 모델을 훈련
    * validation 세트로 가장 높은 성능을 내는 모델을 선택
    * 선택한 모델로 다시 전체 train set으로 학습하여 최종 모델을 만들고 test세트로 평가하여 일반화 오차 추정
  * 위의 경우 검증 세트가 너무 작으면 모델이 정확하게 평가되지 않음 : cross-validation 활용
* 데이터 불일치
  * train세트의 일부를 떼어내어 또다른 세트로 만드는것 : train-dev세트 - 훈련 세트로 학습후 훈련-개발 세트로 평가




    * test세트에서 validation 세트를 빼고 다양한 하이퍼 파라미터 값을 가진 여러 모델을 훈련선택높은 
