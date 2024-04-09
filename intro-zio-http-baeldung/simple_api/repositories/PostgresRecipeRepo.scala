package simple_api.repositories

import zio.*
import io.getquill.*
import io.getquill.jdbczio.*

import simple_api.models.Recipe

case class PostgresRecipeRepo(quill: Quill.Postgres[SnakeCase])
    extends RecipeRepo:
  import quill.*

  private inline def queryRecipe = quote(
    querySchema[Recipe](entity = "recipes")
  )

  def list(): Task[List[Recipe]] = run(queryRecipe)

  def save(recipe: Recipe) = run(
    queryRecipe
      .insert(
        _.id -> lift(recipe.id),
        _.name -> lift(recipe.name),
        _.ingredients -> lift(recipe.ingredients)
      )
      .returning(r => (r.id, r.name, r.ingredients))
  )
    .map((id, name, ingredients) => Recipe(id, name, ingredients))

  def delete(id: Long): Task[Option[Recipe]] = {
    val delete = queryRecipe.dynamic
      .filter(_.id == lift(id))
      .delete

    val read = quote(
      queryRecipe.filter(_.id == lift(id)).value
    )

    transaction {
      val r = run(read)
      run(delete).flatMap(_ => r)
    }
  }

  def find(id: Long): Task[Option[Recipe]] = run(
    queryRecipe
      .filter(_.id == lift(id))
  ).map(_.headOption)

  def update(recipe: Recipe): Task[Option[Recipe]] = {
    val update_query = queryRecipe.dynamic
      .filter(_.id == lift(recipe.id))
      .update(
        _.name -> lift(recipe.name),
        _.ingredients -> lift(recipe.ingredients)
      )

    val read = quote(
      queryRecipe.filter(_.id == lift(recipe.id)).value
    )

    transaction {
      val r = run(update_query)
      r.flatMap(_ => run(read))
    }
  }

//     run(
//     queryRecipe
//       .filter(_.id == lift(recipe.id))
//       .update(
//         _.id -> lift(recipe.id),
//         _.name -> lift(recipe.name),
//         _.ingredients -> lift(recipe.ingredients)
//       )
//       .returning(r => (r.id, r.name, r.ingredients))
//   )
//     .map((id, name, ingredients) => Some(Recipe(id, name, ingredients)))

object PostgresRecipeRepo:
  val live: ZLayer[Quill.Postgres[SnakeCase], Nothing, PostgresRecipeRepo] =
    ZLayer.fromFunction(new PostgresRecipeRepo(_))
