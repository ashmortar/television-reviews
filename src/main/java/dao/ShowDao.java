package dao;

import models.Review;
import models.Show;

import java.util.List;

public interface ShowDao {


    //create
    void add(Show show); //add show to db

    //read
    Show findById(int id); //return specific show
    List<Show> getAll(); //return all shows
    List<Review> getAllReviewsByShow(int showId); //return all reviews for a show
    List<Show> getAllByName(); //returns all by name alphabetized

    //update

    void update(int id, String summary, int seasons);

    //delete

    void deleteById(int id); //delete a single show
    public void clearAllShows(); //delete all shows
}
