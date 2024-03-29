package CriticalPath

import scala.collection.mutable.{HashMap, HashSet, ListBuffer, Set}

class ScalaCP {
  def main(Preq: Array[String], NPreq: Array[String]): (String, Int, List[Int]) = {
    // Case class means there is no need to put new to instantiate the class (new DAG -> DAG)
    case class DAG[T](root: T) extends HashMap[T, Set[T]] {
      var treeString = "" // For PrintTree() function

      def extend(Parent: Set[T], Child: T) = {
        // Preserve model consistency by setting method requirements for extend function
        require(Parent.subsetOf(this.keySet), s"$Parent") // Parent:Set[T] of task must exist in the DAG
        require(!this.keySet.contains(Child), s" $Child")

        // For each element of Parent tasks, increase successor’s set with new Child task
        Parent.foreach(key => this (key) += Child)
        this (Child) = new HashSet[T]
        this
      }

      def insert[T](list: List[T], i: Int, value: T) = {
        val (front, back) = list.splitAt(i)
        front ++ List(value) ++ back
      }

      def TheLargestBranch(node: T): (Int, List[T]) = {
        require(keySet.contains(node), s" $node")

        if (this (node).isEmpty) (1, List(node))
        else {
          var M = 0
          var L: List[T] = Nil
          this (node).foreach(suc => {
            // For any successors, this computes, by recursion, the largest branch using a lambda expression with temporary variables

            //var addedToL = false
           // if (L.contains(suc)) {
           //   insert(L, L.indexOf(suc), node)
            //  M += 1
            //  addedToL = true
            //  println("G was added to")
            //} else { // This if statement adds missing parent nodes (occurs when a child has multiple parents) by
              // checking if the remaining successors have children existing in list G (and therefore list L)
              val (lm, ll) = TheLargestBranch(suc)
              if (lm > M) {
                M = lm
                L = ll
              }
            //}
            println("M " + M)
            println("L " + L)
            /*if (!addedToL) {
              println("yes " + node + " helo")
              node :: L // Matches nodes in list L
            }*/
          }
          )
          println("L " + L)
          (1 + M, node :: L)
        }
      }

      // Prints a visual representation of the largest branch of the DAG
      def PrintTree(origin: T, Next: Int = 0): String = {
        if (contains(origin)) {
          for (i <- 0 until Next - 1) treeString += "   "
          if (Next > 0) treeString+="|__"
          treeString += origin+","
          this (origin).foreach(child => PrintTree(child, Next + 1))
        }
        treeString
      }

      def join(dag: DAG[T]) = this

      // Constructor to initialize itself as a map with the root entry
      this (root) = Set()
    }

    // Takes two input parameters: tasks with prerequisites and tasks without prerequisites - which are managed accordingly
    val Project = DAG[Int](0)

    for (i <- 0 until NPreq.length) {
      var nprq = NPreq(i).toInt
      Project.extend(Set(0), nprq)
      // println("nprq iteration " + i + ": " + nprq)
    }

    for (i <- 0 until Preq.length) { // Preq = [1->2,3+2->4,4->5]
      var arrayP = Preq(i).split("->") // arrayP = when i = 0, [1,2], when i = 1, [3+2,4], when i = 2, [4,5]
      var arrayPL = arrayP(0).split("\\+") // arrayPL = when i = 0, [1], when i = 1, [3,2], when i = 2, [4]

      if (arrayPL.size > 1) { // If more than one prerequisite task, loops through them to add to a sequence, passed into a set for input
        var seq: Seq[Int] = Seq()
        var j = 0
        // The value of "i" is always within bounds for Preq[] (Preq.length) and arrayP[] (always contains two values so arrayP(0) is valid)
        // But arrayPL[] can hold a variable amount depending on how many prerequisite tasks it has, so loop its contents into a sequence
        while (j < arrayPL.size) {
          seq :+= arrayPL(j).toInt // Appends prerequisite task to current sequence
          // println(arrayPL(j))
          // println(seq)
          j += 1
        }
        Project.extend(Set(seq: _*), arrayP(1).toInt)
        // println("seq iteration " + j + ": " + seq + " & child: " + Child)
      } else {
        Project.extend(Set(arrayPL(0).toInt), arrayP(1).toInt)
        // println("arrayPL iteration " + i + ": " + arrayPL(0) + " & child: " + Child)
      }
    }

    val arrayP: (Int, List[Int]) = Project.TheLargestBranch(0) // Contains the size of the critical path as well as the nodes
    val LPath = Project.PrintTree(0)

    // for (i <- 0 until Project.G.length) print("test" + Project.G(i))
    print("test2")

    // arrayP._1 returns the sizeOfLargestBranch
    // arrayP._2 returns the listOfLargestPath
    (LPath, arrayP._1, arrayP._2)
  }
}
