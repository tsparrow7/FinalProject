package com.example.tjgaming.finalproject.Model.TheMovieDB;

import java.util.List;

/**
 * Created by TJ on 11/28/2018.
 */
public class TMDBMovie {

    private String movieId;
    private String moviePosterUrl;
    private String movieTitle;
    private double movieRating;
    private List<String> genreIds;

    public TMDBMovie() {
    }

    public TMDBMovie(String movieId, String moviePosterUrl, String movieTitle, double movieRating, List<String> genreIds) {
        this.movieId = movieId;
        this.moviePosterUrl = moviePosterUrl;
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.genreIds = genreIds;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
