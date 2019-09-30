package RobTheMuseum;

public class Room {
    private int number; //the number of the room
    private int[] connections; //what rooms the room connects to
    private String background; //the path to the appropriate background image
    
    public Room(int n, int[]c) {
        number = n;
        connections = c;
        determineBackground();
    }

    private void determineBackground() {
        //get the path to the file for the room's background image based on where it can connect
        background = "./sprites/MuseumBackground";
        String connectionCode = "";
        for(int c : connections) {
            if(c == -1) connectionCode += "0"; //if there is no connection (connects to -1), add 0 to connectionCode
            else connectionCode += "1"; //if there is a connection (connects to an integer >= 0), add 1 to connectionCode
        }
        
        //add the connectionCode and the file extension to the path
        background += connectionCode + ".png";
    }
    
    public int getNumber() {
        return number;
    }

    public int[] getConnections() {
        return connections;
    }
    
    public String getBackground() {
        return background;
    }
}
