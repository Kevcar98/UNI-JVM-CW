import scala.collection.mutable.{HashMap, HashSet, Set}


class ScalaCP {
  def main(Preq: Array[String], NPreq: Array[String]): (String, Int, List[Int], Int) = {
    // Case class means there is no need to put new to instantiate the class (new DAG -> DAG)
    case class DAG[T](root: T) extends HashMap[T, Set[T]] {
      val handler: TaskHandler = new TaskHandler
      var treeString = "" // For PrintTree() function
      var numNodes = 0 // Targets for setters
      var listPathNodes: List[T] = Nil
      var durationOfPath = 0

      def extend(Parent: Set[T], Child: T) = {
        // Preserve model consistency by setting method requirements for extend function
        require(Parent.subsetOf(this.keySet), s"$Parent") // Parent:Set[T] of task must exist in the DAG
        require(!this.keySet.contains(Child), s" $Child")

        // For each element of Parent tasks, increase successor’s set with new Child task
        Parent.foreach(key => this (key) += Child)
        this (Child) = new HashSet[T]
        this
      }

      def calculateCriticalPath(node: T): (Int, List[T], Int) = {
        require(keySet.contains(node), s" $node")

        if (this (node).isEmpty) (1, List(node), handler.TasksDurationForID(node.toString))
        else {
          var M = 0
          var L: List[T] = Nil
          var D = 0 // Duration of Task
          this (node).foreach(suc => {
            // For any successors, this computes, by recursion, the largest branch using a lambda expression with temporary variables
            val (lm: Int, ll: List[T], d: Int) = calculateCriticalPath(suc)
            if (lm > M) {
              M = lm
              L = ll
              D = d + handler.TasksDurationForID(node.toString) // Calculate Duration of Task
            }

            if (D > durationOfPath) { // Saves the Critical Path based on the highest total duration of each branch
              numNodes = 3 + M // Number of Nodes on Path (3 + M accounts for initial M = 1 at (node).isEmpty, the start task 0, and a missed increment)
              listPathNodes = node :: L // List of Nodes on Path
              durationOfPath = D // Duration of Nodes on Path
              // println("L fin1 " + L)
              (1 + M, L, D) // No need to use node :: L as it has already been added to list L
            }
          }
          )
          // println("L fin2 " + L)
          (1 + M, node :: L, D)
        }
      }

      // Prints a visual representation of the largest branch of the DAG
      def PrintTree(origin: T, Next: Int = 0): String = {
        if (contains(origin)) {
          for (i <- 0 until Next - 1) treeString += "   "
          if (Next > 0) treeString += "|__"
          treeString += origin + ","
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

    Project.calculateCriticalPath(0)
    val LPath = Project.PrintTree(0)

    (LPath, Project.numNodes, Project.listPathNodes, Project.durationOfPath)
  }
}
