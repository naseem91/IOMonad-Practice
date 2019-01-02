import java.io.IOException
import scalaz.zio._
import scalaz.zio.console.putStrLn

//How to use Ref data type {using mutable dt}
object RefTest extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    program.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val program: IO[IOException, String] = for {
    _ <- putStrLn(unsafeRun(x).toString)
  } yield ("")

 val x=  Ref(0).flatMap {
    idCounter => def freshVar: IO[Nothing, String] =
      idCounter.modify(cpt => (s"var${cpt + 1}", cpt + 1))
    for {
      v1 <- freshVar
      v2 <- freshVar
      v3 <- freshVar
    } yield (v1,v2,v3)
  }

}