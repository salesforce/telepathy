package com.salesforce.mce.telepathy

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration

import javax.net.ssl.{SSLSocketFactory, X509TrustManager}

import okhttp3.{HttpUrl, Interceptor, OkHttpClient, Request}

trait TelepathySetting {

  def connectTimeout: Option[Duration] = None
  def readTimeout: Option[Duration] = None
  def writeTimeout: Option[Duration] = None

  def interceptors: Seq[Interceptor] = Seq.empty

  def sslSocketFactory: Option[SSLSocketFactory] = None

  /**
   * Only works when SSLSocketFactory is set
   */
  def trustManager: Option[X509TrustManager] = None

  def headers: Map[String, String] = Map.empty

  def buildClient(): OkHttpClient = {

    def addConnectTimeout(builder: OkHttpClient.Builder): OkHttpClient.Builder =
      connectTimeout.foldLeft[OkHttpClient.Builder](builder)((b, t) =>
        b.connectTimeout(t.toMillis, TimeUnit.MILLISECONDS)
      )

    def addReadTimeout(builder: OkHttpClient.Builder): OkHttpClient.Builder =
      readTimeout.foldLeft[OkHttpClient.Builder](builder)((b, t) =>
        b.readTimeout(t.toMillis, TimeUnit.MILLISECONDS)
      )

    def addWriteTimeout(builder: OkHttpClient.Builder): OkHttpClient.Builder =
      writeTimeout.foldLeft[OkHttpClient.Builder](builder)((b, t) =>
        b.writeTimeout(t.toMillis, TimeUnit.MILLISECONDS)
      )

    def addInterceptors(builder: OkHttpClient.Builder): OkHttpClient.Builder =
      interceptors.foldLeft[OkHttpClient.Builder](builder)((b, i) => b.addInterceptor(i))

    def configureSsl(builder: OkHttpClient.Builder): OkHttpClient.Builder =
      (sslSocketFactory, trustManager) match {
        case (Some(f), Some(t)) => builder.sslSocketFactory(f, t)
        case (Some(f), None) => builder.sslSocketFactory(f)
        case _ => builder
      }

    val baseBuilder = new OkHttpClient.Builder()

    Seq(
      addConnectTimeout _,
      addReadTimeout _,
      addWriteTimeout _,
      addInterceptors _,
      configureSsl _
    ).reduceLeft(_ andThen _)(baseBuilder).build()

  }

  def requestBuilder(url: HttpUrl): Request.Builder = headers
    .foldLeft(new Request.Builder().url(url)) { case (b, (k, v)) =>
      b.addHeader(k, v)
    }

}

object TelepathySetting {

  def apply(): TelepathySetting = new TelepathySetting {}

}
