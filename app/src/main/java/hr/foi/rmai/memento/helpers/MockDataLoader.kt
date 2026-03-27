package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getDemoData(): MutableList<Task> {
        val courses = getDemoCourses()

        return mutableListOf(
            Task("Submit seminar paper", Date(), courses[0], false),
            Task("Prepare for exercises", Date(), courses[1], false),
            Task("Rally a project team", Date(), courses[2], false),
            Task("Connect to server (SSH)", Date(), courses[3], false)
        )
    }

    fun getDemoCourses(): List<TaskCourse> {
        return listOf(
            TaskCourse("EP", "#000080"),
            TaskCourse("SIS", "#FF0000"),
            TaskCourse("RMAI", "#000080"),
            TaskCourse("RWA", "#CCCCCC")
        )
    }
}