/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.mce.telepathy

import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import okhttp3.{HttpUrl, MediaType, Request, RequestBody}

object HttpRequest {

  final val JsonContentType = MediaType.get("application/json; charset=utf-8")

  def request[Rsp: Decoder](url: HttpUrl, request: Request)(implicit
    setting: TelepathySetting
  ): Either[ErrorResponse, Rsp] = {
    val response = setting.buildClient().newCall(request).execute()

    if (response.isSuccessful()) {
      decode[Rsp](response.body().string()).left.map(e => ErrorResponse(-1, Left(e)))
    } else
      Left(ErrorResponse(response.code(), Right(response.message())))
  }

  def get[Rsp: Decoder](url: HttpUrl)(implicit
    setting: TelepathySetting
  ): Either[ErrorResponse, Rsp] =
    request(url, setting.requestBuilder(url).build())

  def put[Rsp: Decoder, D: Encoder](url: HttpUrl, data: D)(implicit
    setting: TelepathySetting
  ): Either[ErrorResponse, Rsp] =
    request(
      url,
      setting
        .requestBuilder(url)
        .put(RequestBody.create(data.asJson.noSpaces, JsonContentType))
        .build()
    )

  def post[Rsp: Decoder, D: Encoder](url: HttpUrl, data: D)(implicit
    setting: TelepathySetting
  ): Either[ErrorResponse, Rsp] =
    request(
      url,
      setting
        .requestBuilder(url)
        .post(RequestBody.create(data.asJson.noSpaces, JsonContentType))
        .build()
    )

}
