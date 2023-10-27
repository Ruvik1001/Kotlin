package com.example.kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.kotlin.database.local.DBSupport

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var support: Support
    private lateinit var view: View
    private lateinit var localDB: DBSupport


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
        view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tableName = GlobalArgs().UserTableName
        val filed = GlobalArgs().UserFiled

        localDB = DBSupport(requireContext())
        localDB.selectTable(tableName, filed)

        setData()
        return view
    }

    @SuppressLint("SetTextI18n")
    fun setData() {
        view.findViewById<ImageButton>(R.id.btn_about).setOnClickListener {
            view.findNavController().navigate(R.id.action_profile_to_about)
        }
        view.findViewById<ImageButton>(R.id.btn_emploee).setOnClickListener {
            view.findNavController().navigate(R.id.action_profile_to_emploee)
        }
        view.findViewById<ImageButton>(R.id.btn_tasks).setOnClickListener {
            val communicate = activity as Support
            communicate.OnTasksButtonClicked()
        }

        val user = localDB.getAllDataFromCurrentTable()
        view.findViewById<TextView>(R.id.login).setText(user[0][1])
        view.findViewById<TextView>(R.id.position).setText(user[0][6])
        view.findViewById<TextView>(R.id.telephon).setText(user[0][7])
        view.findViewById<TextView>(R.id.FCs).setText(
                    user[0][4] + " " +
                    user[0][3] + " " +
                    user[0][5])
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}