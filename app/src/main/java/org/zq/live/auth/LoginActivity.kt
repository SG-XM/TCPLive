package org.zq.live.auth

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.twt.zq.commons.extentions.bindNonNull
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.zq.live.R
import org.zq.live.commons.CommonContext
import org.zq.live.network.ServiceModel

class LoginActivity : AppCompatActivity() {
    companion object {
        val vercodeLiveData = MutableLiveData<Boolean>().apply { value = true }
        val vercodetimeLiveData = MutableLiveData<Int>()


    }

    val forCounter = object : CountDownTimer(60 * 1000, 1000) {
        override fun onFinish() {
            vercodeLiveData.value = true
        }

        override fun onTick(millisUntilFinished: Long) {
            // Log.e("woggle", "1")
            //CommonContext.application.toast("millisUntilFinished.toInt() / 1000")
            vercodetimeLiveData.value = millisUntilFinished.toInt() / 1000
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        CommonContext.registerContext(this)
        bt_login.setOnClickListener {


            //CommonContext.application.startActivity<HomeActivity>()
            //finish()
            if (code_input.text.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }

            ServiceModel.login(account_input.text.toString(), code_input.text.toString())
            //startActivity<HomeActivity>()
        }
        btn_send.setOnClickListener {
            //            modifyText {
//                ServiceModel.token = it
//            }
            if (account_input.text.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            forCounter.start()
            ServiceModel.send(account_input.text.toString())
            btn_send.isEnabled = false
        }
        vercodeLiveData.bindNonNull(this) {
            if (it) {
                btn_send.isEnabled = true
            }
        }
        vercodetimeLiveData.bindNonNull(this) {
            btn_send.text = "请等待($it)"
        }
    }
}
