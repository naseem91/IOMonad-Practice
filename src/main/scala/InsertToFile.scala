import java.io._
import scala.util.Random

object InsertToFile {
  def main(args: Array[String]): Unit = {
    // FileWriter
    val file = new File("myFile.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    for (i <- 1 to 12)
      bw.write(1 + Random.nextInt(20) + "\n")
    bw.close()
  }
}
