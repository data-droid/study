import scala.collection.mutable

trait TestStack[T] {
  def pop():T
  def push(value:T)
}

class TypeSample[T] extends TestStack[T] {
  val stack = new scala.collection.mutable.Stack[T]

  override def pop(): T = {
    stack.pop()
  }

  override def push(value: T): Unit = {
    stack.push(value)
  }
}

