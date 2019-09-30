package RobTheMuseum;

import javax.swing.JLabel;

public class ArtifactPedestal extends GameCharacter {
    
    private boolean hasArtifact;
    
    public ArtifactPedestal(int r) {
        super(r);
        hasArtifact = true;
    }

    public void setHasArtifact(boolean ha) {
        hasArtifact = ha;
    }
    
    @Override
    public void update(int[] r, int[] c, Player p) { //the parameters will not be used
        //toggle visibility depending on whether or not the pedestal and the player are in the same room
        if(getRoom() == p.getRoom()) getGraphic().setVisible(true);
        else getGraphic().setVisible(false);
        
        //if the artifact is gone, change to the empty pedestal
        if(!hasArtifact) getGraphic().setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Pedestal0.png")));
    }
    
    @Override
    protected void initializeGraphic() {
        JLabel graphic = new JLabel();
        graphic.setSize(18, 60);
        graphic.setIcon(new javax.swing.ImageIcon(getClass().getResource("./sprites/Pedestal1.png")));
        graphic.setLocation(292, 270);
        setGraphic(graphic);
    }
}
