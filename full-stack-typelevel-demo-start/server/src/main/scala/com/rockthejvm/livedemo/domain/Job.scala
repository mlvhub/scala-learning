package com.rockthejvm.livedemo.domain

object job:
  case class Job(
      company: String,
      title: String,
      description: String,
      externalUrl: String,
      salaryLo: Option[Int],
      salaryHi: Option[Int],
      currency: Option[String],
      remote: Boolean,
      location: String,
      country: Option[String]
  )

object Job:
  val dummy = job.Job(
    company = "Rock the JVM",
    title = "Software Engineer",
    description = "You will be a software engineer",
    externalUrl = "https://rockthejvm.com",
    salaryLo = Some(10000),
    salaryHi = Some(20000),
    currency = Some("USD"),
    remote = true,
    location = "San Francisco, CA",
    country = Some("US")
  )
