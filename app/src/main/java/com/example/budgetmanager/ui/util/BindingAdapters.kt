package com.example.budgetmanager

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.budgetmanager.database.models.Token
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:showAmount")
fun showAmount(textview: TextView, token: Token) {
    if(token.type == 1) {
        textview.setTextColor(textview.context.getColor(R.color.green))
        textview.text = "+${token.amount}"
    }
    else {
        textview.setTextColor(textview.context.getColor(R.color.red))
        textview.text = "-${token.amount}"
    }
}

@BindingAdapter("app:showDisplayAmount")
fun showDisplayAmount(textview: TextView, token: Token) {
    if(token.type == 1) {
        textview.text = "+${token.amount}"
    }
    else {
        textview.text = "-${token.amount}"
    }
}

@BindingAdapter("app:showIncome")
fun showIncome(textview: TextView, amount: Double) {
    textview.setTextColor(textview.context.getColor(R.color.green))
    textview.text = "+${amount}"
}

@BindingAdapter("app:showExpense")
fun showExpense(textview: TextView, amount: Double) {
    textview.setTextColor(textview.context.getColor(R.color.red))
    textview.text = "-${amount}"
}


@BindingAdapter("app:showDate")
fun showDate(textview: TextView, token: Token) {
    textview.text = SimpleDateFormat("EEEE\nMMM d").format(Date(token.dateLong))
}

@BindingAdapter("app:showFullDate")
fun showFullDate(textview: TextView, token: Token) {
    textview.text = SimpleDateFormat("EEEE, MMM d, yyyy").format(Date(token.dateLong))
}

@BindingAdapter("app:showMonthlyDate")
fun showMonthlyDate(textview: TextView, dateLong: Long) {
    textview.text = SimpleDateFormat("MMM yyyy").format(Date(dateLong))
}

@BindingAdapter("app:setDisplayTokenBackground")
fun setDisplayTokenBackground(cardView: CardView, token: Token) {
    if(token.type == 1) {
        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
    }
    else {
        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
    }
}

@BindingAdapter("app:setTagChips")
fun setTagChip(chipGroup: ChipGroup, token: Token) {
    token.tags?.let {
        chipGroup.removeAllViews()
        for(str in it) {
            val lChip = Chip(chipGroup.context)
            lChip.text = str
            chipGroup.addView(lChip, chipGroup.childCount - 1)
        }
    }
}

@BindingAdapter("app:setDescription")
fun setDescription(textview: TextView, token: Token) {
    if(token.description.isNullOrBlank() || token.description.isNullOrEmpty()) {
        textview.text = textview.context.getString(R.string.no_description)
    }
    else {
        textview.text = token.description
    }
}