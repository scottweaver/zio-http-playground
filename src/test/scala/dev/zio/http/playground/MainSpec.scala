package dev.zio.http.playground

import zio.test.*
import zio.http.*
import zio.http.model.*
import zio.http.model.Headers.*
import zio.*

object MainSpec extends ZIOSpecDefault:

  def spec = suite("Test environment")(
    test("test authentication") {

      val form = Form.makeUrlEncodedUTF8(
        "client_id" -> "spec",
        "client_secret" -> "specs$$ecret"
      )

      val req = Client.request(
        url = "http://localhost:8080/auth",
        method = Method.POST,
        content = Body.fromForm(form)
      )

      val test = for {
        resp <- req
        code = resp.status
        body <- resp.body.asString
        _ <- ZIO.debug(s"Response Body: '${body}'")
      } yield assertTrue(code == Status.Ok)

      test.provide(Client.default)

    }
  )
