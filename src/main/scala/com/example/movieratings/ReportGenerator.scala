package com.example.movieratings

import scala.util.Try
import java.io.File
import Common._

object ReportGenerator {

  val minRatings = 1000
  val yearBounds = (1970, 1990)

  def main(args: Array[String]): Unit = {
    val Array(movieTitlesPath, trainingSetPath, reportPath) = args

    val movies = readMovies(new File(movieTitlesPath))
      .filter(m =>
        m.get(1).toInt >= yearBounds._1
          && m.get(1).toInt <= yearBounds._2
      )

    val ratingFiles = movies
      .map(m => Try(new File(getMovieRatingsPath(trainingSetPath, m.get(0)))))
      .filter(_.isSuccess)
      .map(_.get)

    val report = generateReport(
      movies.map(m => (m.get(0), List(m.get(1), m.get(2)))).toMap,
      ratingFiles
    ).filter(_.last.asInstanceOf[Int] >= minRatings)
      .sortBy(r => (-r(2).asInstanceOf[Double], r(0).asInstanceOf[String]))

    writeReport(report, new File(reportPath))
  }
}
