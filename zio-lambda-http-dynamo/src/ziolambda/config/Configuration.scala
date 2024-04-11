package ziolambda.config

import zio.ZLayer
import zio._

final case class AppConfig(
    oanda: OandaConfig,
    db: DbConfig
)

final case class OandaConfig(
    host: String,
    apiKey: String,
    accountId: String,
    instrumentId: String
)

final case class DbConfig(
    databaseName: String
)

object Configuration:

  val live: ZLayer[Any, Config.Error, AppConfig] =
    ZLayer.fromZIO(
      for {
        host <- ZIO.config(Config.string("OANDA_HOST"))
        apiKey <- ZIO.config(Config.string("OANDA_API_KEY"))
        accountId <- ZIO.config(Config.string("OANDA_ACCOUNT_ID"))
        instrumentId <- ZIO.config(Config.string("OANDA_INSTRUMENT_ID"))
        databaseName <- ZIO.config(Config.string("DATABASE_NAME"))
      } yield AppConfig(
        oanda = OandaConfig(
          host = host,
          apiKey = apiKey,
          accountId = accountId,
          instrumentId = instrumentId
        ),
        db = DbConfig(
          databaseName = databaseName
        )
      )
    )
