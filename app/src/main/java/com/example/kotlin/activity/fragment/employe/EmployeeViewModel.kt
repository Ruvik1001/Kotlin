package com.example.kotlin.activity.fragment.employe

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.R
import com.example.kotlin.data.EmployeeData
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.debug.DBG
import com.example.kotlin.special.global.GlobalArgs
import org.koin.java.KoinJavaComponent.inject


class EmployeeViewModel(
    private val context: Context,
    private val owner: LifecycleOwner) : ViewModel() {
    private val localDB: DBSupport by inject(DBSupport::class.java)
    private val global: GlobalArgs by inject(GlobalArgs::class.java)
    private var filed: List<Pair<String, String>>
    private var tableName: String
    private val firebaseLiveData: FirebaseLiveData by inject(FirebaseLiveData::class.java)
    private val employeeList = MutableLiveData<List<EmployeeData>>()
    val employeeData: LiveData<List<EmployeeData>> = employeeList


    init {
        tableName = global.UserTableName
        filed = global.UserFiled
        localDB.selectTable(tableName, filed)

        val currentLogin = localDB.getAllDataFromCurrentTable()[0][1]

        firebaseLiveData.usersData.observe(owner) {
            if (it != null) {
                DBG().createLogI("EVM user bind was called")
                localDB.deleteRecordsByColumnValue("id", "1", ">")
                val lst_emp: MutableList<EmployeeData> = mutableListOf()
                for (user in it) {
                    if (user.getLogin() == currentLogin)
                        continue
                    val employee: EmployeeData = EmployeeData("","","","")
                    localDB.addDataToCurrentTable(listOf<Pair<String,String>>(
                        Pair(filed[0].first, user.getLogin()),
                        Pair(filed[1].first, user.getPassword()),
                        Pair(filed[2].first, user.getName()),
                        Pair(filed[3].first, user.getLastName()),
                        Pair(filed[4].first, user.getPatronymic()),
                        Pair(filed[5].first, user.getPost()),
                        Pair(filed[6].first, user.getTelephone()),
                    ))
                    employee.name = "${user.getLastName()} ${user.getName()} ${user.getPatronymic()}"
                    employee.email = "${user.getLogin()}"
                    employee.position = "${user.getPost()}"
                    employee.phone = "${user.getTelephone()}"
                    lst_emp.add(employee)
                }
                employeeList.postValue(lst_emp)
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