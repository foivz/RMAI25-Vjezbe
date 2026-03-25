package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getDemoData(): List<Task> = listOf(
        Task("Submit seminar paper", Date(), TaskCourse("EP", "#000080"), false),
        Task("Prepare for exercises", Date(), TaskCourse("SIS", "#FF0000"), false),
        Task("Rally a project team", Date(), TaskCourse("RMAI", "#000080"), false),
        Task("Connect to server (SSH)", Date(), TaskCourse("RWA", "#CCCCCC"), false)
    )
}