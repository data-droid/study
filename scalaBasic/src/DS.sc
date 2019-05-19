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

// 함수!
def add(x: Int, y: Int): Int = {
  return x + y
}
add(3,5)
def add(x: Int): Int = {
  //x = 10 #immutable var
  var y = 10
  y
}
add(3)
def add(x: Int, y: Double) = {
  x + y
}
add(3,4.0)
def add(x:Int, y: Int) = {
  println(x + y)
}
add(3,3)
// 축약형
def add(x:Int, y: Int):Int = return x + y
add(40,60)
// 파라미터 기본값
def add(x:Int, y: Int=10): Unit = println(x+y)
add(3)
// 가변 길이 파라미터
def sum(num:Int*) = num.reduce(_+_)
sum(1,2,3,4,5)
// 변수에 함수 결과 할당
val random1 = Math.random()
var random2 = Math.random()
def random3 = Math.random()
println(random1-random1)
println(random2-random2)
println(random3-random3)
// 함수 중첩
def run(): Unit = {
  def middle(): Unit = {
    println("middle")
  }
  println("Start")
  middle()
  println("End")
}
run

// lambda funtion
def exec(f:(Int,Int)=> Int, x:Int, y:Int) = f(x,y)
exec((x:Int, y:Int)=>x+y,2,3)
exec((x,y)=>x+y,2,3)
exec(_+_,2,3)

// 커링
def modN(n:Int)(x:Int) = ((x%n)==0)
def modOne:Int => Boolean = modN(1)
def modTwo = modN(2) _
println(modOne(4))
println(modTwo(4))
println(modTwo(5))

// 클로져
def divide(n:Int) = (x:Int) => (x/n)
def divideFive = divide(5)
println(divideFive(10))
var factor =10
def multiplier = (x:Int) => x*factor
println(multiplier(4))
factor =100
println(multiplier(4))