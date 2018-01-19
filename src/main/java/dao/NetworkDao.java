package dao;


import models.Network;
import models.Show;

import java.util.List;

public interface NetworkDao {

    //create
    public void add(Network network); //add network to db/

    //read
    public Network findById(int id); //return specific network
    public List<Network> getAll(); //return all shows
    public List<Show> getAllShowsByNetwork(int networkId);  //return all shows from a network
    public List<Network> getAllByAvailability(String availability); //returns all by name availability

    //update

    public void update(int id, String name, String availability);

    //delete

    public void deleteById(int id); //delete a single network
    public void deleteAllShowsByNetwork(int networkId);
    public void clearAllNetwork(); //delete all networks
}
