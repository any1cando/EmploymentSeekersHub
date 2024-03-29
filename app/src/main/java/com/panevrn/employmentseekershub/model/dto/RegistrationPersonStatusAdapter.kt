package com.panevrn.employmentseekershub.model.dto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.panevrn.employmentseekershub.R

class RegistrationPersonStatusAdapter(context: Context, resource: Int, objects: List<RegistrationPersonStatus>): ArrayAdapter<RegistrationPersonStatus>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.spinner_item, parent, false)
        val person = getItem(position)
        val textView = view.findViewById<TextView>(R.id.textViewSpinner)
        textView.text = "${person?.status}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

}