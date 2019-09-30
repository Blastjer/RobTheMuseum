package RobTheMuseum;

import javax.swing.JLabel;

public abstract class MovingCharacter extends GameCharacter {
    private int moveSpeed;
    private boolean[] movementDirections; //index 0 is up, 1 is right, 2 is down, 3 is left
    
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

    public void setMoveSpeed(int s) {
        moveSpeed = s;
    }
    
    public void setMovementDirections(boolean[] md) {
        movementDirections = md;
    }
    
    private void moveInRoom(int[] roomBounds) {
        //move within the room
        //move the graphic as long as it does not pass the boundaries of the room
        JLabel graphic = getGraphic();
        int x = graphic.getX();
        int y = graphic.getY();
        if(movementDirections[0]) { //moving up
            //if the speed is greater than the distance to the boundary, reduce the speed until it is equal or less than that distance
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                if(y-ableSpeed >= roomBounds[0]) {
                    y -= ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[1]) { //moving right
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                if(x+graphic.getWidth()+ableSpeed <= roomBounds[1]) {
                    x += ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[2]) { //moving down
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                if(y+graphic.getHeight()+ableSpeed <= roomBounds[2]) {
                    y += ableSpeed;
                    break;
                }
            }
        }
        if(movementDirections[3]) { //moving left
            for(int ableSpeed = moveSpeed; ableSpeed > 0; ableSpeed--) {
                if(x-ableSpeed >= roomBounds[3]) {
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
    public void update(int[] roomBounds, int[] connections, Player player) {
        //move characters within the room
        moveInRoom(roomBounds);
        
        //move characters between rooms
        moveBetweenRooms(connections);
    }
}
