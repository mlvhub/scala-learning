

final class counter$_ {
def args = counter_sc.args$
def scriptPath = """counter.sc"""
/*<script>*/
import zio.*
import zio.http.*

object ZioEffectApp extends ZIOAppDefault:
  private object CounterHttpApp:
    def apply(): Http[Ref[Int], Nothing, Request, Response] =
      Http.collectZIO[Request] {
        case Method.GET -> Root / "up" =>
          ZIO.serviceWithZIO[Ref[Int]](cRef =>
            response(cRef.updateAndGet(_ + 1))
          )
        case Method.GET -> Root / "get" =>
          ZIO.serviceWithZIO[Ref[Int]](cRef => response(cRef.get))
        case Method.GET -> Root / "reset" =>
          ZIO.serviceWithZIO[Ref[Int]](cRef =>
            response(cRef.updateAndGet(_ => 0))
          )
      }

    private def response(counterUio: UIO[Int]) = counterUio
      .map(_.toString)
      .map(Response.text)

  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    Server
      .serve(CounterHttpApp())
      .provide(
        ZLayer.fromZIO(Ref.make(0)),
        Server.defaultWithPort(8080)
      )

/*</script>*/ /*<generated>*//*</generated>*/
}

object counter_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new counter$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export counter_sc.script as `counter`

