import scalaz.zio._
import scalaz.zio.console._

//Dealing with queues
object QueueTest extends App {

  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    program.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  def program = for {
    _ <- putStrLn(unsafeRun(queue).toString)
    _ <- putStrLn(unsafeRun(queueFib).toString)
    _ <- putStrLn(unsafeRun(queueOfferAll).toString)
  } yield ()

  val queue = for {
    myQueue <- Queue.bounded[Int](100)
    _ <- myQueue.offer(4)
    _ <- myQueue.offer(5)
    _ <- myQueue.offer(2)
    removed <- myQueue.take
  } yield (removed, unsafeRun(myQueue.size))

  val queueFib = for {
    myQueue <- Queue.bounded[Int](1)
    _ <- myQueue.offer(1)
    f <- myQueue.offer(2).fork
    _ <- myQueue.take
    _ <- f.join
  } yield (unsafeRun(myQueue.size))

  val queueOfferAll = for{
    myQueue <- Queue.bounded[Int](100)
    _ <- myQueue.offerAll(List.range(0,100))
  }yield (unsafeRun(myQueue.size))
}

