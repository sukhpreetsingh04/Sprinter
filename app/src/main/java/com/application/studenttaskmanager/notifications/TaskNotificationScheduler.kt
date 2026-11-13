package com.application.studenttaskmanager.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.application.studenttaskmanager.data.TaskItem

class TaskNotificationScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(task: TaskItem) {
        val dueAtMillis = task.dueAtMillis ?: return
        if (dueAtMillis <= System.currentTimeMillis()) return

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            reminderIntent(task),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (canScheduleExact()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dueAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dueAtMillis,
                pendingIntent
            )
        }
    }

    fun cancel(taskId: Long) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            Intent(context, TaskReminderReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun canScheduleExact(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                alarmManager.canScheduleExactAlarms()
    }

    private fun reminderIntent(task: TaskItem): Intent {
        return Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra(TaskReminderReceiver.EXTRA_TASK_ID, task.id)
            putExtra(TaskReminderReceiver.EXTRA_TITLE, task.title)
            putExtra(TaskReminderReceiver.EXTRA_CATEGORY, task.category)
        }
    }
}