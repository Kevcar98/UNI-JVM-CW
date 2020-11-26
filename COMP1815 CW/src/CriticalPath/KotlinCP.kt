package CriticalPath
import TaskHandler


class KotlinCP {



    fun main(Preq: Array<String>, NPreq: Array<String>) {
        // The example dependency graph
        //
        /*
        val end = Task("End", 0)
        val F = Task("F", 2, end)
        val A = Task("A", 3, end)
        val X = Task("X", 4, F, A)
        val Q = Task("Q", 2, A, X)
        val start = Task("Start", 0, Q)
        val allTasks = hashSetOf(end, F, A, X, Q, start)*/
/*
        val start = Task("0 - Start", 0)
        val first = Task("1", 10,start)
        val second = Task("2", 1,first)
        val third = Task("3", 30,start)
        val forth= Task("4", 1,start,second,third)
        val fifth= Task("5", 1,forth)
        val allTasks = hashSetOf(fifth, forth, third, second, first, start)
        val allTasks = hashSetOf(third, first, start)
 */
        // Initialize array of tasks without prerequisites (default value of 0 for KotlinCP.Task)

        val tasks = Array<KotlinCP.Task>(NPreq.size, {j -> Task("0 - Start", 0)})
        val start = Task("0 - Start", 0)

        //String[] AssignedPTasks = preq.split(","); // [1->2,3+2->4,4->5]
        //String[] AssignedNPTasks = nPreq.split(","); // [1,3]

        var j = 0
        for (i in NPreq) {
            println("i is " + i)
            tasks[j] = Task(i, TaskHandler().TasksDurationForID(i), start) // Value of "i" is the actual Task ID string of the non-prerequisite task in the array
            j++
            println("nprq iteration i: " + i)
            println(tasks)
        }

        // Initialize array of tasks with prerequisites (default value of 0 for KotlinCP.Task)
        val tasksWithPreq = Array<KotlinCP.Task>(Preq.size, {k -> Task("0 - Start", 0)})

        var k = 0
        for (m in Preq.indices) {
            var arrayP = Preq[m].split("->") // arrayP = when m = 0, [1,2], when m = 1, [3+2,4], when m = 2, [4,5]
            var arrayPL = arrayP[0].split("+") // arrayPL = when m = 0, [1], when m = 1, [3,2], when m = 2, [4]
            println("Iteration " + m + " has arrayPL size: " + arrayPL.size)

            if (arrayPL.size > 1) { // If more than one prerequisite task, loops through them to add to a sequence, passed into a set for input
                // Initialize array of prerequisite tasks (default value of 0 for KotlinCP.Task)
                var seq = Array<KotlinCP.Task>(arrayPL.size, { p -> Task("0 - Start", 0) })

                var n = 0
                // The value of "m" is always within bounds for Preq[] (Preq.indices) and arrayP[] (always contains two values so arrayP[0] is valid)
                // But arrayPL[] can hold a variable amount depending on how many prerequisite tasks it has, so loop its contents into a sequence
                while (n < arrayPL.size) {
                    // Appends prerequisite task to current sequence // seq[n] contents are always in this format : Task("1", 10, start)
                    seq[n] = Task(arrayPL[n], TaskHandler().TasksDurationForID(arrayPL[n]), start)
                    n += 1
                    println("n iteration: " + n)
                    println("seq: " + seq)
                }
                // Appends tasks with prerequisite tasks using the sequence of prerequisite tasks
                tasksWithPreq[k] = Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), *seq)
                println("tasksWithPreq: " + arrayP[1] + ", " + TaskHandler().TasksDurationForID(arrayP[1]) + ", *seq")
            } else {
                // Appends tasks with only one prerequisite task (by selecting exactly the first element of arrayPL and second element of arrayP) to avoid ArrayOutOfBounds
                tasksWithPreq[k] = Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), Task(arrayPL[0], TaskHandler().TasksDurationForID(arrayPL[0]), start))
                println("tasksWithPreq: " + arrayP[1] + ", " + TaskHandler().TasksDurationForID(arrayP[1]) + ", Task(" + arrayPL[0] + ", " + TaskHandler().TasksDurationForID(arrayPL[0]) + ", start")
            }
            k++
        }

        println("SIZE CHECK : " + tasks.size)
        println("SIZE CHECK : " + tasksWithPreq.size)
        for (r in tasks.indices) {
            println("tasks dependencies: " + tasks[r].dependencies)
        }
        for (q in tasksWithPreq.indices) {
            println("tasksWithPreq dependencies: " + tasksWithPreq[q].dependencies)
        }
        val allTasks = hashSetOf<Task>(*tasks, *tasksWithPreq, start)

        calculateCriticalPath(allTasks) // If Task ID is not found (e.g. when an invalid Task Dependency exists such as Task ID 0), may throw a cyclic error exception
        prettyPrintResult(allTasks)
    }

    fun calculateCriticalPath(tasks: HashSet<Task>) {
        // tasks whose critical cost has been calculated
        val completed = hashSetOf<Task>()
        // tasks whose critical cost needs to be calculated
        val remaining = tasks.toHashSet()

        // Backflow algorithm
        // while there are tasks whose critical cost isn't calculated.
        while (remaining.isNotEmpty()) {
            var progress = false

            // find a new task to calculate
            val it = remaining.iterator()
            while (it.hasNext()) {
                val task = it.next()
                if (completed.containsAll(task.dependencies)) {
                    // all dependencies calculated, critical cost is max dependency critical cost, plus our cost
                    val critical = task.dependencies.map { it.criticalCost }.maxOrNull() ?: 0
                    task.criticalCost = critical + task.duration
                    // set task as calculated an remove
                    completed.add(task)
                    it.remove()
                    // note we are making progress
                    progress = true
                }
            }
            // If we haven't made any progress then a cycle must exist in
            // the graph and we wont be able to calculate the critical path
            if (!progress) throw RuntimeException("Cyclic dependency, algorithm stopped!")
        }

        // get the cost
        val maxCost = tasks.map { it.criticalCost }.maxOrNull() ?: -1
        println("Critical path length (cost): $maxCost")

        calculateLatest(tasks, maxCost)
        calculateEarly(tasks)
    }

    fun calculateLatest(tasks: Collection<Task>, maxCost: Int) = tasks.forEach { it.setLatest(maxCost) }

    fun calculateEarly(tasks: Collection<Task>) = initials(tasks).forEach {
        it.earlyStart = 0
        it.earlyFinish = it.duration
        it.setEarlyForDependencies()
    }

    fun initials(tasks: Collection<Task>): Collection<Task> {
        val dependencies = tasks.flatMap { it.dependencies }.toSet()
        return tasks.filter { it !in dependencies }.also {
            println("Initial nodes: ${it.joinToString { node -> node.name }}\n")
        }
    }

    fun prettyPrintResult(tasks: Collection<Task>) {
        val format = "%1$-10s %2$-5s %3$-5s %4$-5s %5$-5s %6$-5s %7$-10s\n"
        System.out.format(format, "Task", "ES", "EF", "LS", "LF", "Slack", "Critical?")
        tasks.sortedWith { o1, o2 -> o1.name.compareTo(o2.name) }.forEach {
            System.out.format(
                    format, it.name, it.earlyStart, it.earlyFinish, it.latestStart, it.latestFinish,
                    it.latestStart - it.earlyStart, if (it.isCritical()) "Yes" else "No"
            )
        }
    }

    // A wrapper class to hold the tasks during the calculation
    class Task(
            var name: String, // a name for the task for printing
            var duration: Int, // the actual cost of the task
            vararg dependencies: Task
    ) {
        // the cost of the task along the critical path
        var criticalCost = 0

        // the earliest start
        var earlyStart = 0

        // the earliest finish
        var earlyFinish = -1

        // the latest start
        var latestStart = 0

        // the latest finish
        var latestFinish = 0

        // the tasks on which this task is dependant
        var dependencies = hashSetOf(*dependencies)

        fun setLatest(maxCost: Int) {
            latestStart = maxCost - criticalCost
            latestFinish = latestStart + duration
        }

        fun setEarlyForDependencies() {
            val completionTime = earlyFinish
            dependencies.forEach {
                if (completionTime >= it.earlyStart) {
                    it.earlyStart = completionTime
                    it.earlyFinish = completionTime + it.duration
                }
                it.setEarlyForDependencies()
            }
        }

        fun isCritical() = earlyStart == latestStart

        fun isDependent(t: Task): Boolean = dependencies.contains(t) || dependencies.any { it.isDependent(t) }
    }
}