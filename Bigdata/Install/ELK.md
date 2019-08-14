# 간단한 Elastic Stack 구축 

* 목적
    * 개발단계에서 생성되는 통계 데이터 수집 및 확인
    * 통계 스크립트 수정 및 테스트 용도

* 사용되는 Tool
    * 저장소 : Elasticsearch
    * 수집 : filebeat, Logstash
    * 시각화 : Kibana
    * 웹서버 : nginx, javascript

* 흐름
    * 사용자 -> 웹서버(nginx) - > filebeat -> logstash -> elasticsearch -> kibana

* Dependancy 확인
    * Support Matrix 확인 (https://www.elastic.co/kr/support/matrix)
    * Java Version등의 확인 필요

### Kibana
* Elasticsearch에 가장 친근한 시각화 Tool

### Elasticsearch
* 특징
    * Scale out, HA, Multi-tenancy, Real-time Search, Full-text Search, Aggregation
    * Lucene
        * 데이터를 Shard 단위로 저장 (Lucene의 검색 쓰레드)
        * 여러 노드에 Shard들이 저장되면 Replica들이 저장됨.
        * 노드의 수가 변해도 Shard 수는 변함없음
    * 검색과정
        * 클라이언트의 검색에 따라 각각 샤드(모든 샤드 or 복제본)에 전달
        * 샤드는 검색 결과에 맞는 내용을 찾아 내용이 아닌 Lucene doc id와 랭킹 점수만 반환
        * 노드에서 해당 정보를 받은 후 랭킹 점수를 기반으로 정렬 후 유효한 샤드들만 재요청.
        * 해당 샤드들은 문서 전체 내용을 클라이언트로 전달.
        * 위 과정으로 알 수 있는 점은 해당 Shard 없는 노드에 질의를 던져도 샤드단위로 검색이 이루워지기 때문에 상관 없음.
    * default : json document
    * RESTful (method, host, port, index, type, document_id)
        * curl -XPUT http://localhost:9200/estat/log/1 -d ‘{ “access_host”:”127.0.0.1”, … }’ 
* 설치 과정
    * elasticsearch tar download
        * https://www.elastic.co/kr/downloads/elasticsearch
        * tar xzvf elasticsearch-{version}.tar.gz
    * config 수정
        * elasticsearch.yml
            * 클러스터, 노드 이름 설정
            * 데이터, 로그 path
            * memory_lock 옵션
            * network.host 설정 (default :localhost)
            * http.port 설정 (default : 9200)
            * zen 통신
                * discovery.zen.ping.unicast.hosts 에 리스트로 노드 추가
        * jvm.options
            * Java Heap 메모리 설정
    * 실행
        * bin/elasticsearch
            * permission 에러시 데이터,로그 path의 권한 체크!
            * curl -XGET localhost:9200 을 통해 상태 확인 가능.
    * Cluster로 구축
        * 하나의 서버에서 여러 node 실행
            * 첫 노드는 elasticsearch.yml 클러스터(cluster01), 노드 이름(node-01) 설정 후 실행
            * 다음 노드 부터는 아래와 같이 실행
                * bin/elasticsearch -E cluster.name=cluster01 -E node.name=node-02
    * 여러 서버를 통한 cluster
        * elasticsearch.yml에 cluster.name은 같게 node.name은 서로 다르게 설정
        * elasticsearch.yml에 서버들 host 정보 추가
        * 실행

### Logstash
* 특징
    * 플러그인을 이용해 다양한 input을 다양한 output에 저장 가능함.
    * 로그를 중간에서 가공이 가능하여 Elasticsearch에 넣기 용이함.
* Logstash output Elasticsearch
    * batch (20MB per single request)
    * DNS Caching (Dnetworkaddress.cache.ttl값에 따라 cache, keepalive 사용시 DNS 재확인 X)
    * http compression (http.compression값 true시 응답 압축함)
    * DLQ(dead letter queue) 정책 ( 맵핑 실패시 해당 데이터를 따로 기록)
* 구조
    * input
        * filebeat or tail 등의 다양한 input 사용 가능
    * filter
        * grok, json, mutate 등을 이용하여 로그 가공이 가능함
    * output
        * stdout, elasticsearch 등으로 보냄.

### Filebeat
* 특징
    * 파일을 읽어 Logstash or Elasticsearch 등으로 전송
    * 파일 오프셋 관리로 유실 없이 전송 가능함.


