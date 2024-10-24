package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Get X and Y coordinates of the mouse click
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Perform some action with the coordinates, e.g., print or pass to game logic
        System.out.println("Mouse clicked at X: " + mouseX + ", Y: " + mouseY);
        gp.handleMouseClick(mouseX, mouseY); // Pass the coordinates to the game panel if needed
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Optionally handle mouse release logic here
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Optionally handle single-click logic here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Optionally handle mouse entering the component area
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Optionally handle mouse exiting the component area
    }
}
