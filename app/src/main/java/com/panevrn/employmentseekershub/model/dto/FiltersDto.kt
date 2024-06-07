package com.panevrn.employmentseekershub.model.dto

import java.util.Optional

//typealias FiltersDtoList = List<FiltersDto>  ???

data class FiltersDto(
    var title: String,
    val filters: List<Filter>
)

data class Filter(
    val type: String,
    val data: FilterData
)

sealed class FilterData {
    data class CheckBoxOptions(val options: List<Option>): FilterData()
    data class Range(val from: String, val to: String): FilterData()
}

data class Option(
    val title: String,
    val isCheck: Boolean
)
