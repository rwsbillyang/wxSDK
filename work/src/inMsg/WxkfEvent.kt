/*
 * Copyright © 2023 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2023-01-07 21:58
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

package com.github.rwsbillyang.wxSDK.work.inMsg

import com.github.rwsbillyang.wxSDK.msg.WxXmlEvent
import org.w3c.dom.Element


//<xml>
// <ToUserName><![CDATA[wwfc2fead39b1e60dd]]></ToUserName>
// <Encrypt><![CDATA[ksULeHpUG0imG/CKibJVeTn3P35Yf0JcShljm2SkXX6KffMLZHSpwnKwoQJ7ixjh9DM0kkq8atMJf42sG8PDiIubteW6idDu9cVryEmF0s2EkZ/VYtKTp/Ht1NmdoVU8j8AWUyhZJYbP/Vpv/ycOXylWAhr4PRKpxYjKp3pVzf9tl4e/jweQH2plmuWUX1AD+DBw+NF5aYV3M8HJR2CE7dMTnahSutXfoYn3sLbvxh3dtcIv2pWa4j9fBKrL9EuwuRtV6RGgAMEe98Z6Xr2/ICfIqH+3bmxtYKEOmmr0Sd3GQJMP6prihJE5RBaATcWL3bC5snJUYnySe07uXA/zutsjYpAulDWSPbbIACj3is0uLLzR+LZ18gnqUMGnHRKEJfDQ+MVSUd35o7ctMvgZQFgvoZLyyMtDyU2hsE6oMIysDBBRuozpUokxTZVulWdXchh0wuYtnJnkmg5Fp1ASHhRTeFGGQyrxOT/CUAjBcsagjIDRWM7tZ3jo5+cvz54qvWgSVQMF0q7S74z565LFzt80RRevTRgB61SMzjFO/psBuVehmo+HbN1nDSLS6W63di+eTUxjQOY8Rpz8de8UtQ==]]></Encrypt>
// <AgentID><![CDATA[3010151]]></AgentID></xml>

//<xml>
// <ToUserName><![CDATA[wwfc2fead39b1e60dd]]></ToUserName>
// <CreateTime>1673624501</CreateTime>
// <MsgType><![CDATA[event]]></MsgType>
// <Event><![CDATA[kf_msg_or_event]]></Event>
// <Token><![CDATA[ENCFHoY8dNsqMD9XtfnCbU7yze1upBqpiNQsWyjvwXaEXDG]]></Token>
// <OpenKfId><![CDATA[wkfqLUQwAApHj0eaMhYPQOOamS3Wz17w]]></OpenKfId>
// </xml>
class WxkfEvent(xml: String, rootDom: Element): WxXmlEvent(xml, rootDom)
{
    val token = get(rootDom, "Token")
    val openKfId = get(rootDom, "OpenKfId")
}
