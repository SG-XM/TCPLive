package com.twt.zq.commons.extentions

/**
 * Created by SGXM on 2018/7/12.
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_room.view.*
import org.easydarwin.blogdemos.R
import org.easydarwin.blogdemos.network.RoomBean
import org.easydarwin.blogdemos.room.WatchMovieActivity
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity


class RoomItem(val bean: RoomBean) : Item {
    override fun areItemsTheSame(newItem: Item): Boolean {
        return super.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(newItem: Item): Boolean {
        return (newItem as? RoomItem)?.bean?.id == this.bean.id && (newItem as? RoomItem)?.bean?.cover == this.bean.cover
    }

    companion object : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_room, parent, false)
            return ViewHolder(view, view.img_cover, view.tv_room, view.tv_info)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as RoomItem
            holder as ViewHolder
            holder.apply {
                view.setOnClickListener {
                    it.context.startActivity<WatchMovieActivity>("roomId" to item.bean.id)
                }
                Glide.with(pic).load(item.bean.cover).into(pic)
                roomId.text = "房间号:${item.bean.id}"
                info.text = "用户名:${item.bean.user.username}\n在线人数:${item.bean.onlineUserCount}"
            }
        }

    }

    class ViewHolder(val view: View, val pic: ImageView, val roomId: TextView, val info: TextView) :
            RecyclerView.ViewHolder(view)

    override val controller = Companion


}

interface Item {
    val controller: ItemController

    fun areItemsTheSame(newItem: Item): Boolean = false

    fun areContentsTheSame(newItem: Item): Boolean = false
}


interface ItemController {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)
}

open class ItemAdapter(val itemManager: ItemManagerAbstract) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        MutableList<Item> by itemManager {

    init {
        itemManager.observer = this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemManager.getController(viewType).onCreateViewHolder(parent)

    override fun getItemCount() = itemManager.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            itemManager[position].controller.onBindViewHolder(holder, itemManager[position])

    override fun getItemViewType(position: Int) = ItemManager.getViewType(itemManager[position].controller)
}

fun RecyclerView.withItems(items: List<Item>) {
    adapter = ItemAdapter(ItemManager(items.toMutableList()))
}

fun RecyclerView.withItems(init: MutableList<Item>.() -> Unit) =
        withItems(mutableListOf<Item>().apply(init))

interface ItemManagerAbstract : MutableList<Item> {
    var observer: RecyclerView.Adapter<RecyclerView.ViewHolder>?
}

open class ItemManager(private val delegated: MutableList<Item> = mutableListOf()) : ItemManagerAbstract {
    override var observer: RecyclerView.Adapter<RecyclerView.ViewHolder>? =
            null
    val itemListSnapshot: List<Item> get() = delegated

    init {
        ensureControllers(delegated)
    }

    companion object ItemControllerManager {
        private var viewType = 0 // companion object��֤�˵��� ���ViewType�϶��Ǵ�0��ʼ

        // controller to view type
        private val c2vt = mutableMapOf<ItemController, Int>()

        // view type to controller
        private val vt2c = mutableMapOf<Int, ItemController>()

        /**
         * ���Item(��Ӧ��controller)�Ƿ��Ѿ���ע�ᣬ���û�У��Ǿ�ע��һ��ViewType
         */
        private fun ensureController(item: Item) {
            val controller = item.controller
            if (!c2vt.contains(controller)) {
                c2vt[controller] = viewType
                vt2c[viewType] = controller
                viewType++
            }
        }

        /**
         * ����һ��Collection��ViewTypeע�ᣬ�Ƚ���һ��ȥ��
         */
        private fun ensureControllers(items: Collection<Item>): Unit =
                items.distinctBy(Item::controller).forEach(ItemControllerManager::ensureController)

        /**
         * ����ItemController��ȡ��Ӧ��Item -> ����Adapter.getItemViewType
         */
        fun getViewType(controller: ItemController): Int = c2vt[controller]
                ?: throw IllegalStateException("ItemController $controller is not ensured")

        /**
         * ����ViewType��ȡItemController -> ����OnCreateViewHolder����߼�
         */
        fun getController(viewType: Int): ItemController = vt2c[viewType]
                ?: throw IllegalStateException("ItemController $viewType is unused")
    }


    override val size: Int get() = delegated.size

    override fun contains(element: Item) = delegated.contains(element)

    override fun containsAll(elements: Collection<Item>) = delegated.containsAll(elements)

    override fun get(index: Int): Item = delegated[index]

    override fun indexOf(element: Item) = delegated.indexOf(element)

    override fun isEmpty() = delegated.isEmpty()

    override fun iterator() = delegated.iterator()

    override fun lastIndexOf(element: Item) = delegated.lastIndexOf(element)

    override fun listIterator() = delegated.listIterator()

    override fun listIterator(index: Int) = delegated.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = delegated.subList(fromIndex, toIndex)

    override fun add(element: Item) =
            delegated.add(element).also {
                ensureController(element)
                if (it) observer?.notifyItemInserted(size)
            }

    override fun add(index: Int, element: Item) =
            delegated.add(index, element).also {
                ensureController(element)
                observer?.notifyItemInserted(index)
            }

    override fun addAll(index: Int, elements: Collection<Item>) =
            delegated.addAll(elements).also {
                ensureControllers(elements)
                if (it) observer?.notifyItemRangeInserted(index, elements.size)
            }

    override fun addAll(elements: Collection<Item>) =
            delegated.addAll(elements).also {
                ensureControllers(elements)
                if (it) observer?.notifyItemRangeInserted(size, elements.size)
            }

    override fun clear() =
            delegated.clear().also {
                observer?.notifyItemRangeRemoved(0, size)
            }

    override fun remove(element: Item): Boolean =
            delegated.remove(element).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun removeAll(elements: Collection<Item>): Boolean =
            delegated.removeAll(elements).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun removeAt(index: Int) =
            delegated.removeAt(index).also {
                observer?.notifyItemRemoved(index)
                if (index != itemListSnapshot.size)
                    observer?.notifyItemRangeChanged(index, itemListSnapshot.size - index)
            }

    override fun retainAll(elements: Collection<Item>) =
            delegated.retainAll(elements).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun set(index: Int, element: Item) =
            delegated.set(index, element).also {
                ensureController(element)
                observer?.notifyItemChanged(index)
            }

    fun refreshAll(elements: List<Item>) {
        val tag = "refresh diff"
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = delegated[oldItemPosition]
                val newItem = elements[newItemPosition]
                // log(tag, "items the same: old-> $oldItemPosition new-> $newItemPosition diff: ${oldItem.areItemsTheSame(newItem)}")

                return oldItem.areItemsTheSame(newItem)
                //return false
            }

            override fun getOldListSize(): Int = delegated.size
            override fun getNewListSize(): Int = elements.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = delegated[oldItemPosition]
                val newItem = elements[newItemPosition]
                // Log.e(tag, "content the same: old-> $oldItemPosition new-> $newItemPosition diff: ${oldItem.areContentsTheSame(newItem)}")

                return oldItem.areContentsTheSame(newItem)
                //return false

            }
        }
        val result = DiffUtil.calculateDiff(diffCallback, true)
        delegated.clear()
        delegated.addAll(elements)
        ensureControllers(elements)
        result.dispatchUpdatesTo(observer!!)
    }


    fun refreshAll(init: MutableList<Item>.() -> Unit) = refreshAll(mutableListOf<Item>().apply(init))

    fun autoRefresh(init: MutableList<Item>.() -> Unit) {
        val snapshot = this.itemListSnapshot.toMutableList()
        snapshot.apply(init)
        refreshAll(snapshot)
    }


}

internal fun log(tag: String, message: String) {
    Log.e(tag, message)
}

