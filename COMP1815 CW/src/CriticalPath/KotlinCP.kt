package CriticalPath

class KotlinCP {
    fun main() {
        // The example dependency graph
        val end = Task("End", 0)
        val F = Task("F", 2, end)
        val A = Task("A", 3, end)
        val X = Task("X", 4, F, A)
        val Q = Task("Q", 2, A, X)
        val start = Task("Start", 0, Q)
        val allTasks = hashSetOf(end, F, A, X, Q, start)















        calculateCriticalPath(allTasks)
        prettyPrintResult(allTasks)
    }

    fun calculateCriticalPath(tasks: Collection<Task>) {
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