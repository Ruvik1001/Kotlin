package com.example.kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginBottom

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Emploee.newInstance] factory method to
 * create an instance of this fragment.
 */
class Emploee : Fragment() {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_emploee, container, false)
        val emps = view.findViewById<LinearLayout>(R.id.liner_empl)
        for (i in 0..30) {

            val table = TableLayout(this.context)
            table.background = resources.getDrawable(R.drawable.back)
            table.setPadding(20,0,20,40)

            val row_name = TableRow(this.context)

            val pre_FCs = TextView(this.context)
            pre_FCs.setTextColor(resources.getColor(R.color.black))
            pre_FCs.textSize = 18f
            pre_FCs.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_FCs.text = "ФИО:"
            pre_FCs.setPadding(20,10,20,10)
            row_name.addView(pre_FCs)

            val FCs = TextView(this.context)
            FCs.setTextColor(resources.getColor(R.color.black))
            FCs.textSize = 18f
            FCs.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            FCs.text = "Иванов Иван Иванович"
            FCs.setPadding(20,10,20,10)
            row_name.addView(FCs)

            table.addView(row_name)

            val row_position = TableRow(this.context)

            val pre_position = TextView(this.context)
            pre_position.setTextColor(resources.getColor(R.color.black))
            pre_position.textSize = 18f
            pre_position.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_position.text = "Позиция:"
            pre_position.setPadding(20,10,20,10)
            row_position.addView(pre_position)

            val position = TextView(this.context)
            position.setTextColor(resources.getColor(R.color.black))
            position.textSize = 18f
            position.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            position.text = "Участник тестирования DA"
            position.setPadding(20,10,20,10)
            row_position.addView(position)

            table.addView(row_position)

            val row_email = TableRow(this.context)

            val pre_email = TextView(this.context)
            pre_email.setTextColor(resources.getColor(R.color.black))
            pre_email.textSize = 18f
            pre_email.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_email.text = "Email:"
            pre_email.setPadding(20,10,20,10)
            row_email.addView(pre_email)

            val email = TextView(this.context)
            email.setTextColor(resources.getColor(R.color.white_blue))
            email.textSize = 18f
            email.maxWidth = resources.getDimension(R.dimen.text_max_w).toInt()
            var ref = String()
            ref = "rudnevvictor2003@mail.ru"
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
            email.setPadding(20,10,20,10)
            email.isClickable = true

            row_email.addView(email)

            table.addView(row_email)

            val row_phone = TableRow(this.context)

            val pre_phone = TextView(this.context)
            pre_phone.setTextColor(resources.getColor(R.color.black))
            pre_phone.textSize = 18f
            pre_phone.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            pre_phone.text = "Телефон:"
            pre_phone.setPadding(20,10,20,10)
            row_phone.addView(pre_phone)

            val phone = TextView(this.context)
            phone.setTextColor(resources.getColor(R.color.white_blue))
            phone.textSize = 18f
            phone.maxWidth = resources.getDimension(R.dimen.main_window_table_text_max_w).toInt()
            phone.text = "+1(111)111-11-11"
            phone.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            phone.setOnClickListener { startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.text))) }
            phone.setPadding(20,10,20,10)
            row_phone.addView(phone)

            table.addView(row_phone)


            emps.addView(table)
        }

        val save_zone = TextView(this.context)
        save_zone.setText("\n\n\n")
        emps.addView(save_zone)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Emploee.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Emploee().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}