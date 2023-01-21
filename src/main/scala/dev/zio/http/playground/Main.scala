package dev.zio.http.playground

import zio.*
import zio.http.*
import zio.Console.ConsoleLive
import zio.http.model.*
import java.net.URLDecoder

object Main extends ZIOAppDefault:

  def authenticate(body: Body): ZIO[Any, Response, Response] = for {
    form <- body.asURLEncodedForm.mapError(err =>
      Response(
        body = Body.fromString(s"Invalid body, '${err}'"),
        status = Status.BadRequest
      )
    )
    clientId <- ZIO
      .fromOption(form.fields.get("client_id"))
      .mapError(_ =>
        Response(
          body = Body.fromString("Missing client_id"),
          status = Status.Unauthorized
        )
      )
    clientSecret <- ZIO
      .fromOption(form.fields.get("client_secret"))
      .mapError(_ =>
        Response(
          body = Body.fromString("Missing client_secret"),
          status = Status.Unauthorized
        )
      )

  } yield Response.text(
    s"Successfully authenticated '${clientId}' with secret '${clientSecret}'."
  )

  val app: Http[Any, Response, Request, Response] =
    (Http
      .collectZIO[Request] {
        case Method.GET -> !! / "hello" / name =>
          ZIO.succeed(Response.text(s"Hello, $name!"))
        case req @ (Method.POST -> !! / "auth") =>
          authenticate(req.body)
      })

  def run = Server.serve(app).provide(Server.default)
