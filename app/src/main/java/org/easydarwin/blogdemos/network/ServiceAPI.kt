package org.easydarwin.blogdemos.network

import com.twt.zq.commons.common.CommonBody
import com.twt.zq.commons.common.ServiceFactory
import com.twt.zq.commons.common.dealOrNull
import com.twt.zq.commons.common.toast
import com.twt.zq.commons.extentions.coroutineHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.easydarwin.blogdemos.HomeActivity
import org.easydarwin.blogdemos.commons.CommonContext
import org.jetbrains.anko.startActivity
import retrofit2.http.*

/**
 * Created by SGXM on 2020/3/24.
 */
interface ServiceAPI {

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("mobile") mobile: String, @Field("smsCode") smsCode: String): Deferred<CommonBody<Any>>

    @GET("/code/sms")
    fun sms(@Query("mobile") mobile: String): Deferred<CommonBody<Any>>

    companion object : ServiceAPI by ServiceFactory()
}

object ServiceModel {
    fun login(mobile: String, code: String) {
        GlobalScope.launch(Dispatchers.Main + coroutineHandler) {
            ServiceAPI.login(mobile, code).await().dealOrNull {
                toast("登录成功")
                CommonContext.application.startActivity<HomeActivity>()
            }
        }
    }

    fun send(mobile: String) {
        GlobalScope.launch(Dispatchers.Main + coroutineHandler) {
            ServiceAPI.sms(mobile).await().dealOrNull {
                toast("验证码已发送")
            }
        }
    }
}

data class LoginBean(
        val access_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val scope: String,
        val token_type: String
)