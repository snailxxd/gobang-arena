import com.example.model.room.GameRoom;
import com.example.service.manager.RoomManager;
import com.example.ui.GameFrame;

import javax.swing.*;

public class TestGameFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameRoom gameRoom = RoomManager.getInstance().createRoom();
            GameFrame gameFrame = new GameFrame(gameRoom);
        });
    }
}
