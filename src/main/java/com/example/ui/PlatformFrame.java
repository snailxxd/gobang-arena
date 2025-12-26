package com.example.ui;

import com.example.model.room.GameRoom;
import com.example.service.client.ClientEventListener;
import com.example.service.client.ClientNetworkHelper;
import com.example.service.network.MsgType;
import com.example.service.network.NetworkPacket;
import com.example.service.network.PlayerRequest;
import com.example.service.network.RoomRequest;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.utils.Utils.*;

public class PlatformFrame extends JFrame implements ClientEventListener {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientNetworkHelper clientNetworkHelper = ClientNetworkHelper.getInstance();

    private String playerName;
    private String roomId;

    private JLabel nameLabel;
    private JButton createRoomBtn;
    private JButton joinRoomBtn;
    private JTextField roomIdField;

    public PlatformFrame() {
        setTitle("游戏大厅");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        connectServer();
        clientNetworkHelper.setListener(this);

        while (!askForUsername()) {}

        initComponents();
        setVisible(true);
    }

    private void connectServer() {
        try {
            clientNetworkHelper.connect("localhost", SERVER_PORT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "无法连接到服务器: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private boolean askForUsername() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "请输入你的玩家昵称:", "登录", JOptionPane.PLAIN_MESSAGE);
            if (input == null) return false;
            if (!input.trim().isEmpty()) {
                this.playerName = input.trim();
                clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.LOGIN, new PlayerRequest(playerName)));
                return true;
            }
        }
    }

    private void initComponents() {
        // 顶部信息
        JPanel topPanel = new JPanel();
        nameLabel = new JLabel("当前玩家: " + playerName);
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // 中间操作区
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        createRoomBtn = new JButton("创建新房间");

        JPanel joinPanel = new JPanel(new BorderLayout(5, 0));
        roomIdField = new JTextField();
        roomIdField.setToolTipText("输入8位房间号");
        joinRoomBtn = new JButton("加入房间");
        joinPanel.add(new JLabel("房间ID:"), BorderLayout.WEST);
        joinPanel.add(roomIdField, BorderLayout.CENTER);

        centerPanel.add(createRoomBtn);
        centerPanel.add(new JSeparator());
        centerPanel.add(joinPanel);
        centerPanel.add(joinRoomBtn);

        add(centerPanel, BorderLayout.CENTER);

        // 绑定事件
        createRoomBtn.addActionListener(e -> {
            clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.CREATE_ROOM, null));
        });

        joinRoomBtn.addActionListener(e -> {
            String rId = roomIdField.getText().trim();
            if (rId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入房间号");
                return;
            }
            clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.JOIN_ROOM, new RoomRequest(rId)));
        });
    }

    @Override
    public void onRoomUpdate(GameRoom gameRoom) {}

    @Override
    public void onSuccess(String msg) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, msg, "成功", JOptionPane.ERROR_MESSAGE);
        });
    }

    @Override
    public void onError(String msg) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, msg, "错误", JOptionPane.ERROR_MESSAGE);
        });
    }

    @Override
    public void onJoin() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(false);
            clientNetworkHelper.setListener(null);
            GameRoom gameRoom = new GameRoom(roomId);
            new GameFrame(gameRoom, this);
        });
    }

    @Override
    public void onRoomId(String roomId) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, roomId, "房间已创建", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
