package RobTheMuseum;

import java.util.ArrayList;
import javax.swing.JLabel;

public abstract class MovingCharacter extends GameCharacter {
    private int moveSpeed;
    private boolean[] movementDirections; //index 0 is up, 1 is right, 2 is down, 3 is left
    private boolean ignoresBarriers; //if the character can move through nonmoving characters (pedestals, etc.)
    
    public MovingCharacter(int r) {
        super(r);
        movementDirections = new boolean[4];
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }
    
    public boolean[] getMovementDirections() {
        return movementDirections;
    }

    public boolean getIgnoresBarriers() {
        return ignoresBarriers;
    }
    
    public void setMoveSpeed(int s) {
        moveSpeed = s;
    }
    
    public void setMovementDirections(boolean[] md) {
        movementDirections = md;
    }
    
    public void setIgnoresBarriers(boolean ib) {
        ignoresBarriers = ib;
    }
    
    protected void moveInRoom(int[] roomBounds, ArrayList<GameCharacter> nonMovingCharacters) {
        //move within the room
        //move the graphic as long as it does not pass the boundaries of the nonmoving characters
        JLabel graphic = getGraphic();
        int x = graphic.getX();
        int y = graphic.getY();
        if(movementDirections[0]) { //moving up
            //if the speed is greater than the distance to the boundary, reduce the speed until it is equal or less than that distance
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                
                boolean canMove = true;
                if(!ignoresBarriers) {
                    for(GameCharacter c : nonMovingCharacters) {
                        if(y-ableSpeed < c.getGraphic().getY()+c.getGraphic().getHeight() &&
                           x+graphic.getWidth() > c.getGraphic().getX() &&
                           x < c.getGraphic().getX()+c.getGraphic().getWidth()) {
                            canMove = false;

                            if(y+graphic.getHeight() <= c.getGraphic().getY()) canMove = true;
                        }
                    }
                }
                
                if(y-ableSpeed < roomBounds[0]) canMove = false;
                
                if(canMove) {
                    y -= ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[1]) { //moving right
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                
                boolean canMove = true;
                if(!ignoresBarriers) {
                    for(GameCharacter c : nonMovingCharacters) {
                        if(x+graphic.getWidth()+ableSpeed > c.getGraphic().getX() &&
                           y+graphic.getHeight() > c.getGraphic().getY() &&
                           y < c.getGraphic().getY()+c.getGraphic().getHeight()) {
                            canMove = false;

                            if(x >= c.getGraphic().getX()+c.getGraphic().getWidth()) canMove = true;
                        }
                    }
                }
                
                if(x+graphic.getWidth()+ableSpeed > roomBounds[1]) canMove = false;
                
                if(canMove) {
                    x += ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[2]) { //moving down
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                
                boolean canMove = true;
                if(!ignoresBarriers) {
                    for(GameCharacter c : nonMovingCharacters) {
                        if(y+graphic.getHeight()+ableSpeed > c.getGraphic().getY() &&
                           x+graphic.getWidth() > c.getGraphic().getX() &&
                           x < c.getGraphic().getX()+c.getGraphic().getWidth()) {
                            canMove = false;

                            if(y >= c.getGraphic().getY()+c.getGraphic().getHeight()) canMove = true;
                        }
                    }
                }
                
                if(y+graphic.getHeight()+ableSpeed > roomBounds[2]) canMove = false;
                
                if(canMove) {
                    y += ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[3]) { //moving left
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                
                boolean canMove = true;
                if(!ignoresBarriers) {
                    for(GameCharacter c : nonMovingCharacters) {
                        if(x-ableSpeed < c.getGraphic().getX()+c.getGraphic().getWidth() &&
                           y+graphic.getHeight() > c.getGraphic().getY() &&
                           y < c.getGraphic().getY()+c.getGraphic().getHeight()) {
                            canMove = false;

                            if(x+graphic.getWidth() <= c.getGraphic().getX()) canMove = true;
                        }
                    }
                }
                
                if(x-ableSpeed < roomBounds[3]) canMove = false;
                
                if(canMove) {
                    x -= ableSpeed;
                    break;
                }
            }
        }
        graphic.setLocation(x, y);
        setGraphic(graphic);
    }
    
    protected void moveBetweenRooms(int[] connections) {
        //check to see if the character is moving between rooms
        int direction = -1;
        if(getGraphic().getX() >= 265 && getGraphic().getX() <= 293 && getGraphic().getY() == 90 && movementDirections[0]) {
            direction = 0;
        }
        if(getGraphic().getY() >= 260 && getGraphic().getY() <= 278 && getGraphic().getX() == 510-42 && movementDirections[1]) {
            direction = 1;
        }
        if(getGraphic().getX() >= 265 && getGraphic().getX() <= 293 && getGraphic().getY() == 510-62 && movementDirections[2]) {
            direction = 2;
        }
        if(getGraphic().getY() >= 260 && getGraphic().getY() <= 278 && getGraphic().getX() == 90 && movementDirections[3]) {
            direction = 3;
        }
        //if they are
        if(direction != -1) {
            //if the room exists, move the character to that room
            if(connections[direction] != -1) {
                setRoom(connections[direction]);

                //reset the character's position within the room
                JLabel graphic = getGraphic();
                int x = graphic.getX();
                int y = graphic.getY();
                if(direction == 0) y = 510-62;
                if(direction == 1) x = 90;
                if(direction == 2) y = 90;
                if(direction == 3) x = 510-42;
                graphic.setLocation(x, y);
                setGraphic(graphic);
            }
        }
    }
    
    @Override
    public void update(int[] roomBounds, int[] connections, Player player, ArrayList<GameCharacter> nonMovingCharacters) {
        //move characters within the room
        moveInRoom(roomBounds, nonMovingCharacters);
        
        //move characters between rooms
        moveBetweenRooms(connections);
    }
}
