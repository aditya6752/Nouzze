package com.screentimex.nouzze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.screentimex.nouzze.R
import com.screentimex.nouzze.UsageStats.Utils
import com.screentimex.nouzze.models.AppInfo

class AppInfoListAdapter(private val context: Context, private val appInfoList: List<AppInfo>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return appInfoList.size
    }

    override fun getItem(position: Int): AppInfo {
        return appInfoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val listViewHolder: ViewHolder
        val appInfo = appInfoList[position]
        if (convertView == null) {
            listViewHolder = ViewHolder()
            convertView = layoutInflater.inflate(R.layout.app_row_view, parent, false)

            listViewHolder.appName = convertView.findViewById(R.id.list_app_name)
            listViewHolder.appIcon = convertView.findViewById(R.id.list_app_icon)
            listViewHolder.isUsageExceeded = convertView.findViewById(R.id.isUsageExceeded)
            listViewHolder.isTracked = convertView.findViewById(R.id.isTracked)

            convertView.tag = listViewHolder
        } else {
            listViewHolder = convertView.tag as ViewHolder
        }

        listViewHolder.appName.text = appInfo.appName
        listViewHolder.appIcon.setImageDrawable(appInfo.appIcon)
        listViewHolder.isTracked.visibility = View.VISIBLE
        val time = showTimeSpent(appInfo.timeUseApp.toInt())
        listViewHolder.isTracked.text = time
        return convertView!!
    }
    private fun showTimeSpent(timeSpent: Int): String {
        val timesAllowed = Utils.reverseProcessTime(timeSpent)
        return String.format("%02dhr: %02dmin: %02dsec", timesAllowed[0], timesAllowed[1], timesAllowed[2])
    }

    class ViewHolder {
        lateinit var appName: TextView
        lateinit var appIcon: ImageView
        lateinit var isUsageExceeded: ImageView
        lateinit var isTracked: TextView
    }
}
