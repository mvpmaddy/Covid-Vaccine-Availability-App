package com.madan_patil.covidvaccination

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var searchBtn: Button
    private lateinit var pinCodeEdt: EditText
    private lateinit var centerRV: RecyclerView
    private lateinit var loadingPB: ProgressBar
    private lateinit var centerList: List<CenterRVModel>
    private lateinit var centerRVAdapter: CenterRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBtn = findViewById(R.id.idBtnSearch)
        pinCodeEdt = findViewById(R.id.idEdtPinCode)
        centerRV = findViewById(R.id.idRVCenters)
        loadingPB = findViewById(R.id.idPBLoading)
        centerList = ArrayList<CenterRVModel>()

        searchBtn.setOnClickListener{

            val pinCode = pinCodeEdt.text.toString()

            if (pinCode.length != 6){
                Toast.makeText(this, "Please enter a valid Pin Code", Toast.LENGTH_SHORT).show()
            }else {
                (centerList as ArrayList<CenterRVModel>).clear()
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this,
                        { view, year, month, dayOfMonth ->
                            loadingPB.visibility = View.VISIBLE
                            val dateStr = """$dayOfMonth-${month+1}-$year"""
                            getAppointmentDetails(pinCode, dateStr)
                        },
                        year,
                        month,
                        day
                )
                dpd.show()
            }

        }

    }

    private fun getAppointmentDetails(pinCode: String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=$pinCode&date=$date"
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null,{ response ->
            loadingPB.visibility = View.GONE
            try {
                val centerArray = response.getJSONArray("centers")
                if (centerArray.length() == 0){
                    Toast.makeText(this, "No vaccination centers available", Toast.LENGTH_SHORT).show()
                }else{
                    for (i in 0 until centerArray.length()){
                        val centerObj = centerArray.getJSONObject(i)
                        val centerName: String = centerObj.getString("name")
                        val centerAddress: String = centerObj.getString("address")
                        val centerFromTime: String = centerObj.getString("from")
                        val centerToTime: String = centerObj.getString("to")
                        val fee_type: String = centerObj.getString("fee_type")
                        val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                        val availableCapacity: Int = sessionObj.getInt("available_capacity")
                        val ageLimit: Int = sessionObj.getInt("min_age_limit")
                        val vaccineName: String = sessionObj.getString("vaccine")

                        val center = CenterRVModel(
                                centerName, centerAddress, centerFromTime, centerToTime, fee_type, ageLimit, vaccineName, availableCapacity
                        )
                        centerList = centerList+center
                    }
                    centerRVAdapter = CenterRVAdapter(centerList)
                    centerRV.layoutManager = LinearLayoutManager(this)
                    centerRV.adapter = centerRVAdapter
                }
            }catch (e: JSONException){
                loadingPB.visibility = View.GONE
                e.printStackTrace()
            }

        },
            { error ->
                loadingPB.visibility = View.GONE
                Toast.makeText(this, "Failed to fetch the data", Toast.LENGTH_SHORT).show()
            })
        queue.add(request)
    }
}