package CriticalPath

import TaskHandler
import java.util.*

class KotlinCP {
    var initialNodes = 0
    var maxCost = 0

    fun main(Preq: Array<String>, NPreq: Array<String>): Triple<Array<String>, String, String> {

        // Initialize array of tasks without prerequisites (default value of 0 for KotlinCP.Task)
        val tasks = Array<KotlinCP.Task>(NPreq.size, { j -> KotlinCP.Task("0 - Start", 0) })
        val start = KotlinCP.Task("0 - Start", 0)

        var j = 0
        for (i in NPreq) {
            // println("i is " + i)
            tasks[j] = KotlinCP.Task(i, TaskHandler().TasksDurationForID(i), start) // Value of "i" is the actual Task ID string of the non-prerequisite task in the array
            j++
            // println("nprq iteration i: " + i)
            // println(tasks)
        }

        // Initialize array of tasks with prerequisites (default value of 0 for KotlinCP.Task)
        val tasksWithPreq = Array<KotlinCP.Task>(Preq.size, { k -> KotlinCP.Task("0 - Start", 0) })

        var k = 0
        for (m in Preq.indices) {
            var arrayP = Preq[m].split("->") // arrayP = when m = 0, [1,2], when m = 1, [3+2,4], when m = 2, [4,5]
            var arrayPL = arrayP[0].split("+") // arrayPL = when m = 0, [1], when m = 1, [3,2], when m = 2, [4]
            // println("Iteration " + m + " has arrayPL size: " + arrayPL.size)

            if (arrayPL.size > 1) { // If more than one prerequisite task, loops through them to add to a sequence, passed into a set for input
                // Initialize array of prerequisite tasks (default value of 0 for KotlinCP.Task)
                var seq = Array<KotlinCP.Task>(arrayPL.size, { p -> KotlinCP.Task("0 - Start", 0) })

                var n = 0
                // The value of "m" is always within bounds for Preq[] (Preq.indices) and arrayP[] (always contains two values so arrayP[0] is valid)
                // But arrayPL[] can hold a variable amount depending on how many prerequisite tasks it has, so loop its contents into a sequence
                while (n < arrayPL.size) {
                    // Appends prerequisite task to current sequence // seq[n] contents are always in this format : Task("1", 10, start)
                    // Old code: // seq[n] = Task(arrayPL[n], TaskHandler().TasksDurationForID(arrayPL[n]), start)
                    var taskFound1 = false
                    for (t in tasks.indices) {
                        if (arrayPL[n] == tasks[t].name) {
                            seq[n] = tasks[t]
                            taskFound1 = true
                        }
                    }
                    for (u in tasksWithPreq.indices) {
                        if (arrayPL[n] == tasksWithPreq[u].name) {
                            seq[n] = tasksWithPreq[u]
                            taskFound1 = true
                        } else if (u == tasksWithPreq.size - 1 && !taskFound1) {
                            seq[n] = KotlinCP.Task(arrayPL[n], TaskHandler().TasksDurationForID(arrayPL[n]), start)
                            println("WARNING 01: Task Dependencies were not found due to unordered tasks - critical path may be wrong or cause an error.")
                            // Example of what causes issues is if tasks were ordered like this: [4->5,3+2->4]
                        }
                    } // Loops through all currently held tasks and if the prerequisite task exists already, sets seq[n] to it
                    // If later down the line the task is found, repeat this process until all tasks are done...? [To-do?]

                    n += 1
                    // println("n iteration: " + n)
                    // println("seq: " + seq)
                }
                // Appends tasks with prerequisite tasks using the sequence of prerequisite tasks
                // tasksWithPreq[k] = Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), *seq)
                tasksWithPreq[k] = KotlinCP.Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), *seq)
                // println("tasksWithPreq: " + arrayP[1] + ", " + TaskHandler().TasksDurationForID(arrayP[1]) + ", *seq")
            } else {
                // If there is only one prerequisite task
                var taskFound2 = false
                for (v in tasks.indices) {
                    if (arrayPL[0] == tasks[v].name) {
                        tasksWithPreq[k] = KotlinCP.Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), tasks[v])
                        taskFound2 = true
                    }
                }
                for (w in tasksWithPreq.indices) {
                    if (arrayPL[0] == tasksWithPreq[w].name) {
                        tasksWithPreq[k] = KotlinCP.Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), tasksWithPreq[w])
                        taskFound2 = true
                    } else if (w == tasksWithPreq.size - 1 && !taskFound2) {
                        // Appends tasks with only one prerequisite task (by selecting exactly the first element of arrayPL and second element of arrayP) to avoid ArrayOutOfBounds
                        tasksWithPreq[k] = KotlinCP.Task(arrayP[1], TaskHandler().TasksDurationForID(arrayP[1]), KotlinCP.Task(arrayPL[0], TaskHandler().TasksDurationForID(arrayPL[0]), start))
                        // println("[else] tasksWithPreq: " + arrayP[1] + ", " + TaskHandler().TasksDurationForID(arrayP[1]) + ", Task(" + arrayPL[0] + ", " + TaskHandler().TasksDurationForID(arrayPL[0]) + ", start")
                        println("WARNING 02: Task Dependencies were not found due to unordered tasks - critical path may be wrong or cause an error.")
                        // Example of what causes issues is if tasks were ordered like this: [4->5,3->4]
                    }
                }
            }
            k++
        }

        val allTasks = hashSetOf<KotlinCP.Task>(*tasks, *tasksWithPreq, start)

        calculateCriticalPath(allTasks) // If Task ID is not found (e.g. when an invalid Task Dependency exists such as Task ID 0), may throw a cyclic error exception
        return Triple(prettyPrintResult(allTasks), this.initialNodes.toString(), this.maxCost.toString())
    }

    fun calculateCriticalPath(tasks: HashSet<Task>): Array<Task> {
        val completed = HashSet<Task>() // Tasks whose critical cost has been calculated
        val remaining = HashSet(tasks) // Tasks whose critical cost needs to be calculated

        // Repeats until no more remaining tasks
        while (!remaining.isEmpty()) {
            var progress = false

            // Get new task to calculate
            val it = remaining.iterator()
            while (it.hasNext()) {
                val task = it.next()
                if (completed.containsAll(task.dependencies)) {
                    // If the critical cost of all the dependencies have been calculated, find the total critical cost, plus the task's cost
                    var critical = 0
                    for (t in task.dependencies) {
                        if (t.criticalCost > critical) {
                            critical = t.criticalCost
                        }
                    }
                    task.criticalCost = critical + task.cost
                    // Set task as calculated and remove
                    completed.add(task)
                    it.remove()
                    // Note that progress is being made
                    progress = true
                }
            }
            // If we haven't made any progress then a cycle must exist in
            // the graph and we won't be able to calculate the critical path
            if (!progress) throw RuntimeException("ERROR 01: Cyclic dependency in graph - function stopped.")
        }

        // Early Start and Early Finish
        val initialNodes = initials(tasks) // Returns tasks "1", "3"
        val firstPassNodes = calculateEarly(initialNodes) // Sets early start and early finish of initial tasks "1", "3"
        val remainingNodes = HashSet(tasks)
        for (t in tasks) {
            for (f in firstPassNodes) {
                // Removes initial tasks and the "0 - Start" task, equivalent to t.name.equals("0 - Start")
                // as non-starting tasks will always have at least one dependency if not "0 - Start"
                if (t.name == f.name || t.name == "0 - Start") {
                    remainingNodes.remove(t) // "2", "4", "5"
                }
            }
        }
        setEarly(firstPassNodes, remainingNodes)
        maxCost(tasks) // Calculates maximum critical path cost

        // Late Start and Late Finish
        val backPassNodes = HashSet<Task>()
        val leftoverNodes = HashSet(tasks)
        for (t in tasks) {
            if (t.criticalCost == maxCost) { // For last item in critical path, latest finish and start are the same as early finish and start
                t.latestFinish = t.earlyFinish
                t.latestStart = t.earlyStart
                backPassNodes.add(t)
                leftoverNodes.remove(t)
            }
        }
        try {
            setLatest(backPassNodes, leftoverNodes)
        } catch (e: Exception) {
            println("ERROR 02: Possible cycle in task dependency on attempt to generate critical path - please adjust the tasks.")
        }

        // Get the tasks
        val ret = completed.toTypedArray()
        // Create a priority list
        Arrays.sort(ret, Comparator { o1, o2 -> //sort by cost
            val i = o2.criticalCost - o1.criticalCost
            if (i != 0) return@Comparator i

            // Using dependency as a tie breaker
            // Note: if a is dependent on b then critical cost a must be >= critical cost of b
            if (o1.isDependent(o2)) return@Comparator -1
            if (o2.isDependent(o1)) 1 else 0
        })
        return ret
    }

    fun setLatest(backPassNodes: HashSet<Task>, leftoverNodes: HashSet<Task>) {
        val pastNodes = HashSet<Task>()
        for (l in leftoverNodes) { // "0 - Start" "1"
            for (b in backPassNodes) { // "2" "3"
                for (td in b.dependencies) { // "1" "0 - Start"
                    if (l.name == td.name && !pastNodes.contains(l)) {
                        pastNodes.add(l) // Adds tasks from leftover nodes if they are dependencies for the backPassNodes (final tasks)
                    }
                }
            }
        }
        for (p in pastNodes) { // "1" "0 - Start"
            for (b in backPassNodes) { // "2" "3"
                if (b.dependencies.contains(p)) { // If statement to check if the parent nodes are actually dependencies
                    p.latestFinish = b.latestFinish - b.cost
                    p.latestStart = p.latestFinish - p.cost
                }
                if (p.name == "0 - Start") {
                    p.latestFinish = 0
                    p.latestStart = 0
                }
            }
        }
        val nextLeftoverNodes = HashSet<Task>()
        for (l in leftoverNodes) { // "0 - Start", "1", "2", "3"
            if (!pastNodes.contains(l) && !nextLeftoverNodes.contains(l)) { // "0 - Start" "1"
                nextLeftoverNodes.add(l) // Adds remaining task nodes to be checked if it's a dependency in future forward passes
            }
        } // "2", "3" , "0 - Start" "1"
        if (!nextLeftoverNodes.isEmpty() && !backPassNodes.isEmpty()) { // Prevent stack overflow by checking if remaining tasks have eligible parent tasks
            setLatest(pastNodes, nextLeftoverNodes)
        }
    }

    fun calculateEarly(initials: HashSet<Task>): HashSet<Task> {
        val nextTasks = HashSet<Task>() // No initial values
        for (initial in initials) {
            initial.earlyStart = 0
            initial.earlyFinish = initial.cost
            nextTasks.add(initial)
        }
        return nextTasks
    }

    fun setEarly(firstPassNodes: HashSet<Task>, remainingNodes: HashSet<Task>) {
        val futureNodes = HashSet<Task>()
        for (r in remainingNodes) { // "4", "2", "5"
            for (f in firstPassNodes) { // "3", "1"
                for (td in r.dependencies) { // "3+2", "1", "4"
                    if (td.name == f.name && !futureNodes.contains(r)) {
                        futureNodes.add(r) // Adds tasks from remaining nodes if they depend on the firstPassNodes (initial tasks)
                    }
                }
            }
        }
        for (n in futureNodes) {
            for (ntd in n.dependencies) {
                if (ntd.criticalCost > n.earlyStart) {
                    n.earlyStart = ntd.criticalCost
                }
            }
            n.earlyFinish = n.earlyStart + n.cost // Updated "2", "4"
        }
        val nextRemainingNodes = HashSet<Task>()
        for (r in remainingNodes) {
            if (!futureNodes.contains(r) && !nextRemainingNodes.contains(r)) {
                nextRemainingNodes.add(r) // Adds remaining task nodes to be checked if it's a dependency in future forward passes
            }
        }
        if (!nextRemainingNodes.isEmpty()) {
            setEarly(futureNodes, nextRemainingNodes)
        }
    }

    fun initials(tasks: Set<Task>): HashSet<Task> {
        val remaining = HashSet(tasks)
        for (t in tasks) {
            var test = 1
            for (td in t.dependencies) { // If no dependencies, positive
                if (td.name == "0 - Start") {
                } else {
                    test = Math.abs(test) * -1 // Negative if any dependencies that aren't "0 - Start"
                }
            }
            // If task has no dependencies OR the task dependency is something other than "0 - Start", remove the task
            // (Tasks except "0 - Start" will always have at least one dependency)
            if (t.dependencies.isEmpty() || test < 0) {
                remaining.remove(t)
            }
        }
        return remaining // Initial Nodes
    }

    fun maxCost(tasks: Set<Task>) {
        var max = -1
        for (t in tasks) {
            if (t.criticalCost > max) max = t.criticalCost
        }
        maxCost = max
    }

    fun prettyPrintResult(tasks: Collection<KotlinCP.Task>): Array<String> {
        var format = "%-15s %-15s %-15s %-15s %-15s %-10s %-10s\n"
        val firstString = String.format(format, "Task", "Early Start", "Early Finish", "Late Start", "Late Finish", "Slack", "Critical?")
        val array = Array<String>(tasks.size + 1) { "" }
        array[0] = firstString

        var i = 1
        tasks.sortedWith { o1, o2 -> o1.name.compareTo(o2.name) }.forEach {
            val taskStrings = String.format(
                    format, it.name, it.earlyStart, it.earlyFinish, it.latestStart, it.latestFinish,
                    it.latestStart - it.earlyStart, if (it.isCritical()) "Yes" else "No"
            )
            if (it.isCritical()) this.initialNodes += 1
            array[i] = taskStrings
            i++
        }
        return array
    }

    // A wrapper class to hold the tasks during the calculation
    class Task(
            var name: String, // Task ID
            var cost: Int, // Task Duration
            vararg dependencies: Task
    ) {
        var criticalCost = 0
        var earlyStart = 0
        var earlyFinish = 0
        var latestStart = 0
        var latestFinish = 0
        var dependencies = HashSet<Task>()

        fun isDependent(t: Task): Boolean {
            // Is it a direct dependency?
            if (dependencies.contains(t)) {
                return true
            }
            // Is it an indirect dependency?
            for (dep in dependencies) {
                if (dep.isDependent(t)) {
                    return true
                }
            }
            return false
        }

        fun isCritical() = earlyStart == latestStart

        init {
            for (t in dependencies) {
                this.dependencies.add(t)
            }
        }
    }
}
