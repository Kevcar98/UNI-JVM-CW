import java.io.*
import java.time.*


data class Project(
        var ProjectID: Int = 0,
        var Commissioner: String = "",
        var ProjectMng: String = "",
        var AssignedTasksID: String = "",
        var AssignedTeamsID: String = "",
        var StartDate: String = "",
        var FinishDate: String = "",
        var TotalDuration: Int = 0) {
}

class ProjectHandler() {
    val project = mutableListOf<Project>()

    fun createProject(
            ProjectID: String,
            Commissioner: String,
            ProjectMng: String,
            AssignedTasksID: String,
            AssignedTeamsID: String,
            StartDate: String,
            FinishDate: String,
            TotalDuration: String
    ): List<Project> {
        project.add(Project(
                ProjectID = if (ProjectID.isEmpty()) 0 else ProjectID.toInt(),
                Commissioner = if (Commissioner.isEmpty()) "Unknown Commissioner" else Commissioner,
                ProjectMng = if (ProjectMng.isEmpty()) "Unknown Project Manager" else ProjectMng,
                AssignedTasksID = if (AssignedTasksID.isEmpty()) "None Currently Assigned" else AssignedTasksID,
                AssignedTeamsID = if (AssignedTeamsID.isEmpty()) "No Teams Currently Assigned" else AssignedTeamsID,
                StartDate = if (StartDate.isEmpty()) getStartDate() else StartDate,
                FinishDate = if (FinishDate.isEmpty()) getFinishDate(StartDate, TotalDuration.toLong()) else FinishDate,
                TotalDuration = if (TotalDuration.isEmpty()) 0 else TotalDuration.toInt()
        ))
        return project
    }

    val getStartDate = { -> (String) // Declare lambda expression return type (no parameters)
        val date: LocalDate = LocalDate.now()
        date.toString()
    }

    val getFinishDate = { StartDate: String, TotalDuration: Long -> (String) // Declare lambda expression parameters and return type
        val date: LocalDate = LocalDate.parse(StartDate)
        date.plusDays(TotalDuration).toString()
    }

    fun save(project: List<Project>?) {
        CreateFile.createFileProject() save project.toString()
    }

    fun loadProjects(): List<Project>? {
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

                for (i in parts.indices) {
                    createProject(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6],
                            allParts[i][7]
                    ) // Creates an object of type Project for each, using Parameter data
                }
            }
            br.close()
            return project // Returns the list "project" as the createProject function already added the Projects to it
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return null
    }

    fun uniqueIDCheck(ID: String): Boolean {
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

                // Loads Projects from File, then this block checks if inputted Project ID is existing
                var emptyInput = false
                if (ID == "") emptyInput = true
                for (i in parts.indices) {
                    for (j in allParts[i].indices) {
                        if (allParts[i][0] == ID || (emptyInput && allParts[i][0] == "0")) { // if ProjectIDF text field is empty, ProjectID is 0 so check if it exists
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

    fun listTeamsForProject(): Array<String> {
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

                // Loads Teams from File, then this block returns an array of Team IDs
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

    fun updateProjectTaskData(inputProject: String, assignedTask: String) {
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

                // Loads Projects from File, then this block Updates the AssignedTasksID of selected Project ID
                for (i in parts.indices) {
                    if (allParts[i][0] == inputProject) {
                        if (allParts[i][3] == "None Currently Assigned") {
                            allParts[i][3] = ""
                            // Clears contents of AssignedTasksID of selected Project ID to be reassigned below
                            allParts[i][3] += assignedTask
                        } else {
                            allParts[i][3] += " & $assignedTask"
                        }
                        // If array with the selected Project ID is found, another if statement checks if AssignedTasksID is "None Currently Assigned"
                        // If so, then it clears its contents and reassigns it for the selected Project ID
                        // If not, it adds the new task separated by a "&" symbol
                    }
                }

                // Clears current contents of Projects.txt file
                val pw = PrintWriter("Projects.txt")
                pw.close()

                for (i in parts.indices) {
                    createProject(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6],
                            allParts[i][7]
                    ) // Creates an object of type Project for each, using Parameter data
                    save(project) // Saves newly created project from array of projects (now using the task with the modified AssignedTaskID) to emptied Projects.txt file
                    project.clear() // Clears mutable list of projects to avoid saving the entire list of projects each loop through the array
                }
                br.close()
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            // println("Error: IO Exception")
        } catch (e: StringIndexOutOfBoundsException) {
            // println("Warning: String Index Out of Bounds Exception")
        } catch (e: ArrayIndexOutOfBoundsException) {
            // println("Warning: Array Index Out of Bounds Exception")
        }
    }

    fun retrieveAssignedTasksID(inputProject: String): String {
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

                // Loads Projects from File, then this block returns the AssignedTasksID of selected Project ID
                var assignedTaskIDs = ""
                for (i in parts.indices) {
                    if (allParts[i][0] == inputProject) {
                        assignedTaskIDs = allParts[i][3]
                        // If array with the selected Project ID is found, get the AssignedTasksID
                    }
                }
                br.close()
                return assignedTaskIDs
            }
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        }
        return "" // Returns empty string if project empty
    }

    fun updateProject(teamIndex: Int, dataToEdit: Int, newData: String) {
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

                // Loads Projects from File, then this block Updates the chosen data of the selected Project ID
                allParts[teamIndex][dataToEdit] = newData

                // Clears current contents of Projects.txt file
                val pw = PrintWriter("Projects.txt")
                pw.close()

                for (i in parts.indices) {
                    createProject(
                            allParts[i][0],
                            allParts[i][1],
                            allParts[i][2],
                            allParts[i][3],
                            allParts[i][4],
                            allParts[i][5],
                            allParts[i][6],
                            allParts[i][7]
                    ) // Creates an object of type Projects for each, using Parameter data
                    save(project) // Saves newly created project from array of projects (now using the project with the modified data) to emptied Projects.txt file
                    project.clear() // Clears mutable list of projects to avoid saving the entire list of projects each loop through the array
                }
            }
            br.close()
        } catch (e: FileNotFoundException) {
            println("Error: File Not Found")
        } catch (e: IOException) {
            println("Error: IO Exception")
        } catch (e: StringIndexOutOfBoundsException) {
            // println("Warning: String Index Out of Bounds Exception")
        }
    }
}
