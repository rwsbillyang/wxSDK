/*
 * Copyright © 2021 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2021-01-26 15:01
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

package com.github.rwsbillyang.wxOA

import com.github.rwsbillyang.wxOA.fan.FanService
import com.github.rwsbillyang.wxOA.fan.toFan
import com.github.rwsbillyang.wxOA.msg.MsgService
import com.github.rwsbillyang.wxOA.msg.MyMsg
import com.github.rwsbillyang.wxOA.pref.PrefService
import com.github.rwsbillyang.wxSDK.msg.*
import com.github.rwsbillyang.wxSDK.officialAccount.UserApi
import com.github.rwsbillyang.wxSDK.officialAccount.outMsg.ReMusicMsg
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class ReMsgChooser: KoinComponent {
    private val log = LoggerFactory.getLogger("ReMsgChooser")

    private val fanService: FanService by inject()
    private val prefService: PrefService by inject()
    private val msgService: MsgService by inject()


    /**
     * 配置好回复消息后，此处将使用配置的消息进行回复
     *
     * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html
     * */
    fun tryReMsg(appId: String, e: WxXmlMsg) = tryReMsg(appId, e.msgType, "defaultMsg", e.fromUserName, e.toUserName)


    /**
     * 配置好回复消息后，此处将使用配置的消息进行回复
     *
     * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html
     * */
    fun tryReMsg(appId: String, e: WxXmlEvent, upsertUserInfo: Boolean = false) = tryReMsg(appId, e.msgType, "defaultEvent", e.fromUserName, e.toUserName, upsertUserInfo)

    fun upsertFan(appId: String, openId: String) = runBlocking {
        launch {
            val f0 = fanService.findFan(openId)
            if(f0 == null){
                //{"subscribe":1,"openid":"oe-9O5_GY7F3dZoehnhge_N28Y6o","nickname":"","sex":0,"language":"zh_CN","city":"","province":"","country":"","headimgurl":"","subscribe_time":1659583930,"remark":"","groupid":0,"tagid_list":[],"subscribe_scene":"ADD_SCENE_SEARCH","qr_scene":0,"qr_scene_str":""}
                val res = UserApi(appId).getUserInfo(openId)
                if (res.isOK()) {
                    val f = res.toFan(appId)
                    if(f != null){
                        //此处的用户场景信息是用户第一次关注时的。已关注的话，再次扫渠道码获取用户信息时，无渠道码信息
                        log.info("upsertFan: openId=${f._id}, qrs=${f.qrs}, qr=${f.qr}")
                        fanService.saveFan(f)
                    }else{
                        log.warn("fail to convert response to Fan:res.openid=${res.openid}, ${res.toString()}")
                    }
                }else{
                    log.warn("fail to getUserInfo: openid=$openId, err=${res.errMsg}")
                }
            }
        }
    }


    private fun tryReMsg(appId: String, type: String?, defaultType: String, from: String?, to: String?, upsertUserInfo: Boolean = true): ReBaseMSg? {
        //log.info("tryReMsg: type=$type")
        if(upsertUserInfo && from != null)
            upsertFan(appId, from)

        if (type == null) return null

        val myMsg: MyMsg = (prefService.findPrefReInMsg(appId, type)
            ?: prefService.findPrefReInMsg(appId, defaultType))
            ?.let { msgService.findMyMsg(it.msgId) } ?: return null

        return myMsgToReMsg(myMsg, from, to)
    }

    fun myMsgToReMsg(myMsg: MyMsg, from: String?, to: String?) = try {
        when (myMsg.msg) {
            is TextContent -> ReTextMsg(myMsg.msg.content, from, to)
            is ImageContent -> ReImgMsg(myMsg.msg.mediaId, from, to)
            is VoiceContent -> ReVoiceMsg(myMsg.msg.mediaId, from, to)
            is MusicContent -> {
                val music = myMsg.msg
                ReMusicMsg(
                    music.thumbMediaId, music.musicUrl, music.hqMusicUrl, music.title, music.description,
                    from, to
                )
            }
            is VideoContent -> {
                val video = myMsg.msg
                ReVideoMsg(video.mediaId, video.title, video.description, from, to)
            }
            is NewsContent -> {
                val list = myMsg.msg.articles
                ReNewsMsg(list, list.size, from, to)
            }
            else -> {
                log.warn("not support type=${myMsg.msg}")
                null
            }
        }
    } catch (e: Exception) {
        log.warn(e.message)
        null
    }

}