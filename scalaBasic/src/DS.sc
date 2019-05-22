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

// Type
def sample[K](key:K): Unit = {
  println(key)
}
def sample2 = sample[String] _
sample2("Hello")

// Class
class Person(name:String, age:Int)
val p = new Person("Jay", 29)
class A

// Class Variables
// 기본
class Animal(name: String) {
  println(s"${name} Created!")
}
// 가변
class Dog(var name: String) {
  println(s"${name} Created!!")
}
// 불변
class Cat(val name: String) {
  println(s"${name} Created!!!")
}
var ani = new Animal("animals")
var dog = new Dog("dogs")
val cat = new Cat("Cats")
// println(ani.name) // getter 가 없어서 못읽어요 ㅠㅠ
println(dog.name)
println(cat.name)
dog.name = "dogs2"
// cat.name = "cats2"  // setter가 없어서 안되요!!

// default var
class Person1(name:String, age:Int)
class Person2(var name:String, var age:Int=10)
class Person3(val name:String="Jay", val age:Int)
var p1 = new Person1("Jay",29)
var p2 = new Person2("Jay")
var p3 = new Person3("JayK",29)
//var p4 = new Person3(12) // name에 기본값을 입력할 수 없어서 오류!
var p4 = new Person3(age=29)

// class method
class PersonMethod(name:String, age:Int) {
  def greeting() = println(s"${name} is ${age} old")
}

// method override
class PersonOverride(name:String, age:Int, val job:String) {
  def greeting() = println(s"${name} is ${age} old")
  def work() = println(s"${name} is ${job}")
}

class Writer(name:String, age:Int) extends PersonOverride(name, age,"") {
  override def work() = println(s"${name} is writer")
}
var w = new PersonOverride("david",25, "writer")
w.greeting()
w.work()
val po = new PersonOverride("David",15,"Students") {
  override def work() = println(s"${name} is ${job}")
}
po.greeting()
po.work()

// 생성자
class Person4(name:String, age:Int) {
  def greeting() = println(s"${name} is ${age} old")
  println("created!")
}
val pc = new Person4("David",15)

// extends and abstract
abstract class Person5(name:String, age:Int) {
  def work
  def status(str: String)
  def greeting() = println(s"${name} is ${age} old")
}
class Player(name: String, age: Int) extends Person5(name,age) {
  def work = println("Working!")
  def status(str:String) = println(s"${str}!!")
}
var pa = new Player("jay", 29)
pa.work
pa.status("Wake up")

// Case Class
case class Person6(name:String, age:Int)
var pcase = Person6("Jay", 29)
pcase.name
// pcase.name = "jay2" // Error : 불변데이터!!
var pcase2 = Person6("Jay",29)
var pcase3 = Person6("Jayk",29)
pcase == pcase2
pcase2 == pcase3
// 자동 코드 생성 toString, hashCode ...
pcase.toString()
pcase.hashCode()
