package view;

import model.game.LogMove;
import javax.swing.*;
import java.awt.*;

/**
 * Custom cell renderer for displaying chess log entries in a list component.
 * The renderer customizes the appearance of each cell based on the log entry's properties.
 */
public class ChessLogCellRenderer extends DefaultListCellRenderer {
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = Color.pink;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;

    /**
     * Constructs a new instance of the ChessLogCellRenderer.
     * Initializes the label component used for rendering the cell.
     * The label is set to be opaque to enable background color rendering.
     */
    public ChessLogCellRenderer() {
        label = new JLabel("",SwingConstants.CENTER);
        label.setOpaque(true);
    }

    /**
     * Returns the custom component used for rendering each cell in the list.
     *
     * @param list      The list component being rendered.
     * @param value     The value of the cell being rendered.
     * @param index     The index of the cell being rendered.
     * @param selected  {@code true} if the cell is selected, {@code false} otherwise.
     * @param expanded  {@code true} if the cell is expanded, {@code false} otherwise.
     * @return The custom component used for rendering the cell.
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {
//        File file = (File)value;
        LogMove move = (LogMove) value;
        if(move.isTechnical){
            return new JLabel();
        }
        label.setToolTipText(move.toString());
        label.setText(move.toString());
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}
