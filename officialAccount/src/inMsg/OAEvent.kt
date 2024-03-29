package com.github.rwsbillyang.wxSDK.officialAccount.inMsg

import com.github.rwsbillyang.wxSDK.msg.SendPicsInfo
import com.github.rwsbillyang.wxSDK.msg.WxXmlEvent
import org.w3c.dom.Element



/**
 * 关注事件
 *
 * 用户在关注与取消关注公众号时，微信会把这个事件推送到开发者填写的URL。
 * 方便开发者给用户下发欢迎消息或者做帐号的解绑。为保护用户数据隐私，开发者收到用户取消关注事件时需要删除该用户的所有信息。微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
 * 关于重试的消息排重，推荐使用FromUserName + CreateTime 排重。
 * 假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
 * */
class OASubscribeEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)

/**
 * 取消关注事件
 *
 * 用户在关注与取消关注公众号时，微信会把这个事件推送到开发者填写的URL。
 * 方便开发者给用户下发欢迎消息或者做帐号的解绑。为保护用户数据隐私，开发者收到用户取消关注事件时需要删除该用户的所有信息。微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
 * 关于重试的消息排重，推荐使用FromUserName + CreateTime 排重。
 * 假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
 * */
class OAUnsubscribeEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)


/**
 * 用户未关注时，扫码关注后的事件推送
 *
 * @property eventKey EventKey	事件KEY值，qrscene_为前缀，后面为二维码的参数值
 * @property ticket Ticket	二维码的ticket，可用来换取二维码图片
 * */
open class OAScanSubscribeEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
    val ticket = get(rootDom, "Ticket")
}
/**
 * 用户已关注时，扫码关注后的事件推送
 *
 * @property eventKey EventKey	事件KEY值，事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
 * @property ticket Ticket	二维码的ticket，可用来换取二维码图片
 * */
class OAScanEvent(xml: String, rootDom: Element): OAScanSubscribeEvent(xml, rootDom)



/**
 * 上报地理位置事件
 *
 * 用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，或在进入会话后每5秒上报一次地理位置，
 * 公众号可以在公众平台网站中修改以上设置。上报地理位置时，微信会将上报地理位置事件推送到开发者填写的URL。
 *
 * @property latitude Latitude	地理位置纬度
 * @property longitude    地理位置经度
 * @property precision Precision	地理位置精度
 * */
open class OALocationEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val latitude = get(rootDom, "Latitude")?.toFloat()
    val longitude = get(rootDom, "Longitude")?.toFloat()
    val precision = get(rootDom, "Precision")?.toFloat()
}


/**
 * 点击菜单拉取消息时的事件推送
 *
 * 用户点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报。
 *
 * @property eventKey EventKey 事件KEY值，与自定义菜单接口中KEY值对应
 * */
class OAMenuClickEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
}

/**
 * 点击菜单跳转链接时的事件推送
 *
 * @property eventKey EventKey	事件KEY值，设置的跳转URL
 * @property menuId MenuID	指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 * */
class OAMenuViewEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
    val menuId = get(rootDom, "MenuId")
}
/**
 * scancode_push：扫码推事件的事件推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 *
 * @property eventKey EventKey	事件KEY值，由开发者在创建菜单时设定
 * @property scanType ScanType	扫描类型，一般是qrcode
 * @property scanResult ScanResult	扫描结果，即二维码对应的字符串信息
 * */
open class OAMenuScanCodePushEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
    val scanType = get(rootDom, "ScanType")
    val scanResult = get(rootDom, "ScanResult")
}

/**
 * scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框的事件推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 * */
class OAMenuScanCodeWaitEvent(xml: String, rootDom: Element): OAMenuScanCodePushEvent(xml, rootDom)



/**
 * pic_sysphoto：弹出系统拍照发图的事件推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 *
 * @property eventKey EventKey	事件KEY值，由开发者在创建菜单时设定
 * @property picsInfo SendPicsInfo 图片的MD5值，开发者若需要，可用于验证接收到图片
 * */
open class OAMenuPhotoEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
    val sendPicsInfo = SendPicsInfo.fromXml(getChild(rootDom, "SendPicsInfo"))
}
/**
 * pic_photo_or_album：弹出拍照或者相册发图的事件推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 *
 * */
class OAMenuPhotoOrAlbumEvent(xml: String, rootDom: Element): OAMenuPhotoEvent(xml, rootDom)


/**
 * pic_weixin：弹出微信相册发图器的事件推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html
 *
 * */
class OAMenuOAAlbumEvent(xml: String, rootDom: Element): OAMenuPhotoEvent(xml, rootDom)


/**
 * location_select：弹出地理位置选择器的事件推送
 *
 * @property eventKey EventKey	事件KEY值，由开发者在创建菜单时设定
 * @property locationX Location_X	X坐标信息
 * @property locationY Location_Y	Y坐标信息
 * @property scale Scale	精度，可理解为精度或者比例尺、越精细的话 scale越高
 * @property label Label	地理位置的字符串信息
 * @property poiname Poiname	朋友圈POI的名字，可能为空
 * */
class OAMenuLocationEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    /*
       * <EventKey><![CDATA[6]]></EventKey>
           <SendLocationInfo><Location_X><![CDATA[23]]></Location_X>
           <Location_Y><![CDATA[113]]></Location_Y>
           <Scale><![CDATA[15]]></Scale>
           <Label><![CDATA[ 广州市海珠区客村艺苑路 106号]]></Label>
           <Poiname><![CDATA[]]></Poiname>
           </SendLocationInfo>
       * */
    val eventKey = get(rootDom, "EventKey")
    val locationX = get(rootDom, "Location_X")?.toFloat()
    val locationY = get(rootDom, "Location_Y")?.toFloat()
    val scale = get(rootDom, "Scale")?.toInt()
    val label = get(rootDom, "Label")
    val poiname = get(rootDom, "Poiname")
}


/**
 * 点击菜单跳转小程序的事件推送
 *
 * @param eventKey EventKey	事件KEY值，跳转的小程序路径
 * @param menuId MenuID	菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了
 * */
class OAMenuMiniEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val eventKey = get(rootDom, "EventKey")
    val menuId = get(rootDom, "MenuId")
}



/**
 * 模版消息发送结果事件
 *
 * 在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
 *
 * @property status success 送达成功;
 * failed:user block: 送达由于用户拒收（用户设置拒绝接收公众号消息）;
 * failed: system failed: 发送状态为发送失败（非用户拒绝）
 * */
class OATemplateSendJobFinish(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val status = get(rootDom, "Status")
}


/**
 * 群发结果通知推送
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Batch_Sends_and_Originality_Checks.html
 *
 * 在公众平台网站上，为订阅号提供了每天一条的群发权限，为服务号提供每月（自然月）4条的群发权限。而对于某些具备开发能力的公众号运营者，
 * 可以通过高级群发接口，实现更灵活的群发能力。
 *
 * 由于群发任务提交后，群发任务可能在一定时间后才完成，因此，群发接口调用时，仅会给出群发任务是否提交成功的提示，若群发任务提交成功，
 * 则在群发任务结束时，会向开发者在公众平台填写的开发者URL（callback URL）推送事件。需要注意，由于群发任务彻底完成需要较长时间，
 * 将会在群发任务即将完成的时候，就推送群发结果，此时的推送人数数据将会与实际情形存在一定误差
 *
 *
 * @property status Status	群发的结果，为“send success”或“send fail”或“err(num)”。但send success时，也有可能因用户拒收公众号的消息、系统错误等原因造成
 * 少量用户接收失败。err(num)是审核失败的具体原因，可能的情况如下：err(10001):涉嫌广告, err(20001):涉嫌政治, err(20004):涉嫌社会,
 * err(20002):涉嫌色情, err(20006):涉嫌违法犯罪, err(20008):涉嫌欺诈, err(20013):涉嫌版权, err(22000):涉嫌互推(互相宣传),
 * err(21000):涉嫌其他, err(30001):原创校验出现系统错误且用户选择了被判为转载就不群发, err(30002): 原创校验被判定为不能群发,
 * err(30003): 原创校验被判定为转载文且用户选择了被判为转载就不群发, err(40001)：管理员拒绝, err(40002)：管理员30分钟内无响应，超时
 *
 * @property totalCount TotalCount	tag_id下粉丝数；或者openid_list中的粉丝数
 * @property filterCount FilterCount	过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，
 * 原则上，FilterCount 约等于 SentCount + ErrorCount
 * @property sentCount SentCount	发送成功的粉丝数
 * @property errorCount ErrorCount	发送失败的粉丝数
 * @property copyrightCheckResult
 * @property articleUrlResult
 *
 * 群发图文消息的过程如下：
 * 首先，预先将图文消息中需要用到的图片，使用上传图文消息内图片接口，上传成功并获得图片 URL；
 * 上传图文消息素材，需要用到图片时，请使用上一步获取的图片 URL；
 * 使用对用户标签的群发，或对 OpenID 列表的群发，将图文消息群发出去，群发时微信会进行原创校验，并返回群发操作结果；
 * 在上述过程中，如果需要，还可以预览图文消息、查询群发状态，或删除已群发的消息等。
 *
 * 群发图片、文本等其他消息类型的过程如下：
 * 如果是群发文本消息，则直接根据下面的接口说明进行群发即可；
 * 如果是群发图片、视频等消息，则需要预先通过素材管理接口准备好 mediaID。
 * 关于群发时使用is_to_all为true使其进入公众号在微信客户端的历史消息列表：
 *
 * 使用is_to_all为true且成功群发，会使得此次群发进入历史消息列表。
 * 为防止异常，认证订阅号在一天内，只能使用is_to_all为true进行群发一次，或者在公众平台官网群发（不管本次群发是对全体还是对某个分组）一次。
 * 以避免一天内有2条群发进入历史消息列表。
 * 类似地，服务号在一个月内，使用is_to_all为true群发的次数，加上公众平台官网群发（不管本次群发是对全体还是对某个分组）的次数，最多只能是4次。
 * 设置is_to_all为false时是可以多次群发的，但每个用户只会收到最多4条，且这些群发不会进入历史消息列表。
 * 另外，请开发者注意，本接口中所有使用到media_id的地方，现在都可以使用素材管理中的永久素材media_id了。请但注意，使用同一个素材群发出去的
 * 链接是一样的，这意味着，删除某一次群发，会导致整个链接失效。
 *
    <MsgID>1000001625</MsgID>
    <Status><![CDATA[err(30003)]]></Status>
    <TotalCount>0</TotalCount>
    <FilterCount>0</FilterCount>
    <SentCount>0</SentCount>
    <ErrorCount>0</ErrorCount>
    <CopyrightCheckResult>
    <Count>1</Count>
    <ResultList>
    <item>
    <ArticleIdx>1</ArticleIdx>
    <UserDeclareState>0</UserDeclareState>
    <AuditState>2</AuditState>
    <OriginalArticleUrl><![CDATA[Url_1]]></OriginalArticleUrl>
    <OriginalArticleType>1</OriginalArticleType>
    <CanReprint>1</CanReprint>
    <NeedReplaceContent>1</NeedReplaceContent>
    <NeedShowReprintSource>1</NeedShowReprintSource>
    </item>
    </ResultList>
    <CheckState>2</CheckState>
    </CopyrightCheckResult>
    <ArticleUrlResult>
    <Count>1</Count>
    <ResultList>
    <item>
    <ArticleIdx>1</ArticleIdx>
    <ArticleUrl><![CDATA[Url]]></ArticleUrl>
    </item>
    </ResultList>
    </ArticleUrlResult>
 * */
class OAMassSendFinishEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val msgId = get(rootDom, "MsgID")
    val status = get(rootDom, "Status")
    val totalCount = get(rootDom, "TotalCount")
    val filterCount = get(rootDom, "FilterCount")
    val sentCount = get(rootDom, "SentCount")
    val errorCount = get(rootDom, "ErrorCount")
//    val copyrightCheckResult: CopyrightCheckResult? = null
//    val articleUrlResult: ArticleUrlResult? = null
    
//    private fun parseCopyrightCheckResult(reader: XMLEventReader): CopyrightCheckResult {
//        val count:Int? = null
//        val checkState: Int? = null
//        val list = mutableListOf<CopyrightResult>()
//        while (reader.hasNext()) {
//            val event = reader.nextEvent()
//            if (event.isStartElement) {
//                when(event.asStartElement().name.toString()){
//                    "Count" -> count = reader.elementText?.toInt()
//                    "CheckState" -> checkState = reader.elementText?.toInt()
//                    "ResultList" -> {
//                        while (reader.hasNext()) {
//                            val event1 = reader.nextEvent()
//                            if (event1.isStartElement && "item" == event1.asStartElement().name.toString())
//                            {
//                                val map = mutableMapOf<String, String?>()
//                                while (reader.hasNext()){
//                                    val event2 = reader.nextEvent()
//                                    val tag = event1.asStartElement().name.toString()
//                                    map[tag] = reader.elementText
//                                    if(event2.isEndElement && "item" == event2.asEndElement().name.toString()) {
//                                        break
//                                    }
//                                }
//
//                                list.add(
//                                    CopyrightResult(
//                                        map["ArticleIdx"]?.toInt(),
//                                        map["UserDeclareState"]?.toInt(),
//                                        map["AuditState"]?.toInt(),
//                                        map["OriginalArticleUrl"],
//                                        map["OriginalArticleType"]?.toInt(),
//                                        map["CanReprint"]?.toInt(),
//                                        map["NeedReplaceContent"]?.toInt(),
//                                        map["NeedShowReprintSource"]?.toInt(),
//                                    )
//                                )
//                            } else if (event1.isEndElement && "ResultList" == event1.asEndElement().name.toString()) {
//                                break
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//        return CopyrightCheckResult(count, checkState, list)
//    }

    /**
     * <ArticleUrlResult>
    <Count>1</Count>
    <ResultList>
    <item>
    <ArticleIdx>1</ArticleIdx>
    <ArticleUrl><![CDATA[Url]]></ArticleUrl>
    </item>
    </ResultList>
    </ArticleUrlResult>
     * */
//    private fun parseArticleUrlResult(reader: XMLEventReader): ArticleUrlResult {
//        val count:Int? = null
//        val list = mutableListOf<ArticleUrl>()
//        while (reader.hasNext()) {
//            val event = reader.nextEvent()
//            if (event.isStartElement) {
//                when(event.asStartElement().name.toString()){
//                    "Count" -> count = reader.elementText?.toInt()
//                    "ResultList" -> {
//                        while (reader.hasNext()) {
//                            val event1 = reader.nextEvent()
//                            if (event1.isStartElement && "item" == event1.asStartElement().name.toString())
//                            {
//                                val map = mutableMapOf<String, String?>()
//                                while (reader.hasNext()){
//                                    val event2 = reader.nextEvent()
//                                    val tag = event1.asStartElement().name.toString()
//                                    map[tag] = reader.elementText
//                                    if(event2.isEndElement && "item" == event2.asEndElement().name.toString()) {
//                                        break
//                                    }
//                                }
//                                list.add( ArticleUrl( map["ArticleIdx"]?.toInt(),  map["ArticleUrl"]) )
//                            } else if (event1.isEndElement && "ResultList" == event1.asEndElement().name.toString()) {
//                                break
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//        return ArticleUrlResult(count,  list)
//    }
}

/**
 *
 * @param articleIdx ArticleIdx	群发文章的序号，从1开始
 * @param userDeclareState UserDeclareState	用户声明文章的状态
 * @param auditState AuditState	系统校验的状态
 * @param originalArticleUrl OriginalArticleUrl	相似原创文的url
 * @param originalArticleType OriginalArticleType	相似原创文的类型
 * @param canReprint CanReprint	是否能转载
 * @param needReplaceContent NeedReplaceContent	是否需要替换成原创文内容
 * @param needShowReprintSource NeedShowReprintSource	是否需要注明转载来源

<ArticleIdx>1</ArticleIdx>
<UserDeclareState>0</UserDeclareState>
<AuditState>2</AuditState>
<OriginalArticleUrl><![CDATA[Url_1]]></OriginalArticleUrl>
<OriginalArticleType>1</OriginalArticleType>
<CanReprint>1</CanReprint>
<NeedReplaceContent>1</NeedReplaceContent>
<NeedShowReprintSource>1</NeedShowReprintSource>
 * */
class CopyrightResult(
    val articleIdx: Int?,
    val userDeclareState: Int?,
    val auditState: Int?,
    val originalArticleUrl: String?,
    val originalArticleType: Int?,
    val canReprint: Int?,
    val needReplaceContent: Int?,
    val needShowReprintSource: Int?
)

/**
 * 原创校验结果和群发图文的url
 * @param checkState 整体校验结果
 * @param resultList 各个单图文校验结果
<CopyrightCheckResult>
<Count>1</Count>
<ResultList>
<item>
<ArticleIdx>1</ArticleIdx>
<UserDeclareState>0</UserDeclareState>
<AuditState>2</AuditState>
<OriginalArticleUrl><![CDATA[Url_1]]></OriginalArticleUrl>
<OriginalArticleType>1</OriginalArticleType>
<CanReprint>1</CanReprint>
<NeedReplaceContent>1</NeedReplaceContent>
<NeedShowReprintSource>1</NeedShowReprintSource>
</item>
</ResultList>
<CheckState>2</CheckState>
</CopyrightCheckResult>
 * */
class CopyrightCheckResult(count: Int?, checkState: Int?, resultList: List<CopyrightResult>?)

class ArticleUrl(id: Int?, url: String?)

/**
 * <ArticleUrlResult>
<Count>1</Count>
<ResultList>
<item>
<ArticleIdx>1</ArticleIdx>
<ArticleUrl><![CDATA[Url]]></ArticleUrl>
</item>
</ResultList>
</ArticleUrlResult>
 * */
class ArticleUrlResult(count: Int?, resultList: List<ArticleUrl>?)