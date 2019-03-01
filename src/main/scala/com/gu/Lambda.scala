package com.gu.testApiGatewayWorkshop

import io.circe.syntax._
import io.circe.parser._
import java.io._
import java.nio.charset.StandardCharsets.UTF_8

object Lambda {

  // Keep the handler simple
  def handler(in: InputStream, out: OutputStream): Unit = {
    val jsonStringPayload = scala.io.Source.fromInputStream(in).mkString("")
    // JSON in Scala: cannot extract .body from json body. Need to define with case classes OR use Play/Circe cursors to traverse
    val result = Lambda.process(jsonStringPayload)
    val response = APIResponse(200, Map("Content-Type" -> "application/json"), result)
    out.write(response.asJson.noSpaces.getBytes(UTF_8))
  }

  // Move logic to "process" function for testing
  def process(jsonStringPayload: String): String = {

    // For Comprehensions - good for Eithers or Options (where you would otherwise use pattern matching - see notes below)
    val either = for {
      json <- parse(jsonStringPayload)
      name <- json.hcursor.downField("body").as[String]
    } yield s"Hello there ${name}!"

    either match {
      case Left(error) => println(s"Req payload parsing failure: ${error}")
        "" // Decide what you want to do with your error here
      case Right(greeting) => greeting
    }
  }

}

// Run you main() fx from App
object Local extends App {
  val fixture = """{"body":"Maria"}"""
  println("local")
  println(Lambda.process(fixture))
}


// We can chain parseJson and process fx in a For Comprehension ^^^

//  def parseJson(jsonStringPayload: String): Json = {
//    parse(jsonStringPayload) match {
//      case Left(_) => println("Req payload parsing failure")
//        Json.Null // idiomatic Scala to create an error trait => case classes of error types that you can return
//      case Right(json) => json
//    }
//  }

//  def process(parsedJSON: Json): String = {
//    val name = parsedJSON.hcursor.downField("body").as[String] match {
//      case Left(_) => println("No body")
//        ""
//      case Right(string) => string
//    }
//    s"hello there ${name} again"
//  }


