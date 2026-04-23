package hr.foi.rmai.memento.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.services.MementoService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.view.isVisible

class TasksAdapter(private val tasksList : MutableList<Task>, private val onTaskCompleted: ((taskId: Int) -> Unit)? = null) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskTimer: ImageView = view.findViewById(R.id.iv_task_timer)
        private var isTimerActive = false

        private val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)
        private val taskName: TextView
        private val taskDueDate: TextView
        private val taskCourseColor: SurfaceView
        init {
            taskName = view.findViewById(R.id.tv_task_name)
            taskDueDate = view.findViewById(R.id.tv_task_due_date)
            taskCourseColor = view.findViewById(R.id.sv_task_course_color)

            view.setOnClickListener {
                if (Date() < tasksList[bindingAdapterPosition].dueDate) {
                    val intent = Intent(view.context, MementoService::class.java).apply {
                        putExtra("task_id", tasksList[bindingAdapterPosition].id)
                    }
                    isTimerActive = !isTimerActive
                    if (isTimerActive) {
                        taskTimer.visibility = View.VISIBLE
                    } else {
                        intent.putExtra("cancel", true)
                        taskTimer.visibility = View.GONE
                    }
                    view.context.startService(intent)
                } else if (taskTimer.isVisible) {
                    taskTimer.visibility = View.GONE
                }
            }

            view.setOnLongClickListener {
                val currentTask = tasksList[bindingAdapterPosition]

                val alertDialogBuilder = AlertDialog.Builder(view.context)
                    .setTitle(taskName.text)
                    .setNeutralButton("Delete task") { _, _ ->
                        val deletedTask = tasksList[adapterPosition]
                        TasksDatabase.getInstance().getTasksDao().removeTask(deletedTask)
                        removeTaskFromList()
                    }
                    .setPositiveButton("Mark as completed") { _, _ ->
                        val completedTask = tasksList[adapterPosition]
                        completedTask.completed = true
                        TasksDatabase.getInstance().getTasksDao().insertTask(completedTask)
                        removeTaskFromList()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel();
                    }

                if (onTaskCompleted != null) {
                    alertDialogBuilder.setPositiveButton("Mark as completed") { _, _ ->
                        val completedTask = tasksList[adapterPosition]
                        completedTask.completed = true
                        TasksDatabase.getInstance().getTasksDao().insertTask(completedTask)
                        removeTaskFromList()
                        onTaskCompleted.invoke(completedTask.id)
                    }
                }

                alertDialogBuilder.show()

                return@setOnLongClickListener true
            }
        }

        private fun removeTaskFromList() {
            tasksList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }

        fun bind(task: Task) {
            taskName.text = task.name
            taskDueDate.text = sdf.format(task.dueDate)
            taskCourseColor.setBackgroundColor(task.course.color.toColorInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(taskView)
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    fun addTask(newTask: Task) {
        var newIndexInList = tasksList.indexOfFirst { task ->
            task.dueDate > newTask.dueDate
        }
        if (newIndexInList == -1) {
            newIndexInList = tasksList.size
        }
        tasksList.add(newIndexInList, newTask)
        notifyItemInserted(newIndexInList)
    }
}




