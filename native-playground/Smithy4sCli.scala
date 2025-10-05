// NOTE: taken from https://blog.lewisjkl.com/smithy4s-cli-scala-native/
// NOTE: remember to run `smithy4s generate ./Smithy4sCli.smithy`
// ran like this: `LD_LIBRARY_PATH="/home/linuxbrew/.linuxbrew/opt/s2n/lib" scala-cli ./Smithy4sCli.scala ./cli -- get-number-fact 10000 trivia`
// packaged like this: `scala-cli --power package --native --native-version 0.4.17 --native-linking "-L/home/linuxbrew/.linuxbrew/opt/s2n/lib" Smithy4sCli.scala ./cli`

//> using scala 3.7

//> using dep com.disneystreaming.smithy4s::smithy4s-http4s::0.18.42
//> using dep com.disneystreaming.smithy4s::smithy4s-decline::0.18.42
//> using dep org.http4s::http4s-ember-client::0.23.32
//> using dep com.monovore::decline-effect::2.4.1

import smithy4s.http4s.*
import smithy4s.decline.Smithy4sCli
import cats.effect.*
import com.monovore.decline.*
import com.monovore.decline.effect.CommandIOApp
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.kernel.Resource
import org.http4s.client.Client
import org.http4s.Uri

object Main extends IOApp:

  private val http4sClient: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].build

  private val numbersClient = http4sClient.flatMap { baseClient =>
    SimpleRestJsonBuilder(cli.Numbers)
      .client(baseClient)
      .uri(Uri.unsafeFromString("http://numbersapi.com"))
      .resource
  }

  private def createCli(args: List[String]): IO[ExitCode] = numbersClient.use {
    client =>
      val command = Smithy4sCli
        .standalone(Opts(client))
        .command
        .map(
          _.redeem(_ => ExitCode.Error, _ => ExitCode.Success)
        )

      CommandIOApp.run(command, args)
  }

  def run(args: List[String]): IO[ExitCode] = createCli(args)

