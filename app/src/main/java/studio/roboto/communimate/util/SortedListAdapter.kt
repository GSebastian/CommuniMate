package studio.roboto.communimate.util

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup



/**
 * Created by jordan on 16/10/2017.
 * jsf16 :: jordan
 */

abstract class SortedListAdapter<E, T : RecyclerView.ViewHolder>: RecyclerView.Adapter<T> {

    lateinit var mSortedList: SortedList<E>
    private var mClass: Class<E>
    private var mComparator: SortedListComparator<E>

    constructor(mClass: Class<E>, mComparator: SortedListComparator<E>) : super() {
        this.mClass = mClass
        this.mComparator = mComparator
        init()
    }

    private fun init() {
        mSortedList = SortedList<E>(mClass, object : SortedList.Callback<E>() {
            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun areItemsTheSame(item1: E, item2: E): Boolean {
                return mComparator.equal(item1, item2)
            }

            override fun compare(o1: E, o2: E): Int {
                return mComparator.compare(o1, o2)
            }

            override fun areContentsTheSame(oldItem: E, newItem: E): Boolean {
                return mComparator.equal(oldItem, newItem)
            }

        })
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        onBindViewHolder(holder, position, mSortedList.get(position))
    }

    abstract fun onBindViewHolder(holder: T, position: Int, item: E)

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T

    override fun getItemCount(): Int {
        return mSortedList.size()
    }

    fun get(pos: Int): E? {
        if (mSortedList.size() > pos && pos >= 0) {
            return mSortedList.get(pos)
        }
        else {
            return null
        }
    }

    //region Add / Remove methods

    open fun add(model: E) {
        mSortedList.add(model)
    }

    open fun remove(model: E) {
        mSortedList.remove(model)
    }

    open fun add(models: List<E>) {
        mSortedList.addAll(models)
    }

    open fun remove(models: List<E>) {
        mSortedList.beginBatchedUpdates()
        for (model in models) {
            mSortedList.remove(model)
        }
        mSortedList.endBatchedUpdates()
    }

    open fun removeAll() {
        if (mSortedList.size() > 0) {
            mSortedList.beginBatchedUpdates()
            for (i in mSortedList.size() - 1..0) {
                mSortedList.remove(mSortedList[i])
            }
            mSortedList.endBatchedUpdates()
        }
    }

    open fun replace(model: E) {
        mSortedList.beginBatchedUpdates()
        for (i in mSortedList.size() - 1 downTo 0) {
            val mod = mSortedList.get(i)
            if (mComparator.equal(mod, model)) {
                mSortedList.remove(mod)
            }
        }
        mSortedList.add(model)
        mSortedList.endBatchedUpdates()
    }

    open fun replaceAll(models: List<E>) {
        mSortedList.beginBatchedUpdates()
        for (i in mSortedList.size() - 1 downTo 0) {
            val model = mSortedList.get(i)
            if (!models.contains(model)) {
                mSortedList.remove(model)
            }
        }
        mSortedList.addAll(models)
        mSortedList.endBatchedUpdates()
    }

    //endregion
}