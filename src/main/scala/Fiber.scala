import java.io.IOException
import scalaz.zio._
import scalaz.zio.console.putStrLn

object Fiber extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    fiber.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val fiber: IO[IOException, String] = for {
    _ <- putStrLn("hi")
    res1 <- fib(4)
    _ <- putStrLn(res1.toString)
    anly <- analyzed
    _ <- putStrLn(unsafeRun(analyzed).toString) //??
    res2 <- stringFib("naseem mahasnehe cpe ")
    _ <- putStrLn(res2.toString)
    rc <- fib(5) race fib(4)
    _ <-putStrLn(rc.toString)
    _ <- putStrLn(unsafeRun(localFib).toString)
  } yield ("")

  def fib(n: Int): IO[Nothing, Int] =
    if (n <= 1) {
      IO.point(1)
    } else {
      for {
        fiber1 <- fib(n - 2).fork
        fiber2 <- fib(n - 1).fork
        v2 <- fiber2.join
        v1 <- fiber1.join
      } yield v1 + v2
    }

  def stringFib(str: String): IO[Nothing, String] = {
    if (str == null)
      IO.point("empty string")
    var strLenght = str.length
    for {
      fiber1 <- IO.point(str.slice(0, strLenght / 2).map(ch => if (ch == ' ') ',' else ch)).fork
      fiber2 <- IO.point(str.slice(strLenght / 2, strLenght - 1).map(ch => if (ch == ' ') ',' else ch)).fork
      v2 <- fiber2.join
      v1 <- fiber1.join
    } yield v1+v2
  }

  sealed abstract class Analysis

  case object Analyzed extends Analysis

  def analyzeData[A](data: A): IO[Nothing, Analysis] = IO.now(Analyzed)

  def validateData[A](data: A): IO[Nothing, Boolean] = IO.now(true)

  val data: String = "Hi,Naseem"

  val analyzed =
    for {
      fiber1 <- analyzeData(data).fork // IO[E, Analysis]
      fiber2 <- validateData(data).fork // IO[E, Boolean]
      valid <- fiber2.join
      _ <- if (!valid) fiber1.interrupt
      else IO.point("This is me ")
      analyzed <- fiber1.join
    } yield analyzed

  //Local fiber
  val localFib = for {
    local <- FiberLocal.make[Int]
    _ <- local.set(10)
    v <- local.get
  }yield (v)
}
