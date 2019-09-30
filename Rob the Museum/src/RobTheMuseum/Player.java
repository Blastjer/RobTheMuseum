package RobTheMuseum;

import javax.swing.JLabel;

public class Player extends MovingCharacter {
    
    private int artifacts;
    
    public Player(int r) {
        super(r);
        setMoveSpeed(3);
        artifacts = 0;
    }

    public int getArtifacts() {
        return artifacts;
    }
    
    public void takeArtifact(ArtifactPedestal ap) {
        int playerX = getGraphic().getX();
        int playerY = getGraphic().getY();
        int pedestalX = ap.getGraphic().getX();
        int pedestalY = ap.getGraphic().getY();
        //if the player is touching the pedestal, take the artifact
        if(playerX+getGraphic().getWidth() >= pedestalX && //taken from the left
           playerX <= pedestalX+ap.getGraphic().getWidth() && //taken from the right
           playerY+getGraphic().getHeight() >= pedestalY && //taken from the top
           playerY <= pedestalY+ap.getGraphic().getHeight()) { //taken from the bottom
            ap.setHasArtifact(false);
            artifacts++;
        }
    }
    
    @Override
    protected void initializeGraphic() {
        JLabel graphic = new JLabel();
        graphic.setSize(42, 62);
        graphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Bob.png")));
        graphic.setLocation(280, 300);
        setGraphic(graphic);
    }
}
