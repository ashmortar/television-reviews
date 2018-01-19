package dao;


import models.Review;
import models.Show;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oShowDao implements ShowDao{
    private final Sql2o sql2o;

    public Sql2oShowDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void add(Show show) {
        String sql = "INSERT INTO shows (title, summary, releaseDate, networkId, seasons) VALUES (:title, :summary, :releaseDate, :networkId, :seasons)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(show)
                    .executeUpdate()
                    .getKey();
            show.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Show findById(int id) {
        String sql = "SELECT * FROM shows WHERE id= :id";
        try(Connection con =sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Show.class);
        }
    }

    @Override
    public List<Show> getAll() {
        String sql = "SELECT * FROM shows";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Show.class);
        }
    }

    @Override
    public List<Review> getAllReviewsByShow(int showId) {
        String sql = "SELECT * FROM reviews WHERE showId = :showId";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("showId", showId)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Show> getAllByName() {
        String sql = "SELECT * FROM shows ORDER BY title ASC";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Show.class);
        }
    }

    @Override
    public void update(int id, String summary, int seasons) {
        String sql = "UPDATE shows SET summary = :summary, seasons = :seasons WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("summary", summary)
                    .addParameter("seasons", seasons)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM shows WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllShows() {
        String sql = "DELETE FROM shows";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAllReviewsByShow(int showId) {
        String sql = "DELETE FROM reviews WHERE showId = :showId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("showId", showId)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
