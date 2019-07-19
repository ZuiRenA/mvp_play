package project.shen.mvp_play.presenter.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 基于DataBinding的baseRecyclerViewAdapter
 * @param T: 传入的item的类型
 * @param E: DataBinding自动生成的ViewDataBinding类型
 * @param items
 */
abstract class BaseDataBindingAdapter <T, E: ViewDataBinding>(private var items: MutableList<T>)
    : RecyclerView.Adapter<ViewHolder<E>>() {

    fun setItems(newItems: List<T>) {
        items = mutableListOf()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        val temp = items
        items = mutableListOf()
        items.addAll(temp + item)
        notifyItemRangeInserted(items.size, 1)
    }

    fun addItems(addItems: List<T>) {
        val temp = items
        items = mutableListOf()
        items.addAll(temp + addItems)
        notifyDataSetChanged()
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