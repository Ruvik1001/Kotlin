package com.example.kotlin.windows.fragments.bar

import com.example.kotlin.windows.database.DBHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.kotlin.R
import com.mikhaellopez.circularimageview.CircularImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bar.newInstance] factory method to
 * create an instance of this fragment.
 */
class Bar : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_bar, container, false)
        view.findViewById<CircularImageView>(R.id.btn_basket).setOnClickListener {
            findNavController().navigate(R.id.action_bar_to_basket)
        }
        return view
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DBHelper(requireContext(), null)
        val cursor = db.getPositionsByType("bar")

        if (cursor != null && cursor.moveToFirst()) {
            val ll = view.findViewById<LinearLayout>(R.id.ll_bar)
            do {
                val id = db.getIDColValue(cursor)
                val position = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION_COl))
                val res_id = cursor.getInt(cursor.getColumnIndex(DBHelper.RES_ID_COL))
                val cost = cursor.getDouble(cursor.getColumnIndex(DBHelper.COST_COL))
                var count = cursor.getInt(cursor.getColumnIndex(DBHelper.COUNT_COL))
                val type = cursor.getString(cursor.getColumnIndex(DBHelper.TYPE_COL))

                ////////////////////////////////////////
                val table = TableLayout(this.context)
                table.background = resources.getDrawable(R.drawable.menu_frame)
                table.setPadding(20,0,20,40)
                table.minimumHeight = 400
                table.gravity = Gravity.CENTER_VERTICAL

                val row_element = TableRow(this.context)

                val images = ImageView(requireContext())
                images.setImageResource(res_id)
                images.visibility = View.VISIBLE

                row_element.addView(images)

                images.setPadding(10,20,10,20)
                images.layoutParams.width = 330
                images.layoutParams.height = 330

                ////////////////////////////////////////
                val table_inner = TableLayout(this.context)
                val table_cost_inner = TableLayout(this.context)
                val row_name = TableRow(this.context)
                val row_cost = TableRow(this.context)

                val textView_name = TextView(this.context)
                textView_name.setPadding(20,0,20,10)
                textView_name.maxWidth = resources.getDimension(R.dimen.text_w).toInt() / 2
                textView_name.textSize = 18f
                textView_name.setTextColor(resources.getColor(R.color.black))
                textView_name.text = position

                row_name.addView(textView_name)

                val textView_cost = TextView(this.context)
                textView_cost.setPadding(20,10,20,10)
                textView_cost.maxWidth = resources.getDimension(R.dimen.text_w).toInt() / 6 - 15
                textView_cost.minWidth = resources.getDimension(R.dimen.text_w).toInt() / 6 - 15
                textView_cost.textSize = 18f
                textView_cost.setTextColor(resources.getColor(R.color.black))
                textView_cost.text = cost.toString() + "₽"

                row_cost.addView(textView_cost)

                val button_buy = Button(this.context)
                button_buy.setPadding(20,10,20,10)
                button_buy.maxWidth = resources.getDimension(R.dimen.text_w).toInt() / 2
                button_buy.textSize = 12f
                button_buy.setTextColor(resources.getColor(R.color.black))
                button_buy.text = "Добавить в корзину (" + count.toString() + ")"
                if (count == 0)
                    button_buy.setBackgroundColor(resources.getColor(R.color.btn_background_color))
                else
                    button_buy.setBackgroundColor(resources.getColor(R.color.btn_push_background_color))
                button_buy.isClickable = true

                button_buy.setOnClickListener {

                }
                button_buy.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            button_buy.setTextColor(resources.getColor(R.color.white))
                            button_buy.setBackgroundColor(resources.getColor(R.color.btn_push_background_color))
                            count += 1
                            db.updateCountById(id, count)
                            true
                        }
                        MotionEvent.ACTION_UP -> {
                            button_buy.text = "Добавить в корзину (" + count.toString() + ")"
                            button_buy.setTextColor(resources.getColor(R.color.black))
                            button_buy.setBackgroundColor(resources.getColor(R.color.btn_push_background_color))
                            true
                        }
                        else -> false
                    }
                }

                row_cost.addView(button_buy)

                table_inner.addView(row_name)
                table_cost_inner.addView(row_cost)
                table_inner.addView(table_cost_inner)

                row_element.addView(table_inner)

                table.addView(row_element)

                ll.addView(table)
            } while (cursor.moveToNext())

            cursor.close()
        }

        db.close()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Bar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Bar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}