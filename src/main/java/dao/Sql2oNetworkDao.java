package dao;


import models.Network;
import models.Show;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import sun.nio.ch.Net;

import java.util.List;

public class Sql2oNetworkDao implements NetworkDao {

    private final Sql2o sql2o;
    public Sql2oNetworkDao(Sql2o sql2o) {
        this.sql2o =sql2o;
    }

    @Override
    public void add(Network network) {
        String sql = "INSERT INTO networks (name, availability) VALUES (:name, :availability)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(network)
                    .executeUpdate()
                    .getKey();
            network.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Network findById(int id) {
        String sql = "SELECT * FROM networks WHERE id = :id";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Network.class);
        }
    }

    @Override
    public List<Network> getAll() {
        String sql = "SELECT * FROM networks";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .executeAndFetch(Network.class);
        }
    }
    @Override
    public List<Show> getAllShowsByNetwork(int networkId) {
        String sql = "SELECT * FROM shows WHERE networkId = :networkId";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("networkId", networkId)
                    .executeAndFetch(Show.class);
        }
    }

    @Override
    public List<Network> getAllByAvailability(String availability) {
        String sql = "SELECT * FROM networks WHERE availability = :availability";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("availability", availability)
                    .executeAndFetch(Network.class);
        }
    }

    @Override
    public void update(int id, String name, String availability) {
        String sql = "UPDATE networks SET name = :name, availability = :availability WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("availability", availability)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from networks WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
    } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllNetwork() {
        String sql = "DELETE FROM networks";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
