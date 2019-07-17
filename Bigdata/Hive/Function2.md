## UDAF
* N개의 row를 처리하여, 1개의 row를 반환하는 함수.
* count(), sum(), max()와 같은 윈도우 함수.
* 구현
    * AbstractGenericUDAFResolver를 상속
    * Resolver를 상속하여 파라미터 타입체크 처리 후 데이터 처리를 구현한 Evaluator 클래스 반환.
        * Resolver 클래스
            * 파라미터 타입체크
            * 오퍼레이터 구현
            * GenericUDAFEvaluator 반환
        * Evaluator 클래스
            * init(), merge(), terminatePartial()등 실제 처리 구현
            * getNewAggregationBuffer() 집계에 사용할 함수 반환
            * reset으로 aggregation 재사용
            * init으로 입력받은 아규먼트, 반환값 타입 지정
            * iterate로 반복작업
            * terminatePartial으로 부분적으로 집계작업할 때 사용.
            * merge를 통한 결과 합치기
            * termiate으로 작업 종료
    * init()함수로 초기화, iterate()로 데이터 처리, terminatePartial()함수로 중간결과 반환, 리듀스에서 merge(), 마지막으로 termiate()를 통해 결과 반환.
    ![](https://www.wisdomjobs.com/tutorials/data-flow-with-partial-results-for-a-udaf.png)
    * Sum함수 구현 예제
    ```JAVA
    import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
    import org.apache.hadoop.hive.ql.metadata.HiveException;
    import org.apache.hadoop.hive.ql.parse.SemanticException;
    import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
    import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
    import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
    import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
    import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
    import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
    import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
    import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
    import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

    /**
     * String, int 를 입력받아서 합계를 반환
     * 
     * @author User
     *
     */
    public class SumInt extends AbstractGenericUDAFResolver {

        @Override
        public GenericUDAFEvaluator getEvaluator(TypeInfo[] info) throws SemanticException {

            // 파라미터는 하나만 받음 
            if (info.length != 1) {
                throw new UDFArgumentTypeException(info.length - 1, "Exactly one argument is expected.");
            }

            // 파라미터의 카테고리가 프리미티브 타입이 아니면 예외 처리 
            if (info[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
                throw new UDFArgumentTypeException(0, "Only primitive type arguments are accepted but " + info[0].getTypeName() + " was passed as parameter 1.");
            }

            // 전달된 파라미터의 타입이 스트링이면 SumStringEvaluator, 아니면 SumIntEvaluator 처리 
            PrimitiveCategory category = ((PrimitiveTypeInfo)info[0]).getPrimitiveCategory();

            if (category == PrimitiveCategory.STRING || category == PrimitiveCategory.INT) {
                return new SumEvalutor();
            } else {
                throw new UDFArgumentTypeException(0, "Only string, int type arguments are accepted but " + info[0].getTypeName() + " was passed as parameter 1.");
            }
        }

        @SuppressWarnings("deprecation")
        public static class SumEvalutor extends GenericUDAFEvaluator {

            protected PrimitiveObjectInspector inputOI;

            @Override
            public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
                super.init(m, parameters);

                inputOI = (PrimitiveObjectInspector) parameters[0];
                return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
            }

            static class SumAggregationBuffer implements AggregationBuffer {
                int sum;
            }

            @Override
            public AggregationBuffer getNewAggregationBuffer() throws HiveException {
                SumAggregationBuffer sum = new SumAggregationBuffer();
                sum.sum = 0;
                return sum;
            }

            @Override
            public void reset(AggregationBuffer agg) throws HiveException {
                ((SumAggregationBuffer) agg).sum = 0;
            }

            @Override
            public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
                ((SumAggregationBuffer) agg).sum += getInt(parameters[0]);
            }

            @Override
            public Object terminatePartial(AggregationBuffer agg) throws HiveException {
                return ((SumAggregationBuffer) agg).sum;
            }

            @Override
            public void merge(AggregationBuffer agg, Object partial) throws HiveException {
                ((SumAggregationBuffer) agg).sum += getInt(partial);
            }

            @Override
            public Object terminate(AggregationBuffer agg) throws HiveException {
                return ((SumAggregationBuffer) agg).sum;
            }

            public int getInt(Object strObject) {
                return PrimitiveObjectInspectorUtils.getInt(strObject, inputOI);
            }
        }
    }
    ```
    * 사용법
    ```SQL
    -- UDF가 포함된 jar 추가 
    ADD JAR hdfs:///user/hiveUDF.jar;
    CREATE FUNCTION sumInt AS 'com.sec.hive.udf.SumInt';

    -- 데이터 확인 
    hive> select * from intTable;
    OK
    1
    2
    3
    4
    5
    6
    7
    8
    9
    10

    -- 처리 결과 확인 
    select sumInt(col)
      from intTable;
    Query ID = hadoop_20181113081733_822f2f53-139c-419b-bb67-fb9e572994a4
    Total jobs = 1
    Launching Job 1 out of 1
    OK
    55
    Time taken: 14.06 seconds, Fetched: 1 row(s)
    ```

## UDTF
* 1개의 row를 처리하여, N개의 row를 반환하는 함수.
* examples
    * explode, inline, posexplode : array, map, struct 데이터를 테이블 형태로 반환
    * json_tuple : json 파싱하여 반환
    * url_tuple : url 문자를 파싱, HOST,PATH,QUERY,REF,PROTOCOL, AUTHORITY,FILE 를 반환
    * stack : 데이터를 여러개의 행으로 반환.
* 구현
    * GenericUDTF
        * initialize() : 입력된 파라미터의 검증, 컬럼이름 반환
        * process() : 실제 데이터 처리 반환, forward() 함수에 데이터 전달.
        * close() : 자원 반환
    * 딜리미터를 입력받아 문자열을 분할하고 테이블 데이터로 반환하는 예제
    
    ```JAVA
    import java.util.ArrayList;

    import org.apache.hadoop.hive.ql.exec.Description;
    import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
    import org.apache.hadoop.hive.ql.metadata.HiveException;
    import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
    import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
    import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
    import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
    import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
    import org.apache.hadoop.io.Text;

    @Description(name = "string_parse", value = "_FUNC_(delimiter, string) - ")
    public class StringParseUDTF extends GenericUDTF {

        private transient final Object[] forwardListObj = new Object[1];
        protected PrimitiveObjectInspector inputOI1;
        protected PrimitiveObjectInspector inputOI2;

        @Override
        public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {

            inputOI1 = (PrimitiveObjectInspector) argOIs[1];
            inputOI2 = (PrimitiveObjectInspector) argOIs[1];

            ArrayList<String> fieldNames = new ArrayList<String>();
            ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

            fieldNames.add("col");
            fieldOIs.add(inputOI1);
            fieldOIs.add(inputOI2);

            return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
        }

        @Override
        public void process(Object[] o) throws HiveException {

            String delim = (String) inputOI1.getPrimitiveJavaObject(o[0]);
            String datas = (String) inputOI2.getPrimitiveJavaObject(o[1]);

            for(String str: datas.split(delim)) {
                forwardListObj[0] = new Text(str);
                forward(forwardListObj);
            }
        }

        @Override
        public void close() throws HiveException { }
    }
    ```
    
    * 사용법
    ```SQL
    -- UDF가 포함된 JAR 추가 및 함수 생성 
    ADD JAR hdfs:///user/hiveUDF.jar;
    CREATE TEMPORARY FUNCTION parseStr AS 'com.sec.hive.udf.StringParseUDTF';

    hive> SELECT parseStr(",", "1,2,3");
    OK
    1
    2
    3

    hive> SELECT parseStr("-", "a-b-c");
    OK
    a
    b
    c
    ```

## Transform
* 입력 데이터가 정형데이터가 아닐때 파이썬 스크립트를 이용하여 입력 데이터를 정형데이터로 변경할 때 사용.
* 예제
```text
    <입력>
    DATA1
    Column1-1
    Column1-2
    DATA2
    Column2-1
    Column2-2

    <출력>
    DATA1   Column1-1   Column1-2
    DATA2   Column2-1   Column2-2
```
* 구현
```python
    #!/usr/bin/python
    # -*- coding: utf-8 -*-
    import re, json, sys, time

    def readFile():
        with sys.stdin as lines:
            str_list = []

            for line in lines:
                # DATA 시작하면 출력
                if line.startswith("DATA") and len(str_list) != 0:
                    print "\t".join(str_list)
                    del str_list[:]
                    str_list.append(line.strip())
                else:
                    str_list.append(line.strip())

            # 마지막 데이터 출력 
            print "\t".join(str_list)

    if __name__ == "__main__":
        readFile()
```
* 사용법
```SQL
-- 테이블 생성 
    CREATE EXTERNAL TABLE sample_temp
    (
        rawLine                STRING
    )
    LOCATION "/user/data/txt/";

    -- trsnsform(입력 칼럼명) using 파일위치 as (출력 칼럼)
    -- 이런 형태로 입력하면 아래와 같은 결과를 확인할 수 있다. 
    SELECT TRANSFORM(rawLine) USING "hdfs:///user/custom_mapred.py" AS (type, dt1, dt2)
      FROM sample_temp;
    Total MapReduce CPU Time Spent: 1 seconds 710 msec
    OK
    DATA1   Column1-1   Column1-2
    DATA2   Column2-1   Column2-2
```