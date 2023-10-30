package com.example.kotlin.activity.fragment.employe

import android.content.Context
import android.provider.SyncStateContract.Helpers.update
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

class EmployeeViewModel(
    private val context: Context,
    private val owner: LifecycleOwner) : ViewModel() {
    private val localDB: DBSupport = DBSupport(context)
    private val global: GlobalArgs = GlobalArgs()
    private lateinit var filed: List<Pair<String, String>>
    private lateinit var tableName: String
    private lateinit var firebaseLiveData: FirebaseLiveData
    private val employeeList = MutableLiveData<List<EmployeeData>>()
    val employeeData: LiveData<List<EmployeeData>> = employeeList


    init {
        firebaseLiveData = FirebaseLiveData(DBFirebase())

        tableName = global.UserTableName
        filed = global.UserFiled
        localDB.selectTable(tableName, filed)

        firebaseLiveData.usersData.observe(owner) {
            if (it != null) {
                DBG().createLogI("EVM user bind was called")
                localDB.clearSelectedTable()
                val lst_emp: MutableList<EmployeeData> = mutableListOf()
                for (user in it) {
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