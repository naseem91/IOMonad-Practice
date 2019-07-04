import scalaz.zio.ZIO

object EitherTest {
  def main(args: Array[String]): Unit = {
    def divideXByY(x: Int, y: Int): Either[String, Int] = {
      if (y == 0) Left("Dude, can't divide by 0")
      else Right(x / y)
    }

    // a few different ways to use Either, Left, and Right
    println(divideXByY(1, 0))
    println(divideXByY(1, 1))
    divideXByY(1,0) match {
      case Left(s) => println("Answer: " + s)
      case Right(i) => println("Answer: " + i)
    }
  }
}

