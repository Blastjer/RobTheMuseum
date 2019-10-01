package RobTheMuseum;

import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JLabel;

public class Guard extends MovingCharacter {
    
    private final int CHASING_PLAYER = 0;
    private final int SEEKING_CENTER = 1;
    private final int MOVING_TO_ROOM = 2;
    
    private int movementPhase; //uses constants to determine movement
    private int patrolDirection; //the direction the guard moves when patrolling (-1 unless movementPhase = MOVING_TO_ROOM)
    
    public Guard(int r) {
        super(r);
        setMoveSpeed(1);
        movementPhase = SEEKING_CENTER;
        patrolDirection = -1;
    }
    
    //TODO: fix the movement choosing (sometimes it will just go strait (too often to be random), sometimes it will move to a corner, it will bounce off of the wall)
    private void determineMoveDirections(int playerX, int playerY, int playerRoom) {
        int guardX = getGraphic().getX();
        int guardY = getGraphic().getY();
        boolean[] movementDirections = getMovementDirections();
        
        //chase the player if they are in the same room
        if(getRoom() == playerRoom) movementPhase = CHASING_PLAYER;
        else if(movementPhase != MOVING_TO_ROOM) movementPhase = SEEKING_CENTER;
        if(movementPhase == CHASING_PLAYER/* && getRoom() == playerRoom*/) {
            //reset the movement directions
            movementDirections = resetMovementDirections(movementDirections);
            
            setMoveSpeed(2);
            
            //the guard moves in the direction of the player
            if(playerY < guardY) movementDirections[0] = true; //move up
            else movementDirections[0] = false;
            if(playerX > guardX) movementDirections[1] = true; //move right
            else movementDirections[1] = false;
            if(playerY > guardY) movementDirections[2] = true; //move down
            else movementDirections[2] = false;
            if(playerX < guardX) movementDirections[3] = true; //move left
            else movementDirections[3] = false;
        }
        //otherwise, the guard patrols the museum randomly
        else {
            setMoveSpeed(1);
            
            //go to the center of the room
            int centerX = 279;
            int centerY = 270;
            if((guardX != centerX || guardY != centerY) && movementPhase != MOVING_TO_ROOM) movementPhase = SEEKING_CENTER;
            else movementPhase = MOVING_TO_ROOM;
            if(movementPhase == SEEKING_CENTER) {
                //reset the movement directions
                movementDirections = resetMovementDirections(movementDirections);
                
                if(centerY < guardY) movementDirections[0] = true; //move up
                else movementDirections[0] = false;
                if(centerX > guardX) movementDirections[1] = true; //move right
                else movementDirections[1] = false;
                if(centerY > guardY) movementDirections[2] = true; //move down
                else movementDirections[2] = false;
                if(centerX < guardX) movementDirections[3] = true; //move left
                else movementDirections[3] = false;
            }
            //randomly go to another room
            if(movementPhase == MOVING_TO_ROOM) {
                if(patrolDirection == -1) {
                    //reset the movement directions
                    movementDirections = resetMovementDirections(movementDirections);
            
                    patrolDirection = (int)(Math.random() * 4);
                    for(int i = 0; i < movementDirections.length; i++) {
                        if(i == patrolDirection) {
                            movementDirections[i] = true;
                            break;
                        }
                        else movementDirections[i] = false;
                    }
                }
            }
        }
        setMovementDirections(movementDirections);
        
        //allow guard to walk through nonmoving characters if they aren't chasing the player
        if(movementPhase == CHASING_PLAYER) setIgnoresBarriers(false);
        else setIgnoresBarriers(true);
    }
    
    private boolean[] resetMovementDirections(boolean[] md) {
        for(boolean d : md) {
            d = false;
        }
        setMovementDirections(md);
        patrolDirection = -1;
        return md;
    }
    
    private void catchPlayer(int playerX, int playerY) {
        int guardX = getGraphic().getX();
        int guardY = getGraphic().getY();
        //if the guard is close enought to the center of the player, the player is caught
        if(guardX+getGraphic().getWidth() > playerX+16 && //caught from the left
           guardX < playerX+26 && //caught from the right
           guardY+getGraphic().getHeight() > playerY+16 && //caught from the top
           guardY < playerY+46) { //caught from the bottom
            System.out.println("You lose");
            System.exit(0);
        }
    }
    
    @Override
    public void moveInRoom(int[] rb, ArrayList<GameCharacter> nmc) {
        //get the original location
        JLabel oldGraphic = getGraphic();
        Point oldLocation = new Point(oldGraphic.getX(), oldGraphic.getY());
        
        //move
        super.moveInRoom(rb, nmc);
        
        //get the new location
        JLabel newGraphic = getGraphic();
        Point newLocation = new Point(newGraphic.getX(), newGraphic.getY());
        
        //if the guard hasn't moved, make them look asleep
        if(newLocation.equals(oldLocation)) newGraphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Guard_sleeping.png")));
        //otherwise, make them look awake
        else newGraphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Guard.png")));
        
        //update the image
        setGraphic(newGraphic);
    }
    
    @Override
    public void moveBetweenRooms(int[] connections) {
        int oldRoom = getRoom();
        super.moveBetweenRooms(connections);
        
        //when the guard enters a new room, reset its patrol pattern
        int newRoom = getRoom();
        if(oldRoom != newRoom) {
            movementPhase = SEEKING_CENTER;
            patrolDirection = -1;
        }
    }
    
    @Override
    public void update(int[] roomBounds, int[] connections, Player player, ArrayList<GameCharacter> nmc) { //nmc won't be used here
        int playerX = player.getGraphic().getX();
        int playerY = player.getGraphic().getY();
        int playerRoom = player.getRoom();
        
        //determine movement direction
        determineMoveDirections(playerX, playerY, playerRoom);

        //toggle visibility depending on whether or not the guard and the player are in the same room
        if(getRoom() == playerRoom) getGraphic().setVisible(true);
        else getGraphic().setVisible(false);

        //try to catch the player if they are in the same room
        if(getRoom() == playerRoom) catchPlayer(playerX, playerY);
        
        super.update(roomBounds, connections, player, nmc);
    }
    
    @Override
    protected void initializeGraphic() {
        JLabel graphic = new JLabel();
        graphic.setSize(42, 62);
        graphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Guard.png")));
        graphic.setLocation(280, 300);
        setGraphic(graphic);
    }
}
