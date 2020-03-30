package org.easydarwin.blogdemos

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.easydarwin.blogdemos.commons.CommonContext
import org.easydarwin.blogdemos.room.RoomActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bt_2record.setOnClickListener {
            startActivity<RecordActivity>()
        }
        bt_2watch.setOnClickListener {
            startActivity<RoomActivity>()
        }
        CommonContext.registerContext(this)
    }
}


fun Activity.modifyText(hintStr: String = "", callback: (String) -> Unit) {
    alert {
        customView {
            constraintLayout {
                lparams {
                    width = dip(300)
                    height = wrapContent
                    padding = dip(12)
                }
                val edt = editText {
                    hint = hintStr
                }.lparams {
                    width = matchParent
                    topMargin = dip(24)
                }
                positiveButton("确认") {
                    callback(edt.text.toString())
                }
                negativeButton("取消") {}
            }
        }
    }.show()
}