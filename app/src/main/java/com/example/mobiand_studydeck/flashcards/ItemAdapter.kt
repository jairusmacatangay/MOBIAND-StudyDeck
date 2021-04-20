package com.example.mobiand_studydeck.flashcards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiand_studydeck.FlashcardActivity
import com.example.mobiand_studydeck.R

class ItemAdapter(val context: Context, val items: ArrayList<FlashcardsModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvTerm.text = item.term
        holder.tvDefinition.text = item.definition

        holder.ivEdit.setOnClickListener { view ->
            if (context is FlashcardActivity) {
                context.updateRecordDialog(item)
            }
        }

        //Delete Button
        holder.ivDelete.setOnClickListener { view ->

            if (context is FlashcardActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val llMain: LinearLayout = view.findViewById(R.id.llMain)
        val tvTerm: TextView = view.findViewById(R.id.tvTerm)
        val tvDefinition: TextView = view.findViewById(R.id.tvDefinition)
        val ivEdit = view.findViewById<ImageView>(R.id.ivEdit)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
    }
}