import scalaz.zio._
import scalaz.zio.console._

sealed trait MyS {
  def P: IO[Nothing, Long]
  def V: IO[Nothing, Long]
}

object MyS extends App{

  def run(args: List[String]): IO[Nothing, ExitStatus] =
    myApp.attempt.map(_.fold(_=> 1,_=> 0)).map(ExitStatus.ExitNow(_))

  def myApp=for {
    i <- apply(7.longValue)
    s <- i.P // 7
    s <- i.P // 7
    _ <- putStrLn(s.toString)

  }yield("")

  def apply(v: Long): IO[Nothing, MyS] =
    Ref(v).map { vref =>
      new MyS {
        def V = {vref.update(_ + 1)}

        def P = (vref.get.flatMap { v =>
          if (v < 0)
            IO.fail(())// to print IO.point(v)
          else
            vref.modify(v0 => if (v0 == v) (true, v - 1) else (false, v)).flatMap {
              case false => IO.fail(())
              case true => IO.point(v)
            }
        } <> P)
      }
    }
}