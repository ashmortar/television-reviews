package models;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private String content;
    private int showId;
    private String reviewerName;
    private LocalDateTime date;
    private int rating;

    public Review(String content, int showId, String reviewerName, LocalDateTime date, int rating) {
        this.content = content;
        this.showId = showId;
        this.reviewerName = reviewerName;
        this.date = date;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (showId != review.showId) return false;
        if (rating != review.rating) return false;
        if (!content.equals(review.content)) return false;
        return reviewerName != null ? reviewerName.equals(review.reviewerName) : review.reviewerName == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + content.hashCode();
        result = 31 * result + showId;
        result = 31 * result + (reviewerName != null ? reviewerName.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }
}
