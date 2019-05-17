// 비교 연산
1 == 1
'a' == 'a'
1 == 'a'

// 문자열 비교
val s1 = "Hello"
val s2 = "Hello"
val s3 = "HELLO"
s1==s2
s2==s3

// 객체 비교
case class Service(company:String, service:String)
val S1 = Service("zum","hub")
val S2 = Service("zum","news")
val S3 = Service("zum","hub")
S1==S2
S1==S3

// 데이터 형 선언
// 암시적 선언
val x = 10
val y = "abc"
// 명시적 선언
val b: Byte = 10
val s: Short = 10
val i: Int = 10
val l: Long = 10
// 값에 약어를 추가
val f = 10.0f
var d = 20.0d

// 논리형
var t = true
if(t)
  println("True!")
else
  println("False!")

// 문자형
var c1:Char = 'a'
var c2 = 'b'

// 문자열
var str1 = "aaa"
var str2 ="""a
    |b
    |c
  """.stripMargin

// 접두어를 이용한 문자열 처리
val name = "David"
println("Hello! ${name}")
println(s"Hello! ${name}")
println("${1+1}")
println(s"${1+1}")

val height:Double = 182.3
val name2 = "James"
println(f"$name2%s is $height%2.2f meters tall")

s"A\nB"
raw"A\nB"

// 변수!
var variable = 10
val value = 20
variable = 30
//value = 30 # Error reassignment to val


