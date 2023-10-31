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
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin.special.debug.DBG
import com.example.kotlin.R
import com.example.kotlin.data.TaskData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.global.GlobalArgs
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

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

    private lateinit var view: View
    private lateinit var jobViewModel: JobViewModel
    private val jobViewModelFactory: JobViewModelFactory by inject { parametersOf(requireActivity()) }

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
        jobViewModel = ViewModelProvider(this, jobViewModelFactory).get(JobViewModel::class.java)

        jobViewModel.taskData.observe(viewLifecycleOwner) {
            update(it)
        }
        return view
    }

    fun update(data: List<TaskData>) {
        DBG().createLogI("Job UPD")
        val tasks = view.findViewById<LinearLayout>(R.id.liner_tasks)
        tasks.removeAllViews()

        for (elem in data) {
            val table = TableLayout(this.context)
            table.background = resources.getDrawable(R.drawable.back)
            table.setPadding(20,0,20,40)

            val row_task_for = TableRow(this.context)

            val pre_for = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(pre_for)
            pre_for.text = "Для:"
            row_task_for.addView(pre_for)

            val for_user = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(for_user)
            for_user.text = elem.login
            row_task_for.addView(for_user)

            table.addView(row_task_for)

            val row_task_lbl = TableRow(this.context)

            val pre_lbl = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(pre_lbl)
            pre_lbl.text = "Задача:"
            row_task_lbl.addView(pre_lbl)

            val lbl = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(lbl)
            lbl.text = elem.label
            row_task_lbl.addView(lbl)

            table.addView(row_task_lbl)

            val row_task_text = TableRow(this.context)

            val pre_text = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(pre_text)
            pre_text.text = "Описание:"
            row_task_text.addView(pre_text)

            val text_task = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(text_task)
            text_task.text = elem.description
            row_task_text.addView(text_task)

            table.addView(row_task_text)

            val row_date = TableRow(this.context)

            val pre_date = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(pre_date)
            pre_date.text = "Дата:"
            row_date.addView(pre_date)

            val date = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(date)
            date.text = elem.date
            row_date.addView(date)

            table.addView(row_date)

            val row_status = TableRow(this.context)

            val pre_status = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(pre_status)
            pre_status.text = "Статус:"
            row_status.addView(pre_status)

            val status = TextView(this.context)
            jobViewModel.setDefaultTextViewAttributes(status)
            status.text = elem.status
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