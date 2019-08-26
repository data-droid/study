# Platform Validation

* Cluster 구성시 Hardware, Software Validation은 필요.
    * Cluster의 performance check
    * Component check
    
* Validation의 종류
    * Smoke Test
        * hardware, platform, component 문제점 check
        * disk, memory, network 이상시 교체 필요
    * Baseline Test
        * Performance Test
        * Baseline Performance(default) check 후 설정 변경에 따른 performance 비교
    * Stress Test
        * play-book 리허설을 통한 서비스 복구/대응 평가
        * 모니터링, 알람, batch 동작 check
        
* Test 방법
    * 3회 이상 수행하며, 중간 값 기록
    * 파라미터, 결과를 기록
    * 모든 머신을 동일환경, 동일한 Tool 사용.
    * bottom-up 순으로 진행
        * Hardware > Hadoop > Components

### Hardware Validation
* CPU
    * 특징
        * CPU 튜닝은 잘 하지 않음.
        * mis-configuration or performance check 정도
    * 벤치마크
        * dd : 싱글 쓰레드 테스트
            * 과정 : 랜덤 값을 생성하여 결과를 압축
            * `dd if=/dev/urandom bs=1M count=1000 | gzip - >/dev/null`
                * 1G Test
        * Spark app : 싱글/멀티 쓰레드 테스트
            * 과정 : SparkPi Example 을 쓰레드를 조절하며 실행
            * `bin/spark-submit --master local[8] --class org.apache.spark.examples.SparkPi ...jar 10000`
                * local[쓰레드 갯수], 마지막 job 반복 횟수 : 10000
        * **sysbench** : 멀티 쓰레드 테스트
            * 설치 : `yum install sysbench`
            * `sysbench --test=cpu --cpu-max-prime=20000  --num-threads=8 run`
                * thread 수를 바꿔가면서 테스트
                * total event/total time 을 통해 테스트
* Disk
    * 특징
        * 여러 disk가 mounted 되기 때문에 하나씩 다 테스트
        * Sequentail/Radom I/O Test
        * cache 조심!
    * 벤치마크
        * dd : sequentail I/O 테스트
            * Single Write Performance
                * `dd if=/dev/zero bs=1M count=1000 of=/disk1/ddtest oflag=direct`
                    * 1M 파일 1000개를 /disk1/ddtest에 write함
                    * oflag=direct
                        * page cache bypass
            * Single Read Performance
                * `dd if=/disk1/ddtest bs=1M of=/dev/null iflag=direct`
                    * 위에서 쓴 파일을 읽는 Test
                    * iflag=direct
                        * page cache bypass
                        * echo 1 >/proc/sys/vm/drop_cache를 통해 page cache를 empty 할 수도 있음
            * Disk Sequential I/O Perfomance
                * parallel I/O 스크립트 실행
                    * ```bash
                       for n in $(seq 1 12); do
                           num=$(print "%02d" $n)
                           of="/disk${num}/ddtest"
                           dd if=/dev/zero bs=1M count=1000 of=${of} conv=fdatasync 2>${of}.out &
                           WAITPIDS="$WAITPIDS ${!}"
                       done
                       wait $WAITPIDS
                       grep copied /disk??/ddtest.out
                    ```
        * fio : Sequential/Random I/O 테스트, bandwidth/iops 측정
            * Sequential+Random I/O 측정 가능
            * 설치 : `yum install fio`
            * Write Test
                * `sudo fio --name=randwrite --ioengine=libaio --iodepth=1 --rw=randwrite --bs=4k --direct=0 --size=512M --numjobs=4 --runtime=240 --group_reporting`
            * Read Test
                * `sudo fio --name=randread --ioengine=libaio --iodepth=16 --rw=randread --bs=4k --direct=0 --size=512M --numjobs=4 --runtime=240 --group_reporting`
            * Read/Write Test
                * `sudo fio --randrepeat=1 --ioengine=libaio --direct=1 --gtod_reduce=1 --name=test --filename=random_read_write.fio --bs=4k --iodepth=64 --size=4G --readwrite=randrw --rwmixread=75`
        * SMART(Self-Monitoring, Analysis and Reporting Tech) : Disk Health Check
            * `smartctl -H /dev/sda`
        
* Network
    * 특징
        * intra, inter Rack 측정이 필요
        * latency, throughput 측정
        * inflection 포인트 체크
    * 벤치마크
        * ping : Latency 측정
            * `ping -c 4 hostname`
                * hostname에 4개의 packet 정송
                * intra rack 간 single-digit milliseconds
                * inter rack 간 tens of milliseconds
        * iperf3 : Throughput 측정
            * 서버(받는곳) : `iperf3 -s p 13000`
            * 클라이언트(보내는곳) : `iperf3 -c hostname -p 13000 -t 10 -n 104857600 (100MB)`
            * 클러스터에서는 pairwise 테스트하기 어려워 daemon으로 만들어 둠
                * `iperf3 -s -p 13000 -D -I /var/run/iperf3.pid`
    
### Hadoop Validation
* HDFS
    * 벤치마크
        * dd,time : Single I/O 테스트
            * write
                * `dd if=/dev/nrandom bs=1M count=1000 | hdfs dfs -put - /tmp/hdfstest.out`
            * read
                * `time hdfs dfs -cat /tmp/hdfstest.out >/dev/null`
        * TestDFSIO : Distributed I/O 테스트
            * mapper가 파일을 쓰고, reducer가 통계를 냄
                * throughput, average, standard deviation check
                * OS Page Cache 삭제!!
            * ```bash
               MRLIB=/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce
               yarn jar ${MRLIB}/hadoop-mapreduce/hadoop-mapreduce-client-jobclient.jar \
                   TESTDFSIO\
                   -D test.build.data=/user/ian/benchmark\
                   -write\ //write,read,append
                   -resFile //저장경로
                   -nrFiles 6 //파일개수
                   -size 512MB //파일크기
              ```
### General Validation
* TeraSort
    * 구성
        * TeraGen 
            * Write-Only 테스트
            * 명시된 수만큼 10B key, 100B value를 가진 record 생성
            * Disk Test
                * rep 1로 순수 disk performance 체크
                * ```bash
                      yarn jar /path/to/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar\
                          teragen -Ddfs.replication=1 -Ddfs.blocksize=536870912\
                          -Dmapreduce.job.map=600 1100000000 /user/benchmarks/terasort-input
                  ```
                * map은 datanode 총 디스크 수
                * 평균 속도 대비 특정 disk가 적게 나오면 bad disk
            * Disk + Network Test
                * rep 3으로 설정 으로 network traffic 추가 부하
                * ```bash
                      yarn jar /path/to/lib/hadoop-mapreduce/hadoop-mapreduce-examples.jar\
                          teragen -Ddfs.replication=3 -Ddfs.blocksize=536870912\
                          -Dmapreduce.job.map=200 1100000000 /user/benchmarks/terasort-input
                  ```
                * map은 datanode 총 디스크수 / 3 만큼
        * TeraSort 
            * Read, Write 테스트
            * TeraGen에 생성된 파일을 HDFS에서 읽어 정렬하여 씀
        * TeraValidate 
            * Read 테스트
            * 정렬된 파일을 읽어, 정상적으로 정렬되었는지 체크
    * 용도
        * baseline performance check
            * 베이스라인 확인 후 설정 변경에 따른 성능 비교
        * 기능 TEST
        * 하드웨어 TEST
        
### Components Validation
* YCSB
    * Yahoo! Cloud Serving Benchmark
    * NoSQL의 random access performance 체크
    * HBasem Solr, Kudu 등
* TPC-DS
    * Transactions Processing Council
    * RDB Engine 평가
    * Impala, Hive, SparkSQL 등
* Apache JMeter tool
    * HTTP를 통해 custom-query workload를 load testing
        * impala의 known query workload apply
        
        
# Summary
* Tools
    * Configuration Management
        * Ansible, Puppet MCollective, Checf, Python Fabric
    * Monitoring Software
        * Cloudera Manager, Apache Ambari, Grafana(+InfluxDB)
* Hardware Test
    * CPU : sysbench(multi threads)
    * DISK : dd(dequential I/O), SMART(health)
    * Network : ping(latency), iperf3(throughput)
* Hadoop
    * TESTDFSIO : HDFS
    * TeraGen,TeraSort : HDFS, Yarn, MR
