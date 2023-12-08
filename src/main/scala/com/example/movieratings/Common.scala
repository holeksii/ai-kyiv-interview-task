package com.example.movieratings

import java.io.File
import org.apache.commons.csv.CSVRecord

object Common {

  def generateReport(
      movies: Map[String, List[String]],
      reviews: List[File]
  ): List[List[Any]] = {
    reviews
      .map { file =>
        val (movieId, reviews) = readReviews(file)
        getRatedEntry(movieId, reviews, movies)
      }
  }

  def getRatedEntry(
      movieId: String,
      reviews: List[CSVRecord],
      movies: Map[String, List[String]]
  ): List[Any] = {
    val movie = movies.get(movieId)
    val averageRating = calcAverageRating(reviews)
    val totalReviews = reviews.size

    List(movie.get(1), movie.get(0), averageRating, totalReviews)
  }

  def calcAverageRating(ratings: List[CSVRecord]): Double = {
    val sum = ratings.map(_.get(1).toDouble).sum
    sum / ratings.size
  }

  def readMovies(path: File): List[CSVRecord] = {
    CsvUtils
      .readFromFileAsList(path)
      .filter(m => !m.get(1).isNullOrEmpty)
  }

  def readReviews(path: File): (String, List[CSVRecord]) = {
    val entries = CsvUtils
      .readFromFileAsList(path)
    val movieId = entries.head.get(0).dropRight(1)
    val reviews = entries.tail
    (movieId, reviews)
  }

  def writeReport(report: List[List[Any]], path: File): Unit = {
    CsvUtils.writeToFile(report, path)
  }

  def getMovieRatingsPath(dir: String, movieId: String): String =
    dir + File.separator + "mv_" + ("0" * (7 - movieId.length)) + movieId + ".txt"

  implicit class StringExtensions(s: String) {
    def isNullOrEmpty: Boolean =
      s == null || s.isEmpty || s.toLowerCase == "null"
  }
}
