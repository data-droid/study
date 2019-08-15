# Yarn (Yet Another Resource Negotiator)
Hadoop 2.0에서 도입한 클러스터 리소스 관리 및 어플리케이션 라이프 사이클 관리

* why?
    * Hadoop 1.0의 jobtracker 
        * 한대가 모든 클러스터 노드를 관리, 모든 어플리케이션 관리
        * 최대 4000 노드까지 관리. (YARN은 1만대 이상.)
        * 슬롯 단위로 리소스 관리하여 시스템 전체 자원을 효율적으로 사용 X
            * 100개의 슬롯 중 60개 맵, 40개 리듀서로 구분하여 사용되어 남는 리소스가 생김.
        * 맵리듀스 어플리케이션 작업만 처리 가능.
            * SQL 기반 작업처리, 인메모리 기반 작업 처리 X

### YARN 구성
* 자원관리 : 리소스 매니저, 노드매니저
* 어플리케이션 라이프 사이클 관리 : 어플리케이션 마스터, 컨테이너

#### 자원 관리
* 노드매니저
    * 각 노드마다 실행
    * 현재 노드의 자원 상태를 관리하며 리소스매니저에 현재 자원 상태 보고
* 리소스매니저
    * 노드매니저로부터 전달받은 정보를 이용하여 클러스터 전체 자원 관리
    * 자원 사용 상태 모니터링
    * 어플리케이션 마스터에서 자원을 요청하면 비어 있는 자원을 사용할 수 있도록 처리.
* 스케쥴러
    * 자원 분배 규칙 설정
![](http://bigdataanalyticsnews.com/wp-content/uploads/2014/09/Yarn-Architecture.png)

#### 라이프사이클 관리
* 애플리케이션 마스터, 컨테이너를 이용하여 처리
    1. 클라이언트가 리소스 매니저에 어플리케이션 제출
    2. 리소스 매니저는 비어있는 노드에서 어플리케이션 마스터 실행
    3. 어플리케이션 마스터는 작업 실행을 위한 자원을 리소스 매니저에 요청
    4. 자원을 할당 받아서 각 노드에 컨테이너(실제 작업이 실행되는 단위)를 실행
    5. 작업이 종료되면 결과를 어플리케이션 마스터에 알림
    6. 어플리케이션 마스터는 모든 작업이 종료되면 리소스 매니저에 알림
    7. 자원을 해제함.
![](https://www.oreilly.com/library/view/hadoop-the-definitive/9781491901687/images/hddg_0402.png)

#### 다양한 어플리케이션
![](https://2xbbhjxc6wk3v21p62t8n4d4-wpengine.netdna-ssl.com/wp-content/uploads/2014/07/data.png)

# YARN 스케쥴러
* 리소스 매니저의 자원 할당을 위한 정책
    * FIFO 스케쥴러
        * 먼저 들어온 작업이 종료될 때까지 다음 작업 대기
    * FAIR 스케쥴러
        * 동등하게 리소스를 점유.
    * CAPACITY 스케쥴러
        * 기본 스케줄러
        * 트리 형태로 큐를 선언하고 각 큐 별로 이용할 수 있는 자원의 용량을 정하여 자원을 할당.
    
![](https://www.oreilly.com/library/view/hadoop-the-definitive/9781491901687/images/hddg_0403.png)
* 설정
    * yarn-site.xml
        * yarn.resourcemanager.scheduler.class
            * FIFO : org.apache.hadoop.yarn.server.resourcemanager.scheduler.fifo.FifoScheduler
            * Fair : org.apache.hadoop.yarn.server.resourcemanager.scheduler.fair.FairScheduler
            * Capaicty : org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler

```xml
<property>
    <name>yarn.resourcemanager.scheduler.class</name>
    <value>org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler</value>
</property>
```

* Capacity 스케쥴러
    * 100G 메모리 용량을 가지는 A,B 큐에 각각 40%, 60% 설정시 A는 40G, B는 60G 메모리 사용 가능.
    * 설정 값
        * yarn.scheduler.capacity.maximum-applications
            * PRE, RUNNING 상태로 설정 될 수 있는 최대 애플리케이션 수
        * yarn.scheduler.capacity.maximum-am-resource-percent
            * AM에 할당 가능한 최대 비율
            * AM은 실제 작업이 돌지 않고 작업을 관리하는 역할을 하기 때문에 작업에 많은 컨테이너를 할당하기 위해 이 값을 적당히 조절해야 함.
        * yarn.scheduler.capacity.root.queues
            * root 큐에 등록하는 이름
            * 하위 등록할 큐를 위해 논리적으로만 존재함.
        * yarn.scheduler.capacity.root.[큐이름].maximum-am-resource-percent
            * 큐에서 AM이 사용할 수 있는 자원 비율
        * yarn.scheduler.capacity.root.[큐이름].capacity
            * 큐의 용량 비율
        * yarn.scheduler.capacity.root.[큐이름].user-limit-factor
            * 큐에 설정된 용량 X limit-factor 만큼 다른 큐의 용량을 사용 가능.
            * 클러스터 자원을 효율적으로 사용 가능.
            * maxmimum-capacity 이상으로는 이용할 수 없음.
        * yarn.scheduler.capacity.root.[큐이름].maximum-capacity
            * 큐가 최대로 사용가능한 용량
            ```xml
            //capacity-scheduler.xml
            <configuration>

                <property>
                    <name>yarn.scheduler.capacity.maximum-applications</name>
                    <value>10000</value>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.maximum-am-resource-percent</name>
                    <value>0.1</value>
                    <description>
                        애플리케이션 마스터에 할당 가능한 최대 비율.
                    </description>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.resource-calculator</name>
                    <value>org.apache.hadoop.yarn.util.resource.DefaultResourceCalculator</value>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.root.queues</name>
                    <value>prd,stg</value>
                    <description>
                        The queues at the this level (root is the root queue).
                    </description>
                </property>

                <!-- capacity -->
                <property>
                    <name>yarn.scheduler.capacity.root.prd.capacity</name>
                    <value>80</value>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.root.stg.capacity</name>
                    <value>20</value>
                </property>

                <!-- user-limit-factor -->
                <property>
                    <name>yarn.scheduler.capacity.root.prd.user-limit-factor</name>
                    <value>1</value>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.root.stg.user-limit-factor</user-limit-factor</name>
                    <value>2</value>
                </property>

                <!-- maximum-capacity -->
                <property>
                    <name>yarn.scheduler.capacity.root.prd.maximum-capacity</name>
                    <value>100</value>
                </property>

                <property>
                    <name>yarn.scheduler.capacity.root.stg.maximum-capacity</name>
                    <value>30</value>
                </property>

            </configuration>
            ```
* 스케줄러 설정
    * 큐의 계층 구조
        * root
            * prod[capacity:40%, max:100%]
            * dev[capacity:40%, max:75%]
                * A[capacity:50%]
                * B[capacity:50%]
        ```xml
<?xml version="1.0"?>
<configuration>
  <property>
    <name>yarn.scheduler.capacity.root.queues</name>
    <value>prod,dev</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.dev.queues</name>
    <value>eng,science</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.prod.capacity</name>
    <value>40</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.dev.capacity</name>
    <value>60</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.dev.maximum-capacity</name>
    <value>75</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.dev.A.capacity</name>
    <value>50</value>
  </property>
  <property>
    <name>yarn.scheduler.capacity.root.dev.B.capacity</name>
    <value>50</value>
  </property>
</configuration>
        ```
        * 스케줄러 설정 확인
            * mapred queue -list
                ```text
            # Capacity: 설정된 용량
            # MaximumCapacity: 최대 사용할 수 있는 용량, 기본값 100
            # CurrentCapacity: 현재 사용중인 용량 

            $ mapred queue -list
            ======================
            Queue Name : prod 
            Queue State : running 
            Scheduling Info : Capacity: 20.0, MaximumCapacity: 100.0, CurrentCapacity: 0.0 
            ======================
            Queue Name : dev 
            Queue State : running 
            Scheduling Info : Capacity: 60.0, MaximumCapacity: 75.0, CurrentCapacity: 0.0 
                ======================
                Queue Name : eng 
                Queue State : running 
                Scheduling Info : Capacity: 50.0, MaximumCapacity: 100.0, CurrentCapacity: 0.0 
                ======================
                Queue Name : science 
                Queue State : running 
                Scheduling Info : Capacity: 50.0, MaximumCapacity: 100.0, CurrentCapacity: 0.0 
                ```
        * 큐 설정 변경
            * `yarn rmadmin -refreshQueues`
        * Tip
            * 역할에 따른 배분을 잘해야함.
            * 최대 사용 가능 용량과 사용자 제한을 이용하여 자원 용량을 제한해야함.
            * 큐마다 사용할 수 있는 용량이 있지만 여유가 있는경우 다음을 따름
                * 큐의 용량 = min(capacity * user-limit-factor, maximum-capacity)
            * AM에 할당되는 용량 비율을 잘 조절하여 마스터가 많은 자원을 가져 컨테이너 생성 자원이 부족하게 하면 안됨.
       