package de.hackatum2018.sixtcarpool.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ItemListAdapter<Item, ViewHolder : RecyclerView.ViewHolder>(
        context: Context,
        @LayoutRes private val layoutId: Int,
        private val viewHolderProvider: (View) -> ViewHolder,
        private val bindView: ViewHolder.(item: Item, position: Int) -> Unit,
        private val bindEmpty: ViewHolder.() -> Unit = {}

) : RecyclerView.Adapter<ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    var items: List<Item>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(layoutId, parent, false)
        return viewHolderProvider(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items != null) {
            val item = items!![position]
            bindView(holder, item, position)

        } else {
            bindEmpty(holder)
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0
}