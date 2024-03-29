/*
 * Copyright © 2020 rwsbillyang@qq.com
 *
 * Written by rwsbillyang@qq.com at Beijing Time: 2020-11-18 22:49
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

package com.github.rwsbillyang.wxSDK.security


import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.cert.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*





/**
 * 用于微信支付
 * 证书工具，生成PrivateKey
 * https://github.com/wechatpay-apiv3/wechatpay-apache-httpclient/blob/master/src/main/java/com/wechat/pay/contrib/apache/httpclient/util/PemUtil.java
 * */
object PemUtil {
    fun loadPrivateKey(inputStream: InputStream): PrivateKey {
            val array = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                array.write(buffer, 0, length)
            }

            return loadPrivateKey(array.toString("utf-8"))
    }
    fun loadPrivateKey(key: String): PrivateKey {
        //println("key=$key")
        return try {
            val privateKey: String = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s+".toRegex(), "")
            //println("after replace key=$privateKey")
            val kf = KeyFactory.getInstance("RSA")
            kf.generatePrivate(
                    PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("当前Java环境不支持RSA", e)
        } catch (e: InvalidKeySpecException) {
            throw RuntimeException("无效的密钥格式")
        } catch (e: IOException) {
            throw RuntimeException("无效的密钥")
        }
    }
    fun loadCertificate(inputStream: InputStream?): X509Certificate {
        return try {
            val cf = CertificateFactory.getInstance("X509")
            val cert = cf.generateCertificate(inputStream) as X509Certificate
            cert.checkValidity()
            cert
        } catch (e: CertificateExpiredException) {
            throw RuntimeException("证书已过期", e)
        } catch (e: CertificateNotYetValidException) {
            throw RuntimeException("证书尚未生效", e)
        } catch (e: CertificateException) {
            throw RuntimeException("无效的证书", e)
        }
    }
}

fun main(){
    PemUtil.loadPrivateKey(FileInputStream("/Users/bill/Documents/private/company/cert/1290074401_20210308_cert/apiclient_key.pem"))
}