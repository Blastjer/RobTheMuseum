package RobTheMuseum;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

public class RobTheMuseum extends javax.swing.JFrame {
    
    private Museum museum;
    private int numArtifacts;
    private int[] roomBounds;
    private Timer timer; //the game updates when this goes off
    private ArrayList<GameCharacter> characters;
    private Player player;
    
    public RobTheMuseum() {
        initComponents();
        
        museum = new Museum("./museums/TestMuseum1.txt");
        
        roomBounds = new int[] {90, 510, 510, 90};
        
        //start the timer
        setTimer();
        timer.start();
        
        //initialize the characters
        characters = new ArrayList<>();
        
        //use list to assign characters rooms so they don't repeat (won't start in the same room)
        ArrayList<Integer> roomNums = new ArrayList<>();
        for(int i = 0; i < museum.getRooms().size(); i++) roomNums.add(i);
        
        //add a player
        int playerRoom = (int)(Math.random()*roomNums.size());
        player = new Player(playerRoom);
        characters.add(player);
        roomNums.remove(playerRoom);
        
        //add guards
        int numGuards = 1; //the number of guards in the museum
        for(int i = 1; i <= numGuards; i++) {
            int randRoom = (int)(Math.random()*roomNums.size());
            characters.add(new Guard(roomNums.get(randRoom)));
            roomNums.remove(randRoom);
            JLabel guard = characters.get(i).getGraphic();
            layeredPaneGraphics.add(guard, JLayeredPane.PALETTE_LAYER);
        }
        int currentCharacters = characters.size(); //the number of character that currently exist
        
        //add artifact pedestals
        numArtifacts = 7; //the number of artifacts in the museum
        for(int i = currentCharacters; i < currentCharacters+numArtifacts; i++) {
            int randRoom = (int)(Math.random()*roomNums.size());
            characters.add(new ArtifactPedestal(roomNums.get(randRoom)));
            roomNums.remove(randRoom);
            JLabel pedestal = characters.get(i).getGraphic();
            layeredPaneGraphics.add(pedestal, JLayeredPane.PALETTE_LAYER);
        }
        
        JLabel playerGraphic = player.getGraphic();
        layeredPaneGraphics.add(playerGraphic, JLayeredPane.PALETTE_LAYER);
        validate();
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layeredPaneGraphics = new javax.swing.JLayeredPane();
        labelBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rob the Museum");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(613, 635));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        labelBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RobTheMuseum/MuseumBackground0000.png"))); // NOI18N
        labelBackground.setToolTipText("");
        labelBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelBackground.setFocusable(false);

        layeredPaneGraphics.setLayer(labelBackground, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneGraphicsLayout = new javax.swing.GroupLayout(layeredPaneGraphics);
        layeredPaneGraphics.setLayout(layeredPaneGraphicsLayout);
        layeredPaneGraphicsLayout.setHorizontalGroup(
            layeredPaneGraphicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelBackground)
        );
        layeredPaneGraphicsLayout.setVerticalGroup(
            layeredPaneGraphicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelBackground)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPaneGraphics)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPaneGraphics)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setTimer() {
        int time = 15; //the game will update every 15 miliseconds
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                update();
            }
        };
        timer = new Timer(time, al);
    }
    
    private void update() {
        //if the player has all the artifacts, they win
        if(player.getArtifacts() == numArtifacts) {
            System.out.println("You win");
            System.exit(0);
        }
                
        //update the characters
        for(GameCharacter c : characters) {
            //if c is a MovingCharacter, create an ArrayList of non moving characters (used for movement boundaries)
            ArrayList<GameCharacter> nonMovingCharacters = new ArrayList<>();
            if(c instanceof MovingCharacter) {
                for(GameCharacter gc : characters) {
                    if(!(gc instanceof MovingCharacter) && gc.getRoom() == c.getRoom()) {
                        nonMovingCharacters.add(gc);
                    }
                }
            }
            
            int characterRoom = c.getRoom();
            c.update(roomBounds, museum.getRooms().get(characterRoom).getConnections(), player, nonMovingCharacters);
        }
        
        //change the background image
        String background = museum.getRooms().get(player.getRoom()).getBackground();
        labelBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource(background)));
    }
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        //player movement
        boolean[] directions = player.getMovementDirections();
        //moving up
        if(evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_W) {
            directions[0] = true;
        }
        //moving right
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_D) {
            directions[1] = true;
        }
        //moving down
        if(evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_S) {
            directions[2] = true;
        }
        //moving left
        if(evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_A) {
            directions[3] = true;
        }
        player.setMovementDirections(directions);
        
        //taking artifacts
        if(evt.getKeyCode() == KeyEvent.VK_SPACE) {
            //find the artifact that is in the same room as the player, if there is any
            ArtifactPedestal ap = null;
            int playerRoom = player.getRoom();
            for(GameCharacter c : characters) {
                if(c instanceof ArtifactPedestal && c.getRoom() == playerRoom) {
                    ap = (ArtifactPedestal)c;
                    break;
                }
            }
            if(ap != null) {
                player.takeArtifact(ap);
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        boolean[] directions = player.getMovementDirections();
        //moving up
        if(evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_W) {
            directions[0] = false;
        }
        //moving right
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_D) {
            directions[1] = false;
        }
        //moving down
        if(evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_S) {
            directions[2] = false;
        }
        //moving left
        if(evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_A) {
            directions[3] = false;
        }
        player.setMovementDirections(directions);
    }//GEN-LAST:event_formKeyReleased
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RobTheMuseum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RobTheMuseum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RobTheMuseum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RobTheMuseum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RobTheMuseum().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelBackground;
    private javax.swing.JLayeredPane layeredPaneGraphics;
    // End of variables declaration//GEN-END:variables
}
