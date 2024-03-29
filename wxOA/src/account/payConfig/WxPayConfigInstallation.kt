/*
 * Copyright © 2021 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2021-10-09 00:34
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

package com.github.rwsbillyang.wxOA.account.payConfig

import com.github.rwsbillyang.ktorKit.server.LifeCycle
import com.github.rwsbillyang.wxSDK.wxPay.WxPay
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory


class WxPayConfigInstallation(application: Application) : LifeCycle(application) {
    private val log = LoggerFactory.getLogger("WxPayConfigInstallation")
    init {
        onStarted {
            val configService: PayConfigService by application.inject()
            configService.findWxPayConfigList().forEach {
                //数据库中有配置时的情形
                WxPay.config {
                    appId = it.appId
                    mchId = it.mchId // 商户号
                    serialNo = it.serialNo // API密钥
                    apiV3Key = it.apiV3Key //APIv3密钥
                    apiKeyCertFilePath = it.privateKeyPath
                    notifyHost = it.notifyHost
                }
            }
            logConfig()
        }
    }

    private fun logConfig(){
        log.info("============================WxPay==================================")
        WxPay.ApiContextMap.forEach { t, u ->
            log.info("WxPay.notifyPath=${WxPay.payNotifyPath(t)}")
        }
        log.info("=============================WxPay=================================")
    }
}