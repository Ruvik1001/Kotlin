package com.example.kotlin.activity.fragment.employe

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.kotlin.special.global.GlobalArgs
import com.example.kotlin.R
import com.example.kotlin.data.EmployeeData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.debug.DBG

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Employee.newInstance] factory method to
 * create an instance of this fragment.
 */
class Employee : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var localDB: DBSupport
    private lateinit var view: View
    private lateinit var emploeeViewModel: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_employe, container, false)
        emploeeViewModel = ViewModelProvider(this, EmployeeViewModelFactory(requireContext(), viewLifecycleOwner)).get(EmployeeViewModel::class.java)

        emploeeViewModel.employeeData.observe(viewLifecycleOwner) {
            update(it)
        }

        return view
    }

    fun update(data: List<EmployeeData>) {
        DBG().createLogI("Emp UPD")
        val emps = view.findViewById<LinearLayout>(R.id.liner_empl)
        emps.removeAllViews()

        for (user in data) {

            val table = TableLayout(this.context)
            table.background = resources.getDrawable(R.drawable.back)
            table.setPadding(20,0,20,40)

            val row_name = TableRow(this.context)

            val pre_FCs = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(pre_FCs)
            pre_FCs.text = "ФИО:"
            row_name.addView(pre_FCs)

            val FCs = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(FCs)
            FCs.text = user.name
            row_name.addView(FCs)

            table.addView(row_name)

            val row_position = TableRow(this.context)

            val pre_position = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(pre_position)
            pre_position.text = "Позиция:"
            row_position.addView(pre_position)

            val position = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(position)
            position.text = user.position
            row_position.addView(position)

            table.addView(row_position)

            val row_email = TableRow(this.context)

            val pre_email = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(pre_email)
            pre_email.text = "Email:"
            row_email.addView(pre_email)

            val email = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(email)
            var ref = String()
            ref = user.email
            email.text = ref
            email.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            email.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(ref));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                intent.type = "message/rfc822"
                startActivity(Intent.createChooser(intent, "Choose an email client"))
            }
            email.isClickable = true

            row_email.addView(email)

            table.addView(row_email)

            val row_phone = TableRow(this.context)

            val pre_phone = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(pre_phone)
            pre_phone.text = "Телефон:"
            row_phone.addView(pre_phone)

            val phone = TextView(this.context)
            emploeeViewModel.setDefaultTextViewAttributes(phone)
            phone.text = user.phone
            phone.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            phone.setOnClickListener { startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.text))) }
            row_phone.addView(phone)

            table.addView(row_phone)

            emps.addView(table)
        }

        val save_zone = TextView(this.context)
        save_zone.setText("\n\n\n")
        emps.addView(save_zone)
    }

    override fun onDestroy() {
        super.onDestroy()
        val localDB = DBSupport(requireContext())
        localDB.deleteRecordsByColumnValue("id", "1", ">")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmployeeData.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Employee().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}