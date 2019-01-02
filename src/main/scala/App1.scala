import java.io.IOException
import scalaz.zio._
import scalaz.zio.console._
import scala.util.Try

//This is the conversion to App0 using ZIO monad
object App1 extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] =
    myApp.attempt.map(_.fold(_=> 1,_=> 0)).map(ExitStatus.ExitNow(_))

  var myList: List[IO[Nothing, Int]] = Nil

  def myApp: IO[IOException, String] = for {
    size <- inputSize()
    checkedSize <- size match {
      case Some(a) => addToList(List(Int.MinValue), a)
      case None => putStrLn("Invalid size ")
    }

  } yield (null)

  //Input size
  def inputSize() = {
    for {
      _ <- putStrLn("Enter size of the list")
      size <- getStrLn
      checkedSize <- check(IO.point(size))
    } yield (checkedSize)
  }

  def check(input: IO[IOException, String]): IO[Nothing, Option[Int]] = {
    val s = Try(unsafeRun(input).toInt).toOption match {
      case Some(a) => Some(a)
      case None => None
    }
    IO.point(s)
  }

  def addToList(list: List[Int], sizeOfList: Int): IO[Nothing, Any] = for {
    myList <- Ref(list)
    l <- myList.get
    _ <- sizeOfList match {
      case 0 => putStrLn(unsafeRun(myList.get).toString)
      case e => unsafeRun(enterElemnt) match {
        case Some(a) => {
          val newList = unsafeRun(myList.update(x=> a :: unsafeRun(myList.get)))
          myList.set(newList)
          addToList(unsafeRun(myList.get), sizeOfList - 1)
        }
      }
    }
  } yield ()

  def enterElemnt(): IO[IOException, Option[Int]] = for {
    _ <- putStrLn("Enter new list element")
    element <- getStrLn
    enteredElement <- Try((element).toInt).toOption match {
      case Some(a) => IO.point(Some(a))
      case None => IO.point(None)
    }
  } yield (enteredElement)
}