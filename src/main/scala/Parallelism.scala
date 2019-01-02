import scalaz.zio.IO
import java.io.IOException
import scalaz.zio._
import scalaz.zio.console._

//How to make parallel operations over data structures
object Parallelism extends App{

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    addition.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val myList = List.range(1,20)
  val addition: IO[IOException, String] = for {
     res <- mapValue(myList.slice(0,9)).par(mapValue(myList.slice(10,19)))
     (l1,l2)=res
    _ <-putStrLn(res.toString())
  } yield ("")

  def mapValue(l:List[Int]):IO[Nothing,List[Int]]={
    IO.point(l.map(a => if(a%2 == 0) a*2 else a*3 ))//odd *3 ,even *2
  }
}
