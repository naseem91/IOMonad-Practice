import scalaz.zio._
import java.io.IOException
import scalaz.zio.console._
import scalaz.zio.duration._

//Promise data type
object PromiseTest extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    program.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val program: IO[IOException, Unit] = for {
    promise <- Promise.make[Nothing, String]
    sendHelloWorld <- promise.complete("Hi world").delay(1.second)
    getAndPrint <- promise.get.flatMap(putStrLn)
//    fiberA <- sendHelloWorld.fork
//    fiberB <- getAndPrint.fork
//    f1 <- fiberA.join
//    f2 <- fiberB.join
  } yield (getAndPrint)//case using fiber you can call f1,f2

}
