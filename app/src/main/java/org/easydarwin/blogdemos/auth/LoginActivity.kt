package org.easydarwin.blogdemos.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.easydarwin.blogdemos.HomeActivity
import org.easydarwin.blogdemos.R
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bt_login.setOnClickListener {
            startActivity<HomeActivity>()
        }
    }
}
