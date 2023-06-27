package core;

import javax.swing.*;

/**
 * Timer class to manage the time left for each player in a Chess game.
 */
public class Timer implements Runnable{
    private boolean isWhite;
    private long timeBefore = 0;
    private long timeLeft = 5 * 60 * 1000;

    private volatile boolean pause = true;
    private volatile boolean stop = false;

    private final JLabel message;
    private final Board board;

    /**
     * Initializes the Timer with the given player color, message label, and board.
     *
     * @param isWhite  True if the timer is for the white player, false otherwise.
     * @param message  The JLabel used to display the timer message.
     * @param board    The Board instance for the current game.
     */
    public Timer(boolean isWhite, JLabel message, Board board){
        this.isWhite = isWhite;
        this.message = message;
        this.board = board;
    }

    /**
     * The run method for the Timer thread.
     */
    public void run(){
        while(!stop){
            if(!pause){
                if(timeBefore != 0){
                    long deltaTime = System.currentTimeMillis() - timeBefore;
                    timeLeft -= deltaTime;
                }
                timeBefore = System.currentTimeMillis();
                synchronized (board){
                    checkLoseCondition();
                }
                synchronized (message) {
                    updateTimerMessage();
                }
            }
            try {
                // update approximately 5 times per second
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the player has run out of time and sets the winner accordingly.
     */
    private void checkLoseCondition() {
        synchronized (board){
            if(timeLeft <= 0){
                board.setWinner(!isWhite, (isWhite?"Black":"White")+ " wins due to opponent is run ouf of time!");
                stop();
            }
        }
    }

    /**
     * Updates the timer message displayed on the JLabel.
     */
    private void updateTimerMessage() {
        synchronized (message) {
            String text = message.getText();
            String[] textSplet = text.split(" \\| ");
            if (textSplet.length == 2) {
                if (isWhite) {
                    textSplet[0] = "White: " + getTimeLeft();
                } else {
                    textSplet[1] = "Black: " + getTimeLeft();
                }
                text = textSplet[0] + " | " + textSplet[1];

            } else {
                text = "White: 5:00 | Black: 5:00";
            }

            message.setText(text);
            message.validate();
        }
    }

    /**
     * Returns the time left formatted as a string.
     *
     * @return A string representing the time left.
     */
    private String getTimeLeft() {
        return "" + timeLeft / 1000 / 60 + ":" + String.format("%2d",  timeLeft / 1000 % 60);
    }

    /**
     * Toggles the timer thread.
     */
    public void toggleThread(){
        setPause(!pause);
        if(pause){
            timeBefore=0;
        }
    }

    /**
     * Sets the pause state of the timer.
     *
     * @param flag True to pause the timer, false to resume.
     */
    private void setPause(boolean flag){
        pause = flag;
    }

    /**
     * Stops the timer
     */
    public void stop(){
        stop = true;
    }


    /**
     * Checks if the timer is currently running.
     *
     * @return True if the timer is running, false otherwise.
     */
    public boolean isRuns(){
        return !pause;
    }

}
