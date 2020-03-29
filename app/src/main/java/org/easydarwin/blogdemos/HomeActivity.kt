package org.easydarwin.blogdemos

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.easydarwin.blogdemos.commons.CommonContext
import org.easydarwin.blogdemos.room.WatchMovieActivity
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bt_2record.setOnClickListener {
            startActivity<RecordActivity>()
        }
        bt_2watch.setOnClickListener {
            startActivity<WatchMovieActivity>()
        }
        CommonContext.registerContext(this)
    }
}
