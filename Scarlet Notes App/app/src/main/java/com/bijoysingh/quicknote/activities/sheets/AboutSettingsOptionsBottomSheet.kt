package com.bijoysingh.quicknote.activities.sheets

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.View
import com.bijoysingh.quicknote.R
import com.bijoysingh.quicknote.activities.MainActivity
import com.bijoysingh.quicknote.items.OptionsItem
import com.github.bijoysingh.starter.util.IntentUtils

class AboutSettingsOptionsBottomSheet : OptionItemBottomSheetBase() {

  override fun setupViewWithDialog(dialog: Dialog) {
    setOptions(dialog, getOptions())
  }

  private fun getOptions(): List<OptionsItem> {
    val activity = context as MainActivity
    val options = ArrayList<OptionsItem>()
    options.add(OptionsItem(
        title = R.string.home_option_about_page,
        subtitle = R.string.home_option_about_page_subtitle,
        icon = R.drawable.ic_info,
        listener = View.OnClickListener {
          AboutUsBottomSheet.openSheet(activity)
          dismiss()
        }
    ))
    options.add(OptionsItem(
        title = R.string.home_option_open_source_page,
        subtitle = R.string.home_option_open_source_page_subtitle,
        icon = R.drawable.ic_code_white_48dp,
        listener = View.OnClickListener {
          OpenSourceBottomSheet.openSheet(activity)
          dismiss()
        }
    ))
    options.add(OptionsItem(
        title = R.string.home_option_rate_and_review,
        subtitle = R.string.home_option_rate_and_review_subtitle,
        icon = R.drawable.ic_rating,
        listener = View.OnClickListener {
          IntentUtils.openAppPlayStore(activity)
          dismiss()
        }
    ))
    options.add(OptionsItem(
        title = R.string.whats_new_title,
        subtitle = R.string.whats_new_subtitle,
        icon = R.drawable.ic_format_quote_white_48dp,
        listener = View.OnClickListener {
          WhatsNewItemsBottomSheet.openSheet(activity)
          dismiss()
        }
    ))
    options.add(OptionsItem(
        title = R.string.home_option_fill_survey,
        subtitle = R.string.home_option_fill_survey_subtitle,
        icon = R.drawable.ic_note_white_48dp,
        listener = View.OnClickListener {
          activity.startActivity(Intent(
              Intent.ACTION_VIEW,
              Uri.parse(SURVEY_LINK)))
          dismiss()
        }
    ))
    return options
  }

  override fun getLayout(): Int = R.layout.layout_options_sheet

  companion object {

    const val SURVEY_LINK = "https://goo.gl/forms/UbE2lARpp89CNIbl2"

    fun openSheet(activity: MainActivity) {
      val sheet = AboutSettingsOptionsBottomSheet()

      sheet.show(activity.supportFragmentManager, sheet.tag)
    }
  }
}