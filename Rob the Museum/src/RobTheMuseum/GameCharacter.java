package RobTheMuseum;

import java.util.ArrayList;
import javax.swing.JLabel;

public abstract class GameCharacter {
    private JLabel graphic; //the graphical representation of the character
    private int room; //the room that character is in
    
    public GameCharacter(int r) {
        initializeGraphic();
        room = r;
    }
    
    public JLabel getGraphic() {
        return graphic;
    }
    
    public int getRoom() {
        return room;
    }

    public void setGraphic(JLabel g) {
        graphic = g;
    }
    
    public void setRoom(int r) {
        room = r;
    }
    
    public abstract void update(int[] roomBounds, int[] connections, Player player, ArrayList<GameCharacter> nonMovingCharacters);
    protected abstract void initializeGraphic();
}
