package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.Review;
import models.Show;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class Sql2oShowDaoTest {

    private Sql2oShowDao showDao;
    private Sql2oReviewDao reviewDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        showDao = new Sql2oShowDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);


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
    public Show setupShow() {
        return new Show("title", "summary", "2018-01-18", 1, 1);
    }

    @Test
    public void add_writesToDBandAssignsId_true() throws Exception {
        Show show = setupShow();
        int originalId = show.getId();
        showDao.add(show);
        assertNotEquals(originalId, show.getId());
    }

    @Test
    public void findById_returnsCorrectShow_true() throws Exception {
        Show show = setupShow();
        Show secondShow = setupShow();
        secondShow.setTitle("New Show");
        showDao.add(show);
        showDao.add(secondShow);
        assertEquals("New Show", showDao.findById(2).getTitle());
    }

    @Test
    public void getAll_returnsallShowsAddedToDao_tue() throws Exception {
        Show show = setupShow();
        Show secondShow = setupShow();
        Show notAdded = setupShow();
        showDao.add(show);
        showDao.add(secondShow);
        assertEquals(2, showDao.getAll().size());
    }

    @Test
    public void getAllReviewsByShow_returnsAllReviewsWithShowIdSpecified_true() throws Exception {
        Show show = setupShow();
        Show secondShow = setupShow();
        showDao.add(show);
        showDao.add(secondShow);
        Review review = setupReview();
        Review review1 = setupReview();
        Review review2 = setupReview();
        review2.setShowId(2);
        reviewDao.add(review);
        reviewDao.add(review1);
        reviewDao.add(review2);
        assertEquals(2, showDao.getAllReviewsByShow(1).size());
    }

    @Test
    public void getAllByName_returnsAllShowsByNameAlphabetized_true() throws Exception {
        Show show = setupShow();
        Show secondShow = setupShow();
        show.setTitle("ZTitle");
        showDao.add(show);
        showDao.add(secondShow);
        List<Show> expectedOrder = new ArrayList<>();
        expectedOrder.add(show);
        expectedOrder.add(secondShow);
        assertTrue(expectedOrder.equals(showDao.getAllByName()));
    }
}