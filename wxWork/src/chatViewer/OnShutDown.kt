/*
 * Copyright © 2022 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2022-12-30 16:38
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

package com.github.rwsbillyang.wxWork.chatViewer

import com.github.rwsbillyang.ktorKit.server.LifeCycle

import io.ktor.server.application.*

import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

class OnShutDown(application: Application) : LifeCycle(application) {
    private val log = LoggerFactory.getLogger("OnShutDown")
    init {
        onStopping {
            val fetcher: ChatMsgScheduleTask by application.inject()
            fetcher.onShutdown()
        }
    }
}