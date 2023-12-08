package com.example.movieratings

import java.io.File

object Common {

  def generateReport(
      movies: Map[Int, Movie],
      reviewFiles: List[File]
  ): List[Report] = {
    reviewFiles
      .map { file =>
        val (movieId, reviews) = readReviews(file)
        getRatedEntry(movieId, reviews, movies)
      }
  }

  def getRatedEntry(
      movieId: Int,
      reviews: List[Review],
      movies: Map[Int, Movie]
  ): Report = {
    val movie = movies.get(movieId)
    val averageRating = calcAverageRating(reviews)
    val totalReviews = reviews.size

    Report(
      movie.map(_.title).getOrElse(""),
      movie.map(_.year).getOrElse(0),
      averageRating,
      totalReviews
    )
  }

  def calcAverageRating(reviews: List[Review]): Double = {
    val sum = reviews.map(_.rating).sum
    sum / reviews.size.toDouble
  }

  def getMovieRatingsPath(dir: String, movieId: Int): String =
    dir + File.separator + "mv_" + ("0" * (7 - movieId.toString.length)) + movieId + ".txt"

  def readMovies(path: File): List[Movie] = {
    CsvUtils
      .readFromFileAsList(path)
      .filter(m => !m.get(1).isNullOrEmpty)
      .map(Movie(_))
  }

  def readReviews(path: File): (Int, List[Review]) = {
    val entries = CsvUtils
      .readFromFileAsList(path)
    val movieId = entries.head.get(0).dropRight(1).toInt
    val reviews = entries.tail.map(Review(_))
    (movieId, reviews)
  }

  implicit class StringExtensions(s: String) {
    def isNullOrEmpty: Boolean =
      s == null || s.isEmpty || s.toLowerCase == "null"
  }
}
