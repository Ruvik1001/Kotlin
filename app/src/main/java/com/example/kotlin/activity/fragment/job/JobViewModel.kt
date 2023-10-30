package com.example.kotlin.activity.fragment.job

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.R
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.data.TaskData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.debug.DBG
import com.example.kotlin.special.global.GlobalArgs

class JobViewModel(
    private val context: Context,
    private val owner: LifecycleOwner) : ViewModel() {
    private val localDB: DBSupport = DBSupport(context)
    private val global: GlobalArgs = GlobalArgs()
    private lateinit var filed: List<Pair<String, String>>
    private lateinit var tableName: String
    private lateinit var firebaseLiveData: FirebaseLiveData
    private val tasksList = MutableLiveData<List<TaskData>>()
    val taskData: LiveData<List<TaskData>> = tasksList

    init {
        firebaseLiveData = FirebaseLiveData(DBFirebase())

        tableName = global.TaskTableName
        filed = global.TaskFiled
        localDB.selectTable(tableName, filed)

        firebaseLiveData.tasksData.observe(owner) {
            if (it != null) {
                DBG().createLogI("JMV task bind was called")
                localDB.clearSelectedTable()
                val lst_task: MutableList<TaskData> = mutableListOf()
                for (data in it.get()) {
                    val task: TaskData = TaskData("","","","", "")
                    localDB.addDataToCurrentTable(listOf<Pair<String,String>>(
                        Pair(filed[0].first, data.getLogin()),
                        Pair(filed[1].first, data.getLabel()),
                        Pair(filed[2].first, data.getText()),
                        Pair(filed[3].first, data.getDateFrom()),
                        Pair(filed[4].first, data.getDateTo()),
                        Pair(filed[5].first, data.getStatus()),
                    ))
                    task.login = data.getLogin()
                    task.label = data.getLabel()
                    task.description = data.getText()
                    task.date = "${data.getDateFrom()} - ${data.getDateTo()}"
                    task.status = data.getStatus()
                    lst_task.add(task)
                }
                tasksList.postValue(lst_task)
            }
        }
    }

    fun setDefaultTextViewAttributes(text: TextView) {
        text.setTextColor(context.resources.getColor(R.color.black))
        text.textSize = 18f
        text.maxWidth = context.resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
        text.setPadding(20,10,20,10)
    }
}