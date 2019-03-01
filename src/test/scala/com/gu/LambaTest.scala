package com.gu.testApiGatewayWorkshop

import org.scalatest.{FlatSpec, Matchers}

class LambaTest extends FlatSpec with Matchers {
  private val jsonStringPayload = """{"resource":"/helloWorld","httpMethod":"POST","body":"Maria"}"""
  "Lambda.process" should "return a greeting" in {
    Lambda.process(jsonStringPayload) should be("Hello there Maria!")
  }

}





