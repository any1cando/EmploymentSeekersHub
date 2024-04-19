package com.panevrn.employmentseekershub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.panevrn.employmentseekershub.model.dto.Vacancy

class VacancyAdapter(
    private var vacancies: List<Vacancy>,
    private val onLikeClicked: (Vacancy) -> Unit  // ???
): RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    class VacancyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val vacancyTitle: TextView = view.findViewById(R.id.vacancyTitle)
        val companyTitle: TextView = view.findViewById(R.id.companyTitle)
        val countCandidates: TextView = view.findViewById(R.id.countCandidates)
        val tags: LinearLayout = view.findViewById(R.id.tags)
        val description: TextView = view.findViewById(R.id.description)
        val salary: TextView = view.findViewById(R.id.salary)
        val postedTime: TextView = view.findViewById(R.id.postedTime)
        val likeButton: ImageView = view.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies[position]
        holder.companyTitle.text = vacancy.companyTitle
        holder.vacancyTitle.text = vacancy.vacancyTitle
        holder.countCandidates.text = "${vacancy.countCandidates} Applicants"
        holder.description.text = vacancy.description
        holder.salary.text = vacancy.salary
        holder.postedTime.text = "Posted ${vacancy.postedTime}"

        val likeIcon = if (vacancy.isLiked) R.drawable.full_heart else R.drawable.empty_heart
        holder.likeButton.setImageResource(likeIcon)

        holder.tags.removeAllViews()
        vacancy.tags.forEach {tag ->
            val tagView = LayoutInflater.from(holder.tags.context).inflate(R.layout.tag_item, holder.tags, false) as TextView
            tagView.text = tag
            holder.tags.addView(tagView)
        }

        holder.likeButton.setOnClickListener {
            vacancy.isLiked = !vacancy.isLiked
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = vacancies.size


    inner class VacancyDiffCallback(
        private val oldList: List<Vacancy>,
        private val newList: List<Vacancy>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    fun updateData(newVacancies: List<Vacancy>) {
        val diffCallback = VacancyDiffCallback(vacancies, newVacancies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        vacancies = newVacancies
        diffResult.dispatchUpdatesTo(this)

    }

}