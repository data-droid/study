# 확률변수
확률 실험의 결과를 수치적으로 나타내는 함수.

* 이산형 확률변수
  * 유한한 값을 갖는 경우
  * 확률질량함수 : px = P(X=x)
    * px는 0~1사이 값을 가지며 모든 x값에 대해 더하면 1이 나옴
* 연속형 확률변수
  * 특정 구간안의 모든 값을 다 갖는 경우
  * 확률밀도함수 : P(X in [a,b]) = ![](https://wikimedia.org/api/rest_v1/media/math/render/svg/cac1fab0e1353f0e514fe66f83ff8c0fb3419fd3)
* 기대값과 분산
  * 기대값
    * 확률변수 X에 대한 기대값은 일종의 가중평균으로 해석될 수 있음.
    * E(X) = ΣxP(X=x)
    * ex) 주사위 세번 던질때 1이 나오는 횟수의 기대값
      * μ = 0 * 125/216 + 1 * 25/72 + 2 * 5/72 + 3 * 1/216 = 0.5
  * 분산
    * Var(X) = E(X-E(X))^2
  * 성질
    * E(c) = c
    * E(aX+b) = aE(X)+b
    * Var(aX+b) = a^2Var(X)
* 이산형 확률분포
  * 이항분포
    * 확률실험이 두 개의 결과만 갖는 시행을 베르누이시행이라 함.
    * ![](https://drive.google.com/a/insilicogen.com/uc?id=177U-AXvyDjoVu4qWZMHDxYx3EafEqjV1&export=download)
    * B(n,p) 일 때, E(X) = np, Var(X) = np(1-p) 임
  * 포아송 분포
    * 단위시간이나 단위공간에서 일어나는 특정 사건의 횟수에 대한 확률모형
      * ex) 사거리에 주어진 시간동안 지나가는 자동차 대수, 페이지당 오타수 등..
    * ![](https://mblogthumb-phinf.pstatic.net/MjAxODAzMjRfMTA5/MDAxNTIxODk4MDAxNzAx.A1FRcPzCFDl4z3v22JQx0UyreL0qqGmD2GDff0_a-dIg.bDfC2TgVV2fEwbSUFWerb6EpuB027W61LrSvwJNm_nog.PNG.cj3024/SmartSelectImage_2018-03-24-22-26-36.png?type=w800)
    * E(X) = λ, Var(X) = λ
* 그 외 이산형 분포
  * 기하분포
    * '성공'확률이 p인 베르누이 시행을 독립적으로 반복할 때, 첫 번째 '성공'이 일어날때까지의 시행횟수를 X라고 할 때.
      * f(x) = p(1-p)^(x-1)
      * E(X) = 1/p, Var(X) = (1-p)/p^2
  * 음이항 분포
    * '성공' 확률이 p인 베르누이 시행을 독립적으로 반복할 때, r개의 '성공'을 얻을 때까지 필요한 시행횟수를 X라고 할 때.
      * f(x;r,p) = (x-1 r-1)p^r(1-p)^(x-r)
  * 초기하분포
    * 어떤 주머니에 r개의 빨간 공과 w개의 하얀공이 있는경우 n개의 공을 무작위 비복원추출시 X를 빨간공의 개수라고 할 때.
* 연속형 확률분포
  * 균일 분포
  * 지수 분포
  * 정규 분포
* 결합 분포 
  * 결합 확률질량함수
    * P((X,Y)) = Σp(x,y)
  * 결합 확률밀도함수
    * P(X,Y) = ∫∫f(x,y)dxdy
  * 조건부분포
    * 조건부 확률질량함수
      * p_Y|X(y|x) = p(x,y) / p_X(x)
    * 조건부 확률밀도함수
      * f_Y|X(y|x) = f(x,y) / f_X(x)
    * 독립
      * p(x,y) = p_X(x)p_Y(y)
      * f(x,y) = f_X(x)f_Y(y)
  * 기대값과 공분산
    * 확률변수 X, Y의 함수인 h(X,Y)의 기대값 E[h(X,Y)]
      * E(h(X,Y))
        * 이산형 : ΣxΣy(h(x,y)p(x,y))
        * 연속형 : ∫∫h(x,y)f(x,y)dxdy
    * 확률변수 X,Y의 연관관계 척도인 공분산 Cov(X,Y)
      * Cov(X,Y)
        * Cov(X,Y) = E((X-E(X))(Y-E(Y)) = E(XY) - E(X)E(Y)
    * 단위에 영향을 받지않는 상관계수 p(X,Y)
      * p(X,Y)
        * p(X,Y) = Corr(X,Y) = Cov(X,Y) / (Var(X)Var(Y))^(1/2)
      * p(aX+b,cY+d) = p(X,Y)
    * 독립이면
      * E(XY) = E(X)E(Y)
      * Cov(X,Y) = 0
      * but p(x,y) = 0이라고 해서 독립인 것은 아님. 상관관계가 없다고 말해야함.
    * aX +- bY
      * E(aX +- bY) = aE(X) +- bE(Y)
      * Var(aX + bY) = a^2Var(X) + b^2Var(Y) + 2abCov(X,Y)
      * X,Y가 독립이면 Var(X+Y) = Var(X) + Var(Y)
