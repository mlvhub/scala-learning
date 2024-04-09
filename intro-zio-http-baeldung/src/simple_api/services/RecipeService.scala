package simple_api.services

import simple_api.models.Recipe
import simple_api.repositories.RecipeRepo
import zio._

case class RecipeServiceImpl(recipeRepo: RecipeRepo) extends RecipeService:

  def list(): Task[List[Recipe]] = recipeRepo.list()

  def save(recipe: Recipe): Task[Recipe] = recipeRepo.save(recipe)

  def find(id: Long): Task[Option[Recipe]] = recipeRepo.find(id)

  def update(recipe: Recipe): Task[Option[Recipe]] = recipeRepo.update(recipe)

  def delete(id: Long): Task[Option[Recipe]] = recipeRepo.delete(id)

trait RecipeService:

  def list(): Task[List[Recipe]]

  def save(recipe: Recipe): Task[Recipe]

  def find(id: Long): Task[Option[Recipe]]

  def update(recipe: Recipe): Task[Option[Recipe]]

  def delete(id: Long): Task[Option[Recipe]]

object RecipeService:
  def layer: ZLayer[RecipeRepo, Nothing, RecipeService] =
    ZLayer {
      for {
        recipeRepo <- ZIO.service[RecipeRepo]
      } yield RecipeServiceImpl(recipeRepo)
    }
