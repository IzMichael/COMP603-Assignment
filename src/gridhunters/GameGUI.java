package gridhunters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import gridhunters.tiles.Tile;

public class GameGUI extends JFrame {
    Player player;
    Map map;
    JLabel labelHP;
    JLabel labelClass;
    JLabel labelHelmet;
    JLabel labelChestplate;
    JLabel labelLeggings;
    JLabel labelBoots;
    JLabel labelHand;
    JButton[] btnInventory = new JButton[5];
    JTextArea textLog;
    JPanel panelMap;

    public GameGUI(Player player, Map map) {
        this.player = player;
        this.map = map;

        setTitle("Grid Hunters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 808);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBackground(new Color(25, 25, 25));
        panelSidebar.setPreferredSize(new Dimension(280, 0));
        panelSidebar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel labelStatusHeader = new JLabel("=== PLAYER ===");
        labelStatusHeader.setForeground(Color.ORANGE);
        labelStatusHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelStatusHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelSidebar.add(labelStatusHeader);

        labelHP = new JLabel("HP: 0 / 0");
        labelHP.setForeground(Color.LIGHT_GRAY);
        labelHP.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelHP.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelHP);
        
        labelClass = new JLabel("Name: None | Class: None");
        labelClass.setForeground(Color.LIGHT_GRAY);
        labelClass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelClass.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelClass);
        
        panelSidebar.add(Box.createVerticalStrut(15));

        JLabel labelEquipHeader = new JLabel("=== EQUIPPED ITEMS ===");
        labelEquipHeader.setForeground(Color.ORANGE);
        labelEquipHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelEquipHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelSidebar.add(labelEquipHeader);

        labelHelmet = new JLabel("Helmet: None");
        labelHelmet.setForeground(Color.LIGHT_GRAY);
        labelHelmet.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelHelmet.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelHelmet);

        labelChestplate = new JLabel("Chestplate: None");
        labelChestplate.setForeground(Color.LIGHT_GRAY);
        labelChestplate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelChestplate.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelChestplate);

        labelLeggings = new JLabel("Leggings: None");
        labelLeggings.setForeground(Color.LIGHT_GRAY);
        labelLeggings.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelLeggings.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelLeggings);

        labelBoots = new JLabel("Boots: None");
        labelBoots.setForeground(Color.LIGHT_GRAY);
        labelBoots.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelBoots.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelBoots);

        labelHand = new JLabel("Main Hand: None");
        labelHand.setForeground(Color.LIGHT_GRAY);
        labelHand.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelHand.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        panelSidebar.add(labelHand);

        panelSidebar.add(Box.createVerticalStrut(15));

        JLabel labelInvHeader = new JLabel("=== INVENTORY ===");
        labelInvHeader.setForeground(Color.ORANGE);
        labelInvHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelInvHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelSidebar.add(labelInvHeader);

        JPanel panelInventory = new JPanel(new GridLayout(5, 1, 0, 4));
        panelInventory.setOpaque(false);
        panelInventory.setMaximumSize(new Dimension(260, 160));
        
        for (int i = 0; i < 5; i++) {
            final int slotIndex = i;
            btnInventory[i] = new JButton("[Slot " + (char)('A' + i) + "] Empty");
            btnInventory[i].setBackground(new Color(45, 45, 45));
            btnInventory[i].setForeground(Color.LIGHT_GRAY);
            btnInventory[i].setFocusable(false);
            
            btnInventory[i].addActionListener(e -> {
                if (player != null) {
                    player.useItem(slotIndex);
                    updateHPAndInventory();
                }
            });
            panelInventory.add(btnInventory[i]);
        }
        
        panelSidebar.add(panelInventory);

        add(panelSidebar, BorderLayout.WEST);

        JPanel panelCenterContainer = new JPanel(new BorderLayout(0, 10));
        panelCenterContainer.setBackground(Color.BLACK);

        panelMap = new JPanel();
        panelMap.setBackground(Color.BLACK);
        panelMap.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelCenterContainer.add(panelMap, BorderLayout.CENTER);

        textLog = new JTextArea(8, 40);
        textLog.setBackground(new Color(15, 15, 15));
        textLog.setForeground(new Color(34, 177, 76));
        textLog.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textLog.setEditable(false);
        textLog.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(textLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        panelCenterContainer.add(scrollLog, BorderLayout.SOUTH);

        add(panelCenterContainer, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyboardDirection(e.getKeyCode());
            }
        });
        
        setFocusable(true);
        requestFocusInWindow();
        
        updateHPAndInventory();
        logMessage("Cross tiles via WASD.");
    }

    public void refreshVisualMap() {
        if (player == null || map == null || panelMap == null) {
            return;
        }

        panelMap.removeAll();
        
        int radius = player.getMapRadius();
        int size = (2 * radius) + 1;
        
        panelMap.setLayout(new GridLayout(size, size, 3, 3));

        int playerX = player.getX();
        int playerY = player.getY();

        for (int offsetY = radius; offsetY >= -radius; offsetY--) {
            for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                
                int targetX = playerX + offsetX;
                int targetY = playerY + offsetY;

                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setFont(new Font("SansSerif", Font.BOLD, 16));
                cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                if (offsetX == 0 && offsetY == 0) {
                    cell.setText("P");
                    cell.setBackground(Color.BLUE);
                    cell.setForeground(Color.WHITE);
                } else {
                    Tile t = map.getTile(targetX, targetY);
                    
                    if (t == null) {
                        cell.setText(" ");
                        cell.setBackground(new Color(15, 15, 15));
                    } else if (!t.isVisited()) {
                        cell.setText("?");
                        cell.setBackground(new Color(35, 35, 35));
                        cell.setForeground(Color.GRAY);
                    } else {
                        cell.setText(t.getType());
                        cell.setForeground(Color.WHITE);
                        
                        switch (t.getType()) {
                            case "C" -> cell.setBackground(new Color(65, 65, 65));
                            case "E" -> cell.setBackground(new Color(139, 0, 0));
                            case "T" -> cell.setBackground(new Color(255, 223, 0));
                            default  -> cell.setBackground(new Color(35, 35, 35));
                        }
                    }
                }
                panelMap.add(cell);
            }
        }
        
        panelMap.revalidate();
        panelMap.repaint();
    }

    public void updateHPAndInventory() {
        if (player == null) return;

        labelClass.setText("Name: " + player.getName() + " | Class: " + player.getClassType());
        labelHP.setText("HP: " + player.getHealth() + " / " + player.maxHealth);

        labelHelmet.setText("Helmet: " + getEquipmentName(player.helmet));
        labelChestplate.setText("Chestplate: " + getEquipmentName(player.chestplate));
        labelLeggings.setText("Leggings: " + getEquipmentName(player.leggings));
        labelBoots.setText("Boots: " + getEquipmentName(player.boots));
        labelHand.setText("Main Hand: " + getEquipmentName(player.hand));

        for (int i = 0; i < 5; i++) {
            if (i < player.inventory.size() && player.inventory.get(i) != null) {
                btnInventory[i].setText("[" + (char)('A' + i) + "] " + player.inventory.get(i).toString());
                btnInventory[i].setEnabled(true);
            } else {
                btnInventory[i].setText("[" + (char)('A' + i) + "] Empty");
                btnInventory[i].setEnabled(false);
            }
        }
        
        refreshVisualMap();
    }
    
    private String getEquipmentName(Object item) {
        if (item == null) {
            return "None";
        }
        return item.toString();
    }

    private void handleKeyboardDirection(int keyCode) {
        if (player == null || map == null) {
            return;
        }

        Tile currentTile = map.getTile(player.getX(), player.getY());
        Tile targetTile = null;

        switch (keyCode) {
            case KeyEvent.VK_W -> targetTile = currentTile.getToNorth();
            case KeyEvent.VK_S -> targetTile = currentTile.getToSouth();
            case KeyEvent.VK_D -> targetTile = currentTile.getToEast();
            case KeyEvent.VK_A -> targetTile = currentTile.getToWest();
        }

        if (targetTile != null) {
            targetTile.exploreVisual(); 
            
            logMessage(targetTile.getDescription());
            logMessage("");
            
            updateHPAndInventory(); 
        }
    }

    public void logMessage(String message) {
        textLog.append("\n" + message);
        textLog.setCaretPosition(textLog.getDocument().getLength());
    }
}