package com.github.rwsbillyang.wxSDK.accessToken



import com.github.rwsbillyang.wxSDK.KtorHttpClient
import com.github.rwsbillyang.wxSDK.WxException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 刷新器，刷新后得到某个值
 * */
interface IRefresher{
    /**
     * 刷新后返回新值
     * */
    fun refresh(): String
}

/**
 *  默认的的请求刷新器，如刷新获取 accessToken，ticket等
 *
 *  https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html
 *  https://work.weixin.qq.com/api/doc/90000/90135/91039
 *  https://work.weixin.qq.com/api/doc/90001/90142/90593
 *
 *  @param key 默认解析的值 如：access_token，ticket 等
 *  @param url 提供刷新的url
 *  @param urlBlock 返回url的函数
 * */
open class Refresher(
    private val key: String,
    private val url: String? = null,
    private val urlBlock: (() -> String)? = null): IRefresher
{
    companion object {
        private val log: Logger = LoggerFactory.getLogger("Refresher")
    }
    init {
        require(url != null || urlBlock != null){
            "one of url or urlBlock should not null"
        }
    }

    /**
     * 向远程发出请求，获取最新值然后返回
     */
    override  fun refresh(): String {
        val url2 = url?: urlBlock?.invoke()
        log.info("to refresh for key=$key...,url=$url2")

        return runBlocking {
            val text: String = getResponse(url2!!).readText()

            KtorHttpClient.apiJson
                .parseToJsonElement(text)
                .jsonObject[key]?.jsonPrimitive?.content
                ?: throw WxException("fail refresh key=suite_access_token, not a jsonObject: $text")
        }
    }
    open suspend fun getResponse(url: String) = KtorHttpClient.DefaultClient.get<HttpResponse>(url)
}

open class PostRefresher<T>(key: String,
                    private val postData: T? = null,
                    url: String? = null,
                    urlBlock: (() -> String)? = null)
:Refresher(key,url, urlBlock)
{
    override suspend fun getResponse(url: String) = KtorHttpClient.DefaultClient.post<HttpResponse>(url){
        postData?.let { body = it }
    }
}

/**
 *  请求刷新accessToken，如用于公众号、企业微信等获取accessToken
 * */
class AccessTokenRefresher(url: String): Refresher("access_token",url)

/**
 * 请求刷新Ticket，如用于公众号获取jsTicket
 * */
class TicketRefresher(url: () -> String): Refresher("ticket", null, url)
