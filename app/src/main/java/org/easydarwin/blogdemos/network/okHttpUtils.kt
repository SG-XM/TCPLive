package com.twt.zq.commons.common

import android.os.Build
import android.support.annotation.RequiresApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.easydarwin.blogdemos.App
import org.easydarwin.blogdemos.commons.CommonContext
import org.easydarwin.blogdemos.network.HttpLoggingInterceptor
import org.easydarwin.blogdemos.network.ServiceModel
import org.jetbrains.anko.toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by SGXM on 2018/7/27.
 */

//internal inline val Request.authorized
//    get() = if (header("Authorization") == null)
//        newBuilder().addHeader("Authorization", "bearer ${CommonPreferences.token}").build()
//    else this
//
//internal inline val Request.closed
//    get() = if (header("Connection") == null)
//        newBuilder().addHeader("Connection", "close").build()
//    else this

//object CookieInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val cookieHeader = StringBuilder()
//        CommonPreferences.cookies.forEach {
//            cookieHeader.append(it.name()).append('=').append(it.value()).append(";")
//        }
//        if (cookieHeader.isNotEmpty()) cookieHeader.delete(cookieHeader.length - 1, cookieHeader.length - 1)
//        return chain.proceed(chain.request().newBuilder().addHeader("Cookie", cookieHeader.toString()).build())
//    }
//}

//object CloseInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response =
//            chain.proceed(chain.request().closed)
//}

//object SaveCookieInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val res = chain.proceed(chain.request())
//        val headers = res.headers()
//        val cookieStrings = headers.values("Set-Cookie")
//        val cookies = mutableListOf<Cookie>()
//        cookieStrings.forEach {
//            it.split(";").forEach {
//                Cookie.parse(chain.request().url(), it)?.let {
//                    cookies.add(it)
//                }
//            }
//        }
//        CommonPreferences.cookies = CommonPreferences.cookies.apply { addAll(cookies) }
//        return res
//    }
//}

object AuthInterceptor : Interceptor {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().method() != "GET")
            return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Basic Y213OmNtdw==").build())
        else
            return chain.proceed(chain.request())
    }

}

object TokenInterceptor : Interceptor {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + ServiceModel.token).build())
    }

}

object ServiceFactory {
    private const val BASE_URL = "http://${App.SERVER_HOST}:8080/"
    //private const val BASE_URL = "http://47.92.141.153/"
    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            //.authenticator(RealAuthenticator)
//            .addNetworkInterceptor(CloseInterceptor)
            .addInterceptor(AuthInterceptor)
            .addInterceptor(TokenInterceptor)
            //.addNetworkInterceptor(ErrorPushInterceptor)
            //  .addInterceptor(CookieInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            //.addNetworkInterceptor(SaveCookieInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
//        .cookieJar(object : CookieJar {
//            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
//                CommonPreferences.cookies = cookies
//                for (cookie in cookies) {
////                    println("cookie Name:" + cookie.name())
////                    println("cookie Path:" + cookie.path())
//                }
//            }
//
//            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> = CommonPreferences.cookies.toMutableList()
//        }
            //)
            .build()!!

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()!!

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}

class ServerException(msg: String) : Throwable(msg)

data class CommonBody<out T>(
        val status: Int = 0,
        val msg: String? = "",
        val data: T?
)

data class AnyBody<out T>(
        val status: Boolean,
        val code: Int,
        val message: String,
        val data: T?
)

fun <T> CommonBody<T>.deal(callback: (T) -> Unit) {
    if (status == 0 && data != null) {
        callback(data)
    } else
        toast(msg ?: "无记录 code = ${status}")
}

fun <T> CommonBody<T>.dealOrNull(callback: (T?) -> Unit) {
    if (status == 0) {
        callback(data)
    } else {
        if (msg != null)
            toast(msg)
    }
}

fun toast(msg: String) = CommonContext.application.toast(msg)