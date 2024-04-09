package simple_api.config

import zio.ZLayer
import zio._

final case class RootConfig(
    config: AppConfig
)
final case class AppConfig(
    system: SystemConfig,
    db: DbConfig
)

final case class SystemConfig(
    jwtSecret: String
)

final case class DbConfig(
    databaseUrl: String
)

object Configuration:

  val live: ZLayer[Any, Config.Error, AppConfig] =
    ZLayer.fromZIO(
      for {
        jwtSecret <- ZIO.config(Config.string("JWT_SECRET"))
        databaseUrl <- ZIO.config(Config.string("DATABASE_URL"))
      } yield AppConfig(
        system = SystemConfig(jwtSecret = jwtSecret),
        db = DbConfig(databaseUrl = databaseUrl)
      )
    )
