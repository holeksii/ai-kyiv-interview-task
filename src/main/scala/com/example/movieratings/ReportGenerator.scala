package com.example.movieratings

import scala.annotation.tailrec
import scala.util.Try
import java.io.File
import CsvUtils.{readFromFileAsList, writeToFile}
import org.apache.commons.csv.CSVRecord
import Common._

object ReportGenerator {

  val minRatings = 1000
  val yearBounds = (1970, 1990)

  def main(args: Array[String]): Unit = {
    val Array(movieTitlesPath, trainingSetPath, reportPath) = args

    val movies = readFromFileAsList(new File(movieTitlesPath))
      .filter(m =>
        !m.get(1).isNullOrEmpty
          && m.get(1).toInt >= yearBounds._1
          && m.get(1).toInt <= yearBounds._2
      )
      .map(_.values)

    val ratingFiles = movies
      .map(m => Try(new File(getMovieRatingsPath(trainingSetPath, m(0)))))
      .filter(_.isSuccess)
      .map(_.get)

    val report = generateReport(
      movies.map(m => (m(0), Array(m(1), m(2)))).toMap,
      ratingFiles,
      minRatings
    )

    writeToFile(report, new File(reportPath))
  }
}
