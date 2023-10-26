package com.screentimex.nouzze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.screentimex.nouzze.UsageStats.Utils
import com.screentimex.nouzze.databinding.AppRowViewBinding
import com.screentimex.nouzze.models.AppInfo

class AppInfoListAdapter(private val context: Context, private val list: ArrayList<AppInfo>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun showTimeSpent(timeSpent: Int): String {
        val timesAllowed = Utils.reverseProcessTime(timeSpent)
        return String.format("%02dhr: %02dmin: %02dsec", timesAllowed[0], timesAllowed[1], timesAllowed[2])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(AppRowViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            holder.appIcon.setImageDrawable(model.appIcon)
            holder.appName.text = model.appName
            holder.appTime.text = showTimeSpent(model.timeUseApp.toInt())
        }
    }

    class MyViewHolder(binding: AppRowViewBinding): RecyclerView.ViewHolder(binding.root) {
        val appIcon = binding.listAppIcon
        val appName = binding.listAppName
        val appTime = binding.usageTime
    }
}
