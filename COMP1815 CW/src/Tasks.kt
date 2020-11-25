import java.io.*


data class Tasks(
        var TaskID: Int = 1,
        var ProjectID: Int = 0,
        var Commissioner: String = "",
        var ProjectMng: String = "",
        var Duration: Int = 0,
        var AssignedTeamsID: Int = 0,
        var Progress: Int = 0) {
}

class TaskHandler() {
    val task = mutableListOf<Tasks>()

    fun createTask(
            TaskID: String,
            ProjectID: String,
            Commissioner: String,
            ProjectMng: String,
            Duration: String,
            AssignedTeamsID: String,
            Progress: String
    ): List<Tasks> {
        task.add(
                Tasks(
                        TaskID = if (TaskID.isEmpty()) 1 else TaskID.toInt(),
                        ProjectID = if (ProjectID.isEmpty()) 0 else ProjectID.toInt(),
                        Commissioner = if (Commissioner.isEmpty()) "Unknown Commissioner" else Commissioner,
                        ProjectMng = if (ProjectMng.isEmpty()) "Unknown Project Manager" else ProjectMng,
                        Duration = if (Duration.isEmpty()) 0 else Duration.toInt(),
                        AssignedTeamsID = if (AssignedTeamsID.isEmpty()) 0 else AssignedTeamsID.toInt(),
                        Progress = if (Progress.isEmpty()) 0 else Progress.toInt()
                )
        )
        return task
    }

    fun save(task: List<Tasks>?) {
        CreateFile.createFileTask() save task.toString()
    }

    fun loadTasks(): List<Tasks>? {
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                for (i in parts.indices) {
                    createTask(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6]
                    ) // Creates an object of type Tasks for each, using Parameter data
                }
            }
            br.close()
            return task // Returns the list "task" as the createTask function already added the Tasks to it
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return null
    }

    fun uniqueIDCheck(ID: String): Boolean {
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                // Loads Tasks from File, then this block checks if inputted Task ID is existing
                var emptyInput = false
                if (ID == "") emptyInput = true
                for (i in parts.indices) {
                    for (j in allParts[i].indices) {
                        if (allParts[i][0] == ID || (emptyInput && allParts[i][0] == "1")) { // if TaskIDF text field is empty, TaskID is 1 so check if it exists
                            println("Error: ID is not unique!")
                            br.close()
                            return false
                        }
                    }
                }
            }
            br.close()
            return true
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return false
    }

    fun updateTasksProgress(ID: String, Progress: String): Boolean {
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                // Loads Tasks from File, then this block Updates the Progress of selected Task ID
                var foundID = false
                for (i in parts.indices) {
                    if (allParts[i][0] == ID) {
                        allParts[i][6] = Progress
                        foundID = true
                    }
                }
                if (!foundID) {
                    println("Error: ID not found!")
                    br.close()
                    return false
                }

                // Clears current contents of Tasks.txt file
                val pw = PrintWriter("Tasks.txt")
                pw.close()

                for (i in parts.indices) {
                    createTask(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6]
                    ) // Creates an object of type Tasks for each, using Parameter data
                    save(task) // Saves newly created task from array of tasks (now using the task with the modified Progress) to emptied Tasks.txt file
                    task.clear() // Clears mutable list of tasks to avoid saving the entire list of tasks each loop through the array
                }
                br.close()
                return true
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        } catch (e: StringIndexOutOfBoundsException) {
            // println("Warning: String Index Out of Bounds Exception")
        }
        return false
    }

    fun updateTasksDuration(ID: String, Duration: String): Boolean {
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                // Loads Tasks from File, then this block Updates the Duration of selected Task ID
                var foundID = false
                for (i in parts.indices) {
                    if (allParts[i][0] == ID) {
                        allParts[i][4] = Duration
                        foundID = true
                    }
                }
                if (!foundID) {
                    println("Error: ID not found!")
                    br.close()
                    return false
                }

                // Clears current contents of Tasks.txt file
                val pw = PrintWriter("Tasks.txt")
                pw.close()

                for (i in parts.indices) {
                    createTask(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6]
                    ) // Creates an object of type Tasks for each, using Parameter data
                    save(task) // Saves newly created task from array of tasks (now using the task with the modified Duration) to emptied Tasks.txt file
                    task.clear() // Clears mutable list of tasks to avoid saving the entire list of tasks each loop through the array
                }
                br.close()
                return true
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        } catch (e: StringIndexOutOfBoundsException) {
            // println("Warning: String Index Out of Bounds Exception")
        }
        return false
    }

    fun listProjectsForTask(): Array<String> {
        try {
            val fr = FileReader("Projects.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Project(", "") // Formatting the read input from Projects.txt to parse data into Arrays
                fileLines = fileLines.replace("ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" AssignedTasksID=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" StartDate=", "")
                fileLines = fileLines.replace(" FinishDate=", "")
                fileLines = fileLines.replace(" TotalDuration=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Projects via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(8) { "" } } // Make 3D Array with dimensions: Projects vs. Project Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Project, input their respective Project Parameters into Array via split()
                }

                // Loads Projects from File, then this block returns an array of Project IDs
                var array = Array<String>(parts.size) { "" }
                for (i in parts.indices) {
                    array[i] = allParts[i][0]
                }
                br.close()
                return array
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return Array<String>(0) { "" } // Returns empty array if file empty
    }

    fun listTeamsForTask(teamsInProject: Array<String>): Array<String> {
        try {
            val fr = FileReader("Teams.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Teams(", "") // Formatting the read input from Teams.txt to parse data into Arrays
                fileLines = fileLines.replace("TeamID=", "")
                fileLines = fileLines.replace(" TeamLeader=", "")
                fileLines = fileLines.replace(" TeamMembers=", "")
                fileLines = fileLines.replace(" TeamLoc=", "")
                fileLines = fileLines.replace(" TeamCost=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Teams via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(5) { "" } } // Make 3D Array with dimensions: Teams vs. Teams Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Team, input their respective Team Parameters into Array via split()
                }

                // Loads Teams from File, then this block checks for matching Team IDs in the selected Project
                // by the teamsAssignedToProject() function, before returning them
                var matchingIDs = ""
                for (i in parts.indices) {
                    for (j in teamsInProject.indices) {
                        if (teamsInProject[j] == allParts[i][0]) {
                            matchingIDs += allParts[i][0] + ","
                        }
                    }
                } // Loops through all created teams, loops through teamsInProject, if a match is found, add it to a string (to be returned as an array later)
                val array = matchingIDs.substring(0, matchingIDs.length - 1).split(",").toTypedArray() // Removes comma
                br.close()
                return array
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return Array<String>(0) { "" } // Returns empty array if file empty
    }

    fun teamsAssignedToProject(projectID: String): Array<String> {
        try {
            val fr = FileReader("Projects.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Project(", "") // Formatting the read input from Projects.txt to parse data into Arrays
                fileLines = fileLines.replace("ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" AssignedTasksID=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" StartDate=", "")
                fileLines = fileLines.replace(" FinishDate=", "")
                fileLines = fileLines.replace(" TotalDuration=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Projects via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(8) { "" } } // Make 3D Array with dimensions: Projects vs. Project Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Project, input their respective Project Parameters into Array via split()
                }

                // Loads Projects from File, then this block returns an array of Assigned Teams IDs according to the matching Project ID
                for (i in parts.indices) {
                    for (j in allParts[i].indices) {
                        if (allParts[i][0] == projectID) {
                            val array = allParts[i][4].split(" & ").toTypedArray()
                            br.close()
                            return array
                        }
                    }
                }
                br.close()
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return Array<String>(0) { "" } // Returns empty array if file empty
    }

    fun listTasksForTasks(projectID: String): Array<String> {
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)
                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                // Loads Tasks from File, then this block returns an array of Task IDs according to the matching Project ID
                var matchingIDs = ""
                for (i in parts.indices) {
                    if (allParts[i][1] == projectID) {
                        matchingIDs += allParts[i][0] + ","
                    }
                } // Loops through all created tasks, if a match is found, add it to a string (to be returned as an array later)
                if (!matchingIDs.isEmpty()) {
                    val array = matchingIDs.substring(0, matchingIDs.length - 1).split(",").toTypedArray() // Removes comma
                    br.close()
                    return array
                } else {
                    br.close()
                    return Array<String>(0) { "" } // Returns empty array if none found
                } // Checks if no matching Task ID was found from the given Project ID, so that split() is not used on empty string
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return Array<String>(0) { "" } // Returns empty array if file empty
    }

    fun TasksDurationForID(ID: String): Int {
        var Duration = 0
        try {
            val fr = FileReader("Tasks.txt")
            val br = BufferedReader(fr)
            var fileLines: String
            while (br.ready()) {
                fileLines = br.readLine()
                fileLines = fileLines.replace("Tasks(", "") // Formatting the read input from Tasks.txt to parse data into Arrays
                fileLines = fileLines.replace("TaskID=", "")
                fileLines = fileLines.replace(" ProjectID=", "")
                fileLines = fileLines.replace(" Commissioner=", "")
                fileLines = fileLines.replace(" ProjectMng=", "")
                fileLines = fileLines.replace(" Duration=", "")
                fileLines = fileLines.replace(" AssignedTeamsID=", "")
                fileLines = fileLines.replace(" Progress=", "")
                fileLines = fileLines.replace(")", "")
                val parts: Array<String> = fileLines.substring(1, fileLines.length - 1).split("\\]\\[".toRegex()).toTypedArray() // Creates Array of Tasks via split()
                val allParts = Array<Array<String>>(parts.size) { Array<String>(7) { "" } } // Make 3D Array with dimensions: Tasks vs. Tasks Parameters (ID, etc)

                for (i in parts.indices) {
                    allParts[i] = parts[i].split(",".toRegex()).toTypedArray() // For each Task, input their respective Task Parameters into Array via split()
                }

                // Loads Tasks from File, then this block returns an array of Task IDs according to the matching Project ID


                for(k in ID.indices){
                    println("ID["+k+"]= "+ID[k])
                }


                for(j in ID.indices) {
                    for (i in parts.indices) {
                        //println("i= " + i)

                        if (allParts[i][0] == ID) {
                            println("Duration of "+j+" task " + allParts[j][4])
                            Duration = allParts[i][4].toInt()
                        }
                    } // Loops through all created tasks, if a match is found, add it to a string (to be returned as an array later)
                    // Checks if no matching Task ID was found from the given Project ID, so that split() is not used on empty string

                }




            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return Duration
    }
}


