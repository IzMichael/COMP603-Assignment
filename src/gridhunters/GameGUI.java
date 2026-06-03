package gridhunters;

import gridhunters.tiles.EnemyTile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import gridhunters.tiles.Tile;
import gridhunters.tiles.TreasureTile;
import java.util.ArrayList;

public class GameGUI extends JFrame {
    Player player;
    Map map;
    Game game;
    JLabel labelHP;
    JLabel labelClass;
    JLabel labelHelmet;
    JLabel labelChestplate;
    JLabel labelLeggings;
    JLabel labelBoots;
    JLabel labelHand;
    JButton[] btnInventory = new JButton[5];
    JTextArea textLog;
    JPanel panelMain;
    CardLayout cardLayout;
    JPanel panelMap;
    JPanel panelCombat;
    JPanel panelLoot;
    EnemyTile currentEnemyTile = null;
    TreasureTile currentTreasureTile = null;
    JLabel labelEnemyInfo;
    JLabel labelPlayerCombatHP;
    JButton btnMelee;
    JButton btnMagic;
    JButton btnFlee;

    public GameGUI(Game game, Player player, Map map) {
        this.game = game;
        this.player = player;
        this.map = map;

        setTitle("Grid Hunters");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        
        cardLayout = new CardLayout();
        panelMain = new JPanel(cardLayout);
        panelMain.setBackground(Color.BLACK);
        
        panelMap = new JPanel();
        panelMap.setBackground(Color.BLACK);
        panelMap.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        panelCombat = createCombatPanel();
        
        panelLoot = new JPanel(new BorderLayout(10, 10));
        panelLoot.setBackground(Color.BLACK);
        
        panelMain.add(panelMap, "MAP_VIEW");
        panelMain.add(panelCombat, "COMBAT_VIEW");
        panelMain.add(panelLoot, "LOOT_VIEW");
        
        JPanel panelCenterContainer = new JPanel(new BorderLayout(0, 10));
        panelCenterContainer.setBackground(Color.BLACK);
        panelCenterContainer.add(panelMain, BorderLayout.CENTER);

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
        
        cardLayout.show(panelMain, "MAP_VIEW");
        updateHPAndInventory();
        refreshMapPanel();
        logMessage("Cross tiles via WASD. Fight enemies via F. Loot treature via R.");
        
        addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            try {
                System.out.println("Auto-saving before exiting.");
                game.save();
            } catch (Exception ex) {
                System.err.println("Failed to auto-save game: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                dispose();
                System.exit(0);
            }
        }
        });
    }

    public void refreshMapPanel() {
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
    
private JPanel createCombatPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelEnemyInfo = new JLabel("You engage the creature in a ferocious battle!", SwingConstants.CENTER);
        labelEnemyInfo.setForeground(Color.RED);
        labelEnemyInfo.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(labelEnemyInfo, BorderLayout.NORTH);

        labelPlayerCombatHP = new JLabel("Your HP: ", SwingConstants.CENTER);
        labelPlayerCombatHP.setForeground(Color.GREEN);
        labelPlayerCombatHP.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JPanel centerWrapper = new JPanel(new GridLayout(2, 1));
        centerWrapper.setOpaque(false);
        centerWrapper.add(labelPlayerCombatHP);
        
        JPanel actionsGrid = new JPanel(new GridLayout(1, 3, 10, 10));
        actionsGrid.setOpaque(false);
        
        btnMelee = new JButton("Physical Attack");
        btnMagic = new JButton("Magic Attack");
        btnFlee = new JButton("Attempt Flee");
        
        JButton[] combatButtons = {btnMelee, btnMagic, btnFlee};
        for (JButton btn : combatButtons) {
            btn.setBackground(new Color(30, 30, 30));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setFocusable(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        }
        
        btnMelee.addActionListener(e -> executeCombatTurn("MELEE"));
        btnMagic.addActionListener(e -> executeCombatTurn("MAGIC"));
        btnFlee.addActionListener(e -> executeCombatTurn("FLEE"));
        
        actionsGrid.add(btnMelee);
        actionsGrid.add(btnMagic);
        actionsGrid.add(btnFlee);
        
        centerWrapper.add(actionsGrid, BorderLayout.SOUTH);
        panel.add(centerWrapper, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshCombatPanel() {
        if (currentEnemyTile != null && currentEnemyTile.getEnemy() != null) {
            Enemy enemy = currentEnemyTile.getEnemy();
            labelEnemyInfo.setText(enemy.getName() + " HP: " + enemy.getHealth());
            labelPlayerCombatHP.setText("Your Current HP: " + player.getHealth() + " / " + player.getMaxHealth());
        }
    }
    private void executeCombatTurn(String actionType) {
        if (currentEnemyTile == null) {
            return;
        }

        Enemy enemy = currentEnemyTile.getEnemy();
        java.util.Random r = new java.util.Random();
        
        if (actionType.equals("FLEE")) {
            if (r.nextInt(100) < player.getAgility() * 25) {
                logMessage("You run away from the " + enemy.getName() + ", like a coward!\n");
                currentEnemyTile = null;
                cardLayout.show(panelMain, "MAP_VIEW");
                refreshMapPanel();
                return;
            } else {
                logMessage("Flee attempt failed! The enemy blocks your escape route.\n");
            }
        } else {
            int attackStrength = actionType.equals("MELEE") ? player.getMeleeAttack() : player.getMagicAttack();
            String playerAttack = enemy.executePAttack(attackStrength, player);
            logMessage(playerAttack);

            if (checkCombatEnd(enemy)) {
                return;
            }
        }

        if (enemy.getHealth() > 0) {
            String enemyTurn = enemy.executeEAttack(player); 
            logMessage(enemyTurn);
        }
        
        String playerStatusLogs = player.processStatusEffects();
        String enemyStatusLogs = enemy.processStatusEffects();

        if (!playerStatusLogs.isEmpty()) {
            logMessage(playerStatusLogs.trim());
        }
        if (!enemyStatusLogs.isEmpty()) {
            logMessage(enemyStatusLogs.trim());
        }
       
        if (checkCombatEnd(enemy)) {
            return;
        }
        labelEnemyInfo.setText(enemy.getName() + " - HP: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        labelPlayerCombatHP.setText("Your Current HP: " + player.getHealth() + " / " + player.getMaxHealth());
        updateHPAndInventory();
    }

    private boolean checkCombatEnd(Enemy enemy) {
    if (enemy.getHealth() <= 0) {
        logMessage("You won! The " + enemy.getName() + " has been defeated, and you emerge victorious!\n");
        Item drop = currentEnemyTile.claimDrops();
        if (drop != null) {
            player.addItem(drop);
            logMessage("It dropped a " + drop.toString() + ", which you have picked up.\n");
        }
        cardLayout.show(panelMain, "MAP_VIEW");
        currentEnemyTile = null;
        updateHPAndInventory();
        return true;
    }
    
    if (player.getHealth() <= 0) {
        logMessage("You died! GAME OVER!");
        JOptionPane.showMessageDialog(this, "You have been defeated by " + enemy.getName() + "! Game Over.\n");
        System.exit(0);
        return true;
    }
    return false;
}
 
    private void refreshLootPanel() {
        panelLoot.removeAll();
        
        JLabel labelTitle = new JLabel("Treasure Chest Storage System", SwingConstants.CENTER);
        labelTitle.setForeground(Color.YELLOW);
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelLoot.add(labelTitle, BorderLayout.NORTH);

        JPanel panelItemsGrid = new JPanel(new GridLayout(5, 1, 5, 5));
        panelItemsGrid.setOpaque(false);

        ArrayList<Item> chestItems = currentTreasureTile.getTreasureChest();
        for (int i = 0; i < chestItems.size(); i++) {
            final int index = i;
            Item chestSlotItem = chestItems.get(index);
            String labelText = (chestSlotItem == null) ? "[Empty Slot]" : chestSlotItem.toString();
            
            JButton btnTake = new JButton("Slot " + (char)('A' + index) + ": " + labelText);
            if (chestSlotItem == null) {
                btnTake.setEnabled(false);
            }
            
            btnTake.addActionListener(e -> {
                Item itemInChest = chestItems.get(index);
                Item returnedFromInventory = null;
                
                if (itemInChest != null) {
                    if (itemInChest.equip == Item.Equipment.HELMET) {
                        returnedFromInventory = player.swapHelmet(itemInChest);
                    } else if (itemInChest.equip == Item.Equipment.CHESTPLATE) {
                        returnedFromInventory = player.swapChestplate(itemInChest);
                    } else if (itemInChest.equip == Item.Equipment.LEGGINGS) {
                        returnedFromInventory = player.swapLeggings(itemInChest);
                    } else if (itemInChest.equip == Item.Equipment.BOOTS) {
                        returnedFromInventory = player.swapBoots(itemInChest);
                    } else if (itemInChest.equip == Item.Equipment.SWORD || itemInChest.equip == Item.Equipment.WAND) {
                        returnedFromInventory = player.swapHand(itemInChest);
                    } else {
                        returnedFromInventory = player.swapItem(itemInChest);
                    }
                }
                chestItems.set(index, returnedFromInventory);
                logMessage("Swapped items with the chest container.");
                refreshLootPanel();
                updateHPAndInventory();
            });
            panelItemsGrid.add(btnTake);
        }
        panelLoot.add(panelItemsGrid, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close Chest");
        btnClose.addActionListener(e -> {
            currentTreasureTile = null;
            cardLayout.show(panelMain, "MAP_VIEW");
            refreshMapPanel();
        });
        panelLoot.add(btnClose, BorderLayout.SOUTH);
        
        panelLoot.revalidate();
        panelLoot.repaint();
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
        
        if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
            return; 
        }

        int currentX = player.getX();
        int currentY = player.getY();

        int targetX = currentX;
        int targetY = currentY;

        switch (keyCode) {
            case KeyEvent.VK_W -> targetY = currentY + 1;
            case KeyEvent.VK_S -> targetY = currentY - 1;
            case KeyEvent.VK_D -> targetX = currentX + 1;
            case KeyEvent.VK_A -> targetX = currentX - 1;
            
            case KeyEvent.VK_F -> {
                fightStart();
                return;
            }
            case KeyEvent.VK_R -> {
                lootStart();
                return;
            }
            default -> {
                return;
            }
        }

        Tile targetTile = map.getTile(targetX, targetY);

        if (targetTile != null) {
            player.x = targetX; 
            player.y = targetY;
            currentEnemyTile = null;
            currentTreasureTile = null;
            
            targetTile.setVisited(true);
            logMessage(targetTile.getDescription());
            targetTile.playerArrive(this);
            logMessage("");
            
            if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
                fightStart();
                updateHPAndInventory(); 
            }
            
            refreshMapPanel(); 
            updateHPAndInventory(); 
        }
    }
    
    public void setEnemyTile(EnemyTile tile) {
        this.currentEnemyTile = tile;
    }

    public void setTreasureTile(TreasureTile tile) {
        this.currentTreasureTile = tile;
    }
    
    private void fightStart() {
        if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
            refreshCombatPanel();
            cardLayout.show(panelMain, "COMBAT_VIEW");
            Enemy enemy = currentEnemyTile.getEnemy();
            logMessage("You engage the " + enemy.getName() + " in a ferocious battle!\n");
            
            switch (player.getClassType()) {
                case "Warrior" -> {
                    btnMelee.setText("Slash");
                    btnMagic.setText("Arcane Slice");
                }
                case "Mage" -> {
                    btnMelee.setText("Staff Bonk");
                    btnMagic.setText("Magic Bolt");
                }
                case "Tank" -> {
                    btnMelee.setText("Shield Bash");
                    btnMagic.setText("Mystical Slam");
                }
                case "Rogue" -> {
                    btnMelee.setText("Backstab");
                    btnMagic.setText("Mana Infused Knives");
                }
                case "Cleric" -> {
                    btnMelee.setText("Divine Punch");
                    btnMagic.setText("Holy Smite");
                }
                default -> {
                    btnMelee.setText("Melee Attack");
                    btnMagic.setText("Magic Attack");
                }
            }
            btnFlee.setText("Attempt Flee");

            panelCombat.revalidate();
            panelCombat.repaint();
    } else {
            logMessage("There is no enemy there, you swing your sword. That was a good swing.\n");
        }
    }
    
    private void lootStart() {
        if (currentTreasureTile != null) {
            refreshLootPanel();
            cardLayout.show(panelMain, "LOOT_VIEW");
            logMessage("You open the treasure chest.\n");
            this.revalidate();
            this.repaint();
        } else {
            logMessage("There is no treasure chest here, did you imagine it?\n");
        }
    }
    
    public void logMessage(String message) {
        textLog.append("\n" + message);
        textLog.setCaretPosition(textLog.getDocument().getLength());
    }
}