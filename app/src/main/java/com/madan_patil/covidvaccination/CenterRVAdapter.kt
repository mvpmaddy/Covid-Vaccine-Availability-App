package com.madan_patil.covidvaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdapter (private val centerList: List<CenterRVModel>): RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {


    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val centerNameTV: TextView = itemView.findViewById(R.id.idTVCenterName)
        val centerLocationTV: TextView = itemView.findViewById(R.id.idTVCenterLocation)
        val centerTimingsTV: TextView = itemView.findViewById(R.id.idTVCenterTimings)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.idTVVaccineName)
        val vaccineFeesTV: TextView = itemView.findViewById(R.id.idTVVaccineCost)
        val ageLimitTV: TextView = itemView.findViewById(R.id.idTVAgeLimit)
        val availabilityTV: TextView = itemView.findViewById(R.id.idTVAvailability)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item, parent, false)
        return CenterRVViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {

        val center = centerList[position]
        holder.centerNameTV.text = center.centerName
        holder.centerLocationTV.text = center.centerAddress
        holder.centerTimingsTV.text = ("From: " + center.centerFromTime + " To: " + center.centerToTime)
        holder.vaccineNameTV.text = center.vaccineName
        holder.vaccineFeesTV.text = center.feeType
        holder.ageLimitTV.text = ("Age Limit: " + center.ageLimit.toString())
        holder.availabilityTV.text = ("Availability: " + center.availableCapacity.toString())

    }

    override fun getItemCount(): Int {
        return centerList.size
    }
}