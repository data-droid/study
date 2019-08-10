# WordCount
![](https://i.stack.imgur.com/199Q1.png)
```JAVA
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    // 맵
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            // 입력된 한라인(value)을 공백을 기준으로 분할하여 
            // context 객체를 이용하여 임시파일로 저장

            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    // 리듀스 
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        // 키(문자)별로 전달된 문자의 개수를 모두 더하여 출력  
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        // Job 객체를 이용하여 하둡 작업을 실행 
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));  // 입력 파일위치
        FileOutputFormat.setOutputPath(job, new Path(args[1]));  // 출력 파일위치 
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

* 실행
    * 빌드 후 jar 파일로 생성하고 실행.
    * `> hadoop jar Mapreduce.jar sdk.WordCount $input $output`
    
* 처리 단계
    * 입력
        * 파일입력 : `FileInputFormat.addInputPath(job, new Path(args[0]));`
    * 맵
        * <key, value>에서 value를 공백 단위로 분할하여 하나씩 나열. <문자,1>
    * 컴바이너
        * Map 결과물을 1차로 reduce 해줌 <문자, List(1)> <LOVE,List(1,1,1,1,1)>
    * 리듀서
        * key별 전달된 전체 List 수 더하여 최종 카운트 생성 <LOVE, 5>
    * 출력
        * 지정한 위치에 파일로 출력. 리듀서마다 출력함.
        
# wordcount2
* setup 함수 사용
* counter 사용
* 분산캐시 사용
* 옵션파서 사용

```JAVA
package sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

public class WordCount2 {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        // 문자의 개수를 세는 카운터 
        static enum CountersEnum {
            INPUT_WORDS
        }

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        private boolean caseSensitive;  // 대소문자 구분 
        private Set<String> patternsToSkip = new HashSet<String>(); // 문자에서 제거할 기호 

        private Configuration conf;

        // 맵작업의 설정 
        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            conf = context.getConfiguration();
            caseSensitive = conf.getBoolean("wordcount.case.sensitive", true);  // 대소문자 구분 
            if (conf.getBoolean("wordcount.skip.patterns", false)) {    // 스킵 패턴 파일을 분산 캐쉬에서 가져와서 patternsToSkip에 설정 
                URI[] patternsURIs = Job.getInstance(conf).getCacheFiles();
                for (URI patternsURI : patternsURIs) {
                    Path patternsPath = new Path(patternsURI.getPath());
                    String patternsFileName = patternsPath.getName().toString();
                    parseSkipFile(patternsFileName);
                }
            }
        }

        private void parseSkipFile(String fileName) {
            try (BufferedReader fis = new BufferedReader(new FileReader(fileName))) {
                String pattern = null;
                while ((pattern = fis.readLine()) != null) {
                    patternsToSkip.add(pattern);
                }
            } catch (IOException ioe) {
                System.err.println("Caught exception while parsing the cached file '" + StringUtils.stringifyException(ioe));
            }
        }

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String line = (caseSensitive) ? value.toString() : value.toString().toLowerCase();

            // patternsToSkip과 일치하는 문자를 제거 
            for (String pattern : patternsToSkip) {
                line = line.replaceAll(pattern, "");
            }

            StringTokenizer itr = new StringTokenizer(line);
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);

                // 카운터에 1을 더함  
                Counter counter = context.getCounter("User Custom Counter", CountersEnum.INPUT_WORDS.toString());
                counter.increment(1);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        // 옵션 파서를 이용해서 옵션의 개수가 2 또는 4개가 아니면 오류 메시지 출력후 종료
        // -D로 전달되는 옵션은 conf에 설정 
        GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
        String[] remainingArgs = optionParser.getRemainingArgs();
        if (!(remainingArgs.length != 2 || remainingArgs.length != 4)) {
            System.err.println("Usage: wordcount <in> <out> [-skip skipPatternFile]");
            System.exit(2);
        }

        // 맵리듀스 잡 설정 
        Job job = Job.getInstance(conf, "word count 2");
        job.setJarByClass(WordCount2.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 옵션으로 -skip 으로 전달된 파일 경로를 분산 캐쉬에 추가하고,
        // wordcount.skip.patterns를 true로 설정 
        List<String> otherArgs = new ArrayList<String>();
        for (int i = 0; i < remainingArgs.length; ++i) {
            if ("-skip".equals(remainingArgs[i])) {
                job.addCacheFile(new Path(remainingArgs[++i]).toUri());
                job.getConfiguration().setBoolean("wordcount.skip.patterns", true);
            } else {
                otherArgs.add(remainingArgs[i]);    // 파일경로 
            }
        }
        FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

* setup 함수
    * map, reduce 함수 실행정 setup 함수를 호출
    * 설정값과 전처리를 처리.
    * 컨텍스트에서 설정값을 가져와서 설정에 이용하면 됨.

* Counter 이용
    * enum을 이용하여 counter를 등록
    * context에서 counter를 가져와 사용하면 됨.
    
* 분산 캐시 이용
    * job에 addCacheFile을 이용하여 등록.
    * map, reduce에서 getCacheFile을 이용함.

* 옵션 파서를 이용
    * 사용자가 개별적으로 설정을 추가하지 않아도, 설정값을 효율적으로 추가 가능
    * 실행시점에 사용자가 입력한 설정값의 접두어를 분석하여 conf 설정
    
* 실행
    * 대소문자 구분없이 실행
        ```bash
        > hadoop jar SdkMapreduce.jar sdk.WordCount2 -Dwordcount.case.sensitive=false /user/word/input2 /user/word/output2
        ```
    * 기호 제거 처리
        ```bash
        > echo "\!\n\,\n\." > replace.txt
        > hadoop fs -put ./replace.txt /user/word/replace.txt 
        > hadoop jar SdkMapreduce.jar sdk.WordCount2 -Dmapred.job.queue.name=queue_name -Dwordcount.case.sensitive=false /user/word/input2 /user/word/output2 -skip /user/word/replace.txt
        ```