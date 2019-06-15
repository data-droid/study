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

// 패턴 매칭
def matching(x:Int): String = x match {
  case 0 => "Zero"
  case 1 => "One"
  case 2 => "Two"
  case _ => "Default"
}
matching(1)
matching(4)
matching('a')
// case class matching
abstract class Notification
case class Email(sender: String, title: String, body: String) extends Notification
case class SMS(caller: String, message: String) extends Notification
case class VoiceRecording(contactName: String, link: String) extends Notification
def showNotification(notification: Notification): String = {
  notification match {
      // body는 반환값에 사용하지 않기 때문에 _로 처리가능
    case Email(email, title, _) => s"You got an email from ${email} with title: ${title}"
    case SMS(number, message) => s"You got a SMS from ${number}! Message:${message}"
    case VoiceRecording(name, link) => s"You received a Voice Recording from $name! Click the link to hear it : $link"
  }
}
val email = Email("jaekyung.dev@gmail.com","Hello","I hame a Question")
val someSMS = SMS("010-0101-0101", "Where Are You?")
val someVoiceRecording = VoiceRecording("Jay","jaekyung.dev")
println(showNotification(email))
println(showNotification(someSMS))
println(showNotification(someVoiceRecording))
// 케이스 클래스 매칭 패턴 가드
def showImportantNotification(notification: Notification, importantPeopleInfo: Seq[String]): String = {
  notification match {
    // importantPeopleInfo에 같은 이메일이 존재시!
    case Email(email,_,_) if importantPeopleInfo.contains(email) => "You got an email from special someone!"
    // importantPeopleInfo에 같은 번호가 존재시!
    case SMS(number,_) if importantPeopleInfo.contains(number) => "You got a SMS from special someone!"
    // 아니면 showNotification 호출!
    case other => showNotification(other)
  }
}
val importantPeopleInfo = Seq("010-0101-0102","jaekyung.dev2@gmail.com")
val importantEmail = Email("jaekyung.dev2@gmail.com","Drinks?","Food?")
val importantSMS = SMS("010-0101-0102","I'm here! Where are you?")
println(showImportantNotification(someSMS, importantPeopleInfo))
println(showImportantNotification(someVoiceRecording, importantPeopleInfo))
println(showImportantNotification(importantEmail,importantPeopleInfo))
println(showImportantNotification(importantSMS, importantPeopleInfo))
// 클래스 타입 매칭
abstract class Device
case class Phone(model: String) extends Device {
  def screenOff = "Turning screen off"
}
case class Computer(model: String) extends Device {
  def screenSaverOn = "Turning screen saver on..."
}
def goIdle(device: Device) = device match {
  case p: Phone => p.screenOff
  case c: Computer => c.screenSaverOn
}
val phone = Phone("IPhone")
val computer = Computer("Macbook")
println(goIdle(phone))
println(goIdle(computer))

// 믹스인 컴포지션
abstract class A {
  val message:String
}
class B extends A {
  val message = "I'm an instance of class B"
}
trait C extends A {
  def loudMessage = message.toUpperCase()
}
class D extends B with C
val mix = new D
println(mix.message)
println(mix.loudMessage)

// Trait
trait Machine {
  val serialNumber: Int = 1
  def work(message: String)
}
trait KrMachine {
  var conturyCode: String = "kr"
  def print() = println("Korean!")
}
class Computer2(location:String) extends Machine with KrMachine {
  this.conturyCode = "us"
  def work(message: String) = println(message)
}
class Car(location:String) extends Machine with KrMachine {
  def work(message: String) = println(message)
  override def print() = println("Driving")
}
var machine = new Computer2("Laptop")
var car = new Car("Sonata")
machine.work("Coumputing")
machine.print()
println(machine.conturyCode)
car.work("driving...")
car.print()
println(car.conturyCode)

// 싱글톤 객체
object Bread {
  val name: String = "Basic Bread"
  def cooking() = println("Baking...")
}
import Bread.cooking // import를 이용한 접근
cooking
Bread.cooking  // 직접 접근
// 컴패니언
class Dog2
object  Dog2 { // class와 같은 이름으로 object 생성
  def bark = println("bark") // 정적 메소드 저장.
}
Dog2.bark
// 컴패니언을 이용한 팩토리 예제!
class Email2(val username: String, val domainName: String)
object Email2 {
  def fromString(emailString:String): Option[Email2] = {
    emailString.split('@') match {
      case Array(a,b) => Some(new Email2(a,b)) // @로 나눠지면 Email 객체 생성
      case _ => None
    }
  }
}
val scalaCenterEmail = Email2.fromString("scala.centor@epfl.ch")
scalaCenterEmail match {
  case Some(email) => println(
    s"""Registered an email
       |Username: ${email.username}
       |Domain name: ${email.domainName}
     """.stripMargin)
  case None => println("Error: could not parse email")
}

// for
for (x <- 0 to 10 if x%2==0)
  println(x)
for (x <- 0 to 2; y <- 0 to 2; if x!=y)
  println(x,y)
val strs = Array("A", "B", "C", "D", "E")
for((value, index) <- strs.zipWithIndex)
  println(value, index)
def fives(n: Int) = {
  for (x <- 0 to n; if x%5==0)
    yield x //시퀀스 output
}
for(num <- fives(50))
  println(num)
def checkSum(num: Int, sum: Int) = {
  for (
    start <- 0 until num;
    inner <- start until num if start + inner == sum
  ) yield (start, inner);
}
checkSum(20,32) foreach {
  case(i,j) =>
    println(s"($i,$j)")
}
var i2 = 0;
do {
  println(i2)
  i2+=1
} while (i2<3)
var i3 = 0;
while(i3<3) {
  println(i3)
  i3+=1
}
// map
var list = (1 to 10)
list.map(_+1) //list 각 아이템에 +1
val strs2 = List("david", "kevin", "james")
strs2.map(_.toUpperCase) // 대문자로~
// reduce, fold
list.reduce(_+_) // 55
list.reduceLeft(_+_) // 55
list.reduceRight(_+_) // 55
list.reduce(_-_) // -53
list.reduceLeft(_-_) // -53
list.reduceRight(_-_) // -5
list.fold(10)(_+_) // 65
// groupBy
var datas = List(("A",1),("B",2),("C",6),("B",2),("A",8),("C",2))
datas.groupBy(_._1).foreach({case(k,v) => printf("key: %s, value: %s\n", k, v) })
// filter
list.filter(_ > 5) // 5 이상 만
list.partition(_ % 2 == 0) // 홀수, 짝수 데이터 분리
list.find(_ == 3) // 3을 검색
val list2 = List(1,2,3,-1,4,5,6)
list2.takeWhile(_>0) // 1,2,3
list2.dropWhile(_<0) // -1,4,5,6
// zip
for ( item <- List(1,2,3).zip(List(1,2,3)))// (1,1),(2,2),(3,3)
  println(item)
// mapValues
var maps = Map("A" -> 1, "B" -> 2, "C" -> 3, "D" -> 4, "E" -> 5)
maps.mapValues(x=>x*x).foreach(x=>x match {case(k,v) => printf("key: %s, value: %s\n",k,v)})
var maps = Map("A" -> List(1, 2, 3), "B" -> List(4, 5, 6), "C" -> List(7, 8, 9))
maps.mapValues(_.sum).foreach({case(k,v)=>printf("key: %s, value: %s\n",k,v)})
// sort
val list = List(4,6,1,6,0)
list.sorted // 0,1,4,6,6
list.sorted(Ordering.Int.reverse) // 6,6,4,1,0
val sList = List ("aa","bb","cc")
sList.sortBy(_.charAt(0)) // "aa","bb","cc"
list.sortWith(_ <= _) // 0,1,4,6,6
list.sortWith(_ >= _) // 6,6,4,1,0

case class Person(index:Int, var correct:Int)

val persons = Array(Person(1,3),Person(2,4),Person(3,4))
val list =persons.sortWith((x:Person, y:Person) => {
  if(x.correct == y.correct)
    x.index >= y.index
  x.correct > y.correct
}).toList
println(list)