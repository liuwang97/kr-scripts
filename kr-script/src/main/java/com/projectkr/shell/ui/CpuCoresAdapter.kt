package com.omarea.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.projectkr.shell.CpuCoreInfo
import com.projectkr.shell.R
import com.projectkr.shell.ui.CpuChatView
import java.util.*

class CpuCoresAdapter(private val context: Context, private val list: ArrayList<CpuCoreInfo>?) : BaseAdapter() {

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    override fun getItem(position: Int): CpuCoreInfo {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun subFreqStr(freq: String?): String {
        if (freq == null) {
            return ""
        }
        if (freq.length > 3) {
            return freq.substring(0, freq.length - 3)
        } else if (freq.isEmpty()) {
            return "0"
        } else {
            return freq
        }
    }

    fun setData(list: ArrayList<CpuCoreInfo>): CpuCoresAdapter {
        this.list!!.clear();
        this.list.addAll(list)
        notifyDataSetChanged()
        return this;
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cpu_core_item, null)
        }
        val coreInfo = getItem(position)
        val cpuChatView = convertView!!.findViewById<CpuChatView>(R.id.core_cpu_loading_chat)
        cpuChatView.setData(100f, 100 - coreInfo.loadRatio.toFloat() + 0.5f)
        cpuChatView.invalidate()

        val index = convertView.findViewById<TextView>(R.id.cpu_core_load)
        index.text = coreInfo.loadRatio.toInt().toString() + "%"

        val currentFreq = convertView.findViewById<TextView>(R.id.cpu_core_current_freq)
        val freqMhz = subFreqStr(coreInfo.currentFreq)
        if (freqMhz == "0") {
            currentFreq.text = "离线"
        } else {
            currentFreq.text = freqMhz + " Mhz"
        }

        val freqRanage = convertView.findViewById<TextView>(R.id.cpu_core_freq_ranage)
        val freq = subFreqStr(coreInfo.minFreq) + " ~ " + subFreqStr(coreInfo.maxFreq) + "Mhz"
        freqRanage.text = freq
        return convertView
    }
}