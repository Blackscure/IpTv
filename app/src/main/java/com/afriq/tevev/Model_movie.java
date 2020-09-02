package com.afriq.tevev;




public class Model_movie {
    public String title;
    public String year;
    public String rated;
    public String genre;
    public String released;
    public String runtime;
    public String director;
    public String plot;
    public String poster;
    public String rating;
    public String url;

    public Model_movie(String title, String year, String rated, String genre, String released, String runtime, String director, String plot, String poster, String rating, String url) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.genre = genre;
        this.released = released;
        this.runtime = runtime;
        this.director = director;
        this.plot = plot;
        this.poster = poster;
        this.rating = rating;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getUrl() {
        return url;
    }
}