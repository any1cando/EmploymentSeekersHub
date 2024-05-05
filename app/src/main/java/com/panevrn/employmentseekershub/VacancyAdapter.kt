package com.panevrn.employmentseekershub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.panevrn.employmentseekershub.model.dto.VacancyDto
import kotlin.math.max

class VacancyAdapter(
    private var vacancies: List<VacancyDto>?,
    private val onLikeClick: ((VacancyDto) -> Unit),
    private val onItemClick: ((VacancyDto) -> Unit)
): RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    class VacancyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val vacancyTitle: TextView = view.findViewById(R.id.vacancyTitle)
        private val companyTitle: TextView = view.findViewById(R.id.companyTitle)
        private val countCandidates: TextView = view.findViewById(R.id.countCandidates)
        private val tags: LinearLayout = view.findViewById(R.id.tags)
        private val description: TextView = view.findViewById(R.id.description)
        private val salary: TextView = view.findViewById(R.id.salary)
        private val postedTime: TextView = view.findViewById(R.id.postedTime)
        private val likeButton: ImageView = view.findViewById(R.id.likeButton)

        fun bind(vacancy: VacancyDto, onLikeClick: (VacancyDto) -> Unit, onItemClick: (VacancyDto) -> Unit) {
            companyTitle.text = vacancy.companyTitle
            vacancyTitle.text = vacancy.vacancyTitle
            countCandidates.text = "${vacancy.countCandidates} Applicants"
            description.text = vacancy.description
            salary.text = vacancy.salary.amount.toString()
            postedTime.text = "Posted ${vacancy.postedTime}"

            likeButton.setImageResource(if (vacancy.isLiked) R.drawable.full_heart else R.drawable.empty_heart)

            val maxTagsToShow = 3  // Ограничение по тегам
            tags.removeAllViews()
            vacancy.tags.take(maxTagsToShow).forEach { tag ->
                val tagView = LayoutInflater.from(tags.context).inflate(R.layout.tag_item, tags, false) as TextView
                tagView.text = tag
                tags.addView(tagView)
            }

            if (vacancy.tags.size > maxTagsToShow) {
                val moreTagsView = LayoutInflater.from(tags.context).inflate(R.layout.tag_item, tags, false) as TextView
                moreTagsView.text = "..."
                tags.addView(moreTagsView)
            }

            likeButton.setOnClickListener {
                onLikeClick(vacancy)
            }
            itemView.setOnClickListener {
                onItemClick(vacancy)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies!![position]
        holder.bind(vacancy, onLikeClick, onItemClick)

//        holder.companyTitle.text = vacancy.companyTitle
//        holder.vacancyTitle.text = vacancy.vacancyTitle
//        holder.countCandidates.text = "${vacancy.countCandidates} Applicants"
//        holder.description.text = vacancy.description
//        holder.salary.text = vacancy.salary.amount.toString()
//        holder.postedTime.text = "Posted ${vacancy.postedTime}"

//        val likeIcon = if (vacancy.isLiked) R.drawable.full_heart else R.drawable.empty_heart
//        holder.likeButton.setImageResource(likeIcon)

//        val maxTagsToShow = 3  // Ограничение по тегам
//        holder.tags.removeAllViews()

        // Отрисовываем теги с ограничением по количеству - 3 штуки
//        vacancy.tags.take(maxTagsToShow).forEach { tag ->
//            val tagView = LayoutInflater.from(holder.tags.context).inflate(R.layout.tag_item, holder.tags, false) as TextView
//            tagView.text = tag
//            holder.tags.addView(tagView)
//        }

        // Проверяем: если тегов больше, чем максимальное количество, то добавляем '...' четвертым тегом
//        if (vacancy.tags.size > maxTagsToShow) {
//            val moreTagsView = LayoutInflater.from(holder.tags.context).inflate(R.layout.tag_item, holder.tags, false) as TextView
//            moreTagsView.text = "..."
//            holder.tags.addView(moreTagsView)
//        }

//        holder.likeButton.setOnClickListener {
//            vacancy.isLiked = !vacancy.isLiked
//            notifyItemChanged(position)
//        }
    }

    override fun getItemCount(): Int = vacancies!!.size


    inner class VacancyDiffCallback(
        private val oldList: List<VacancyDto>,
        private val newList: List<VacancyDto>): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }


    // Обновление списка для отрисовки, если добавится новая вакансия
    fun updateData(newVacancies: List<VacancyDto>) {
        val diffCallback = VacancyDiffCallback(oldList = vacancies!!, newList = newVacancies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        vacancies = newVacancies
        diffResult.dispatchUpdatesTo(this)

    }

}