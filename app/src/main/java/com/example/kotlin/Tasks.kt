package com.example.kotlin

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Tasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class Tasks : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val tasks = view.findViewById<LinearLayout>(R.id.liner_tasks)
        for (i in 0..30) {

            val table = TableLayout(this.context)
            table.background = resources.getDrawable(R.drawable.back)
            table.setPadding(20,0,20,40)

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
            lbl.text = "Утвердить документ"
            lbl.setPadding(20,10,20,10)
            row_task_lbl.addView(lbl)

            table.addView(row_task_lbl)

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
            date.text = "до 01.01.2024"
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
            status.text = "В обработке"
            status.setPadding(20,10,20,10)
            row_status.addView(status)

            table.addView(row_status)

            tasks.addView(table)
        }

        val save_zone = TextView(this.context)
        save_zone.setText("\n\n\n")
        tasks.addView(save_zone)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tasks.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Tasks().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}