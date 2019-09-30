package RobTheMuseum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Museum {
    private ArrayList<Room> rooms;
    
    public Museum(String mn) {
        rooms = new ArrayList<>();
        readMuseum(mn);
    }
    
    private void readMuseum(String museumName) {
        try {
            //read the museum from file
            Scanner scan = new Scanner(new File(museumName));
            int roomNumber = 0;
            while(scan.hasNextLine()) {
                //separate the input into an array of room connections
                String[] input = scan.nextLine().split(",");
                int[] connections = new int[4];
                for(int i = 0; i < input.length; i++) {
                    connections[i] = Integer.parseInt(input[i]);
                }
                
                //create a new Room object and add to list
                Room r = new Room(roomNumber, connections);
                rooms.add(r);
                
                //increment the roomNumber
                roomNumber++;
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
