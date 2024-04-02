package com.panevrn.employmentseekershub.adapter.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.panevrn.employmentseekershub.R
import com.panevrn.employmentseekershub.model.dto.RegistrationPersonStatus

class RegistrationPersonStatusAdapter(context: Context, resource: Int, objects: List<RegistrationPersonStatus>): ArrayAdapter<RegistrationPersonStatus>(context, resource, objects) {
    // Метод 'getView' вызывается для каждого элемента в списке данных, когда адаптеру нужно создать новое представление (View) для отображения.
    var isSelectionMade: Boolean = false
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.spinner_item, parent, false)
        val person = getItem(position)
        val textView = view.findViewById<TextView>(R.id.textViewSpinnerItem)
        val imageView = view.findViewById<ImageView>(R.id.imageViewSpinnerIconItem)

        if (!isSelectionMade) {
            textView.text = context.getString(R.string.choose_prompt)
            imageView.setImageResource(R.drawable.default_spinner_icon)
            isSelectionMade = true
        }
        else {
            textView.text = person?.status
            imageView.setImageResource(getIconResourceByStatus(person?.status))
        }

        return view
    }
    // Метод 'getDropDownView' вызывается для создания представления элементов, отображаемых в выпадающем списке
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {  // Все тоже самое, что и с методом getView()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.spinner_item, parent, false)
        val person = getItem(position)
        val textView = view.findViewById<TextView>(R.id.textViewSpinnerItem)
        val imageView = view.findViewById<ImageView>(R.id.imageViewSpinnerIconItem)
        textView.text = "${person?.status}"
        imageView.setImageResource(getIconResourceByStatus(person?.status))
        return view
    }

    private fun getIconResourceByStatus(status: String?): Int {
        return when (status) {
            context.resources.getString(R.string.part_of_a_team) -> R.drawable.part_of_a_team
            context.resources.getString(R.string.solo_creator) -> R.drawable.solo_creator
            else -> R.drawable.default_spinner_icon
        }
    }

}