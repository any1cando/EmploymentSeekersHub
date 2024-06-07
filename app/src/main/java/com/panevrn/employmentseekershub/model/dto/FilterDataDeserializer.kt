package com.panevrn.employmentseekershub.model.dto

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class FilterDataDeserializer: JsonDeserializer<FiltersDto> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): FiltersDto {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val title = jsonObject.get("title").asString
        val filters = mutableListOf<Filter>()
        Log.i("JSON TEST", json.toString())
        jsonObject.getAsJsonArray("filters").forEach {element ->
            val filterObject = element.asJsonObject
            val type = filterObject.get("type").asString

            val data: FilterData = when (type) {
                "checkBox" -> {
                    val options = filterObject.getAsJsonArray("data").map {
                        context!!.deserialize<Option>(it, Option::class.java)
                    }
                    FilterData.CheckBoxOptions(options)
                }
                "range" -> {
                    context!!.deserialize<FilterData.Range>(filterObject.get("data"), FilterData.Range::class.java)
                }
                else -> throw JsonParseException("Unknown type: $filterObject")
            }
            filters.add(Filter(type, data))
        }
        return FiltersDto(title, filters)  // Здесь возвращается пересобранный объект FiltersDto
    }
}