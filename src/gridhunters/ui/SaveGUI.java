package gridhunters.ui;

import gridhunters.SaveDAO;
import gridhunters.io.SaveFile;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

public class SaveGUI extends JFrame {

    private transient SaveDAO saveDAO;
    private SaveFile selectedSave = null;
    private SaveCallback callback;

    public interface SaveCallback {
        void onSaveSelected(SaveFile save);
    }

    public SaveGUI(SaveCallback callback) {
        this.callback = callback;

        try {
            this.saveDAO = new SaveDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading saves: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        setTitle("Grid Hunters - Select Save");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel(
            "<html><div style='text-align: center;'>Welcome to GridHunters<br>Select a save file</div></html>",
            SwingConstants.CENTER
        );
        lblHeader.setForeground(Color.ORANGE);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblHeader, BorderLayout.NORTH);

        JPanel panelSaves = new JPanel();
        panelSaves.setLayout(new BoxLayout(panelSaves, BoxLayout.Y_AXIS));
        panelSaves.setBackground(new Color(20, 20, 20));
        panelSaves.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        HashMap<String, SaveFile> saves = saveDAO.getAllSaves();
        List<String> keys = new ArrayList<>(saves.keySet());

        if (keys.isEmpty()) {
            JLabel lblNoSaves = new JLabel(
                "You have no previous save files.",
                SwingConstants.CENTER
            );
            lblNoSaves.setForeground(Color.LIGHT_GRAY);
            lblNoSaves.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelSaves.add(Box.createVerticalGlue());
            panelSaves.add(lblNoSaves);
            panelSaves.add(Box.createVerticalGlue());
        } else {
            for (String key : keys) {
                SaveFile save = saves.get(key);
                JButton btnSave = new JButton(save.getName());
                btnSave.setMaximumSize(new Dimension(380, 45));
                btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnSave.setBackground(new Color(45, 45, 45));
                btnSave.setForeground(Color.WHITE);
                btnSave.setFont(new Font("SansSerif", Font.PLAIN, 14));
                btnSave.setFocusable(false);

                btnSave.addActionListener(e -> {
                    this.selectedSave = save;
                    finishSelection();
                });

                panelSaves.add(btnSave);
                panelSaves.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelSaves);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setBackground(Color.BLACK);
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JButton btnNewGame = new JButton("[+] Create New Save File");
        btnNewGame.setPreferredSize(new Dimension(250, 40));
        btnNewGame.setBackground(new Color(68, 207, 108));
        btnNewGame.setForeground(Color.WHITE);
        btnNewGame.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNewGame.setFocusable(false);

        btnNewGame.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(
                this,
                "Please name your save file:",
                "New Game",
                JOptionPane.PLAIN_MESSAGE
            );
            if (name != null && !name.trim().isEmpty()) {
                SaveFile newSave = new SaveFile(name);
                newSave.setName(name.trim());
                this.selectedSave = newSave;
                finishSelection();
            }
        });

        panelBottom.add(btnNewGame);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void finishSelection() {
        this.dispose();
        if (callback != null) {
            callback.onSaveSelected(this.selectedSave);
        }
    }
}
