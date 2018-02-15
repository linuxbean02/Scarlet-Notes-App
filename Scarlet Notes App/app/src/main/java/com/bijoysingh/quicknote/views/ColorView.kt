package com.bijoysingh.quicknote.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.bijoysingh.quicknote.R
import com.bijoysingh.quicknote.utils.CircleDrawable

class ColorView : LinearLayout {

  lateinit internal var root: View
  lateinit internal var icon: ImageView

  constructor(context: Context) : super(context) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init(context)
  }

  @TargetApi(21)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    init(context)
  }

  fun init(context: Context) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    root = inflater.inflate(R.layout.layout_color, null)

    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    root.layoutParams = params
    icon = root.findViewById<ImageView>(R.id.color_icon)
    addView(root)
  }

  fun setColor(color: Int, selected: Boolean) {
    this.icon.setImageResource(if (selected) R.drawable.ic_done_white_48dp else 0)
    this.icon.background = CircleDrawable(color)
    this.icon.setColorFilter(Color.WHITE)
  }
}
