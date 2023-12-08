package com.example.movieratings

import java.io.File
import Common._

object ReportGenerator {

  val minRatings = 1000
  val yearBounds = (1970, 1990)

  def main(args: Array[String]): Unit = {
    val Array(movieTitlesPath, trainingSetPath, reportPath) = args

    val movies = readMovies(new File(movieTitlesPath))
      .filter(m => m.year >= yearBounds._1 && m.year <= yearBounds._2)

    val ratingFiles = movies
      .map(m => new File(getMovieRatingsPath(trainingSetPath, m.id)))
      .filter(_.exists)

    val report = generateReport(
      movies.map(m => (m.id, m)).toMap,
      ratingFiles
    ).filter(_.totalReviews >= minRatings)
      .sortBy(r => (-r.averageRating, r.movieTitle))

    writeReport(report, new File(reportPath))
  }
}
