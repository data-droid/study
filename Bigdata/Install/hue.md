
# Hue
하둡의 다양한 에코시스템 (hive, impala, spark)등의 분석 툴들을 한 곳에서 사용가능 하도록 해줌. Python 노트북 같은 그런 느낌?

## 설치 과정
#### hue.tar 받기
```bash
wget https://www.dropbox.com/s/bv2al5bvc7uwgls/hue-4.3.0.tgz?dl=0
```

#### [install dependancies](http://cloudera.github.io/hue/docs-4.4.0/administrator/installation/dependencies/)
```bash
sudo yum install ant asciidoc cyrus-sasl-devel cyrus-sasl-gssapi cyrus-sasl-plain gcc gcc-c++ krb5-devel libffi-devel libxml2-devel libxslt-devel make  mysql mysql-devel openldap-devel python-devel sqlite-devel gmp-devel
```
#### make & install
```bash
PREFIX=저장할위치 make install
```

## 설정 셋팅
#### DB setting
``` bash
#Mysql
$ sudo -u hue <HUE_HOME>/build/env/bin/hue dumpdata > <some-temporary-file>.json
```

```mysql
mysql> create database hue;
mysql> grant all on hue.* to 'hue'@'localhost' identified by '<secretpassword>';
hue.ini
```

```config
# hue.ini config file
host=localhost
port=3306
engine=mysql
user=hue
password=<secretpassword>
name=hue
```

```bash
# Migration
$ sudo -u hue <HUE_HOME>/build/env/bin/hue syncdb --noinput
$ sudo -u hue <HUE_HOME>/build/env/bin/hue migrate
```

```mysql
mysql > SHOW CREATE TABLE auth_permission;
mysql > ALTER TABLE auth_permission DROP FOREIGN KEY content_type_id_refs_id_XXXXXX;
mysql > DELETE FROM hue.django_content_type;
```

```bash
$ <HUE_HOME>/build/env/bin/hue loaddata <some-temporary-file>.json
```

```mysql
mysql > ALTER TABLE auth_permission ADD FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);
```

#### HDFS setting
```config
# hue.ini config file
server_user=hue
server_group=hue
default_user=hue
default_hdfs_superuser=hdfs
[[hdfs_clusters]]
fs_defaultfs=hdfs://hadoop-server:8020
webhdfs_url=http://hadoop-server:50070/webhdfs/v1
hadoop_conf_dir=$HADOOP_CONF_DIR
[[yarn_clusters]]
resourcemanager_host=hadoop-server
resourcemanager_port=8032
submit_to=True
resourcemanager_api_url=http://hadoop-server:8088
proxy_api_url=http://hadoop-server:8088
history_server_api_url=http://hadoop-server:19888

# hdfs-site.xml config file (namenode말고 resource manager도 모두 사용하는 값이기 때문에 전체 다 적용해줘야 함.)
dfs.webhdfs.enabled : true
core-site.xml
hadoop.proxyuser.hue.hosts : *
hadoop.proxyuser.hue.groups: *
```

#### Hive setting
```config
# hue.ini config file
[beeswax]
hive_server_host=hiveserver2-server
hive_server_port=10000
hive_conf_dir=$HIVE_CONF_DIR
```

#### Sorl setting
```config
# hue.ini config file
[search]
solr_url=http://sorl-server/solr/
```

#### Oozie setting
```config
# hue.ini config file
[liboozie] 
oozie_url=http://oozie-server:11000/oozie
[indexer] 
config_jdbc_libs_path, config_jars_libs_path값 oozie 홈으로 변경.
```

#### 그 외 setting
```config
# hue.ini config file
[desktop]
time_zone=Asia/Seoul
django_debug_mode=false
http_host=0.0.0.0
secret_key=”asdfasdfasdfasdf” (secure hashing)
app_blacklist=impala,security,pig,hbase,sqoop,spark (안사용할거 적어주면 깔끔해짐)
```

```bash
# smtp install
$ yum install -y sendmail sendmail-cf
$ vi /etc/mail/sendmail.mc
# 주석 해제
# 52: TRUST_AUTH_MECH(`EXTERNAL DIGEST-MD5 CRAM-MD5 LOGIN PLAIN')dnl
# 53: define(`confAUTH_MECHANISMS', `EXTERNAL GSSAPI DIGEST-MD5 CRAM-MD5 LOGIN PLAIN')dnl
# 118:
$ m4 sendmail.mc > sendmail.cf
$ systemctl start sendmail
```

```config
# hue.ini config file
[smtp]
host, port 입력
default_from_email 입력 (있는 계정으로 해야함)

# 외부 DB 데이터 저장소 연결
[[interpreters]]
[[[mysql]]] : name=test interface=rdbms

[librdbms]
[[databases]]
[[[mysql]]]
nice_name=”test”
engine=mysql
host=
port=3306
user=test
password=
```

## 기타
#### Oozie 설치
```bash
# Download
$ wget http://apache.mirror.cdnetworks.com/oozie/5.1.0/oozie-5.1.0.tar.gz

# Compile
$ export MAVEN_OPTS=-Xmx1024m
$ tar zxvf ./oozie-5.1.0.tar.gz
$ bin/mkdistro.sh -DskipTests

# make Oozie Directory
$ ./distro/target/oozie-5.1.0/*.gz # 파일을 원하는 위치로 복사
$ tar zxvf ./oozie-5.1.0.tar.gz # 압축 풀기
$ mv ./oozie-5.1.0 ./oozie
```

#### 설정 Setting
```xml
# oozie-site.xml
<property> 
<name>oozie.services.ext</name> 
<value>org.apache.oozie.service.HadoopAccessorService,org.apache.oozie.service.InstrumentationService</value>
</property> 
<property> 
<name>oozie.service.HadoopAccessorService.hadoop.configurations</name> 
<value>*=/하둡 설정 경로</value>
</property>
<property> 
<name>oozie.http.hostname</name> 
<value>oozie-server-ip</value>
</property>
<property> 
<name>oozie.email.smtp.host</name> 
<value>smtp-server-ip</value>
</property>
<property> 
<name>oozie.email.from.address</name> 
<value>email(from)</value>
</property>
<property> 
<name>oozie.service.ProxyUserService.proxyuser.hue.hosts</name> 
<value>*</value>
</property>
<property> 
<name>oozie.service.ProxyUserService.proxyuser.hue.groups</name> 
<value>*</value>
</property>
<property>  
<name>oozie.service.JPAService.jdbc.url</name>  
<value>jdbc:mysql://DB서버:3306/oozie</value>  
</property>  
<property>  
<name>oozie.service.JPAService.create.db.schema</name>  
<value>true</value>  
</property>  
<property>  
<name>oozie.service.JPAService.jdbc.driver</name>  
<value>com.mysql.jdbc.Driver</value>  
</property>  
<property>  
<name>oozie.service.JPAService.jdbc.username</name>  
<value>oozie</value>  
</property>  
<property>  
<name>oozie.service.JPAService.jdbc.password</name>  
<value>비번</value>  
</property>  
```

```mysql
# Mysql
mysql > create user 'oozie'@'%' identified by '비번'; 
mysql > grant all on *.* to 'oozie'@oozie서버 identified by '비번';
mysql > flush privileges; 
mysql > create database oozie; 
```

```bash
# JDBC Setting
$ mkdir $OOZIE_HOME/libext # 여기서는 하이브 설치때 받아놓은 mysql connector jdbc 를 넣어줌.

# ext install
$ cd $OOZIE_HOME/libext
$ wget http://archive.cloudera.com/gplextras/misc/ext-2.2.zip
# dependancies
$ cp $OOZIE_COMPILE_HOME/server/target/dependency/*.jar $OOZIE_HOME/libext/
$ ./bin/oozie-setup.sh sharelib create -fs hdfs://하둡클러스터
$ vi ./bin/oozie-jetty-server.sh
$ cp 뒤에  $OOZIE_HOME/libext/*
# 위 과정 중 lib과 libext에 동시에 들어가는 라이브러리의 경우 충돌 발생할 수 있기 때문에 제거해줘야함.

# DB 설정
$ ./bin/ooziedb.sh create -sqlfile oozie.sql -run
# jetty 설정
$ ./bin/oozie-setup.sh
# 시작
$ ./bin/oozied.sh start
# 기타
# DOAS 에러시
# core-site.xml에 oozie user proxyuser 추가
```
