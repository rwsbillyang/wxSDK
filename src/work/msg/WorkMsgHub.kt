package com.github.rwsbillyang.wxSDK.work.msg

import com.github.rwsbillyang.wxSDK.common.msg.*
import com.github.rwsbillyang.wxSDK.common.aes.WXBizMsgCrypt
import javax.xml.stream.XMLEventReader


class WorkMsgHub(
    private val msgHandler: IWorkMsgHandler,
    private val eventHandler: IWorkEventHandler,
    wxBizMsgCrypt: WXBizMsgCrypt
): WxMsgHub(wxBizMsgCrypt) {
    override fun dispatchMsg(reader: XMLEventReader, base: BaseInfo): ReBaseMSg?{
        return when(base.msgType){
            BaseInfo.TEXT -> msgHandler.onWorkTextMsg(
                WorkTextMsg(base).apply { read(reader) }
            )
            BaseInfo.IMAGE -> msgHandler.onWorkImgMsg(
                WorkImgMSg(base).apply { read(reader) }
            )
            BaseInfo.VOICE -> msgHandler.onWorkVoiceMsg(
                WorkVoiceMsg(base).apply { read(reader) }
            )
            BaseInfo.VIDEO -> msgHandler.onWorkVideoMsg(
                WorkVideoMsg(base).apply { read(reader) }
            )

            BaseInfo.LOCATION -> msgHandler.onWorkLocationMsg(
                WorkLocationMsg(base).apply { read(reader) }
            )
            BaseInfo.LINK -> msgHandler.onWorkLinkMsg(
                WorkLinkMsg(base).apply { read(reader) }
            )
            else -> msgHandler.onDefault(WorkBaseMsg(base).apply { read(reader) })
        }
    }

    override fun dispatchEvent(reader: XMLEventReader, base: BaseInfo): ReBaseMSg?{
        val baseEvent = WorkBaseEvent(base).apply { read(reader) }
        return when (baseEvent.event) {
            WxBaseEvent.SUBSCRIBE -> eventHandler.onWorkSubscribeEvent(
                WorkSubscribeEvent(base).apply { read(reader) }
            )
            WxBaseEvent.UNSUBSCRIBE -> eventHandler.onWorkUnsubscribeEvent(
                WorkUnsubscribeEvent(base).apply { read(reader) }
            )
            WxBaseEvent.LOCATION -> eventHandler.onWorkLocationEvent(
                WorkLocationEvent(base).apply { read(reader) }
            )
            WxBaseEvent.CLICK -> eventHandler.onWorkMenuClickEvent(
                WorkMenuClickEvent(base).apply { read(reader) }
            )
            WxBaseEvent.VIEW -> eventHandler.onWorkMenuViewEvent(
                WorkMenuViewEvent(base).apply { read(reader) }
            )
            WxBaseEvent.SCAN_CODE_PUSH ->
                eventHandler.onWorkMenuScanCodePushEvent(
                    WorkMenuScanCodePushEvent(base).apply { read(reader) }
                )
            WxBaseEvent.SCAN_CODE_WAIT_MSG -> eventHandler.onWorkMenuScanCodeWaitEvent(
                WorkMenuScanCodeWaitEvent(base).apply { read(reader) }
            )
            WxBaseEvent.PIC_SYS_PHOTO -> eventHandler.onWorkMenuPhotoEvent(
                WorkMenuPhotoEvent(base).apply { read(reader) }
            )
            WxBaseEvent.PIC_PHOTO_OR_ALBUM -> eventHandler.onWorkMenuPhotoOrAlbumEvent(
                WorkMenuPhotoOrAlbumEvent(base).apply { read(reader) }
            )
            WxBaseEvent.PIC_WEIXIN -> eventHandler.onWorkMenuWorkAlbumEvent(
                WorkMenuWorkAlbumEvent(base).apply { read(reader) }
            )
            WxBaseEvent.LOCATION_SELECT -> eventHandler.onWorkMenuLocationEvent(
                WorkMenuLocationEvent(base).apply { read(reader) }
            )

            WorkBaseEvent.ENTER_AGENT -> eventHandler.onWorkEnterAgent(
                WorkEnterAgent(base).apply { read(reader) }
            )
            WorkBaseEvent.BATCH_JOB_RESULT -> eventHandler.onWorkBatchJobResultEvent(
                WorkBatchJobResultEvent(base).apply { read(reader) }
            )
            WorkBaseEvent.OPEN_APPROVAL_CHANGE -> eventHandler.onWorkApprovalStatusChangeEvent(
                WorkApprovalStatusChangeEvent(base).apply { read(reader) }
            )
            WorkBaseEvent.TASK_CARD_CLICK -> eventHandler.onWorkTaskCardClickEvent(
                WorkTaskCardClickEvent(base).apply { read(reader) }
            )
            WorkBaseEvent.CHANGE_CONTACT ->{
                val e = WorkChangeContactEvent(base).apply { read(reader) }
                when(e.changeType){
                    WorkChangeContactEvent.CREATE_PARTY -> eventHandler.onWorkPartyCreateEvent(
                        WorkPartyCreateEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.UPDATE_PARTY -> eventHandler.onWorkPartyUpdateEvent(
                        WorkPartyUpdateEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.DELETE_PARTY -> eventHandler.onWorkPartyDelEvent(
                        WorkPartyDelEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.UPDATE_USER -> eventHandler.onWorkUserUpdateEvent(
                        WorkUserUpdateEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.CREATE_USER -> eventHandler.onWorkUserCreateEvent(
                        WorkUserCreateEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.DELETE_USER -> eventHandler.onWorkUserDelEvent(
                        WorkUserDelEvent(base).apply { read(reader) }
                    )
                    WorkChangeContactEvent.UPDATE_TAG -> eventHandler.onWorkTagUpdateEvent(
                        WorkTagUpdateEvent(base).apply { read(reader) }
                    )
                    else -> {
                        log.warn("not support changeType: ${e.changeType}")
                        eventHandler.onDefault(e)
                    }
                }
            }
            else -> {
                log.warn("not support event: ${baseEvent.event}")
                eventHandler.onDefault(baseEvent)
            }
        }
    }
}