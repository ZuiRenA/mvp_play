package project.shen.mvp_play.presenter.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseDataBindingAdapter <T, E: ViewDataBinding>(private var items: MutableList<T>)
    : RecyclerView.Adapter<ViewHolder<E>>() {

    fun setItems(newItems: List<T>) {
        items.removeAll { true }
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        items.add(item)
        notifyItemRangeInserted(items.size, 1)
    }

    fun addItems(addItems: List<T>) {
        items.addAll(addItems)
        notifyItemRangeInserted(items.size, addItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder<E> {
        val viewDataBinding = DataBindingUtil.inflate<E>(
            LayoutInflater.from(parent.context), getLayoutId(i), parent, false)

        return ViewHolder(viewDataBinding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<E>, position: Int) {
        viewHolder.getViewDataBinding()?.let {
            onBindData(viewDataBinding = it, item = items[position],
                itemView = viewHolder.itemView, position = position)
        }
    }

    abstract fun onBindData(viewDataBinding: E, item: T, itemView: View, position: Int)

    override fun getItemCount(): Int = items.size

    protected abstract fun getLayoutId(type: Int): Int
}

class ViewHolder<E: ViewDataBinding> (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var viewDataBinding: E? = DataBindingUtil.getBinding(itemView)

    fun getViewDataBinding(): E? = viewDataBinding
}