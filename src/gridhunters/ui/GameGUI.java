package gridhunters.ui;

import gridhunters.Enemy;
import gridhunters.Game;
import gridhunters.items.Item;
import gridhunters.tiles.Map;
import gridhunters.Player;
import gridhunters.items.Artefact;
import gridhunters.patterns.CombatMediator;
import gridhunters.patterns.GameObserver;
import gridhunters.tiles.EnemyTile;
import gridhunters.tiles.Tile;
import gridhunters.tiles.TreasureTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGUI extends JFrame implements GameObserver {
    // Game Core Elements
    private final Game game;
    private final Player player;
    private final Map map;
    private final CardLayout cardLayout;
    private final Random random = new Random();

    // UI Containers
    private final JPanel panelMain;
    private final JPanel panelMap;
    private final JPanel panelCombat;
    private final JPanel panelLoot;
    private final JTextArea textLog;

    // Sidebar Components
    private JLabel labelHP;
    private JLabel labelClass;
    private JLabel labelHelmet;
    private JLabel labelChestplate;
    private JLabel labelLeggings;
    private JLabel labelBoots;
    private JLabel labelHand;
    private final JButton[] btnInventory = new JButton[5];

    // Combat Components
    private JLabel labelEnemyInfo;
    private JLabel labelPlayerCombatHP;
    private JButton btnMelee;
    private JButton btnMagic;
    private JButton btnFlee;

    // Contextual State
    private EnemyTile currentEnemyTile = null;
    private TreasureTile currentTreasureTile = null;
    private CombatMediator activeMediator = null;

    public GameGUI(Game game, Player player, Map map) {
        this.game = game;
        this.player = player;
        this.map = map;
        this.game.registerObserver(this);

        setTitle("Grid Hunters");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1280, 808);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        add(createSidebarPanel(), BorderLayout.WEST);
        
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

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleAutoSaveAndExit();
            }
        });

        cardLayout.show(panelMain, "MAP_VIEW");
        updateHPAndInventory();
        refreshMapPanel();
        logMessage("Cross tiles via WASD. Fight enemies via F. Loot treasure via R.");
    }

    private JPanel createSidebarPanel() {
        JPanel panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBackground(new Color(25, 25, 25));
        panelSidebar.setPreferredSize(new Dimension(280, 0));
        panelSidebar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panelSidebar.add(createSectionHeader("=== PLAYER ==="));
        labelHP = createSidebarLabel("HP: 0 / 0");
        labelClass = createSidebarLabel("Name: None | Class: None");
        panelSidebar.add(labelHP);
        panelSidebar.add(labelClass);

        panelSidebar.add(Box.createVerticalStrut(15));

        panelSidebar.add(createSectionHeader("=== EQUIPPED ITEMS ==="));
        labelHelmet = createSidebarLabel("Helmet: None");
        labelChestplate = createSidebarLabel("Chestplate: None");
        labelLeggings = createSidebarLabel("Leggings: None");
        labelBoots = createSidebarLabel("Boots: None");
        labelHand = createSidebarLabel("Main Hand: None");
        panelSidebar.add(labelHelmet);
        panelSidebar.add(labelChestplate);
        panelSidebar.add(labelLeggings);
        panelSidebar.add(labelBoots);
        panelSidebar.add(labelHand);

        panelSidebar.add(Box.createVerticalStrut(15));

        panelSidebar.add(createSectionHeader("=== INVENTORY ==="));
        JPanel panelInventory = new JPanel(new GridLayout(5, 1, 0, 4));
        panelInventory.setOpaque(false);
        panelInventory.setMaximumSize(new Dimension(260, 160));

        for (int i = 0; i < 5; i++) {
            final int slotIndex = i;
            btnInventory[i] = new JButton("[Slot " + (char) ('A' + i) + "] Empty");
            btnInventory[i].setBackground(new Color(45, 45, 45));
            btnInventory[i].setForeground(Color.LIGHT_GRAY);
            btnInventory[i].setFocusable(false);
            btnInventory[i].addActionListener(e -> {
                if (player != null) {
                    player.useItem(slotIndex, this);
                    updateHPAndInventory();
                }
            });
            panelInventory.add(btnInventory[i]);
        }
        panelSidebar.add(panelInventory);

        return panelSidebar;
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
    
    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.ORANGE);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return label;
    }

    private JLabel createSidebarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        return label;
    }

    public void refreshMapPanel() {
        if (player == null || map == null || panelMap == null) return;

        panelMap.removeAll();
        int radius = player.getMapRadius();
        int size = (2 * radius) + 1;
        panelMap.setLayout(new GridLayout(size, size, 3, 3));

        int playerX = player.getX();
        int playerY = player.getY();

        for (int offsetY = radius; offsetY >= -radius; offsetY--) {
            for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                panelMap.add(createMapCell(playerX + offsetX, playerY + offsetY, offsetX == 0 && offsetY == 0));
            }
        }

        panelMap.revalidate();
        panelMap.repaint();
    }

    private JLabel createMapCell(int targetX, int targetY, boolean isPlayerPosition) {
        JLabel cell = new JLabel("", SwingConstants.CENTER);
        cell.setOpaque(true);
        cell.setFont(new Font("SansSerif", Font.BOLD, 16));
        cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        if (isPlayerPosition) {
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
                    default -> cell.setBackground(new Color(35, 35, 35));
                }
            }
        }
        return cell;
    }

    private void refreshCombatPanel() {
        if (currentEnemyTile != null && currentEnemyTile.getEnemy() != null) {
            Enemy enemy = currentEnemyTile.getEnemy();
            labelEnemyInfo.setText(enemy.getName() + " HP: " + enemy.getHealth());
            labelPlayerCombatHP.setText("Your Current HP: " + player.getHealth() + " / " + player.getMaxHealth());
            setupCombatButtonLabels();
        }
    }

    private void setupCombatButtonLabels() {
        switch (player.getClassType()) {
            case "Warrior" -> { btnMelee.setText("Slash"); btnMagic.setText("Arcane Slice"); }
            case "Mage"    -> { btnMelee.setText("Staff Bonk"); btnMagic.setText("Magic Bolt"); }
            case "Tank"    -> { btnMelee.setText("Shield Bash"); btnMagic.setText("Mystical Slam"); }
            case "Rogue"   -> { btnMelee.setText("Backstab"); btnMagic.setText("Mana Infused Knives"); }
            case "Cleric"  -> { btnMelee.setText("Divine Punch"); btnMagic.setText("Holy Smite"); }
            default        -> { btnMelee.setText("Physical Attack"); btnMagic.setText("Magic Attack"); }
        }
        btnFlee.setText("Attempt Flee");
    }

    private void executeCombatTurn(String actionType) {
        if (currentEnemyTile == null) return;

        Enemy enemy = currentEnemyTile.getEnemy();

        if (actionType.equals("FLEE")) {
            if (random.nextInt(100) < player.getAgility() * 25) {
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
            logMessage(activeMediator.executePAttack(attackStrength, player));
            if (checkCombatEnd(enemy)) return;
        }

        if (enemy.getHealth() > 0) {
            logMessage(activeMediator.executeEAttack(player));
        }

        String playerStatusLogs = player.processStatusEffects();
        String enemyStatusLogs = enemy.processStatusEffects();

        if (!playerStatusLogs.isEmpty()) logMessage(playerStatusLogs.trim());
        if (!enemyStatusLogs.isEmpty()) logMessage(enemyStatusLogs.trim());

        if (checkCombatEnd(enemy)) return;

        labelEnemyInfo.setText(enemy.getName() + " - HP: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        labelPlayerCombatHP.setText("Your Current HP: " + player.getHealth() + " / " + player.getMaxHealth());
        updateHPAndInventory();
    }

    private boolean checkCombatEnd(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            logMessage("You won! The " + enemy.getName() + " has been defeated, and you emerge victorious!\n");
            
            if (currentEnemyTile.isBoss()) {
                Artefact drop = currentEnemyTile.claimBossDrops();
                if (drop != null) {
                    logMessage("It dropped a legendary artefact, " + drop + ", which you have picked up.\n");
                }
            } else {
                Item drop = currentEnemyTile.claimDrops();
                player.addItem(drop);
                logMessage("It dropped a " + drop + ", which you have picked up.\n");
            }
            cardLayout.show(panelMain, "MAP_VIEW");
            currentEnemyTile = null;
            updateHPAndInventory();
            return true;
        }

        if (player.getHealth() <= 0) {
            onGameOver(false);
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

            JButton btnTake = new JButton("Slot " + (char) ('A' + index) + ": " + labelText);
            btnTake.setEnabled(chestSlotItem != null);

            btnTake.addActionListener(e -> {
                Item itemInChest = chestItems.get(index);
                if (itemInChest != null) {
                    Item returnedFromInventory = switch (itemInChest.equip) {
                        case HELMET -> player.swapHelmet(itemInChest);
                        case CHESTPLATE -> player.swapChestplate(itemInChest);
                        case LEGGINGS -> player.swapLeggings(itemInChest);
                        case BOOTS -> player.swapBoots(itemInChest);
                        case SWORD, WAND -> player.swapHand(itemInChest);
                        default -> player.swapItem(itemInChest);
                    };
                    chestItems.set(index, returnedFromInventory);
                    logMessage("Swapped items with the chest container.");
                    refreshLootPanel();
                    updateHPAndInventory();
                }
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
                btnInventory[i].setText("[" + (char) ('A' + i) + "] " + player.inventory.get(i).toString());
                btnInventory[i].setEnabled(true);
            } else {
                btnInventory[i].setText("[" + (char) ('A' + i) + "] Empty");
                btnInventory[i].setEnabled(false);
            }
        }
    }

    private String getEquipmentName(Object item) {
        return item == null ? "None" : item.toString();
    }

    private void handleKeyboardDirection(int keyCode) {
        if (player == null || map == null) return;
        
        if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
            logMessage("You cannot walk away! Finish the fight or use 'Attempt Flee'.");
            return; 
        }

        int targetX = player.getX();
        int targetY = player.getY();

        switch (keyCode) {
            case KeyEvent.VK_W -> targetY += 1;
            case KeyEvent.VK_S -> targetY -= 1;
            case KeyEvent.VK_D -> targetX += 1;
            case KeyEvent.VK_A -> targetX -= 1;
            case KeyEvent.VK_F -> { fightStart(); return; }
            case KeyEvent.VK_R -> { lootStart(); return; }
            default -> { return; }
        }

        Tile targetTile = map.getTile(targetX, targetY);
        
        if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
            cardLayout.show(panelMain, "COMBAT_VIEW");
            targetTile.playerArrive(this);
            return;
        }
        
        if (targetTile != null) {
            player.x = targetX;
            player.y = targetY;
            currentEnemyTile = null;
            currentTreasureTile = null;

            targetTile.setVisited(true);
            logMessage(targetTile.getDescription());
            targetTile.playerArrive(this);
            logMessage("");

            refreshMapPanel();
            updateHPAndInventory();
        }
    }

    private void fightStart() {
        if (currentEnemyTile != null && !currentEnemyTile.isDefeated()) {
            refreshCombatPanel();
            cardLayout.show(panelMain, "COMBAT_VIEW");
            logMessage("You engage the " + currentEnemyTile.getEnemy().getName() + " in a ferocious battle!\n");
        } else {
            logMessage("There is no enemy there, you swing your sword. That was a good swing.\n");
        }
    }

    private void lootStart() {
        if (currentTreasureTile != null) {
            refreshLootPanel();
            cardLayout.show(panelMain, "LOOT_VIEW");
            logMessage("You open the treasure chest.\n");
        } else {
            logMessage("There is no treasure chest here, did you imagine it?\n");
        }
    }

    private void handleAutoSaveAndExit() {
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

    public void setEnemyTile(EnemyTile tile) {
        this.currentEnemyTile = tile;
    }
    
    public void setTreasureTile(TreasureTile tile) {
        this.currentTreasureTile = tile;
    }

    @Override
    public void onPlayerStatsChanged(int health, int maxHealth, String logText) {
        labelHP.setText("HP: " + health + " / " + maxHealth);
        labelPlayerCombatHP.setText("Your HP: " + health);
        if (logText != null && !logText.isEmpty()) {
            logMessage(logText);
        }
    }

    @Override
    public void onTileDiscovered(Tile tile, String description) {
        logMessage(description);
    }

    @Override
    public void onCombatTriggered(Enemy enemy) {
        labelEnemyInfo.setText(enemy.getName() + " - HP: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        labelPlayerCombatHP.setText("Your Current HP: " + player.getHealth() + " / " + player.getMaxHealth());
        this.activeMediator = new CombatMediator(this.player, enemy, this);
        setupCombatButtonLabels();
        cardLayout.show(panelMain, "COMBAT_VIEW");
    }

    @Override
    public void onLootDiscovered(List<Item> items) {
        refreshLootPanel();
        cardLayout.show(panelMain, "LOOT_VIEW");
        logMessage("You look inside the container...");
    }

    @Override
    public void onGameOver(boolean won) {
        JOptionPane.showMessageDialog(this, won ? "Victory! You completed the challenge!" : "You have been defeated! Game Over.");
        System.exit(0);
    }

    public void logMessage(String message) {
        textLog.append("\n" + message);
        textLog.setCaretPosition(textLog.getDocument().getLength());
    }
}