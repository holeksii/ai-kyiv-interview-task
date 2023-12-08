package com.example.movieratings

import java.io.File

object Common {

  def generateReport(
      movies: Map[String, Array[String]],
      reviews: List[File],
      minRatings: Int
  ): List[List[Any]] = {
    reviews
      .map(getRatedEntry(_, movies, minRatings))
      .filter(_.isDefined)
      .map(_.get)
  }

  def getRatedEntry(
      reviews: File,
      movies: Map[String, Array[String]],
      minRatings: Int
  ): Option[List[Any]] = {
    val ratings = CsvUtils.readFromFileAsList(reviews)

    if (ratings.size < minRatings) {
      None
    } else {

      val movieId = ratings.head.get(0).dropRight(1)
      val movie = movies.get(movieId)
      val averageRating = calcAverageRating(ratings.tail.map(_.values))
      val totalReviews = ratings.size

      Some(List(movie.get(1), movie.get(0), averageRating, totalReviews))
    }
  }

  def calcAverageRating(ratings: List[Array[String]]): Double = {
    val sum = ratings.map(_(1).toDouble).sum
    sum / ratings.size
  }

  def getMovieRatingsPath(dir: String, movieId: String): String =
    dir + File.separator + "mv_" + ("0" * (7 - movieId.length)) + movieId + ".txt"

  implicit class StringExtensions(s: String) {
    def isNullOrEmpty: Boolean =
      s == null || s.isEmpty || s.toLowerCase == "null"
  }
}
