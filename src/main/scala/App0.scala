import scala.io.StdIn.readLine
//This  code require to input a list from user and then find the second maximum number
object App0 {
  def main(args: Array[String]): Unit = {
    var max1 ,max2 = Int.MinValue
    println("Enter size of the list ")
    val size = readLine().toInt
    if (size <= 2) {
      println(" Invalid Input")
      return 0
    }
    var i = 0
    var myList:List[Int]=Nil
    for(i <- 1 to size){
      println("Enter new list element ")
      var elem = readLine().toInt
      myList = elem::myList
    }
    println(myList)

    for(i <- 1 to size-1){
      if(myList(i)>max1){
        max2 = max1
        max1 = myList(i)
      }
      else if (myList(i) > max2 && myList(i) != max1)
        max2 = myList(i)
    }
    println(max2)
  }
}
