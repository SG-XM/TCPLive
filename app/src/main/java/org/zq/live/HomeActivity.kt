package org.zq.live

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.zq.live.commons.CommonContext
import org.zq.live.room.RoomActivity
import pub.devrel.easypermissions.EasyPermissions


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bt_2record.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) { //已经同意过
                startActivity<RecordActivity>()
            } else { //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "需要相机和录音权限",  //提示文言
                        1,  //请求码
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) //权限列表

            }


        }
        bt_2watch.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) { //已经同意过
                startActivity<RoomActivity>()
            } else { //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "需要相机和录音权限",  //提示文言
                        1,  //请求码
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) //权限列表

            }


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