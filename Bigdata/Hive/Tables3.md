## **SerDe** (Serializer/Deserializer)
* 하이브가 데이터를 해석하는 방법.
* 파일포맷을 이용하여 파일을 읽고, Deserializer를 이용하여 원천 데이터를 테이블 포맷에 맞는 데이터로 변환.
* Serializer를 이용하여 키, 밸류 형태로 변경하고 파일 포맷을 이용하여 저장 위치에 씀.
    * HDFS files --> InputFileFormat --> Deserializer --> Row object
    * Row object --> Serializer --> OutputFileFormat --> HDFS files
* 하이브 기본 서데
    * Avro, ORC, RegEx, Thrift, Parquet, CSV, JsonSerDe
    ```SQL
    # ORC 테이블 생성 
    CREATE TABLE orc_tbl (
      col STRING
    ) STORED AS ORC;

    # ORC 테이블 확인 
    hive> desc formatted orc_tbl;

    # ORC 테이블의 서데, 인풋 아웃풋 포맷
    # Storage Information        
    SerDe Library:          org.apache.hadoop.hive.ql.io.orc.OrcSerde    
    InputFormat:            org.apache.hadoop.hive.ql.io.orc.OrcInputFormat  
    OutputFormat:           org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat    

    # TXT 테이블 생성 
    CREATE TABLE txt_tbl (
      col STRING
    );

    # TXT 테이블 확인 
    hive> desc formatted txt_tbl;

    # TXT 테이블의 서데, 인풋 아웃풋 포맷
    # Storage Information        
    SerDe Library:          org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe   
    InputFormat:            org.apache.hadoop.mapred.TextInputFormat     
    OutputFormat:           org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat   
    ```
* Custom SerDe
    * 아래의 파일을 읽어 데이터 입력시 !를 제거하기 위한 커스텀 SerDe
    ```
    $ cat sample.txt
    david   23!
    cole    3!5
    anna    !92
    ```
    * LazySimpleSerDe 를 상속.
    ```JAVA
    import org.apache.hadoop.hive.serde2.SerDeException;
    import org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe;
    import org.apache.hadoop.io.Text;
    import org.apache.hadoop.io.Writable;

    public class SampleSerDe extends LazySimpleSerDe {

        public SampleSerDe() throws SerDeException {
            super();
        }

        @Override
        public Object doDeserialize(Writable field) throws SerDeException {
                    // 느낌표는 제거  
            String temp = field.toString().replaceAll("!", "");
            return super.doDeserialize(new Text(temp));
        }
    }
    ```
    * 사용법
    ```SQL
    # 클래스가 들어 있는 jar 파일 추가 
    hive> ADD JAR ./hiveUDF.jar;

    # 테이블 생성 시에 서데 정보 및 프로퍼티 정보 전달 
    hive> CREATE TABLE serde_tbl
    (
      col1 STRING
      , col2 STRING
    ) 
    ROW FORMAT SERDE 'com.sec.hive.serde.SampleSerDe'
    WITH SERDEPROPERTIES ( "field.delim" = "\t" )
    ;

    # 샘플 데이터를 입력 
    hive> LOAD DATA LOCAL INPATH './sample.txt' INTO TABLE serde_tbl;

    # 데이터 조회 
    hive> select * from serde_tbl;
    OK
    david   23
    cole    35
    anna    92
    ```
    
## 가상컬럼
* 입력된 원천데이터의 위치를 확인하기 위한 Virtual Column이 존재.
    * INPUTFILENAME : 매퍼의 입력으로 들어온 파일의 이름.
    * BLOCK_OFFSET_INSIDE_FILE : 파일에서 현재 데이터의 위치
```SQL
    select INPUT__FILE__NAME, key, BLOCK__OFFSET__INSIDE__FILE 
      from src;

    select key, count(INPUT__FILE__NAME) 
      from src 
     group by key 
     order by key;

    select * 
      from src 
     where BLOCK__OFFSET__INSIDE__FILE > 12000 
     order by key;
```

## 쿼리 분석
* explain 명령어를 이용하여 쿼리 실행 계획 확인가능.
```SQL
    EXPLAIN [EXTENDED|AST|DEPENDENCY|AUTHORIZATION|LOCKS|VECTORIZATION|ANALYZE] query

    hive> EXPLAIN select * from tbl;
    hive> EXPLAIN EXTENDED select * from tbl;
```
* 스테이지 정보, 처리되는 작업의 정보 출력.
```JSON
{
  "STAGE DEPENDENCIES": {
    "Stage-1": {
      "ROOT STAGE": "TRUE"
    },
    "Stage-8": {
      "DEPENDENT STAGES": "Stage-1",
      "CONDITIONAL CHILD TASKS": "Stage-5, Stage-4, Stage-6"
    },
    "Stage-5": {},
    "Stage-2": {
      "DEPENDENT STAGES": "Stage-5, Stage-4, Stage-7"
    },
    "Stage-0": {
      "DEPENDENT STAGES": "Stage-2"
    },
    "Stage-3": {
      "DEPENDENT STAGES": "Stage-0"
    },
    "Stage-4": {},
    "Stage-6": {},
    "Stage-7": {
      "DEPENDENT STAGES": "Stage-6"
    }
  }
  ,
  "STAGE PLANS": {
    "Stage-1": {
      "Tez": {
      ...
```
* CBO를 이용하면 테이블의 통계정보를 이용하여 최적화 함.
* 옵션
    * EXTENDED : 추가 정보 확인
    * AST : Abstract Syntax Tree 정보 확인
    * Dependency : 테이블간 의존 정보 확인
    * AUTHORIZATION : 테이블 조회 권한 정보 확인
    * LOCKS : 테이블 락 정보 확인
    * VECTORIZATION : 벡터화 처리 정보 확인
    * ANALYZE : 실제 참조하는 row 정보 확인.
    