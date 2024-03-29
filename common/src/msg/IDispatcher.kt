package com.github.rwsbillyang.wxSDK.msg

import org.w3c.dom.Element

interface IDispatcher {
    /**
     * 未知类型的msg或event可以继续进行读取其额外信息，从而可以自定义分发和处理
     * 返回null表示由onDefault继续处理
     * */
    fun onDispatch(appId: String, agentId:String?, xml: String, rootDom: Element, msgOrEventType: String?): ReBaseMSg?
}