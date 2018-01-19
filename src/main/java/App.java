import dao.Sql2oNetworkDao;
import dao.Sql2oReviewDao;
import dao.Sql2oShowDao;
import models.Network;
import models.Review;
import models.Show;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import sun.misc.resources.Messages_pt_BR;
import sun.nio.ch.Net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        //tells spark/handelbars where to find images, css
        staticFileLocation("/public");

        //connect to database
        String connectionString = "jdbc:h2:~/televisionreviews.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");

        //load in daos for use in routes
        Sql2oReviewDao reviewDao = new Sql2oReviewDao(sql2o);
        Sql2oShowDao showDao = new Sql2oShowDao(sql2o);
        Sql2oNetworkDao networkDao = new Sql2oNetworkDao(sql2o);


        //=====================  routes ==========================//

        //get: homepage
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();        //

            //add all data to path
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            List<Show> shows = showDao.getAll();
            model.put("shows", shows);
            List<Review> reviews = reviewDao.getAll();
            model.put("reviews", reviews);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        //delete all routes (need to be before dynamic ids)

        //get: delete all networks
        get("/networks/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            networkDao.clearAllNetwork();
            return new ModelAndView(model, "network-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all shows
        get("/shows/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            showDao.clearAllShows();
            return new ModelAndView(model, "show-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all reviews
        get("/reviews/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            reviewDao.clearAllReviews();
            return new ModelAndView(model, "review-success.hbs");
        }, new HandlebarsTemplateEngine());

        //create routes

        //get: show a form to create a new Network
        get("/networks/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            return new ModelAndView(model, "network-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new network form
        post("/networks/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            String name = request.queryParams("name");
            String availability = request.queryParams("availability");
            Network newNetwork = new Network(name, availability);
            networkDao.add(newNetwork);

            return new ModelAndView(model, "network-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to create a new show
        get("networks/:networkId/shows/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            int networkId = Integer.parseInt(request.params(":networkId"));
            Network network = networkDao.findById(networkId);

            model.put("network", network);

            return new ModelAndView(model, "show-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new show form
        post("networks/:networkId/shows/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            int networkId = Integer.parseInt(request.params(":networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            String title = request.queryParams("title");
            String summary = request.queryParams("summary");
            String releaseDate = request.queryParams("releaseDate");
            int seasons = Integer.parseInt(request.queryParams("seasons"));

            Show newShow = new Show(title, summary, releaseDate, networkId, seasons);
            showDao.add(newShow);
            return new ModelAndView(model, "show-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to create a new review
        get("/networks/:networkId/shows/:showId/reviews/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            int networkId = Integer.parseInt(request.params(":networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params(":showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);

            return new ModelAndView(model, "review-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new review form
        post("/networks/:networkId/shows/:showId/reviews/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            int networkId = Integer.parseInt(request.params(":networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params(":showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);

            String content = request.queryParams("content");
            String reviewerName = request.queryParams("reviewerName");
            int rating = Integer.parseInt(request.queryParams("rating"));

            Review newReview = new Review(content, showId, reviewerName, rating);
            reviewDao.add(newReview);
            return new ModelAndView(model, "review-success.hbs");
        }, new HandlebarsTemplateEngine());


        //read routes

        //get: show all networks
        get("/networks", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //add all networks to model
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);

            return new ModelAndView(model, "networks.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a single network and list shows
        get("/networks/:networkId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            List<Show> shows = networkDao.getAllShowsByNetwork(networkId);
            model.put("shows", shows);

            return new ModelAndView(model, "network-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get: a single show detail and a list of reviews
        get("/networks/:networkId/shows/:showId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);

            List<Review> reviews = showDao.getAllReviewsByShow(showId);
            model.put("reviews", reviews);

            return new ModelAndView(model, "show-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a single review detail
        get("/networks/:networkID/shows/:showId/reviews/:reviewId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);

            int reviewId = Integer.parseInt(request.params("reviewId"));
            Review review = reviewDao.findById(reviewId);
            model.put("review", review);

            return new ModelAndView(model, "review-detail.hbs");
        }, new HandlebarsTemplateEngine());


        //update routes

        //get: show a form to update a network
        get("/networks/:networkId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network networkToEdit = networkDao.findById(networkId);
            model.put("networkToEdit", networkToEdit);

            return new ModelAndView(model, "network-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process update network form
        post("/networks/:networkId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            String newName = request.queryParams("newName");
            String newAvailability = request.queryParams("newAvailability");
            networkDao.update(networkId, newName, newAvailability);

            return new ModelAndView(model, "network-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a show
        get("/networks/:networkId/shows/:showId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);
            int showId = Integer.parseInt(request.params("showId"));
            Show showToEdit = showDao.findById(showId);
            model.put("showToEdit", showToEdit);

            return new ModelAndView(model, "show-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process update show form
        post("/networks/:networkId/shows/:showId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);
            int showId = Integer.parseInt(request.params("showId"));
            String newSummary = request.queryParams("newSummary");
            int newSeasons = Integer.parseInt(request.queryParams("newSeasons"));
            showDao.update(showId, newSummary, newSeasons);

            return new ModelAndView(model, "show-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show form to update a review
        get("/networks/:networkId/shows/:showId/reviews/:reviewId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);
            int reviewId = Integer.parseInt(request.params("reviewId"));
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            Review reviewToEdit = reviewDao.findById(reviewId);
            model.put("reviewToEdit", reviewToEdit);

            return new ModelAndView(model, "review-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process update review form
        post("/networks/:networkId/shows/:showId/reviews/:reviewId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);
            int reviewId = Integer.parseInt(request.params("reviewId"));
            String newContent = request.queryParams("newContent");
            int newRating = Integer.parseInt(request.queryParams("newRating"));

            reviewDao.update(reviewId, newContent, newRating);

            return new ModelAndView(model, "review-success.hbs");
        }, new HandlebarsTemplateEngine());


        //delete routes

        //get: delete a network
        get("/networks/:networkId/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));

            reviewDao.deleteById(networkId);

            return new ModelAndView(model, "network-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete a show from a network
        get("/networks/:networkId/shows/:showId/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));

            showDao.deleteById(showId);

            return new ModelAndView(model, "show-success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete a review from a show
        get("/networks/:networkId/shows/:showId/reviews/:reviewId/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //for navbar
            List<Network> networks = networkDao.getAll();
            model.put("networks", networks);
            int networkId = Integer.parseInt(request.params("networkId"));
            Network network = networkDao.findById(networkId);
            model.put("network", network);

            int showId = Integer.parseInt(request.params("showId"));
            Show show = showDao.findById(showId);
            model.put("show", show);
            int reviewId = Integer.parseInt(request.params("reviewID"));

            reviewDao.deleteById(reviewId);

            return new ModelAndView(model, "review-success.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
