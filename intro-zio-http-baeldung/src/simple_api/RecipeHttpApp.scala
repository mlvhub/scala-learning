package simple_api

import zio.*
import zio.http.{Root, *}
import zio.json.*
import simple_api.services.RecipeService
import simple_api.repositories.RecipeRepo
import simple_api.models.Recipe
// import simple_api.models.Recipe.*

import scala.util.{Failure, Success, Try}

object RecipeHttpApp:

  val appContext = "recipes"

  private type RecipeEffect = ZIO[RecipeService, Throwable, Response]

  private def jsonErrorResponse(error: String) = {
    ZIO
      .debug(s"Failed to parse the input: $error")
      .as(
        Response.text(error).withStatus(Status.BadRequest)
      )
  }

  val postHandler: Http[RecipeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req @ (Method.POST -> Root / RecipeHttpApp.appContext) =>
        (for {
          u <- req.body.asString.map(_.fromJson[Recipe])
          response <- u match {
            case Left(e) => jsonErrorResponse(e)
            case Right(recipe) =>
              ZIO
                .serviceWithZIO[RecipeService](_.save(recipe))
                .map(recipe => Response.json(recipe.toJson))
          }
        } yield response).orDie
    }

  val allHandler: Http[RecipeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> Root / RecipeHttpApp.appContext =>
        ZIO
          .serviceWithZIO[RecipeService](_.list())
          .map(recipes => Response.json(recipes.toJson))
          .orDie
    }

  val getHandler: Http[RecipeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> Root / RecipeHttpApp.appContext / id =>
        (for {
          idLong <- ZIO.fromTry(Try(id.toLong))
          response <- ZIO
            .serviceWithZIO[RecipeService](_.find(idLong))
            .map({
              case Some(recipe) => Response.json(recipe.toJson)
              case None         => Response.status(Status.NotFound)
            })
        } yield response).orDie
    }

  val putHandler: Http[RecipeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req @ Method.PUT -> Root / RecipeHttpApp.appContext / id =>
        (for {
          u <- req.body.asString.map(_.fromJson[Recipe])
          response <- u match {
            case Left(e) => jsonErrorResponse(e)
            case Right(recipe) =>
              ZIO
                .serviceWithZIO[RecipeService](_.update(recipe))
                .map({
                  case Some(recipe) => Response.json(recipe.toJson)
                  case None         => Response.status(Status.NotFound)
                })
          }
        } yield response).orDie
    }

  val deleteHandler: Http[RecipeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.DELETE -> Root / RecipeHttpApp.appContext / id =>
        (for {
          idLong <- ZIO.fromTry(Try(id.toLong))
          response <- ZIO
            .serviceWithZIO[RecipeService](_.delete(idLong))
            .map({
              case Some(recipe) => Response.json(recipe.toJson)
              case None         => Response.status(Status.NotFound)
            })
        } yield response).orDie
    }

  def apply(): Http[RecipeService, Throwable, Request, Response] =
    allHandler
      ++
        postHandler
        ++
        getHandler
        ++
        putHandler
        ++
        deleteHandler
