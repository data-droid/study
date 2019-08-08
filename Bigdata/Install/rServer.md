# R server Install
* R Server란?
    * 고성능 서버에 R을 올려 다양한 사람들이 접근하여 분석할 수 있는 환경.
    * R의 다양한 패키지를 이용하여 빅데이터 분석도 가능함.

--------------------------------------
CentOS에 R서버를 설치하여 분석환경 구축하기.
## R 설치
```shell
    sudo yum install epel-release
    sudo yum install R
```
설치중 다른 library충돌 및 multi lib 에러등이 나는 경우 해당 라이브러리만 재설치하거나 다운그레이드 하는 귀찮은 작업들이 추가로 필요함.
        
        
## R Studio 설치
* RPM 다운로드 및 설치 ([최신 rpm 확인](https://www.rstudio.com/products/rstudio/download-server/))
```shell
$ wget https://download2.rstudio.org/server/centos6/x86_64/rstudio-server-rhel-1.2.1335-x86_64.rpm
$ sudo yum install rstudio-server-rhel-1.2.1335-x86_64.rpm
```
* R Studio 접근
    * http://localhost:8787
    * 계정은 CentOS의 사용자 계정을 따름.
        * 같은 계정으로 동시에 접근 X

## 설정
* 유저 관리
    * Rstudio는 해당 서버의 system user의 계정을 사용함 (/etc/passwd)
* auth-minimum-user-id
    * default가 1000으로 되어있음.
    * 사용자 user-id가 1000미만일 경우 로그인이 안되는 이슈가 있음.
    * /etc/rstudio/rserver.conf에 auth-minimum-user-id를 낮추면 됨.
        * 다만 보안성은 떨어질 수 있음.
* auth-stay-signed-in(-day)
    * 사용자 로그인 세션 유지 기간
    * default는 30
        * 수치를 낮춰서 보안성을 높일 필요가 있음.
* pamtester
    * 혹시 로그인이 안되면 PAM이 이상한 경우일 수 있음
    * /usr/lib/rstudio-server/bin/pamtester를 이용해 PAM 테스트를 해볼 필요가 있음. 
    * `sudo /usr/lib/rstudio-server/bin/pamtester --verbose rstudio <username> authenticate`
* auth-required-user-group
    * 리눅스 환경의 그룹단위로 로그인 가능 유무를 조절 할 수 있음.
        * auth-required-user-group=test,analysts,admin,rstudio-users …
    * 리눅스에서 그룹 만드는 방법
        ```shell
        sudo groupadd <groupname>
        sudo usermod -a -G <groupname> <username>
        ```
* www-port
    * 웹포트 8787이 아닌 다른 포트 사용시
* ip-rules
    * 접근 ip관리를 원하는 경우
    * /etc/rstudio/ip-rules 수정
        * deny 192.168.1.10
        * allow 192.168.1.0/24
        * deny all
* ssl
    * SSL 설정
        ```text
        ssl-enabled=1
        ssl-certificate=/var/certs/your_domain_name.crt
        ssl-certificate-key=/var/certs/your_domain_name.key 
        ```
* R 세션 관리
    * /etc/rstudio/profiles의 셋팅으로 사용자별 R버전, 세션 관리, 하드웨어 사용관리 등 가능.
    ``` text
    [*]
    cpu-affinity = 1-4
    max-processes = 100
    max-memory-mb = 2048
    session-timeout-minutes=60
    session-timeout-kill-hours=24

    [@groupname]
    cpu-affinity = 5-16
    nice = -10
    max-memory-mb = 4096

    [username]
    r-version = /opt/R/3.1.0
    session-timeout-minutes=360
    ```
* working directories
    * rsession.conf를 변경하여 working 디렉토리 및 project 위치 설정
        * session-default-working-dir=~/working
        * session-default-new-project-dir=~/projects
        * r-libs-user=~/R/library
