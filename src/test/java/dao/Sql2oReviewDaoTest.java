package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oReviewDaoTest {

    private Sql2oReviewDao reviewDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        reviewDao = new Sql2oReviewDao(sql2o); //ignore me for now

        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Review setupReview() {
        return new Review("content", 1, "reviewerName", 5);
    }

    @Test
    public void add_placesObjectInDBAssignsId_true() throws Exception {
        Review review = setupReview();
        int originalId = review.getId();
        reviewDao.add(review);
        assertNotEquals(originalId, review.getId());
    }

    @Test
    public void findById_returnsCorrectReview_true() throws Exception {
        Review review = setupReview();
        Review secondReview = setupReview();
        secondReview.setReviewerName("Fred");
        reviewDao.add(review);
        reviewDao.add(secondReview);
        assertEquals("Fred", reviewDao.findById(2).getReviewerName());
    }

    @Test
    public void getAll_returnsAllReviewsStoredInDAO_true() throws Exception {
        Review review = setupReview();
        Review secondReview = setupReview();
        Review notAdded = setupReview();
        reviewDao.add(review);
        reviewDao.add(secondReview);
        assertEquals(2, reviewDao.getAll().size());
    }

    @Test
    public void getAllByRating_returnsAllReviewsOfACertainRating() throws Exception {
        Review review = setupReview();
        Review review2 = setupReview();
        Review oneStarReview = setupReview();
        oneStarReview.setRating(1);
        reviewDao.add(review);
        reviewDao.add(review2);
        reviewDao.add(oneStarReview);
        assertEquals(2, reviewDao.getAllByRating(5).size());
    }

    @Test
    public void update_changesValuesCorrectly_true() throws Exception {
        Review review = setupReview();
        Review secondReview = setupReview();
        reviewDao.add(review);
        reviewDao.add(secondReview);
        reviewDao.update(1, "new content", 3);
        assertEquals("new content", reviewDao.findById(1).getContent());
        assertEquals(3, reviewDao.findById(1).getRating());
        assertEquals("content", reviewDao.findById(2).getContent());
    }

    @Test
    public void deleteBuId_removesCorrectEntry_true() throws Exception {
        Review review = setupReview();
        Review secondReview = setupReview();
        reviewDao.add(review);
        reviewDao.add(secondReview);
        reviewDao.deleteById(1);
        assertEquals(1, reviewDao.getAll().size());
        assertTrue(reviewDao.getAll().contains(secondReview));
    }

    @Test
    public void clearAllReviews_removesAllInstancesOfReview_true() throws Exception {
        Review review = setupReview();
        Review review2 = setupReview();
        Review review3 = setupReview();
        reviewDao.add(review);
        reviewDao.add(review2);
        reviewDao.add(review3);
        assertEquals(3, reviewDao.getAll().size());
        reviewDao.clearAllReviews();
        assertEquals(0, reviewDao.getAll().size());
    }
}

