package simple_api.repositories

import zio.{Ref, Task, UIO, ZLayer}
import simple_api.models.Recipe

case class InMemoryRecipeRepo(map: Ref[Map[Long, Recipe]]) extends RecipeRepo:

  def list(): UIO[List[Recipe]] = map.get.map(_.values.toList)

  def save(recipe: Recipe): UIO[Recipe] =
    for _ <- map.update(_ + (recipe.id -> recipe))
    yield recipe

  def find(id: Long): UIO[Option[Recipe]] =
    map.get.map(_.get(id))

  def update(recipe: Recipe): Task[Option[Recipe]] =
    for
      _ <- map.update(_ + (recipe.id -> recipe))
      recipeOpt <- map.get.map(_.get(recipe.id))
    yield recipeOpt

  def delete(id: Long): Task[Option[Recipe]] =
    for
      recipe <- map.get.map(_.get(id))
      _ <- map.update(_ - id)
    yield recipe

object InMemoryRecipeRepo {
  def layer: ZLayer[Any, Nothing, RecipeRepo] =
    ZLayer.fromZIO(
      Ref.make(Map.empty[Long, Recipe]).map(new InMemoryRecipeRepo(_))
    )
}
