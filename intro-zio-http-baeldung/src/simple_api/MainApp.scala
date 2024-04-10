package simple_api

import zio.*
import zio.http.*
import zio.json.*

import simple_api.services.RecipeService
import simple_api.repositories.RecipeRepo
import simple_api.repositories.PostgresRecipeRepo
import simple_api.models.Recipe
import simple_api.db.*
import simple_api.config.Configuration

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =

    val headerMiddleware =
      RequestHandlerMiddlewares.addHeader("X-Environment", "Dev")

    val loggingMiddleware = RequestHandlerMiddlewares.requestLogging(
      logRequestBody = true,
      logResponseBody = true
    )

    val recipeApp = RecipeHttpApp() @@ headerMiddleware @@ loggingMiddleware

    Console.printLine("Starting server on port 8080") *>
      Server
        .serve(
          recipeApp.withDefaultErrorResponse
        )
        .provide(
          Configuration.live,
          Db.dataSourceLive,
          Db.quillLive,
          PostgresRecipeRepo.live,
          RecipeService.layer,
          Server.defaultWithPort(8080)
        )
