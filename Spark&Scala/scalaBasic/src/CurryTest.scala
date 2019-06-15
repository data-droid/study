object CurryTest extends App {
  def filter(xs: List[Int], p:Int=>Boolean) : List[Int] =
    if (xs.isEmpty) xs
    else if (p(xs.head)) xs.head :: filter(xs.tail, p) // :: 리스트 연결
    else filter(xs.tail, p)

  def modN(n:Int)(x:Int) = ((x%n)==0)
  val nums = List(1,2,3,4,5,6,7,8,9,10)
  println(filter(nums, modN(2)))
  println(filter(nums, modN(3)))
}
