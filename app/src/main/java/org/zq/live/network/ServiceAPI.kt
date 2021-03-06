package org.zq.live.network

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.twt.zq.commons.common.CommonBody
import com.twt.zq.commons.common.ServiceFactory
import com.twt.zq.commons.common.dealOrNull
import com.twt.zq.commons.common.toast
import com.twt.zq.commons.extentions.coroutineHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.jetbrains.anko.startActivity
import org.zq.live.HomeActivity
import org.zq.live.commons.CommonContext
import retrofit2.http.*

/**
 * Created by SGXM on 2020/3/24.
 */
interface ServiceAPI {

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("mobile") mobile: String, @Field("smsCode") smsCode: String): Deferred<CommonBody<LoginBean>>

    @GET("/code/sms")
    fun sms(@Query("mobile") mobile: String): Deferred<CommonBody<Any>>

    @GET("/room")
    fun room(): Deferred<CommonBody<MutableList<RoomBean>>>


    @POST("/room/cover")
    @Multipart
    fun cover(@Part file: MultipartBody.Part): Deferred<CommonBody<MutableList<RoomBean>>>

    companion object : ServiceAPI by ServiceFactory()
}

object ServiceModel {
    var token: String = "1ccc45bc-5404-4a70-9e6d-7dea10b9938b"
    val rooms = MutableLiveData<MutableList<RoomBean>>()
    fun login(mobile: String, code: String) {
        GlobalScope.launch(Dispatchers.Main + coroutineHandler) {
            ServiceAPI.login(mobile, code).await().dealOrNull {
                toast("登录成功")
                token = it!!.access_token!!
                CommonContext.application.startActivity<HomeActivity>()
            }
        }
    }

    fun cover(file: MultipartBody.Part) {
        GlobalScope.launch(Dispatchers.Main + coroutineHandler) {
            ServiceAPI.cover(file).await().dealOrNull {
                Log.e("woggle", "sss")
            }
        }
    }

    fun getRoom() {
        GlobalScope.launch(Dispatchers.Main + coroutineHandler) {
            ServiceAPI.room().await().dealOrNull {
                rooms.value = it
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


data class RoomBean(
        val active: Boolean,
        val cover: String,
        val createTime: String,
        val id: Int,
        val onlineUserCount: Int,
        val updateTime: String,
        val user: User
)

data class User(
        val authorities: Any,
        val createTime: String,
        val id: Int,
        val mobile: String,
        val updateTime: String,
        val username: String
)

data class LoginBean(
        val access_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val scope: String,
        val token_type: String
)