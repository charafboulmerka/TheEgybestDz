
package com.mawsome20.aflam20.models.single_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    @SerializedName("videosId")
    @Expose
    private String videosId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("release")
    @Expose
    private String release;
    @SerializedName("runtime")
    @Expose
    private String runtime;
    @SerializedName("videoQuality")
    @Expose
    private String videoQuality;
    @SerializedName("isTvseries")
    @Expose
    private String isTvseries;
    @SerializedName("isPaid")
    @Expose
    private String isPaid;
    @SerializedName("enableDownload")
    @Expose
    private String enableDownload;
    @SerializedName("downloadLinks")
    @Expose
    private List<DownloadLink> downloadLinks = null;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("posterUrl")
    @Expose
    private String posterUrl;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;
    @SerializedName("genre")
    @Expose
    private List<Genre> genre = null;
    @SerializedName("country")
    @Expose
    private List<Country> country = null;
    @SerializedName("director")
    @Expose
    private List<Director> director = null;
    @SerializedName("writer")
    @Expose
    private List<Writer> writer = null;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("castAndCrew")
    @Expose
    private List<CastAndCrew> castAndCrew = null;
    @SerializedName("relatedMovie")
    @Expose
    private List<RelatedMovie> relatedMovie = null;

    public String getVideosId() {
        return videosId;
    }

    public void setVideosId(String videosId) {
        this.videosId = videosId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getIsTvseries() {
        return isTvseries;
    }

    public void setIsTvseries(String isTvseries) {
        this.isTvseries = isTvseries;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(String enableDownload) {
        this.enableDownload = enableDownload;
    }

    public List<DownloadLink> getDownloadLinks() {
        return downloadLinks;
    }

    public void setDownloadLinks(List<DownloadLink> downloadLinks) {
        this.downloadLinks = downloadLinks;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public List<Director> getDirector() {
        return director;
    }

    public void setDirector(List<Director> director) {
        this.director = director;
    }

    public List<Writer> getWriter() {
        return writer;
    }

    public void setWriter(List<Writer> writer) {
        this.writer = writer;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<CastAndCrew> getCastAndCrew() {
        return castAndCrew;
    }

    public void setCastAndCrew(List<CastAndCrew> castAndCrew) {
        this.castAndCrew = castAndCrew;
    }

    public List<RelatedMovie> getRelatedMovie() {
        return relatedMovie;
    }

    public void setRelatedMovie(List<RelatedMovie> relatedMovie) {
        this.relatedMovie = relatedMovie;
    }

}
