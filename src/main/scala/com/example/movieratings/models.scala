package com.example.movieratings

import java.time.LocalDate
import org.apache.commons.csv.CSVRecord

case class Movie(id: Int, year: Int, title: String)
case class Review(customerId: Int, rating: Int, Date: LocalDate)
case class Report(
    movieTitle: String,
    year: Int,
    averageRating: Double,
    totalReviews: Int
)

object Movie {
  def apply(record: CSVRecord): Movie = {
    Movie(
      record.get(0).toInt,
      record.get(1).toInt,
      record.get(2)
    )
  }
}

object Review {
  def apply(record: CSVRecord): Review = {
    Review(
      record.get(0).toInt,
      record.get(1).toInt,
      LocalDate.parse(record.get(2))
    )
  }
}
