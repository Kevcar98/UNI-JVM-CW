import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException


abstract class CreateFile {
    val fileNameP = "Projects.txt"
    val fileNameTm = "Teams.txt"
    val fileNameTk = "Tasks.txt"

    companion object {
        fun createFileProject() = FileProject()
        fun createFileTask() = FileTask()
        fun createFileTeam() = FileTeam()
    }

    abstract infix fun save(data: String)
}

class FileProject() : CreateFile() {
    override fun save(data: String) {
        val path = System.getProperty("user.dir") + "\\$fileNameP"
        try {
            val fw = FileWriter(path, true)
            fw.write(data)
            fw.close()
            println("Saved $data to an existing file")
        } catch (e: IOException) {
            File(fileNameP).writeText(data)
            println("Saved $data to a new file")
        } catch (e: FileNotFoundException) {
            println("Error: File not Found")
        }
    }
}

class FileTask() : CreateFile() {
    override fun save(data: String) {
        val path = System.getProperty("user.dir") + "\\$fileNameTk"
        try {
            val fw = FileWriter(path, true)
            fw.write(data)
            fw.close()
            println("Saved $data to an existing file")
        } catch (e: IOException) {
            File(fileNameTk).writeText(data)
            println("Saved $data to a new file")
        } catch (e: FileNotFoundException) {
            println("Error: File not Found")
        }
    }
}

class FileTeam() : CreateFile() {
    override fun save(data: String) {
        val path = System.getProperty("user.dir") + "\\$fileNameTm"
        try {
            val fw = FileWriter(path, true)
            fw.write(data)
            fw.close()
            println("Saved $data to an existing file")
        } catch (e: IOException) {
            File(fileNameTm).writeText(data)
            println("Saved $data to a new file")
        } catch (e: FileNotFoundException) {
            println("Error: File not Found")
        }
    }
}
