package com.github.rwsbillyang.wxSDK.security

import java.util.*


internal class ByteGroup {
    var byteContainer = ArrayList<Byte>()
    fun toBytes(): ByteArray {
        val bytes = ByteArray(byteContainer.size)
        for (i in byteContainer.indices) {
            bytes[i] = byteContainer[i]
        }
        return bytes
    }

    fun addBytes(bytes: ByteArray): ByteGroup {
        for (b in bytes) {
            byteContainer.add(b)
        }
        return this
    }

    fun size(): Int {
        return byteContainer.size
    }
}
