package com.github.rwsbillyang.wxSDK.msg

import com.github.rwsbillyang.wxSDK.security.AesException
import com.github.rwsbillyang.wxSDK.security.WXBizMsgCrypt
import com.github.rwsbillyang.wxSDK.security.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory

/**
 * xml消息事件通知的解包、解析、分发处理,xml消息为腾讯post过来的body，重点处理Encrypt中的内容
 * */
abstract class WxMsgHub(private val wxBizMsgCrypt: WXBizMsgCrypt?)
{
    companion object{
        val log: Logger = LoggerFactory.getLogger("WxMsgHub")
    }
    /**
     * @param appId 公众号appId或企业微信中的corpId
     * */
    fun handleXmlMsg(
        appId: String, //公众号通过路径传入appId，企业微信为corpId or suiteId
        agentId: Int?, //企业微信内建应用
        body: String,
        msgSignature: String,
        timestamp: String,
        nonce: String,
        encryptType: String?
    ): String?
    {
        //log.debug("Receive post data: $body")
        try {
            var map: Map<String, String?> = mapOf()

            val decryptedXmlText = if(wxBizMsgCrypt == null) {
                log.info("wxBizMsgCrypt is null, appId=$appId, agentId=$agentId")
                body
            }else{
                map = XmlUtil.extract(body)//body转换成的map数据结构
                // 提取密文
                val encryptText = map["Encrypt"]?:""//提取body中的Encrypt字段
               // corpId = appId?:map["ToUserName"]
                wxBizMsgCrypt.decryptWxMsg(appId, msgSignature, timestamp, nonce, encryptText, encryptType)
            }

            log.info("after decrypt: $decryptedXmlText")


           val reMsg = parseXml(appId, agentId, decryptedXmlText, map)


            return reMsg?.toXml()?.let { wxBizMsgCrypt?.encryptMsg(appId, it)?.first }

        }catch (e: AesException){
            log.warn("AesException: ${e.localizedMessage}, agentId=$agentId,  appId=$appId, msgSignature=$msgSignature,  timestamp=$timestamp, nonce=$nonce, body=$body")
        }catch (e: Exception){
            log.warn("Exception: ${e.localizedMessage}")
        }

        return null
    }

    /**
     * @param decryptedXmlText 由外层xml中的Encrypt字段解析出来的新xml字段
     * @param outerMap 外层xml得到的map数据,即body直接转换成的map数据
     * */
    protected open fun parseXml(appId: String, agentId: Int?, decryptedXmlText: String, outerMap: Map<String, String?>): ReBaseMSg?{
        val reader: XMLEventReader = XMLInputFactory.newInstance().createXMLEventReader(decryptedXmlText.byteInputStream())

        val base = BaseInfo.fromXml(decryptedXmlText, reader)

        val reMsg = when(base.msgType){
            MsgType.EVENT -> dispatchEvent(appId, agentId, reader,base)
            else -> dispatchMsg(appId, agentId, reader,base)
        }
        reader.close()
        return reMsg
    }



    abstract fun dispatchMsg(appId: String, agentId: Int?, reader: XMLEventReader, base: BaseInfo): ReBaseMSg?

    abstract fun dispatchEvent(appId: String, agentId: Int?, reader: XMLEventReader, base: BaseInfo): ReBaseMSg?
}