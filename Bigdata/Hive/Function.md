# 함수
## 기본 함수
* 목록, 확인
    * show 
        * 함수, 테이블, 데이터베이스의 목록을 확인
        * `show databases;`
    * desc 
        * 함수, 테이블, 데이터베이스의 설정값 확인.
        * extended, formatted을 이용해 상세 설정 확인.
        * `desc formatted table_name;`
* 관계/논리형
    * A between B and C
        * A가 B이상 C이하인 경우 True
    * A IN ('a','b')
        * A가 a 또는 b인 경우 True
* 복합형 생성
    * Map, Array, Struct 타입 생성.
    * `select map('key1', 'value1', 'key2', 'value2');`
    * `select array(1,2,3);`
    * `select struct(1,"a");`
* 기본 명렁어
    * CAST
        * 형 변환시 사용.
        * `select cast( "1" as int);`
    * 콜렉션
        * size(Map or Array)
            * 맵, 배열 사이즈 반환.
            * `select size(map('key1', 'value1', 'key2', 'value2'));`
        * array_contains(Array,value)
            * 배열에 해당 값이 있는지 확인.
            * `select array_contains(array(1, 2, 3), 2);`
        * sort_array(Array)
            * 배열 정렬
            * `select sort_array(array(3, 1, 2));`
    * 날짜
        * `unix_timestamp(string date[, string format])`
            * unix타임(1514764800)으로 반환
            * `select unix_timestamp("2018-01-01 00:00:00", 'yyyy-MM-dd HH:mm:ss');`
        * `from_unixtime(bigint unixtime[, string format])`
            * 일자(2018-01-01 00:00:00)로 반환
            * `select from_unixtime(1514764800, 'yyyy-MM-dd HH:mm:ss');`
    * condition
        * if(조건, 참, 거짓)
            * 조건을 이용하여 맞는 값 반환
            * `select if(1=1,'a','b');`
            * return : `a`
        * isnull(a)
            * null 체크
            * `select isnull(null);`
            * return : `true`
        * nvl(T value, T default_value)
            * null이면 default_value 반환
            * `select nvl(null, 'a');`
            * return : `a`
        * COALESCE(T v1, T v2, ...)
            * null이 아닌 최초의 값 반환.
            * `select coalesce(null,'a','b');`
            * return : `a`
    * 문자열
        * concat(a,...)
            * 문자열 병합.
            * `select concat('A','B','C');`
            * return : `ABC`
        * concat_ws(string SEP, string A, string B)
            * SEP을 이용하여 문자열 병합
            * `select concat_ws(',','a','b');`
            * return : `a,b`
        * `concat_ws(string SEP, array<string>)`
            * SEP을 이용하여 문자열 병합
            * `select concat_ws(',',array('a','b','c'));`
            * return : `a,b,c`
        * substr(string A, int start [,int len])
            * A문자열의 start부터 len까지 반환
            * len이 없는경우 끝까지 반환.
            * `select substr('123456789',3,2);`
            * return : 34
        * trim(string A)
            * A문자열의 앞,뒤 공백 제거
        * replace(string A, string old, string new)
            * A 문자열의 old 문자열을 new로 변경.
        * str_to_map(text[,delimiter1, delimiter2])
            * text를 구분자를 이용하여 키,값으로 구분하여 맵으로 변환.
            * `select str_to_map('a:1,b:2,c:3', ',' ,':');`
            * `{"a":"1","b":"2","c":"3"}`
        * split(string str, string SEP)
            * 문자열을 SEP으로 구분하여 배열로 반환.
            * `select split('a,b,c',',');`
            * `["a","b","c"]`
            

## UDF
* 1개의 row를 처리하여, 1개의 row를 반환하는 함수.
* substr(), round() 등..
* 구현
    * UDF(org.apache.hadoop.hive.ql.exec.UDF) 상속
        * evaluate() 함수 구현
        * 실제 데이터가 처리되는 부분만 구현.
        ```java
        import java.util.Map;
        import org.apache.hadoop.hive.ql.exec.UDF;
        import org.apache.hadoop.io.Text;

        public class SampleUDF extends UDF {

            public Text evaluate(Text text) {
                // 입력받은 문자를 대문자로 반환
                return new Text(text.toString().toUpperCase());
            }

            public int evaluate(int number) {
                // 입력받은 숫자에 1을 더하여 반환
                return number + 1;
            }

            public String evaluate(Map<String, String> map, String key) {
                // 입력받은 키의 밸류가 있으면 반환하고, 없으면 None를 반환
                return map.containsKey(key) ? map.get(key) : "None";
            }
        }
        ```
        * 사용법
        ```SQL
        -- UDF가 포함된 jar 추가 
        ADD JAR hdfs:///user/hiveUDF.jar;
        CREATE FUNCTION func AS 'com.sec.hive.udf.GeneralUDF';

        // int 형은 +1 
        hive> select func(1);
        OK
        2
        Time taken: 0.816 seconds, Fetched: 1 row(s)

        -- 문자형은 대문자 반환 
        hive> select func('small');
        OK
        SMALL
        Time taken: 0.032 seconds, Fetched: 1 row(s)

        -- 일치하는 값이 없으면 오류 
        hive> select func(array(1, 2, 3));
        FAILED: SemanticException [Error 10014]: Line 1:7 Wrong arguments '3': No matching method for class com.sec.hive.udf.GeneralUDF with (array<int>). Possible choices: _FUNC_(int)  _FUNC_(map<string,string>, string)  _FUNC_(string)          
        ```
    * GenericUDF(org.apache.hadoop.hive.ql.udf.generic.GenericUDF) 상속
        * initialize(), evaluate(), getDisplayString() 함수 구현
        * 복합 타입의 데이터를 입력받아 처리할 때 사용.
        * 데이터의 검증 부분과 실제 처리 부분을 구현.
        ```JAVA
        import java.util.List;
        import org.apache.hadoop.hive.ql.exec.Description;
        import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
        import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
        import org.apache.hadoop.hive.ql.metadata.HiveException;
        import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
        import org.apache.hadoop.hive.serde2.lazy.LazyString;
        import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
        import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
        import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
        import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
        import org.apache.hadoop.io.IntWritable;

        @Description(name = "sumListStringLength", value = "_FUNC_(value) - Returns value that sum list string length.", extended = "Example:\n  > SELECT _FUNC_(Array<String>) FROM table LIMIT 1;")
        public class ListGenericUDF extends GenericUDF {

            ListObjectInspector listOi;

            @Override
            public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

                // initialize 함수에서는 
                // 입력받은 파라미터에 대한 검증
                // 반환하는 파라미터에 대한 검증
                // 함수에 입력받는 파라미터 개수 확인
                if (arguments.length != 1)
                    throw new UDFArgumentLengthException("function argument need 1.");

                // 파라미터의 타입 확인
                ObjectInspector inspector = arguments[0];
                if (!(inspector instanceof ListObjectInspector))
                    throw new UDFArgumentException("function argument need List");

                listOi = (ListObjectInspector) inspector;

                // 입력받는 리스트내 엘리먼트의 객체 타입 확인
                if (!(listOi.getListElementObjectInspector() instanceof StringObjectInspector))
                    throw new UDFArgumentException("array argument need ");

                // 반환은 문자열의 수이므로 int 객체 반환
                return PrimitiveObjectInspectorFactory.writableIntObjectInspector;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Object evaluate(DeferredObject[] arguments) throws HiveException {
                // arguments의 객체를 형변환
                List<LazyString> list = (List<LazyString>) listOi.getList(arguments[0].get());

                if (list == null)
                    return null;

                int sum = 0;

                for (LazyString str : list) {
                    sum += str.getWritableObject().getLength();
                }

                return new IntWritable(sum);
            }

            @Override
            public String getDisplayString(String[] children) {

                StringBuffer buffer = new StringBuffer();
                buffer.append("sumListStringLength(Array<String>), ");

                for (String child : children)
                    buffer.append(child).append(",");

                return buffer.toString();
            }
        }
        ```
        * 사용법
        ```SQL
        ADD JAR hdfs:///user/hiveUDF.jar;
        CREATE FUNCTION listFunc AS 'com.sec.hive.udf.ListGenericUDF';

        hive> select * from listTable;
        OK
        ["1","2","3"]
        ["4","5","6"]
        ["7","8","9"]
        ["abcdefg","alskdjfalskd","alksdfjalskdfj"]
        ["aslkdfjalskdf","asldkjfalskd","asldkfja"]
        ["asldkfjalskd","asdlkfjalskdjflaksd","asldkjfalsdkjflkasd","alsdkjfalkdjf"]

        -- col_list 를 입력받아서 문자열의 길이를 더하여 반환 
        hive> select listFunc(col_list)
            >   from listTable;
        OK
        3
        3
        3
        33
        33
        63
        Time taken: 0.307 seconds, Fetched: 6 row(s)        
        ```