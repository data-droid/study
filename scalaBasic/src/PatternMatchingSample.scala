package  sdk.scala.sample

object PatternMatchingSample extends App {
  class Color(val red:Int, val green:Int, val blue:Int)

  case class Red(r:Int) extends Color(r,0,0)
  case class Green(g:Int) extends  Color(0,g,0)
  case class Blue(b:Int) extends  Color(0,0,b)

  def printColor(c:Color) = c match {
    case Red(v) => println("Red: " + v)
    case Green(v) => println("Green: " + v)
    case Blue(v) => println("Blue: " + v)
    case col:Color => {
      printf("Red: %d, Green: %d, Blue: %d\n",col.red, col.green, col.blue)
    }
    case _ => println("Invalid color")
  }

  printColor(Red(10))
  printColor(Green(20))
  printColor(Blue(30))
  printColor(new Color(10,20,30))
  printColor(null)
}
