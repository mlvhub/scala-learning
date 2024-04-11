//> using scala "3"

object State {
  private val states = Map(
    "AZ" -> "Arizona",
    "CA" -> "California",
    "ID" -> "Idaho",
    "IN" -> "Indiana",
    "MA" -> "Massachusetts",
    "OK" -> "Oklahoma",
    "PA" -> "Pennsylvania",
    "VA" -> "Virginia"
  )

  def byState(s: String): String = {
    val emptyStatesMap: Map[String, Seq[String]] =
      states.map { case (k, v) => (v, Seq.empty) }

    s
      .split("\n")
      .foldLeft(emptyStatesMap)((byStateMap, str) => {
        val address :+ stateShort = str.split(" ").toSeq
        val state = states(stateShort)
        val withNewAddress: Seq[String] =
          byStateMap(
            state
          ) :+ (address :+ state).mkString(" ")

        byStateMap.updated(state, withNewAddress)
      })
      .filter(_._2.nonEmpty)
      .toList
      .sortBy(_._1)
      .map[String] { case (state: String, addresses: Seq[String]) =>
        s"$state\n..... ${addresses.sorted.mkString("\n..... ")}"
      }
      .mkString("\n ")
      .replace(",", "")
  }
}

val ad0 = "John Daggett, 341 King Road, Plymouth MA\n" +
  "Alice Ford, 22 East Broadway, Richmond VA\n" +
  "Orville Thomas, 11345 Oak Bridge Road, Tulsa OK\n" +
  "Terry Kalkas, 402 Lans Road, Beaver Falls PA\n" +
  "Eric Adams, 20 Post Road, Sudbury MA\n" +
  "Hubert Sims, 328A Brook Road, Roanoke VA\n" +
  "Amy Wilde, 334 Bayshore Pkwy, Mountain View CA\n" +
  "Sal Carpenter, 73 6th Street, Boston MA"
println(State.byState(ad0))
