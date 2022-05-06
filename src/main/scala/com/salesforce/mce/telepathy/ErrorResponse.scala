package com.salesforce.mce.telepathy

case class ErrorResponse(code: Int, message: Either[Exception, String])
