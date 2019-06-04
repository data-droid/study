# 빅데이터 - 스칼라, 스파크로 시작하기
* 스파크는 인메모리 기반 처리로 하둡 맵리듀스에 비해 100배 빠른 속도를 제공하며 ML, Graph 분석 등 다양한 컴포넌트를 가짐.
* 스파크는 스칼라로 작성되어 자바보다 짧은 코드로 표현 가능하며, JVM에서 동작하기 때문에 기존 자바 라이브러리를 모두 사용 가능.

## Scala
* 객체 지향 언어의 특징과 함수형 언어의 특징을 가지는 다중 패러다임 프로그래밍 언어.

### 특징
* JVML
    * JVM 위에서 동작하는 언어 (kotlin, Groovy 등..)
    * 자바의 모든 라이브러리를 사용가능함
    * 스칼라 컴파일러를 통해 바이트 코드로 변환하고, 바이트 코드는 JVM상에서 자바와 동일하게 실행됨.
* 함수형 언어
    * 자바에 비해 코드길이가 간결함.
    * getter, setter, 생성자를 생략하고, 표현식을 간소함.
* 바이트 코드 최적화
    * 자바보다 20% 속도가 빠름.
* 동시성에 강함
    * Immutable 변수를 많이 가지고 있어, 속성을 변경 불가능하게 하고, 순수 함수를 사용하여 병렬 프로그래밍에 강점이 있음.
    
### 함수형 프로그래밍
* 함수형 언어 
    * 함수형 프로그래밍의 패러다임을 따르는 언어
    * C#, Java 같은 객체지향언어에서도 람다 함수의 도입을 통해 함수형 지원하려고 함.
* 함수형 프로그래밍
    * 자료 처리를 수학적 함수의 계산으로 취급하고 상태 변화와 가변 데이터를 피함.
    * 순수 함수와 보조 함수의 조합으로 조건문, 반복문을 제거하여 복잡성을 낮춤.
    * 변수 사용을 줄여 상태 변경을 피함.
* 순수 함수(Pure Function)
    * 함수의 실행이 외부에 영향을 끼치지 않는 함수.(병렬 계산이 가능!)
* 익명 함수(Anonymous Function)
    * 함수 선언 없이 익명 함수를 생성하여 코드 길이를 줄임.
    `Arrays.asList(1,2,3).stream().reduce((a,b)->a-b).get()`
* 고차 함수(Higher-Order Function)
    * 함수를 인수로 취급하는 함수.
    * 함수를 입력 파라미터나 출력 값으로 처리.
    * `Collection.sort(new ArrayList<Integer>(), (x,y) -> x>=y? -1:1)`
    
 ### 객체
* 기본 자료형, 함수, 클래스 모두 객체로 취급.
* 비교 연산은 ==, != 으로 모두 가능!
 
 ### 자료형
 * 자바와 동일하며 자바의 Primitive Type은 존재 하지 않으며 모든 것이 클래스.
 * 컴파일 시점에 자바 자료형으로 변함.
 * 숫자형, 논리형, 문자형이 있으며, 선언하지 않고 사용할 수 있으며, 정수형은 Int, 실수는 Double로 선언 됨.
 * 기본 자료형
    * Byte, Short, Int, Long, Float, Double, Char, Boolean
 * 참조 자료형
    * String, Unit, NULL, Nothing, Any, AnyVal, AnyRef
 * 숫자형
    * 정수형 : Byte, Short, Int, Long
    * 실수형 : Float, Double
 * 캐스팅
    * 업캐스팅(사이즈가 더 큰걸로)은 자동으로 됨. 
    * 다운캐스팅은 명시해야함.
 * 접두어 처리
    * 접두어를 붙여 컴파일시점에 문자열 변환 처리 가능
    * s
        * ${변수명{을 이용하여 문자열 내 변수를 값으로 치환. 연산도 가능.
    * f
        * 문자열 포맷팅. printf()와 같은 방식
    * raw
        * 특수문자를 처리하지 않고 원본 문자로 인식.

### 변수
* var : 가변 변수 ( 재할당 가능 ! )
* val : 불변 변수 ( 재할당 불가능 ! ), 동시 처리!!
   
### 함수
* 특징
    * def 로 선언
    * 리턴문과 리턴 타입 생략이 가능함.
    * 매개변수의 파라미터 타입은 생략 불가!
    * 리턴값이 없는 경우 Unit
    * 매개변수는 immutable 변수임! 수정불가!
    * 리턴 타입이 Unit이 아닌 경우 마지막 값을 리턴!
* 축약형
    * 1라인으로 처리 가능한 경우 중괄호 없이 선언 가능!
    * `def printUpper(message:String):Unit = println(message.toUpperCase())`
    * `def printLower(message:String) = println(message.toLowerCase())`
* 파라미터 default
    * def add(x: Int, y: Int=10): Unit = println(x+y)
* 가변 길이 파라미터
    * \*을 이용하면 Seq형으로 변환되어 입력
    * `def sum(num:Int*) = num.reduce( _ + _ )`
* 변수에 함수 결과 할당
    * val, var는 호출할 때마다 같은 값 반환
    * def는 호출할 때마다 다른 값 반환
    * `val random1 = Math.random()`
    * `var random2 = Math.random()`
    * `def random3 = Math.random()`
* 함수 내 다른 함수를 선언할 수 있음!

#### 람다 함수
* \_를 이용하여 묵시적인 파라미터 지정가능.
```scala
 def exec(f: (Int,Int) => Int, x:Int, y:Int) = f(x,y)
 exec((x:Int, y:Int) => x+y, 2, 3)
 exec((x,y)=>x+y, 2,3)
 exec(_ + _ , 3, 1)
```

#### 커링
* 여러 개 인수 목록을 여러 개의 괄호로 정의
* 정해진 파라미터 수보다 적은 인수로 호출시 리턴 값은 나머지 파라미터를 받는 함수.
* `def modN(n:Int, x:Int) = ((x%n)==0)`
* `def modN(n:Int)(x:Int) = ((x%n)==0)`
    * n값을 미리 바인딩 하는 다른 함수로 선언 가능. 
    * 다른 함수의 파라미터로 전달 가능.
    
#### 클로저
* 내부에 참조되는 인수에 대한 바인딩.

#### 타입
* 타입(T)를 이용해 클래스와 함수를 제너릭하게 생성 가능.

### 클래스
* 멤버 변수 선언 및 생략 가능
   ```scala
     class Person(name:String, age:Int) //선언
     val p = new Person("Jay", 29) // 생성
     class A // 멤버변수 생략 가능
   ```
* 클래스 멤버 변수
    * 가변 변수
        * 컴파일러가 클래스 내부에 자동으로 getter, setter 메소드 생성
        * 가변 변수를 읽고, 쓰기 가능
    * 불변 변수
        * 컴파일러가 getter만 생성.
        * 불변 변수는 읽기만 가능
    * 그 외 변수
        * getter, setter가 생성되지 않아 클래스 내에서만 사용가능.
    * default
        * 기본, 변수, 상수 모두 기본값 설정 가능.
* 클래스의 메소드
    * 함수와 마찬가지로 def로 선언
    * override 선언자를 통해 오버라이드 가능.
    * new 를 이용하여 클래스 생성시 오버라이드하여 매소스 재정의 가능.
* 생성자
    * 생성자가 따로 존재하지 않음.
* 상속과 추상 클래스
    * extends
        * 일반 클래스, 추상클래스 모두 상속 가능.
    * abstract
        * 매개변수를 가질 수 있음.
        * 메소드 선언만 하고 자식 클래스에서 구현 가능.
        * 기본 메소드를 구현 가능.
* Sealed Class
    * 하위 타입이 모두 한파일에 있어야 함.
    * 관련 클래스를 한파일에 모두 입력하게 강제할 수 있어 관리 효율성이 높아짐.
    * sealed를 이용하고 trait도 봉인 가능
    * file1.scala
        * `sealed abstract class Furniture`
        * `case class Couch() extends Furniture`
        * `case class Chair() extends Furniture`
    * file2.scala
        * `case class Desk() extends Furniture`
        * sealed class를 생성하고 다른 파일에서 클래스 선언시 illegal inheritance from sealed class Furniture 오류 발생!
#### Case Class       
* 일반 클래스와 달리 인스턴스를 생성할 때 new를 사용하지 않음.
* 불변 데이터
    * 멤버변수는 기본적으로 불변 변수로 선언.
* 패턴 매칭
    * 자바의 case와 유사하지만 더 강력함.
* 데이터 비교
    * 케이스 클래스의 비교는 참조값을 이용하지 않고, 멤버변수의 데이터를 이용해 처리.
* 초기화가 간단함
    * new를 이용하지 않고 초기화 가능.
    * `var p = Person("A", 10)`
* 자동 코드 생성
    * toString, hashCode, equals를 자동으로 생성.
    * 새로운 복제 객체를 리턴하는 copy() 메서드를 자동으로 생성.
#### 패턴 매칭
* 자바의 switch 문과 유사하지만, 기본 자료형외에 케이스 클래스를 이용한 처리 가능.
* param의 값이 value1의 값과 비교되어 일치하는 값의 결과를 반환.
* 언더바(_)는 일치하지 않을 때 처리 결과를 출력.
#### 믹스인 컴포지션
* 클래스와 트레잇을 상속할 때 서로 다른 부모의 변수, 메소드를 섞어서 새로 정의.
### 트레잇 (trait)
* 자바의 인터페이스와 유사.
* 메소드를 정의만 할 수도, 기본 구현을 할 수도 있음.
* 추상 클래스와 달리 생성자 파라미터는 가질 수 없음!
* 가변, 불변 변수 모두 선언 가능.
* 기본 메소드는 상속되고, override를 통한 메소드 재정의 가능
* extends로 상속하고 여러개의 트레잇을 with 키워드로 동시에 구현 가능
* 상속하여 구현하기 때문에 추상클래스와 유사하지만 멤버변수를 가질 수 없음.
* 추상 클래스는 하나만 상속 가능하지만, 트레잇은 여려개를 상속 가능.
* 생성자 멤버변수가 필요하면 추상클래스!, 멤버 변수가 필요없으면 트레잇!
### 싱글톤 객체
* object 선언자로 싱글톤 객체 생성
* 메서드는 전역적으로 접근하고 참조 가능
* 직접 접근 or import 선언.
* 컴패니언
    * 싱글톤 객체와 클래스가 같은 이름인 경우
    * 정적 메소드의 보관 장소를 제공
    * 자바의 static을 이용한 정적 데이터는 컴패니언을 이용하여 처리.
    * 팩토리 메소드 같은 정적 메소드는 컴패니언을 이용하여 작성.
    * 일반적인 데이터는 클래스를 이용하여 정적 데이터와 분리!
### 콜렉션
* 배열
    * 길이가 고정된 자료구조
    * 선언 : val array1 = Array(1,2,3)
    * 접근 : array1(0)
    * 변경 : array1(0) = 10
    * 배열 연결 : val array2 = array1 ++ array1
    * 데이터 추가
        * val array3 = 0 +: array2
        * val array4 = array2 :+ 100
* 리스트
    * 가변 길 데이터를 저장
    * 선언 
        * val list1 = list(1,2,3,4)
        * val list2 = (1 to 100).toList
        * val list3 = array1.toList
    * 접근 
        * list1(0)
        * list1.head
        * list1.tail
* 셋(set)
    * 중복을 허용하지 않는 자료구조
    * 선언 : val s1 = Set(1,1,2) // Set(1,2)
    * 값 유무 확인
        * s1(1) // true
        * s1(3) // false
* 튜플(tuple)
    * 불변의 데이터를 저장
    * 선언 : val hostPort = ("localhost", 80)
    * 접근 : hostPort._1 // localhost
* 맵(map)
    * 사전 형식으로 데이터 저장
    * return이 Option 타입
    * getOrElse를 이용하거나, get 반환값 Option을 패턴매칭을 이용하는게 좋음.
    * 선언
        * val map1 = Map(1->2)
        * val map2 = Map("foo"->"bar")
    * Option 타입 반환
        * map1.get(1) // Option[Int] = Some(2)
    * 키와 일치하는 데이터가 없으면 기본값 반환
        * map1.getOrElse(1,0) // 2
        * map1.getOrElse(10,0) // 0
#### 반복문
* for
    * for (num <- 0 to 3) //to는 이하
    * for (num <- 0 until 3) // until은 미만
    * for (str <- Array("A","B","C"))
    * for (index <- 0 until strs.length)
    * for ((value, index) <- strs.zipWithIndex)
    * for ((k,v) <- Map("k1"->"v1","k2"->"v2","k3"->"v3"))
    * for (x <- 0 to 2; y <- 0 to 3)
    * for (x <- 0 to 10; if x%2)
* do..while
    * val i=0; do { i+=1 } while (i<3)
* while
    * val i=0; while(i<3) { i+=1 }
#### sort, group, filter functions
* map
    * 콜렉션의 각 아이템에 대해 동일 작업을 시행
    * var list = (1 to 10)
    * list.map(_+1) //list 각 아이템에 +1
    * val strs = List("david", "kevin", "james")
    * strs.map(_.toUpperCase) // 대문자로~
* reduce, fold
    * 콜렉션의 아이템을 집계할 때 사용.
    * fold는 기본값(초기값)을 제공할 수 있음.
    * 각 함수모두 left, right 방향을 가질 수 있음.
    * val list = (1 to 10)
    * list.reduce(\_+\_) // 55
    * list.reduceLeft(\_+\_) // 55
    * list.reduceRight(\_+\_) // 55
    * list.reduce(\_-\_) // -53
    * list.reduceLeft(\_-\_) // -53
    * list.reduceRight(\_-\_) // -5
    * list.fold(10)(\_+\_) // 65
* groupBy
    * 데이터를 키 기준으로 병합.
    * 결과를 Map 형식으로 반환
    * 전달된 키와 벨류를 리스트 형식으로 반환
    * `var datas = List(("A",1),("B",2),("C",6),("B",2),("A",8),("C",2))`
    * datas.groupBy(\_.\_1).foreach({case(k,v) => printf("key: %s, value: %s\n", k, v) })
* filter
    * 데이터를 필터링하여 없애거나 분류함
    * partition : 분류할 때 사용.
    * find : 데이터를 검색할 때
    * takeWhile, dropWhile을 이용하여 원하는 부분까지 데이터 선택
   ```scala
    val list = (1 to 10)
    list.filter(\_ > 5) // 5 이상 만
    list.partition(\_ % 2 == 0) // 홀수, 짝수 데이터 분리
    list.find(\_ == 3) // 3을 검색
    val list2 = List(1,2,3,-1,4,5,6)
    list2.takeWhile(\_>0) // 1,2,3
    list2.dropWhile(\_<0) // -1,4,5,6
   ```
* zip
    * 두개의 콜렉션을 같은 인덱스의 데이터로 묶음.
    * 길이가 다르면 작은 것 기준.
    `for ( item <- List(1,2,3).zip(List(1,2,3))) // (1,1),(2,2),(3,3)`
* mapValues
    * Map에서 벨류만 map 함수 처리하고 싶을 때 사용
   ```scala
    var maps = Map("A" -> 1, "B" -> 2, "C" -> 3, "D" -> 4, "E" -> 5)
    maps.mapValues(x=>x*x).foreach(x=>x match {case(k,v) => printf("key: %s, value: %s\n",k,v)})
    var maps = Map("A" -> List(1, 2, 3), "B" -> List(4, 5, 6), "C" -> List(7, 8, 9))
    maps.mapValue(\_.sum).foreach({case(k,v)=>printf("key: %s, value: %s\n",k,v)})
   ```
* sort
    * sorted, sortwith, sortBy
   ```scala
    val list = List(4,6,1,6,0)
    list.sorted // 0,1,4,6,6
    list.sorted(Ordering.Int.reverse) // 6,6,4,1,0
    val sList = List ("aa","bb","cc")
    sList.sortBy(_.charAt(0)) // "aa","bb","cc"
    list.sortWith(_ <= _) // 0,1,4,6,6
    list.sortWith(_ >= _) // 6,6,4,1,0
   ```
