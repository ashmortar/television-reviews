package dao;


import com.sun.org.apache.regexp.internal.RE;
import models.Review;
import org.sql2o.Sql2o;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oReviewDao implements ReviewDao {

    private final Sql2o sql2o;
    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) {
        String sql = "INSERT INTO reviews (content, showId, reviewerName, rating) VALUES (:content, :showId, :reviewerName, :rating)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(review)
                    .executeUpdate()
                    .getKey();
            review.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Review findById(int id) {
        String sql = "SELECT * FROM reviews WHERE id = :id";
        try(Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Review.class);
        }
    }

    @Override
    public List<Review> getAll() {
        String sql = "SELECT * FROM reviews";
        try(Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> getAllByRating(int rating) {
        String sql = "SELECT * FROM reviews WHERE rating = :rating";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("rating", rating)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public void update(int id, String content, int rating) {
        String sql = "UPDATE reviews SET content = :content, rating = :rating WHERE id = :id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("content", content)
                    .addParameter("rating", rating)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from reviews WHERE id = :id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllReviews() {
        String sql = "DELETE FROM reviews";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
