import scalaz.zio.IO
import java.io.IOException
import scalaz.zio._
import scalaz.zio.console.putStrLn
import org.apache.commons.io.FileUtils
import java.io.File
import scala.collection.JavaConversions._
import scala.util.Try

//This code read from file int numbers then trying to find even ints using fibers
object FileManpulations extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    app.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  val app = for {
    list <- readFile("myFile.txt")
    fiber1 <- list match {
      case li: List[String] => findEven(li.slice(0, li.length / 2)).fork
    }
    fiber2 <- list match {
      case li: List[String] => findEven(li.slice(li.length / 2, li.length - 1)).fork
    }
    even1 <- fiber1.join
    even2 <- fiber2.join
    _ <- putStrLn((even1).toString + even2.toString())
  } yield ("")

  //Read file into array
  def readFile(s: String) = {
    IO.syncCatch(FileUtils.readLines(new File(s), "UTF8").toList) {
      case e: IOException => "Null pointer exception "
    }
  }

  def countEven(list: List[String]) = for {
    counter <- Ref(0)
    optionsList <- IO.point(list.map(str => Try(str.toInt).toOption match {
      case Some(num) => num
    }))
    list3 <- IO.point(optionsList.filter(x => x % 2 == 0))
    _ <- counter.set(list3.length)
  } yield unsafeRun(counter.get)

  def findEven(list: List[String]) = for {
    optionsList <- IO.point(list.map({ str => Try(str.toInt).toOption }))
    intList <- IO.point(optionsList.map(num => num match {
      case Some(n) => n
      case None => ???
    }))
    evenList <- IO.point(intList.filter(x => x % 2 == 0))

  } yield (evenList)
}
