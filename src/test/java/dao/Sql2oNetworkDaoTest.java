package dao;

import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import models.Network;
import models.Review;
import models.Show;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import sun.nio.ch.Net;

import static org.junit.Assert.*;


public class Sql2oNetworkDaoTest {

    private Sql2oNetworkDao networkDao;
    private Sql2oShowDao showDao;
    private Sql2oReviewDao reviewDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        networkDao = new Sql2oNetworkDao(sql2o);
        showDao = new Sql2oShowDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);

        conn =sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Network setupNetwork() {
        return new Network("Network", "available");
    }
    public Review setupReview() {
        return new Review("content", 1, "reviewerName", 5);
    }
    public Show setupShow() {
        return new Show("title", "summary", "2018-01-18", 1, 1);
    }

    @Test
    public void add_addUniqueIdToNetworkDatabase_true() {
        Network network = setupNetwork();
        int originalId = network.getId();
        networkDao.add(network);
        assertNotEquals(originalId, network.getId());
    }

    @Test
    public void findById_returnsCorrectInstance_true() throws Exception {
        Network network = setupNetwork();
        network.setName("abc");
        Network secondNetwork = setupNetwork();
        networkDao.add(network);
        networkDao.add(secondNetwork);
        assertEquals("abc", networkDao.findById(1).getName());
    }

    @Test
    public void getAll_returnsAllINstancesOFNetworkaddedToDao() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        Network notAdded = setupNetwork();
        networkDao.add(network);
        networkDao.add(secondNetwork);
        assertEquals(2, networkDao.getAll().size());
    }

    @Test
    public void getAllShowsByNetwork_returnsAllShowsWithCorrespondingNetworkID_true() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        Show showNet1 = setupShow();
        Show showNet12 = setupShow();
        Show showNet2 = setupShow();
        showNet2.setNetworkId(2);
        showDao.add(showNet1);
        showDao.add(showNet2);
        showDao.add(showNet12);
        networkDao.add(network);
        networkDao.add(secondNetwork);
        assertEquals(2, networkDao.getAllShowsByNetwork(1).size());
    }

    @Test
    public void getAllByAvailability_returnsAllShowsWithMatchingAvailability_true() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        Network thirdNetwork = setupNetwork();
        thirdNetwork.setAvailability("subscription");
        networkDao.add(network);
        networkDao.add(secondNetwork);
        networkDao.add(thirdNetwork);
        assertEquals(2, networkDao.getAllByAvailability("available").size());
    }

    @Test
    public void update_ChangesValuesCorreclty_true() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        networkDao.add(network);
        networkDao.add(secondNetwork);
        networkDao.update(1, "abc", "broadcast");
        assertEquals("Network", networkDao.findById(2).getName());
        assertEquals("abc", networkDao.findById(1).getName());
        assertEquals("broadcast", networkDao.findById(1).getAvailability());
    }

    @Test
    public void deleteById_removesCorrectEntry_true() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        networkDao.add(network);
        networkDao.add(secondNetwork);
        networkDao.deleteById(1);
        assertEquals(1, networkDao.getAll().size());
        assertTrue(networkDao.getAll().contains(secondNetwork));
    }

    @Test
    public void clearAllNetwork_removesAllTheNetworks() {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        networkDao.add(network);
        networkDao.add(secondNetwork);
        assertEquals(2, networkDao.getAll().size());
        networkDao.clearAllNetwork();
        assertEquals(0, networkDao.getAll().size());
    }

    @Test
    public void deleteAllShowsByNetwork_removesAllShowsWithThatNetId_true() throws Exception {
        Network network = setupNetwork();
        Network secondNetwork = setupNetwork();
        Show showNet1 = setupShow();
        Show showNet12 = setupShow();
        Show showNet2 = setupShow();
        showNet2.setNetworkId(2);
        showDao.add(showNet1);
        showDao.add(showNet2);
        showDao.add(showNet12);
        networkDao.add(network);
        networkDao.add(secondNetwork);
        assertEquals(3, showDao.getAll().size());
        networkDao.deleteAllShowsByNetwork(1);
        assertEquals(1, showDao.getAll().size());
    }
}