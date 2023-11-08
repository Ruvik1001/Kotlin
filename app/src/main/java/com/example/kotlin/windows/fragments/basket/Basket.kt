package com.example.kotlin.windows.fragments.basket

import com.example.kotlin.windows.database.DBHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import com.example.kotlin.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Basket.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basket : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_basket, container, false)
        return view
    }

    @SuppressLint("Range", "UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DBHelper(requireContext(), null)
        val cursor = db.getRecordsWithCondition("count", ">", "0")

        if (cursor != null && cursor.moveToFirst()) {
            val ll = view.findViewById<LinearLayout>(R.id.ll_basket)
            val rezult = view.findViewById<TextView>(R.id.basket_rez_sum)
            view.findViewById<Button>(R.id.btn_basket_pay).setOnClickListener {
                pay()
                ll.removeAllViews()
            }
            do {
                val id = db.getIDColValue(cursor)
                val position = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION_COl))
                //val res_id = cursor.getInt(cursor.getColumnIndex(com.example.kotlin.windows.database.DBHelper.RES_ID_COL))
                val cost = cursor.getDouble(cursor.getColumnIndex(DBHelper.COST_COL))
                var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COUNT_COL))
                //val type = cursor.getString(cursor.getColumnIndex(com.example.kotlin.windows.database.DBHelper.TYPE_COL))

                ////////////////////////////////////////
                val table = TableLayout(this.context)
//                table.background = resources.getDrawable(R.drawable.menu_frame)
                table.setPadding(20,0,20,40)
                table.minimumHeight = 200
                table.gravity = Gravity.CENTER_VERTICAL


                val row_name = TableRow(this.context)

                val textView_name = TextView(this.context)
                textView_name.setPadding(20,0,20,10)
                textView_name.maxWidth = resources.getDimension(R.dimen.text_w).toInt()
                textView_name.textSize = 18f
                textView_name.setTextColor(resources.getColor(R.color.black))
                textView_name.text = position

                row_name.addView(textView_name)

                table.addView(row_name)

                val inner_table = TableLayout(this.context)

                val row_cost = TableRow(this.context)

                val textView_cost = TextView(this.context)
                textView_cost.setPadding(20,10,20,10)
                textView_cost.maxWidth = resources.getDimension(R.dimen.text_w).toInt() / 5 - 15
                textView_cost.minWidth = resources.getDimension(R.dimen.text_w).toInt() / 7 - 15
                textView_cost.textSize = 18f
                textView_cost.setTextColor(resources.getColor(R.color.black))
                textView_cost.text = cost.toString()

                row_cost.addView(textView_cost)

                val spec1 = TextView(this.context)
                spec1.setPadding(20,10,20,10)
                spec1.textSize = 18f
                spec1.setTextColor(resources.getColor(R.color.black))
                spec1.text = " X "

                row_cost.addView(spec1)

                val btn_minus = Button(this.context)
                btn_minus.setBackgroundColor(resources.getColor(R.color.btn_push_background_color))
                btn_minus.setText("-1")
                btn_minus.setTextColor(resources.getColor(R.color.text_background_color))
                btn_minus.textSize = 18f
                btn_minus.gravity = Gravity.CENTER

                row_cost.addView(btn_minus)

                btn_minus.layoutParams.width = 100
                btn_minus.layoutParams.height = 100



                val cur_count = TextView(this.context)
                cur_count.setPadding(20,10,20,10)
                cur_count.textSize = 18f
                cur_count.setTextColor(resources.getColor(R.color.black))
                cur_count.text = count.toString()

                row_cost.addView(cur_count)

                val btn_plus = Button(this.context)
                btn_plus.maxWidth = 1
                btn_plus.setBackgroundColor(resources.getColor(R.color.btn_push_background_color))
                btn_plus.setText("+1")
                btn_plus.setTextColor(resources.getColor(R.color.text_background_color))
                btn_plus.textSize = 18f
                btn_minus.gravity = Gravity.CENTER

                row_cost.addView(btn_plus)

                btn_plus.layoutParams.width = 100
                btn_plus.layoutParams.height = 100

                val spec2 = TextView(this.context)
                spec2.setPadding(20,10,20,10)
                spec2.textSize = 18f
                spec2.setTextColor(resources.getColor(R.color.black))
                spec2.text = " = "

                row_cost.addView(spec2)

                val cur_rez = TextView(this.context)
                cur_rez.setPadding(20,10,20,10)
                cur_rez.textSize = 18f
                cur_rez.setTextColor(resources.getColor(R.color.black))
                cur_rez.text = ((cost * count).toString())
                cur_rez.maxWidth = resources.getDimension(R.dimen.text_w).toInt() / 6 - 15
                cur_rez.minWidth = resources.getDimension(R.dimen.text_w).toInt() / 4 - 15

                row_cost.addView(cur_rez)

                val spec3 = TextView(this.context)
                spec3.setPadding(20,10,20,10)
                spec3.textSize = 18f
                spec3.setTextColor(resources.getColor(R.color.black))
                spec3.text = "₽"

                row_cost.addView(spec3)

                inner_table.addView(row_cost)

                table.addView(inner_table)

                ll.addView(table)


                btn_minus.setOnClickListener {
                    count -= 1
                    if (count == 0)
                        ll.removeView(table)
                    cur_count.setText(count.toString())
                    cur_rez.setText(((cur_rez.text as String).toDouble() - cost.toDouble()).toString())
                    rezult.setText(((rezult.text as String).toDouble() - cost.toDouble()).toString())
                }

                btn_plus.setOnClickListener {
                    if (count == 99)
                        return@setOnClickListener
                    count += 1
                    cur_count.setText(count.toString())
                    cur_rez.setText(((cur_rez.text as String).toDouble() + cost.toDouble()).toString())
                    rezult.setText(((rezult.text as String).toDouble() + cost.toDouble()).toString())
                }

                rezult.setText(((rezult.text as String).toDouble() + cost.toDouble() * count.toDouble()).toString())

            } while (cursor.moveToNext())

            cursor.close()
        }

        db.close()
    }

    //It's only for test - not real
    private fun pay() {
        Log.i("APP_TAG", "Basket::pay() was called")
        val db = DBHelper(requireContext(), null)
        db.updateFieldForAll("count", "0")
        db.close()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Basket.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Basket().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}