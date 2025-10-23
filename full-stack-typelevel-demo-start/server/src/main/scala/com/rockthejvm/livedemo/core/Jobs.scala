package com.rockthejvm.livedemo.core

import java.util.UUID

import cats.effect.*
import cats.syntax.all.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

import com.rockthejvm.livedemo.domain.job.*

trait Jobs[F[_]] { // "algebra"
  def create(job: Job): F[UUID]
  def all: F[List[Job]]
}

class JobsLive[F[_]: Concurrent] private (transactor: Transactor[F]) extends Jobs[F]:
  override def all: F[List[Job]] =
    sql"""
      SELECT
        company,
        title,
        description,
        externalUrl,
        salaryLo,
        salaryHi,
        currency,
        remote,
        location,
        country
      FROM jobs
    """
      .query[Job]
      .stream
      .transact(transactor)
      .compile
      .toList

  override def create(job: Job): F[UUID] =
    sql"""
      INSERT INTO jobs(
        company,
        title,
        description,
        externalUrl,
        salaryLo,
        salaryHi,
        currency,
        remote,
        location,
        country
      ) VALUES (
        ${job.company},
        ${job.title},
        ${job.description},
        ${job.externalUrl},
        ${job.salaryLo},
        ${job.salaryHi},
        ${job.currency},
        ${job.remote},
        ${job.location},
        ${job.country}
      )
    """.update
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(transactor)
