# Grafana Install

## [Grafana](https://grafana.com/)?
* DashBoard!!
    * 다양한 그래프 및 표를 지원하며 유연하고 일관성있는 방법으로 데이터 표현 형식을 자유롭게 바꿀 수 있음!
* Kibana와의 차이점은?
    * 용도의 차이
        * Grafana는 Time-series Metric에 적합
        * Kibana는 로그 Data Search와 Exploring에 적합
    * 접근제어
        * Grafana는 Role-Based Access
        * Kibana는 유료 라이센스 필요
    * 사용 가능한 Source
        * Grafana는 MySQL, ElasticSearch, InfluxDB, Graphite, OpenTSDB등 다양한 저장소 지원
        * Kibana는 ElasticSearch만 지원
    * 그 외 특징
        * Kibana의 경우 Elastic의 Stack을 이용하면 쉽게 데이터 수집, 저장할 수 있으며, 시각화 외에도 데이터 탐색 기능을 지원함.
* 좋은점!
    * 저장소지원이 다양해서 통계를 보여주기위해 다양한 스토리지 사용이 가능함!!
        * Summary 데이터는 Mysql, 실시간 데이터는 InfluxDB, 서버 시스템 메트릭은 ElasticSearch로 활용 중
    * Panel 사용이 편함.
        * 지도, bar, line 등 다양한 그래프는 물론 table, table search등도 가능하여 사용성이 좋음.
    * DashBoard 구성
        * templating, annotation, version history등을 지원함.
            * templating : 변수 처리
                * 같은 대시보드 구성에서 server이름을 변경하여 같은 형식으로 보이거나, 주기 변경등에 유용함!
            * annotation : event 이슈 정리
            * version history : 대시보드 변경점에 대한 히스토리 관리 및 roll back 기능
            * 그 외 alert 기능, auto refresh 등 다양한 기능 지원.
        * 유연한 panel 사용
            * panel 에 사용한 데이터를 간단한 SQL 질의로 사용가능함.
            * metric의 보조축 활용 및 축별 단위 등을 지정 가능하며, stack 지표, 마우스 오버시 정렬 등의 다양한 기능 제공!

## 설치
* RPM을 이용한 특정 버전 설치
     ```SHELL
     sudo yum install https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana-4.6.3-1.x86_64.rpm
     # configure : /etc/grafana/grafana.ini
     # mkdir (grafana) : /var/run/grafana
     # log : /var/log/grafana
     ```
* yum repo 추가
    ```SHELL
        vi /etc/yum.repos.d/grafana.repo
        # append
            # [grafana]
            # name=grafana
            # baseurl=https://packagecloud.io/grafana/stable/el/6/$basearch
            # repo_gpgcheck=1
            # enabled=1
            # gpgcheck=1
            # gpgkey=https://packagecloud.io/gpg.key https://grafanarel.s3.amazonaws.com/RPM-GPG-KEY-grafana
            # sslverify=1
            # sslcacert=/etc/pki/tls/certs/ca-bundle.crt

        # Install
        sudo yum install grafana
    ```

## 업데이트
* yum update
   * `sudo yum update grafana`
        
## Package Details
``` TEXT
binary : /usr/sbin/grafana-server
init.d : /etc/init.d/grafana-server
environment : /etc/sysconfig/grafana-server
configuration : /etc/grafana/grafana.ini
log file : /var/log/grafana/grafana.log
```