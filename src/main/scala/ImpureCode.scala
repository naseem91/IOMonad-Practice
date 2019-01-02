import java.io.IOException
import scalaz.zio._
import scalaz.zio.console._
import java.io.File
import org.apache.commons.io.FileUtils

//Know how to deal with exceptions and catch them
object ImpureCode extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] =
    ImpureCode.attempt.map(_.fold(_=> 1, _=> 0)).map(ExitStatus.ExitNow(_))

  def ImpureCode: IO[IOException, String] = for {
    _ <- putStrLn("")
  } yield ("")

  def readFile(fName:String):IO[String,Long]={
    //IO.syncException(FileUtils.sizeOf(new File(fName)))
    // you should change the string to exception
    IO.syncCatch(FileUtils.sizeOf(new File(fName))){
      case e:IOException => "couldn't read file size"
    }
  }
  //  def makeRequest(req: Request): IO[HttpException, Response] =
  //    IO.async[HttpException, Response](k => Http.req(req, k))


}
