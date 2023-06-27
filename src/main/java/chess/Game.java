package chess;

import view.BoardView;
import view.MenuView;

import javax.swing.*;
import java.awt.*;


/**
 * Main class for the Chess game.
 */
public class Game {

    /**
     * Manages the game state and handles switching between different views.
     */
    public static class StateManager{
        private static JFrame applicationFrame;
        private static Component currentView;

        /**
         * Initializes the StateManager with the given application frame and current view.
         *
         * @param applicationFrame The application's main JFrame.
         * @param currentView The current view Component.
         */
        public StateManager(JFrame applicationFrame, Component currentView){
            StateManager.applicationFrame = applicationFrame;
            StateManager.currentView = currentView;
        }

        /**
         * Switches the current view to the specified view.
         *
         * @param viewToSwitch The new view Component to switch to.
         */
        private static void switchToView(Component viewToSwitch){
            if(currentView != null){
                applicationFrame.remove(currentView);
            }
            applicationFrame.add(viewToSwitch);
            applicationFrame.pack();
            applicationFrame.setMinimumSize(applicationFrame.getSize());
            applicationFrame.setVisible(true);
            currentView = viewToSwitch;
        }

        /**
         * Switches to the main menu view.
         */
        public static void runMainMenu() {
            switchToView(new MenuView().getMenuPanel());
            applicationFrame.setTitle("Main menu");
        }

        /**
         * Switches to the Player vs Player view.
         */
        public static void runPvP(){
            switchToView(new BoardView().getGui());
            applicationFrame.setTitle("Epic Rap battle");

        }

        /**
         * Returns the main JFrame of the application.
         *
         * @return The main JFrame of the application.
         */
        public static JFrame getMainFrame() {
            return applicationFrame;
        }

    }
    private static StateManager stateManager = null;

    final static JFrame applicationFrame = new JFrame("Chess by A.R");
    static Component currentView = null;

    /**
     * Initializes the StateManager.
     */
    public static void initStateManager(){
        if(stateManager == null){
            stateManager = new StateManager(applicationFrame, currentView);
        }
    }

    /**
     * The main method that starts the Chess game application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args){
        initStateManager();
        applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        applicationFrame.setLocationByPlatform(true);
        Runnable app = StateManager::runMainMenu;
        SwingUtilities.invokeLater(app);
    }


}
