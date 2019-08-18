# Spark Install
분산 클러스터 컴퓨팅을 위한 범용 프레임워크이다.

* 하둡의 MapReduce와 차이점
	* 스파크는 메인메모리 기반의 데이터 처리를 하여 속도가 빠름.
	* batch application 뿐 아니라 interactive, query, streaming 처리 가능
  * python, java, scala, sql API 지원
  * 하둡 클러스터 위에서도 실행 가능.

## 간단한 설치법!
* spark bin 다운로드 [링크](https://spark.apache.org/downloads.html)
  * 하둡과 같이 사용시 버전을 확인하여 다운로드 받을것!
* 압축 풀기
```shell
tar -zxf spark-2.4.3-bin-hadoop2.7.tgz
ln -sf spark-2.4.3-bin-hadoop2.7 spark
```
* directory 설명
  * bin : 스파크 실행 파일
  * core, streaming, python .. : 주요 컴포넌트의 소스코드
  * examples : 스파크 api에 대한 예제 코드
* local 모드에서 실행하기
  * 대화형 prompt 실행
    * spark-shell 실행 (SCALA)
      * `./bin/spark-shell`
    * pyspark 실행 (Python)
      * `./bin/pyspark`
  * prompt 로그 줄이기
    * 로그 설정 파일 생성
      * `cp conf/log4j.properties.template conf/log4j.properties`
    * 설정 변경
      * `log4j.rootCategory=INFO, console => log4j.rootCategory=WARN, console`
