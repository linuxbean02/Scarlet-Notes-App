package com.bijoysingh.quicknote.formats

import android.util.Log

import com.bijoysingh.quicknote.R
import com.bijoysingh.quicknote.recyclerview.EmptyFormatHolder
import com.bijoysingh.quicknote.recyclerview.EmptyRecyclerHolder
import com.bijoysingh.quicknote.recyclerview.FormatListViewHolder
import com.bijoysingh.quicknote.recyclerview.FormatTextViewHolder
import com.github.bijoysingh.starter.recyclerview.MultiRecyclerViewControllerItem

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

class Format : Comparable<Format> {

  var formatType: FormatType = FormatType.TEXT

  var uid: Int = 0

  var text: String = ""

  var forcedMarkdown = false

  val markdownText: String
    get() {
      when (formatType) {
        FormatType.BULLET_LIST, FormatType.NUMBERED_LIST -> return "- " + text
        FormatType.HEADING -> return "# " + text
        FormatType.CHECKLIST_CHECKED -> return "\u2612 " + text
        FormatType.CHECKLIST_UNCHECKED -> return "\u2610 " + text
        FormatType.IMAGE, FormatType.SUB_HEADING -> return "### " + text
        FormatType.CODE -> return "```\n$text\n```"
        FormatType.QUOTE -> return "> " + text
        FormatType.TEXT -> return text
        else -> return text
      }
    }

  constructor() {}

  constructor(formatType: FormatType) {
    this.formatType = formatType
  }

  constructor(formatType: FormatType, text: String) {
    this.formatType = formatType
    this.text = text

    if (formatType === FormatType.TAG) {
      forcedMarkdown = true
    }
  }

  fun toJson(): JSONObject? {
    if (text.trim { it <= ' ' }.isEmpty()) {
      return null
    }

    val map = HashMap<String, Any>()
    map["format"] = formatType.name
    map["text"] = text
    return JSONObject(map)
  }

  override fun compareTo(other: Format): Int {
    return when {
      other.formatType == FormatType.CHECKLIST_CHECKED && formatType == FormatType.CHECKLIST_UNCHECKED -> -1
      other.formatType == FormatType.CHECKLIST_UNCHECKED && formatType == FormatType.CHECKLIST_CHECKED -> 1
      else -> 0
    }
  }

  companion object {
    const val KEY_NOTE = "note"

    @Throws(JSONException::class)
    fun fromJson(json: JSONObject): Format {
      val format = Format()
      format.formatType = FormatType.valueOf(json.getString("format"))
      format.text = json.getString("text")
      return format
    }

    fun getNote(formats: List<Format>): String {
      val array = JSONArray()
      for (format in formats) {
        val json = format.toJson()
        if (json != null) array.put(json)
      }

      val cache = HashMap<String, Any>()
      cache[KEY_NOTE] = array
      return JSONObject(cache).toString()
    }

    fun getFormats(note: String): List<Format> {
      val formats = ArrayList<Format>()
      try {
        val json = JSONObject(note)
        val array = json.getJSONArray(KEY_NOTE)
        for (index in 0 until array.length()) {
          try {
            val format = fromJson(array.getJSONObject(index))
            format.uid = formats.size
            formats.add(format)
          } catch (innerException: JSONException) {
          }
        }
      } catch (exception: Exception) {
      }
      return formats
    }

    val list: List<MultiRecyclerViewControllerItem<Format>>
      get() {
        val list = ArrayList<MultiRecyclerViewControllerItem<Format>>()
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.TAG.ordinal)
                .layoutFile(R.layout.item_format_tag)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.TEXT.ordinal)
                .layoutFile(R.layout.item_format_text)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.HEADING.ordinal)
                .layoutFile(R.layout.item_format_heading)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.SUB_HEADING.ordinal)
                .layoutFile(R.layout.item_format_sub_heading)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.QUOTE.ordinal)
                .layoutFile(R.layout.item_format_quote)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.CODE.ordinal)
                .layoutFile(R.layout.item_format_code)
                .holderClass(FormatTextViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.CHECKLIST_CHECKED.ordinal)
                .layoutFile(R.layout.item_format_list)
                .holderClass(FormatListViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.CHECKLIST_UNCHECKED.ordinal)
                .layoutFile(R.layout.item_format_list)
                .holderClass(FormatListViewHolder::class.java)
                .build())
        list.add(
            MultiRecyclerViewControllerItem.Builder<Format>()
                .viewType(FormatType.EMPTY.ordinal)
                .layoutFile(R.layout.item_format_fab_space)
                .holderClass(EmptyFormatHolder::class.java)
                .build())
        return list
      }

    fun getNextFormatType(type: FormatType): FormatType {
      when (type) {
        FormatType.BULLET_LIST -> return FormatType.BULLET_LIST
        FormatType.NUMBERED_LIST -> return FormatType.NUMBERED_LIST
        FormatType.HEADING -> return FormatType.SUB_HEADING
        FormatType.CHECKLIST_CHECKED, FormatType.CHECKLIST_UNCHECKED -> return FormatType.CHECKLIST_UNCHECKED
        FormatType.IMAGE, FormatType.SUB_HEADING, FormatType.CODE, FormatType.QUOTE, FormatType.TEXT -> return FormatType.TEXT
        else -> return FormatType.TEXT
      }
    }
  }
}
