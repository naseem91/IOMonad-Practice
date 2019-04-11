import java.io.IOException
import scalaz.zio._
import scalaz.zio.console.putStrLn
import org.apache.commons.io.FileUtils
import java.io.File

//dealing with failure and catch errors
object IOFailure extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    app.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val app = for {
    _ <- putStrLn("")
    _ <- putStrLn(unsafeRun(read).toString)
    sq <- sqrt(IO.point(9.0)) match {
//      case e:Exception => putStrLn("minus not allowed ")
      case num =>putStrLn(unsafeRun(num).toString)

    }
  } yield ("")

  def openFile(s: String): IO[String, Long]= {
    IO.syncCatch(FileUtils.sizeOf(new File(s))) {
      case e:Exception => e.getMessage
    }
  }

  val read = openFile("file1323.txt").attempt.map {
    case Left(left) => left
    case Right(right) => right
  }

  def sqrt(io: IO[Nothing, Double]):IO[String, Double] =
    IO.absolve(
      io.map(value =>
        if (value < 0.0) Left("Value must be >= 0.0")
        else Right(Math.sqrt(value))
      )
    )
}
