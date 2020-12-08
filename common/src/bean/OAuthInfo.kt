/*
 * Copyright © 2020 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2020-11-27 11:12
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

package com.github.rwsbillyang.wxSDK.bean

import kotlinx.serialization.Serializable

/**
 * @param appId	appid 是	公众号的唯一标识 or corpId
 * @param redirectUri	redirect_uri 是	授权后重定向的回调链接地址， 使用 urlEncode 链接进行处理过
 * @param scope	是	应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
 * @param state	否	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
 * */
@Serializable
class OAuthInfo(
        val appId: String,
        val redirectUri: String,
        val scope: String,
        val state: String,
        var authorizeUrl: String? = null
){
    init {
        authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}" +
                "&redirect_uri=${redirectUri}&response_type=code&scope=${scope}&state=${state}#wechat_redirect"
    }
}