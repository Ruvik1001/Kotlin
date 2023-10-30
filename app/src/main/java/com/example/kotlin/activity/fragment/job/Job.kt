package com.example.kotlin.activity.fragment.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.kotlin.special.debug.DBG
import com.example.kotlin.R
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.global.GlobalArgs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Job.newInstance] factory method to
 * create an instance of this fragment.
 */
class Job : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var localDB: DBSupport
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val tableName = GlobalArgs().TaskTableName
        val filed = GlobalArgs().TaskFiled

        localDB = DBSupport(requireContext())
        localDB.selectTable(tableName, filed)

        val localDBUser = DBSupport(requireContext())
        localDBUser.selectTable(GlobalArgs().UserTableName, GlobalArgs().UserFiled)

        localDB.clearSelectedTable()

        DBFirebase().findTasks(localDBUser.getAllDataFromCurrentTable()[0][1]) {
            if (!it.get().isEmpty()) {
                for (task in it.get()) {
                    localDB.addDataToCurrentTable(listOf<Pair<String,String>>(
                        Pair(filed[0].first, task.getLogin()),
                        Pair(filed[1].first, task.getLabel()),
                        Pair(filed[2].first, task.getText()),
                        Pair(filed[3].first, task.getDateFrom()),
                        Pair(filed[4].first, task.getDateTo()),
                        Pair(filed[5].first, task.getStatus()),
                    ))
                }
                update()
            }
        }
        update()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localDB.clearSelectedTable()
    }

    fun update() {
        val tasks = view.findViewById<LinearLayout>(R.id.liner_tasks)
        tasks.removeAllViews()

        for (i in localDB.getAllDataFromCurrentTable()) {
            val dbg = DBG()
            for (elem in i) {
                dbg.createLogD(elem)
            }
            dbg.createLogD("==============================")
            val table = TableLayout(this.context)
            table.background = resources.getDrawable(R.drawable.back)
            table.setPadding(20,0,20,40)

            val row_task_for = TableRow(this.context)

            val pre_for = TextView(this.context)
            pre_for.setTextColor(resources.getColor(R.color.black))
            pre_for.textSize = 18f
            pre_for.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_for.text = "Для:"
            pre_for.setPadding(20,10,20,10)
            row_task_for.addView(pre_for)

            val for_user = TextView(this.context)
            for_user.setTextColor(resources.getColor(R.color.black))
            for_user.textSize = 18f
            for_user.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            for_user.text = i[1]
            for_user.setPadding(20,10,20,10)
            row_task_for.addView(for_user)

            table.addView(row_task_for)

            val row_task_lbl = TableRow(this.context)

            val pre_lbl = TextView(this.context)
            pre_lbl.setTextColor(resources.getColor(R.color.black))
            pre_lbl.textSize = 18f
            pre_lbl.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_lbl.text = "Задача:"
            pre_lbl.setPadding(20,10,20,10)
            row_task_lbl.addView(pre_lbl)

            val lbl = TextView(this.context)
            lbl.setTextColor(resources.getColor(R.color.black))
            lbl.textSize = 18f
            lbl.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            lbl.text = i[2]
            lbl.setPadding(20,10,20,10)
            row_task_lbl.addView(lbl)

            table.addView(row_task_lbl)

            val row_task_text = TableRow(this.context)

            val pre_text = TextView(this.context)
            pre_text.setTextColor(resources.getColor(R.color.black))
            pre_text.textSize = 18f
            pre_text.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_text.text = "Описание:"
            pre_text.setPadding(20,10,20,10)
            row_task_text.addView(pre_text)

            val text_task = TextView(this.context)
            text_task.setTextColor(resources.getColor(R.color.black))
            text_task.textSize = 18f
            text_task.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            text_task.text = i[3]
            text_task.setPadding(20,10,20,10)
            row_task_text.addView(text_task)

            table.addView(row_task_text)

            val row_date = TableRow(this.context)

            val pre_date = TextView(this.context)
            pre_date.setTextColor(resources.getColor(R.color.black))
            pre_date.textSize = 18f
            pre_date.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_date.text = "Дата:"
            pre_date.setPadding(20,10,20,10)
            row_date.addView(pre_date)

            val date = TextView(this.context)
            date.setTextColor(resources.getColor(R.color.black))
            date.textSize = 18f
            date.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            date.text = i[4] + "-" + i[5]
            date.setPadding(20,10,20,10)
            row_date.addView(date)

            table.addView(row_date)

            val row_status = TableRow(this.context)

            val pre_status = TextView(this.context)
            pre_status.setTextColor(resources.getColor(R.color.black))
            pre_status.textSize = 18f
            pre_status.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_status.text = "Статус:"
            pre_status.setPadding(20,10,20,10)
            row_status.addView(pre_status)

            val status = TextView(this.context)
            status.setTextColor(resources.getColor(R.color.black))
            status.textSize = 18f
            status.maxWidth = resources.getDimension(R.dimen.text_max_w).toInt()
            status.text = i[6]
            status.setPadding(20,10,20,10)
            row_status.addView(status)

            table.addView(row_status)

            tasks.addView(table)
        }

        val save_zone = TextView(this.context)
        save_zone.setText("\n\n\n")
        tasks.addView(save_zone)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Job.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Job().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}