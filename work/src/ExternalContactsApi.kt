/*
 * Copyright © 2020 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2020-11-02 14:38
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rwsbillyang.wxSDK.work

import com.github.rwsbillyang.wxSDK.IBase
import com.github.rwsbillyang.wxSDK.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 企业内的员工可以添加外部企业的联系人进行工作沟通，外部联系人分为企业微信联系人和微信联系人两种类型。
 * 配置了客户联系功能的成员所添加的外部联系人为企业客户。
 *
 * 配置可使用客户联系功能的成员
 * 进入“客户联系”-“权限配置”-“使用范围”页面，管理员设置哪些人可使用联系客户功能，如未配置，则无法调用后文提到的相关接口。
 *
 * 配置可使用客户联系接口的应用
 * 管理员进入企业微信管理后台后点击“客户联系”-“客户”页面。点开“API”小按钮，可以看到secret，此为外部联系
 * 人secret。如需使用自建应用调用外部联系人相关的接口，需要在“可调用应用”中进行配置。
 *
 * 使用客户联系相关接口
 * 企业通过配置过权限的应用调用外部联系人选人相关接口，如：获取成员选择的外部联系人的userid以及通过
 * 该userid获取外部联系人详情。（ 外部联系人选人接口 | 获取外部联系人详情）
 *
 * 企业需要使用系统应用“客户联系”或配置到“可调用应用”列表中的自建应用的secret所获取的accesstoken来
 * 调用（accesstoken如何获取？）；第三方/自建应用调用时，返回的跟进人follow_user仅包含应用可见范围之
 * 内的成员。
 * */
class ExternalContactsApi(corpId: String?, agentId: String?, suiteId: String?)
    : WorkBaseApi(corpId, agentId,suiteId)
{

    override val group = "externalcontact"
    override var sysAccessTokenKey: String? = SysAgentKey.ExternalContact.name

    /* 企业服务人员（客服），「联系我」 */

    /**
     * 获取配置了客户联系功能的成员列表
     *
     * 销售或客服人员列表
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92571
     * */
    fun getFollowUserList() = doGetRaw("get_follow_user_list")


    /**
     * 配置客户联系「联系我」方式
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92227
     * */
    fun addContactWay(body: ContactWayConfig):ResponseAddContactWay = doPost("add_contact_way", body)

    /**
     * 获取企业已配置的「联系我」方式
     *
     * @param configId 来自addContactWay的返回结果中的值
     * */
    fun getContactWay(configId: String):ResponseGetContactWay = doPost("get_contact_way", mapOf("config_id" to configId))

    /**
     * 更新企业已配置的「联系我」方式
     *
     * 更新企业配置的「联系我」二维码和「联系我」小程序按钮中的信息，如使用人员和备注等。
     * */
    fun updateContactWay(body: ContactWayConfig): Response = doPost("update_contact_way", body)

    /**
     * 删除企业已配置的「联系我」方式
     * */
    fun delContactWay(configId: String): Response = doPost("del_contact_way", mapOf("config_id" to configId))


    /* 客户管理 */
    /**
     * 获取客户列表
     *
     * 企业可通过此接口获取指定成员添加的客户列表。客户是指配置了客户联系功能的成员所添加的外部联系人。
     * 没有配置客户联系功能的成员，所添加的外部联系人将不会作为客户返回。
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92113
     * */
    fun list(userId: String): ResponseCustomerList = doGet("list", mapOf("userid" to userId))

    /**
     * 获取客户详情
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92114
     * */
    fun detail(externalUserId: String):ResponseExternalContactDetail = doGet("get", mapOf("external_userid" to externalUserId))

    /**
     * 修改客户备注信息
     *
     * 企业可通过此接口修改指定用户添加的客户的备注信息。
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92115
     * */
    fun updateRemark(body: Map<String, Any?>) = doPostRaw("remark", body)

    /* 客户标签 */
    /**
     * 获取企业标签库
     *
     * 要查询的标签id，如果不填则获取该企业的所有客户标签，目前暂不支持标签组id
     * https://work.weixin.qq.com/api/doc/90000/90135/92117
     * */
    fun getCorpTagList(id: String?) = doPostRaw("get_corp_tag_list", mapOf("tag_id" to id))

    /**
     * 添加企业客户标签
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92117
     * */
    fun addCorpTag(body: Map<String, Any?>) = doPostRaw("add_corp_tag", body)

    /**
     * 编辑企业客户标签
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92117
     * */
    fun editCorpTag(id: String, name: String?, order: Int?) = doPostRaw(
            "edit_corp_tag",
            mapOf("id" to id, "name" to name, "order" to order))

    /**
     * 删除企业客户标签
     *
     * 企业可通过此接口删除客户标签库中的标签，或删除整个标签组。
     *
     * tag_id和group_id不可同时为空。
     * 如果一个标签组下所有的标签均被删除，则标签组会被自动删除。
     * https://work.weixin.qq.com/api/doc/90000/90135/92117
     * */
    fun delCorpTag(tagIds: List<String>?, groupIds: List<String>?) = doPostRaw(
            "del_corp_tag",
            mapOf("tag_id" to tagIds, "group_id" to groupIds))


    /**
     * 编辑客户企业标签
     *
     * 企业可通过此接口为指定成员的客户添加上由企业统一配置的标签。
     * https://work.weixin.qq.com/api/doc/90000/90135/92118
     *
     * userid	是	添加外部联系人的userid
     * external_userid	是	外部联系人userid
     * add_tag	否	要标记的标签列表
     * remove_tag	否	要移除的标签列表
     *
     * add_tag和remove_tag不可同时为空。
     * */
    fun markTag(userId: String, externalUserId: String, addTag: List<String>?, removeTag: List<String>?) = doPostRaw(
            "mark_tag",
            mapOf("userid" to userId, "external_userid" to externalUserId, "add_tag" to addTag, "remove_tag" to removeTag))


    /* 客户群 */

    /**
     * 获取客户群列表
     *
     * 用于获取配置过客户群管理的客户群列表
     * https://work.weixin.qq.com/api/doc/90000/90135/92119
     * */
    fun getGroupChatList(body: Map<String, Any?>) = doPostRaw("groupchat/list", body)


    /**
     * 获取客户群详情
     *
     * 通过客户群ID，获取详情。包括群名、群成员列表、群成员入群时间、入群方式。（客户群是由具有客户群使用权限的成员创建的外部群）
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92119
     *
     * @param chatId 客户群ID
     * */
    fun getGroupChatDetail(chatId: String) = doPostRaw("groupchat/get", mapOf("chat_id" to chatId))


    /* 消息推送 */

    /**
     * 添加企业群发消息任务
     *
     * 企业可通过此接口添加企业群发消息的任务并通知客服人员发送给相关客户。（注：企业微信终端需升级到2.7.5版本及以上）
     * 注意：调用该接口并不会直接发送消息给客户，需要相关的客服人员操作以后才会实际发送（客服人员的企业微信需要升级到2.7.5及以上版本）
     * 同一个企业对一个客户一个自然周内（周一至周日）至多只能发送一条消息，超过限制的用户将会被忽略。
     *
     *  https://work.weixin.qq.com/api/doc/90000/90135/92135
     * */
    fun sendMsg(body: Map<String, Any?>) = doPostRaw("add_msg_template", body)

    /**
     * 获取企业群发消息发送结果
     *
     * msgId来自sendMsg的返回结果
     * */
    fun getMsgSendResult(msgId: String) = doPostRaw("get_group_msg_result", mapOf("msgid" to msgId))

    /**
     * 发送新客户欢迎语
     *
     * 企业微信在向企业推送添加外部联系人事件时，会额外返回一个welcome_code，企业以此为凭据调用接口，即可通过成员向新添加的客户发送个性化的欢迎语。
     * 为了保证用户体验以及避免滥用，企业仅可在收到相关事件后20秒内调用，且只可调用一次。
     * 如果企业已经在管理端为相关成员配置了可用的欢迎语，则推送添加外部联系人事件时不会返回welcome_code。
     * 每次添加新客户时可能有多个企业自建应用/第三方应用收到带有welcome_code的回调事件，但仅有最先调用的可以发送成功。后续调用将返回41051（externaluser has started chatting）错误，请用户根据实际使用需求，合理设置应用可见范围，避免冲突。
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92137
     * */
    fun sendWelcomeMsg(body: Map<String, Any?>) = doPostRaw("send_welcome_msg", body)

    /**
     * 群欢迎语素材管理
     * https://work.weixin.qq.com/api/doc/90000/90135/92366
     * */
    fun addWelcome(body: Map<String, Any?>) = doPostRaw("group_welcome_template/add", body)
    fun editWelcome(body: Map<String, Any?>) = doPostRaw("group_welcome_template/edit", body)
    fun getWelcome(templateId: String) = doPostRaw("group_welcome_template/get", mapOf("template_id" to templateId))
    fun delWelcome(templateId: String) = doPostRaw("group_welcome_template/del", mapOf("template_id" to templateId))


    /* 离职管理 */

    /**
     * 获取离职成员的客户列表
     *
     * page_id	否	分页查询，要查询页号，从0开始
     * page_size	否	每次返回的最大记录数，默认为1000，最大值为1000
     * 注意:返回数据按离职时间的降序排列，当page_id为1，page_size为100时，表示取第101到第200条记录
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92124
     * */
    fun getLeaveList(page: Int? = null, pageSize: Int? = null) = doPostRaw(
            "get_unassigned_list",
            mapOf("page_id" to page, "page_size" to pageSize))

    /**
     * 离职成员的外部联系人再分配
     *
     *  external_userid	是	外部联系人的userid，注意不是企业成员的帐号
     *  handover_userid	是	离职成员的userid
     *  takeover_userid	是	接替成员的userid
     * */
    fun transferExternalUsers(externalUserId: String, handoverUserId: String, takeoverUserId: String) = doPostRaw(
            "transfer", mapOf("external_userid" to externalUserId, "handover_userid" to handoverUserId, "takeover_userid" to takeoverUserId))

    /**
     * 离职成员的群再分配
     *
     * chat_id_list	是	需要转群主的客户群ID列表。取值范围： 1 ~ 100
     * new_owner	是	新群主ID
     * 群主离职了的客户群，才可继承
     * 继承给的新群主，必须是配置了客户联系功能的成员
     * 继承给的新群主，必须有设置实名
     * 继承给的新群主，必须有激活企业微信
     * */
    fun transferGroupChat(chatIdList: List<String>, newOwner: String) = doPostRaw(
            "groupchat/transfer",
            mapOf("chat_id_list" to chatIdList, "new_owner" to newOwner))


    /* 统计管理 */

    /**
     * 获取联系客户统计数据
     *
     * 获取成员联系客户的数据，包括发起申请数、新增客户数、聊天数、发送消息数和删除/拉黑成员的客户数等指标。
     *
     * userid	否	用户ID列表
     * partyid	否	部门ID列表
     * start_time	是	数据起始时间
     * end_time	是	数据结束时间
     * userid和partyid不可同时为空;
     * 此接口提供的数据以天为维度，查询的时间范围为[start_time,end_time]，即前后均为闭区间，支持的最大查询跨度为30天；
     * 用户最多可获取最近60天内的数据；
     * 当传入的时间不为0点时间戳时，会向下取整，如传入1554296400(Wed Apr 3 21:00:00 CST 2019)会被自动转换为1554220800（Wed Apr 3 00:00:00 CST 2019）;
     * 如传入多个userid，则表示获取这些成员总体的联系客户数据。
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92132
     * */
    fun getStatUserBehaviour(startTime: Long, endTime: Long, userIds: List<String>?, partIds: List<String>?) = doPostRaw("get_user_behavior_data", mapOf("start_time" to startTime, "end_time" to endTime, "userid" to userIds, "partyid" to partIds))


    /**
     * 获取客户群统计数据
     * 获取指定日期全天的统计数据。注意，企业微信仅存储60天的数据。
     *
     * https://work.weixin.qq.com/api/doc/90000/90135/92133
     * */
    fun getStatGroupChat(body: Map<String, Any?>) = doPostRaw("groupchat/statistic", body)

    /**
     * 暂不支持企业微信外部联系人（ExternalUserid为wo开头）的userid转openid。
     * */
    fun convertToOpenId(userId: String): ResponseToOpenId = doPost("convert_to_openid", mapOf("external_userid" to userId))

}


@Serializable
class ContactWayConfig(
    val type: Int, //联系方式类型,1-单人, 2-多人  不给默认值是为了适应默认值不序列化的配置
    val scene: Int, //场景，1-在小程序中联系，2-通过二维码联系
    val style: Int = 1, //在小程序中联系时使用的控件样式，详见附表 https://work.weixin.qq.com/api/doc/90000/90135/92572
    val remark: String? = null, //联系方式的备注信息，用于助记，不超过30个字符
    val skip_verify: Boolean = true, //外部客户添加时是否无需验证，默认为true
    val state: String? = null, //企业自定义的state参数，用于区分不同的添加渠道，在调用“获取外部联系人详情”时会返回该参数值，不超过30个字符
    val user: List<String>, //使用该联系方式的用户userID列表，在type为1时为必填，且只能有一个
    val party: List<Int>? = null, //使用该联系方式的部门id列表，只在type为2时有效
    val is_temp: Boolean = false, //是否临时会话模式，true表示使用临时会话模式，默认为false  当设置为临时会话模式时（即is_temp为true），联系人仅支持配置为单人，暂不支持多人
    val expires_in: Int? = null, //临时会话二维码有效期，以秒为单位。该参数仅在is_temp为true时有效，默认7天
    val chat_expires_in: Int? = null, //临时会话有效期，以秒为单位。该参数仅在is_temp为true时有效，默认为添加好友后24小时
    val unionid: String? = null, //可进行临时会话的客户unionid，该参数仅在is_temp为true时有效，如不指定则不进行限制 使用unionid需要调用方（企业或服务商）的企业微信“客户联系”中已绑定微信开发者账户
    //val conclusions: Conclusions? = null //TODO:结束语，会话结束时自动发送给客户，可参考“结束语定义”，仅在is_temp为true时有效
    val config_id: String? = null //用于 get_contact_way中请求结果、update_contact_way中的请求参数
)

@Serializable
class ResponseAddContactWay(
    @SerialName("errcode")
    override val errCode: Int = 0,
    @SerialName("errmsg")
    override val errMsg: String? = null,
    @SerialName("config_id")
    val configId: String? = null,
    @SerialName("qr_code")
    val qrCode: String? = null
): IBase

@Serializable
class ResponseGetContactWay(
    @SerialName("errcode")
    override val errCode: Int = 0,
    @SerialName("errmsg")
    override val errMsg: String? = null,
    @SerialName("contact_way")
    val contactWayConfig: ContactWayConfig? = null
): IBase

@Serializable
class ResponseExternalContactDetail(
        @SerialName("errcode")
        override val errCode: Int = 0,
        @SerialName("errmsg")
        override val errMsg: String? = null,
        val external_contact: ExternalContact? = null,
        val follow_user: List<FollowUser>? = null
): IBase
/**
 * 外部联系人
* */
@Serializable
class ExternalContact(
        val external_userid: String,//外部联系人的userid
        val name: String? = null,//如果是微信用户，则返回其微信昵称。如果是企业微信联系人，则返回其设置对外展示的别名或实名
        val avatar: String? = null,//外部联系人头像，代开发自建应用需要管理员授权才可以获取，第三方不可获取
        val type: Int, //外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
        val gender: Int, //外部联系人性别 0-未知 1-男性 2-女性
        val unionid: String? = null,//外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。仅当联系人类型是微信用户，且企业或第三方服务商绑定了微信开发者ID有此字段：https://work.weixin.qq.com/api/doc/90000/90135/92114
        val position: String? = null,//外部联系人的职位，如果外部企业或用户选择隐藏职位，则不返回，仅当联系人类型是企业微信用户时有此字段
        val corp_name: String? = null,//外部联系人所在企业的简称，仅当联系人类型是企业微信用户时有此字段
        val corp_full_name: String? = null,//外部联系人所在企业的主体名称，仅当联系人类型是企业微信用户时有此字段
        val external_profile: ExternalProfile? = null//外部联系人的自定义展示信息，可以有多个字段和多种类型，包括文本，网页和小程序，仅当联系人类型是企业微信用户时有此字段

)


@Serializable
class FollowUser(
        val userid: String, //添加了此外部联系人的企业成员userid
        val remark: String? = null, //该成员对此外部联系人的备注
        val description: String? = null,//该成员对此外部联系人的描述
        val createtime: Long? = null,//该成员添加此外部联系人的时间
        val tags: List<TagInfo>? = null, //标签信息
        val remark_corp_name: String? = null,//该成员对此客户备注的企业名称
        val remark_mobiles: List<String>? = null,//该成员对此客户备注的手机号码，代开发自建应用需要管理员授权才可以获取，第三方不可获取
        val add_way: Int,//该成员添加此客户的来源
        val oper_userid: String? = null,//发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid
        val state: String? = null,//企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定
        val next_cursor: String? = null//分页的cursor，当跟进人多于500人时返回
)


@Serializable
class TagInfo(
        val group_name: String? = null,//该成员添加此外部联系人所打标签的分组名称（标签功能需要企业微信升级到2.7.5及以上版本）
        val tag_name: String,//该成员添加此外部联系人所打标签名称
        val type: Int,//该成员添加此外部联系人所打标签类型, 1-企业设置，2-用户自定义，3-规则组标签（仅系统应用返回）
        val tag_id: String? = null //该成员添加此外部联系人所打企业标签的id，用户自定义类型标签（type=2）不返回
)



@Serializable
class ResponseCustomerList(
        @SerialName("errcode")
        override val errCode: Int = 0,
        @SerialName("errmsg")
        override val errMsg: String? = null,
        val external_userid: List<String>? = null
): IBase