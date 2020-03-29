package org.easydarwin.blogdemos.room

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.twt.zq.commons.extentions.ItemAdapter
import com.twt.zq.commons.extentions.ItemManager
import com.twt.zq.commons.extentions.RoomItem
import com.twt.zq.commons.extentions.bindNonNull
import kotlinx.android.synthetic.main.activity_room.*
import org.easydarwin.blogdemos.R
import org.easydarwin.blogdemos.network.ServiceModel

class RoomActivity : AppCompatActivity() {

    private val itm = ItemManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        initView()
        initData()

    }

    private fun initView() {
        rec_room.layoutManager = LinearLayoutManager(this)
        rec_room.adapter = ItemAdapter(itm)
    }

    private fun initData() {
        ServiceModel.apply {
            getRoom()
            rooms.bindNonNull(this@RoomActivity) {
                itm.refreshAll(it.map { RoomItem(it) })
            }
        }
    }
}