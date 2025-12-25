import com.example.model.room.ChessBoard;
import com.example.model.room.Stone;
import com.example.ui.BoardPanel;

import javax.swing.*;

public class TestBoardUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessBoard board = new ChessBoard();
            board.move(1,1, Stone.BLACK);
            board.move(2,2, Stone.WHITE);
            board.move(5,4, Stone.BLACK);
            board.move(9,3, Stone.WHITE);

            JFrame frame = new JFrame("五子棋");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoardPanel panel = new BoardPanel(board);
            frame.add(panel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
