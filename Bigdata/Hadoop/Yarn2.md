# YARN 메모리설정
yarn-site.xml을 이용하여 노드매니저의 메모리, CPU 개수, 컨테이너의 최대,최소 메모리 등 설정
* 리소스 매니저 설정
    * yarn.nodemanager.resource.memory-mb
        * 클러스터의 각 노드에서 컨테이너 운영에 설정 가능한 메모리 총량
        * 노드의 OS 운영할 메모리 제외하고 설정!
            * 32G인 경우 OS 4G 제외하고 28G 설정
    * yarn.nodemanager.resource.cpu-vcores
        * 클러스터 각 노드에서 컨테이너 운영에 설정 가능한 CPU 개수
    * yarn.scheduler.maximum-allocation-mb
        * 컨테이너 하나에 할당가능한 메모리 최대값 (default 8G)
    * yarn.scheduler.minimum-allocation-mb
        * 컨테이너 하나에 할당가능한 메모리 최소값 (default 1G)
    * yarn.nodemanager.vmem-pmem-ratio
        * 실제 메모리 대비 가상메모리 사용 비율
        * mapreduce.map.memory.mb 설정값의 비율로 사용 가능
            * 메모리 1G로 설정하고, 이 값을 10으로 설정하면 가상메모리 10G 사용
    * yarn.nodemanager.vmem-check-enabled
        * 가상메모리 제한 확인후 true일 경우 메모리 사용량 넘어서면 컨테이너 종료
    * yarn.nodemanager.pmem-check-enabled
        * 물리메모리 제한 확인후 true일 경우 메모리 사용량 넘어서면 컨테이너 종료

```xml
  <property>
    <name>yarn.nodemanager.resource.memory-mb</name>
    <value>28672</value>
  </property>
  <property>
    <name>yarn.nodemanager.resource.cpu-vcores</name>
    <value>8</value>
  </property>
  <property>
    <name>yarn.scheduler.maximum-allocation-mb</name>
    <value>8192</value>
  </property>
  <property>
    <name>yarn.scheduler.minimum-allocation-mb</name>
    <value>1024</value>
  </property>
  <property>
    <name>yarn.nodemanager.vmem-pmem-ratio</name>
    <value>2.1</value>
  </property>
  <property>
    <name>yarn.nodemanager.pmem-check-enabled</name>
    <value>true</value>
  </property>
  <property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
  </property>
```

## YARN 명령어
* User 명령어
    * application
        * 어플리케이션 정보 확인, 작업 종료
        * 작업 목록 확인
            * `yarn application -list`
        * 작업 상태 확인
            * `yarn application -status application_1111_1`
        * 작업 종료
            * `yarn application -kill application_1111_1`
    * applicationattempt
        * 어플리케이션의 현재 attempt 정보 확인
            * `yarn applicationattempt -list application_1111_1`
    * container
        * 현재 어플리케이션이 동작중인 컨테이너 정보 확인
            * `yarn container -list appattempt_1111_1_000001`
    * logs
        * 작업이 종료된 job 로그 확인
        * 작업중인 job 로그는 히스토리 서버에 저장되기전이라서 확인 불가
            * URL 트래킹에서 확인 가능
        * `yarn logs -applicationId application_1111_1`
* admin
    * daemonlog
        * 로그 레벨 설정
    * nodemanager
        * 노드매니저 실행
    * proxyserver
        * 프록시 서버 실행
    * resourcemanager
        * 리소스매니저 실행
    * rmadmin
        * 리소스매니저에 등록된 큐와 노드 정보 갱신
            * 큐정보 갱신
                * `yarn rmadmin -refreshQueues`
    * schedulerconf
        * 스케쥴러 설정 업데이트
    * scmadmin
        * 공유 캐시 매니저 어드민 명령
    * sharedcachemanager
        * 공유 캐시 매니저 실행
    * timelineserver
        * 타임라인 서버 실행
        
## YARN REST API
클러스터 상태정보, 운영정보를 확인 가능한 REST API
* 클러스터 Metric 정보 확인
    * Method : GET
    * HEADER : { 'Content-Type': 'application/json' }
    * `http://<rm http address:port>/ws/v1/cluster/metrics`
   
```python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import urllib, json, urllib2, datetime
from urllib2 import HTTPError

def request_get(request_url):
    return request(request_url, "GET", "", {'Content-Type': 'application/json'})

def request(request_url, request_type="GET", data="", header={}):

    opener = urllib2.build_opener(urllib2.HTTPHandler)
    request_get = urllib2.Request(request_url, data, header)
    request_get.get_method = lambda: request_type

    response = opener.open(request_get)

    response_info = response.info()
    response_body = response.read()
    json_obj = json.loads(response_body)

    print(json.dumps(json_obj, sort_keys=True, indent=4, separators=(',', ': ')))

def main():
    rma_url = "http://<RMA주소>:<RMA포트>/ws/v1/cluster/metrics"
    request_get(rma_url)

if __name__ == '__main__':
    main()
```
return
```json
{
    "clusterMetrics": {
        "activeNodes": 2,
        "allocatedMB": 0,
        "allocatedVirtualCores": 0,
        "appsCompleted": 25000,
        "appsFailed": 1,
        "appsKilled": 1,
        "appsPending": 0,
        "appsRunning": 0,
        "appsSubmitted": 1,
        "availableMB": 1,
        "availableVirtualCores": 23,
        "containersAllocated": 0,
        "containersPending": 0,
        "containersReserved": 0,
        "decommissionedNodes": 0,
        "decommissioningNodes": 0,
        "lostNodes": 0,
        "rebootedNodes": 0,
        "reservedMB": 0,
        "reservedVirtualCores": 0,
        "totalMB": 25000,
        "totalNodes": 2,
        "totalVirtualCores": 23,
        "unhealthyNodes": 0
    }
}
```

# 그 외 하둡
* DistCP
    * 하둡 클러스터내의 대규모 데이터 이동기능
    * 맵리듀스를 이용하여 대규모의 파일을 병렬로 복사

```bash
# a 폴더를 b 로 복사 
$ hadoop distcp hdfs:///user/a hdfs:///user/b

# a, b 폴더를 c로 복사 
$ hadoop distcp hdfs:///user/a hdfs:///user/b hdfs:///user/c

# -D 옵션을 이용하여 큐이름 설정 
$ hadoop distcp -Dmapred.job.queue.name=queue hdfs:///user/a hdfs:///user/b

# 파일이름, 사이즈를 비교하여 변경 내역있는 파일만 이동 
$ hadoop distcp -update hdfs:///user/a hdfs:///user/b hdfs:///user/c

# 목적지의 파일을 덮어씀 
$ hadoop distcp -overwrite hdfs:///user/a hdfs:///user/b hdfs:///user/c
```

* 하둡 아카이브
    * 작은 사이즈 파일이 많으면 네임노드에 부하
    * 블록 사이즈 정도로 파일 유지하는것이 좋음
    * 맵리듀스 작업을 이용하여 파일을 생성
    * 생성된 아카이브는 ls 명령어로는 디렉토리 처럼 보임.
    * har 스키마를 이용해 파일 호가인 가능
    
```bash
# 사용 방법 
hadoop archive -archiveName <NAME>.har -p <parent path> [-r <replication factor>]<src>* <dest>

# 사용 예제(큐 이름 적용)
$ hadoop archive -archiveName -Dmapred.job.queue.name=queue_name sample.har -p /user/data/ /user/
19/01/14 01:57:52 INFO mapreduce.Job: Job job_1520227878653_38308 running in uber mode : false
19/01/14 01:57:52 INFO mapreduce.Job:  map 0% reduce 0%
19/01/14 01:57:56 INFO mapreduce.Job:  map 100% reduce 0%
19/01/14 01:58:01 INFO mapreduce.Job:  map 100% reduce 100%
19/01/14 01:58:01 INFO mapreduce.Job: Job job_1520227878653_38308 completed successfully
19/01/14 01:58:01 INFO mapreduce.Job: Counters: 49
    File System Counters
        FILE: Number of bytes read=126

# sample.har 확인 
# ls 명령으로 보면 sample.har 디렉토리가 생성된 것을 알 수 있음 
$ hadoop fs -ls /user/
Found 1 items
drwxr-xr-x   - hadoop hadoop          0 2019-01-14 01:57 /user/sample.har

# sample.har 디렉토리 확인 
$ hadoop fs -ls /user/sample.har/
Found 4 items
-rw-r--r--   2 hadoop hadoop          0 2019-01-14 01:57 /user/sample.har/_SUCCESS
-rw-r--r--   5 hadoop hadoop        117 2019-01-14 01:57 /user/sample.har/_index
-rw-r--r--   5 hadoop hadoop         23 2019-01-14 01:57 /user/sample.har/_masterindex
-rw-r--r--   2 hadoop hadoop        746 2019-01-14 01:57 /user/sample.har/part-0

# har 스키마를 이용한 데이터 확인 
$ hadoop fs -ls har:///user/sample.har/
Found 1 items
-rw-r--r--   2 hadoop hadoop        746 2018-05-23 04:15 har:///user/sample.har/test.txt
```

    * 압축 해제는 `distcp` 사용!
```bash
#sample.har 압축 해제 
$ hadoop distcp -Dmapred.job.queue.name=queue_name har:///user/sample.har/ /user/decompress/

# 압축 해제 확인 
$ hadoop fs -ls /user/decompress/
Found 1 items
-rw-r--r--   2 hadoop hadoop        746 2019-01-14 04:04 /user/decompress/test.txt
```