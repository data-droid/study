# MapReduce
* 간단한 단위작업을 반복하여 처리할 때 사용하는 프로그래밍 모델. 
    * 간단한 단위작업을 처리하는 맵(Map) 작업과 맵 작업의 결과물을 모아서 집계하는 리듀스(Reduce) 단계로 구성.
* 맵, 리듀스작업은 병렬로 처리가 가능한 작업으로, 여러 컴퓨터에서 동시에 작업을 처리하여 속도를 높임.

* MapReduce 작업 단위
    * Hadoop V1에서는 Job, V2에서는 Application.
    * Job은 맵과 리듀스 테스크로 나눠지며 Attempt 단위로 실행됨.
    * ID
         * 하둡 : job_1520227878653_30484
         * Yarn : application_1520227878653_30484
         * 맵/리듀스 테스크 : attempt_1520227878653_30484_(m/r)_000000_0
* MapReduce Failover
    * 오류 발생시 설정된 회수만큼 자동으로 반복하며 그 후에도 오류 발생시 종료 (attempt의 마지막 숫자가 +1 됨.)
        * map 반복 설정 : mapreduce.map.maxattempts
        * reduce 반복 설정 : mapreduce.reduce.maxattempts
* Map Input Split
    * 큰 데이터를 하나의 노드에서 처리하지 않고, 분할하여 동시에 병렬 처리하여 작업 시간 단축
    * split이 작으면 작업 부하가 분산되어 성능은 높일 수 있으나, Map task 수가 증가하여 task 생성을 위한 오버헤드가 증가하여 느려질 수 있음.
    * split 크기는 HDFS 블록 크기와 동일하게 하면 데이터 locality의 이점을 얻을 수 있음.
* Map Data Locality
    ![](https://lovingtocode.files.wordpress.com/2015/09/asdf1.jpg)
    * 맵 작업은 실행할 노드에 데이터가 있을 때 > 동일한 랙의 노드 > 다른 랙의 노드 순으로 네트워크 사용량을 낮춰 속도가 빠름.
    * Split 크기가 HDFS 블록 크기와 가타면 단일 노드에 해당 블록이 모두 저장되어 locality를 극대화 할 수 있음.
        * Split 크기가 다르면 일부는 다른 노드에 있을 수 있어 전송 받아 처리해야 할 수도 있음.
    * 맵 작업 결과는 local disk에 임시 저장되고 해당 데이터가 리듀서의 input이므로 reduce는 지역성 장점 X
* MapReduce 작업 종류
    * 리듀서가 하나인 경우
        * ![](https://autofei.files.wordpress.com/2010/06/2-2.png?w=300&h=165)
        * 정렬 작업.
        * 시간이 오래걸림.
    * 리듀서가 여러개인 경우
        * ![](https://autofei.files.wordpress.com/2010/06/2-3.png?w=300)
        * 일반적인 집계 작업
    * Mapper Only
        * ![](https://autofei.files.wordpress.com/2010/06/2-4.png?w=300&h=254)
        * 리듀서 작업이 없어 빠름.

### MapReduce 처리
* 맵리듀스 처리 단계

    ![](http://www.openwith.net/wp-content/uploads/2013/04/HadoopTutorialF0405.bmp)
    1. 입력
        * 데이터 입력하는 단계
        * 텍스트, csv, gzip 형태의 데이터를 읽어서 맵으로 전달
    2. 맵(map)
        * 입력을 분할하여 key별로 데이터를 처리
    3. 컴바이너(Combiner)
        * 네트워크를 줄이기 위한 맵 결과 정리
        * 로컬 리듀서
        * 컴바이너는 작업 설정에 따라 없을 수도 있음.
    4. 파티셔너(Partitioner)
        * 맵 출력 결과 키 값을 해쉬처리하여 어떤 리듀서로 넘길지 결정
    5. 셔플(Shuffle)
        * 각 리듀서로 데이터 이동
    6. 정렬(Sort)
        * 리듀서로 전달된 데이터를 키 기준으로 정렬
    7. 리듀서(Reduce)
        * 리듀서로 데이터를 처리하고 결과 저장
    8. 출력
        * 리듀서 결과를 정의된 형태로 저장
* 입력
    * InputFormat
        * InputSplit, RecordReader 선언
        * ```java
        package org.apache.hadoop.mapreduce;

        @InterfaceAudience.Public
        @InterfaceStability.Stable
        public abstract class InputFormat<K, V> {

          /** 
           * Logically split the set of input files for the job.  
           */
          public abstract 
            List<InputSplit> getSplits(JobContext context
                                       ) throws IOException, InterruptedException;

          /**
           * Create a record reader for a given split. The framework will call
           */
          public abstract 
            RecordReader<K,V> createRecordReader(InputSplit split,
                                                 TaskAttemptContext context
                                                ) throws IOException, 
                                                         InterruptedException;

        }
        ```
        * InputSplit
            * 맵의 입력으로 들어오는 데이터를 분할하는 방식.
            * 데이터의 위치와 읽어 들이는 길이 정의.
            * ```java
                package org.apache.hadoop.mapreduce;

                @InterfaceAudience.Public
                @InterfaceStability.Stable
                public abstract class InputSplit {
                  /**
                   * Get the size of the split, so that the input splits can be sorted by size.
                   */
                  public abstract long getLength() throws IOException, InterruptedException;

                  /**
                   * Get the list of nodes by name where the data for the split would be local.
                   * The locations do not need to be serialized.
                   */
                  public abstract 
                    String[] getLocations() throws IOException, InterruptedException;

                  /**
                   * Gets info about which nodes the input split is stored on and how it is
                   * stored at each location.
                   */
                  @Evolving
                  public SplitLocationInfo[] getLocationInfo() throws IOException {
                    return null;
                  }
                }
            ```
        * RecordReader
            * 실제 파일에 접근하여 데이터를 읽음.
            * 데이터를 읽어 <Key, Value>로 반환.
            * ```java
            package org.apache.hadoop.mapreduce;

            import java.io.Closeable;
            import java.io.IOException;

            import org.apache.hadoop.classification.InterfaceAudience;
            import org.apache.hadoop.classification.InterfaceStability;

            /**
             * The record reader breaks the data into key/value pairs for input to the
             */
            @InterfaceAudience.Public
            @InterfaceStability.Stable
            public abstract class RecordReader<KEYIN, VALUEIN> implements Closeable {

              /**
               * Called once at initialization.
               */
              public abstract void initialize(InputSplit split,
                                              TaskAttemptContext context
                                              ) throws IOException, InterruptedException;

              /**
               * Read the next key, value pair.
               */
              public abstract 
              boolean nextKeyValue() throws IOException, InterruptedException;

              /**
               * Get the current key
               */
              public abstract
              KEYIN getCurrentKey() throws IOException, InterruptedException;

              /**
               * Get the current value.
               */
              public abstract 
              VALUEIN getCurrentValue() throws IOException, InterruptedException;

              /**
               * The current progress of the record reader through its data.
               */
              public abstract float getProgress() throws IOException, InterruptedException;

              /**
               * Close the record reader.
               */
              public abstract void close() throws IOException;
            }
            ```
* Mapper
    * Mapper 클래스를 상속하여 map() 메소드를 구현
    * run() 메소드를 보면 실제 매퍼 작업이 동작하는 방식을 알 수 있음.
    * setup() 메소드로 매퍼를 초기화
    * RecoderReader를 이용하여 데이터를 읽어 map(key,value)함수를 호출
    * 데이터를 모두 처리할때까지 반복.
    * cleanup() 메소드로 사용한 리소스 반환.
```java
    package org.apache.hadoop.mapreduce;

    @InterfaceAudience.Public
    @InterfaceStability.Stable
    public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

        /**
         * The <code>Context</code> passed on to the {@link Mapper} implementations.
         */
        public abstract class Context implements MapContext<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
        }

        /**
         * Called once at the beginning of the task.
         */
        protected void setup(Context context) throws IOException, InterruptedException {
            // NOTHING
        }

        /**
         * Called once for each key/value pair in the input split. Most applications
         * should override this, but the default is the identity function.
         */
        @SuppressWarnings("unchecked")
        protected void map(KEYIN key, VALUEIN value, Context context) throws IOException, InterruptedException {
            context.write((KEYOUT) key, (VALUEOUT) value);
        }

        /**
         * Called once at the end of the task.
         */
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // NOTHING
        }

        /**
         * Expert users can override this method for more complete control over the
         * execution of the Mapper.
         */
        public void run(Context context) throws IOException, InterruptedException {
            setup(context);
            try {
                while (context.nextKeyValue()) {
                    map(context.getCurrentKey(), context.getCurrentValue(), context);
                }
            } finally {
                cleanup(context);
            }
        }
    }
```

* Combiner
    * 리듀스 작업 전달하기 전 정리하여 데이터 정송 자원 줄임.
    * 최대, 최소, 카운트 같은 함수는 가능.
    * 평균 함수는 사용 불가.
    * 컴바이너 함수를 리듀스 함수로 완전 대체 불가.

* Partitioner & Shuffle
    * Shuffle : 매퍼에서 리듀서로 데이터 전달하는 것
    * Partition : 맵의 결과 키를 리듀서로 분배하는 기준을 만드는 것
```java
    package org.apache.hadoop.mapreduce.lib.partition;

    @InterfaceAudience.Public
    @InterfaceStability.Stable
    public class HashPartitioner<K, V> extends Partitioner<K, V> {

        /** Use {@link Object#hashCode()} to partition. */
        public int getPartition(K key, V value, int numReduceTasks) {
            return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
        }

    }
```
    * 기본 파티셔너에서 `Interger.MAX_VALUE`값을 이용하여 논리합 연산을 하는 이유는 파티션 번호가 양수여야 하기 때문
    
* Sort
    * 리듀스 작업전 전달받은 키를 이용해 정렬
    * 리듀스 작업을 위한 `List<Value>` 형태로 만들기 위한 그루핑 작업도 함께 수행
    * 리듀스의 키와 다른 값을 함께 이용하는 복합키의 경우 파티셔닝, 소팅, 그룹핑 작업을 거치면서 복합키를 기준으로 정렬.
    * ![](https://i.stack.imgur.com/l6IEl.png)
    * 파티션 기준이 되는 Primary key외 다른 값을 기준으로 정렬 할 수 있음. (Secondary Sort)
    
* Reduce
    * 키별로 정렬된 데이터를 이용하여 리듀스 작업 진행
    * Reducer 클래스를 상속하고 reduce() 메소드를 구현하면 됨.
    * run() 메소드를 보면 실제 매퍼 작업이 동작하는 방식 알 수 있음.
    * setup() 메소드로 리듀스를 초기화. 
    * 데이터를 읽어서 `run(key, list<Value>)` 함수 호출
    * 반복 후 cleanup() 메소드로 사용한 리소스 반환
```java
package org.apache.hadoop.mapreduce;

@Checkpointable
@InterfaceAudience.Public
@InterfaceStability.Stable
public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {

  /**
   * The <code>Context</code> passed on to the {@link Reducer} implementations.
   */
  public abstract class Context 
    implements ReduceContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
  }

  /**
   * Called once at the start of the task.
   */
  protected void setup(Context context
                       ) throws IOException, InterruptedException {
    // NOTHING
  }

  /**
   * This method is called once for each key. Most applications will define
   * their reduce class by overriding this method. The default implementation
   * is an identity function.
   */
  @SuppressWarnings("unchecked")
  protected void reduce(KEYIN key, Iterable<VALUEIN> values, Context context
                        ) throws IOException, InterruptedException {
    for(VALUEIN value: values) {
      context.write((KEYOUT) key, (VALUEOUT) value);
    }
  }

  /**
   * Called once at the end of the task.
   */
  protected void cleanup(Context context
                         ) throws IOException, InterruptedException {
    // NOTHING
  }

  /**
   * Advanced application writers can use the 
   * {@link #run(org.apache.hadoop.mapreduce.Reducer.Context)} method to
   * control how the reduce task works.
   */
  public void run(Context context) throws IOException, InterruptedException {
    setup(context);
    try {
      while (context.nextKey()) {
        reduce(context.getCurrentKey(), context.getValues(), context);
        // If a back up store is used, reset it
        Iterator<VALUEIN> iter = context.getValues().iterator();
        if(iter instanceof ReduceContext.ValueIterator) {
          ((ReduceContext.ValueIterator<VALUEIN>)iter).resetBackupStore();        
        }
      }
    } finally {
      cleanup(context);
    }
  }
}
```

# 출력
* OutputFormat
    * Text, csv형식 등을 설정하고 파일을 쓰는 방법 설정
    ```JAVA
    import java.io.IOException;

    @InterfaceAudience.Public
    @InterfaceStability.Stable
    public abstract class OutputFormat<K, V> {

      public abstract RecordWriter<K, V> 
        getRecordWriter(TaskAttemptContext context
                        ) throws IOException, InterruptedException;

      public abstract void checkOutputSpecs(JobContext context
                                            ) throws IOException, 
                                                     InterruptedException;

      public abstract 
      OutputCommitter getOutputCommitter(TaskAttemptContext context
                                         ) throws IOException, InterruptedException;
    }
    ```
* RecordWriter