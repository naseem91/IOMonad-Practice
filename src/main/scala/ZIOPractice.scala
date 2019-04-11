import java.io.IOException

import .unsafeRun
import scalaz.zio._
import scalaz.zio.console._

import scala.util.Try

object ZIOPractice extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] =
    ZIOPractice.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))

  var myList: IO[Nothing, List[Option[Int]]] = IO.point(Nil)
  val z: IO[Nothing,Int] = IO.point(5)

  def ZIOPractice: IO[IOException, String] = for {
    _ <- putStrLn("Enter your name")
    name <- getStrLn
    _ <- putStrLn("Hello " + name)
    _ <- putStrLn("Input number ")
    num <- getStrLn
    checkedNum <- check(IO.point(num))
    _ <- putStrLn("the number is " + checkedNum)
    //   _ <- myList =myList :: IO.point(checkedNum)
    _ <-z.attempt.map{
      case Left(_) => putStrLn(1000.toString)
      case Right(d)=>putStrLn(d.toString)
    }
  } yield ("")

  def check(input: IO[IOException, String]) = {
    val s = Try(unsafeRun(input).toInt).toOption match {
      case Some(a) => Some(a)
      case None => None
    }
    IO.point(s)
  }


}
