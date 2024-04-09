package simple_api.repositories

import zio._
import simple_api.models.Recipe

trait RecipeRepo:
  def list(): Task[List[Recipe]]

  def save(recipe: Recipe): Task[Recipe]

  def find(id: Long): Task[Option[Recipe]]

  def update(recipe: Recipe): Task[Option[Recipe]]

  def delete(id: Long): Task[Option[Recipe]]
