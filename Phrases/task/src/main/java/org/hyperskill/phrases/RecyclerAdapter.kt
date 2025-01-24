package org.hyperskill.phrases


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val phrases: List<String>) : RecyclerView.Adapter<RecyclerAdapter.PhrasesViewHolder>() {

    class PhrasesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhrase = view.findViewById<TextView>(R.id.phraseTextView)
        val tvDelete = view.findViewById<TextView>(R.id.deleteTextView)
    }

    val mutablePhrases = phrases.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhrasesViewHolder =
        PhrasesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false))

    override fun getItemCount(): Int =
        mutablePhrases.size

    override fun onBindViewHolder(holder: PhrasesViewHolder, position: Int) {
        holder.tvPhrase.text = mutablePhrases[position]
        holder.tvDelete.setOnClickListener {
            mutablePhrases.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mutablePhrases.size - position)
        }
    }

}