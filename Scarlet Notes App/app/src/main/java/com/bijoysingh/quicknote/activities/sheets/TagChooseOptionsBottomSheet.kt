package com.bijoysingh.quicknote.activities.sheets

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import com.bijoysingh.quicknote.R
import com.bijoysingh.quicknote.activities.ThemedActivity
import com.bijoysingh.quicknote.database.Note
import com.bijoysingh.quicknote.database.Tag
import com.bijoysingh.quicknote.items.TagOptionsItem

class TagChooseOptionsBottomSheet : TagOptionItemBottomSheetBase() {

  var note: Note? = null
  var dismissListener: () -> Unit = {}

  override fun setupViewWithDialog(dialog: Dialog) {
    if (note === null) {
      dismiss()
      return
    }

    setOptions(dialog, getOptions())
    setOptionTitle(dialog, R.string.tag_sheet_choose_tag)
  }

  override fun onNewTagClick() {
    val activity = context as ThemedActivity
    CreateOrEditTagBottomSheet.openSheet(activity, Tag.gen(), { tag, _ ->
      note!!.toggleTag(tag)
      note!!.save(context)
      reset(dialog)
    })
  }

  override fun onDismiss(dialog: DialogInterface?) {
    super.onDismiss(dialog)
    dismissListener()
  }

  private fun getOptions(): List<TagOptionsItem> {
    val options = ArrayList<TagOptionsItem>()
    val tags = note!!.tagUUIDs
    for (tag in Tag.db(context).all) {
      options.add(TagOptionsItem(
          tag = tag,
          listener = View.OnClickListener {
            note!!.toggleTag(tag)
            note!!.save(context)
            reset(dialog)
          },
          selected = tags.contains(tag.uuid)
      ))
    }
    return options
  }

  companion object {
    fun openSheet(activity: ThemedActivity, note: Note, dismissListener: () -> Unit) {
      val sheet = TagChooseOptionsBottomSheet()

      sheet.note = note
      sheet.dismissListener = dismissListener
      sheet.show(activity.supportFragmentManager, sheet.tag)
    }
  }
}