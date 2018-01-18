package dao;

import models.Review;

import java.util.List;

public interface ReviewDao {

    //create
      void add(Review review); //add review to db
    //read
      Review findById(int id); //return specific review
      List<Review> getAll(); //return all reviews
      List<Review> getAllByRating(int rating);

    //update

    void update(int id, String content, int rating);

    //delete

    void deleteById(int id); //delete a single review
    public void clearAllReviews(); //delete all reviews
}
