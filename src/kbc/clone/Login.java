package kbc.clone;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Login extends JFrame implements ActionListener {

    private JButton btnRules, btnExit;
    private JTextField tfName;

    Login() {
        setTitle("KBC – Player Login");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(buildLeftPanel());
        add(buildRightPanel());

        setVisible(true);
    }

    // ── Left panel (blue brand side with fp.png) ──────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0,            new Color(8, 55, 130),
                    0, getHeight(),  new Color(20, 100, 210)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 24, 28, 24));

        panel.add(Box.createVerticalGlue());

        // ── fp.png logo image ──
        try {
            ImageIcon raw = new ImageIcon(ClassLoader.getSystemResource("java icons/fp.png"));
            // fallback: load from file path used at dev time
            if (raw.getIconWidth() <= 0) {
                raw = new ImageIcon("src/java icons/fp.png");
            }
            Image scaled = raw.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(imgLabel);
        } catch (Exception e) {
            // Fallback: painted circle if image not found
            LogoCircle fallback = new LogoCircle();
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(fallback);
        }

        panel.add(Box.createVerticalStrut(16));

        // Tagline
        JLabel tagline = new JLabel("THE HOT SEAT IS READY!", SwingConstants.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tagline.setForeground(new Color(255, 215, 0));   // gold
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tagline);

        panel.add(Box.createVerticalStrut(6));

        // Subtitle
        JLabel sub = new JLabel("Java Edition  ·  10 Questions", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(255, 255, 255, 170));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sub);

        panel.add(Box.createVerticalStrut(18));

        // Decorative dots
        DotsPanel dots = new DotsPanel();
        dots.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dots);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ── Right panel (login form) ───────────────────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Tag
        JLabel tag = new JLabel("PLAYER LOGIN");
        tag.setBounds(50, 60, 300, 18);
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(30, 144, 255));
        panel.add(tag);

        // Heading
        JLabel heading = new JLabel("Welcome, Contestant!");
        heading.setBounds(50, 82, 360, 40);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(20, 20, 20));
        panel.add(heading);

        // Sub
        JLabel sub = new JLabel("Enter your name to begin.");
        sub.setBounds(50, 122, 360, 22);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(130, 130, 130));
        panel.add(sub);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(50, 156, 340, 1);
        sep.setForeground(new Color(235, 235, 235));
        panel.add(sep);

        // Field label
        JLabel nameLabel = new JLabel("Your name");
        nameLabel.setBounds(50, 174, 200, 18);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(120, 120, 120));
        panel.add(nameLabel);

        // Styled text field
        tfName = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? new Color(30, 144, 255) : new Color(210, 210, 210));
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.8f : 1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose();
            }
        };
        tfName.setBounds(50, 198, 340, 44);
        tfName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tfName.setOpaque(false);
        tfName.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        tfName.setBackground(Color.WHITE);
        tfName.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { tfName.repaint(); }
            @Override public void focusLost (FocusEvent e) { tfName.repaint(); }
        });
        tfName.addActionListener(this);   // Enter key submits
        panel.add(tfName);

        // Hint
        JLabel hint = new JLabel("3 lifelines  ·  No negative marking  ·  15 s per question");
        hint.setBounds(50, 248, 360, 16);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(new Color(175, 175, 175));
        panel.add(hint);

        // Exit button
        btnExit = new JButton("✕  Exit");
        btnExit.setBounds(50, 315, 105, 44);
        styleOutlineBtn(btnExit);
        btnExit.addActionListener(this);
        panel.add(btnExit);

        // Rules / Start button
        btnRules = new JButton("View Rules & Start  ▶");
        btnRules.setBounds(168, 315, 222, 44);
        styleSolidBtn(btnRules);
        btnRules.addActionListener(this);
        panel.add(btnRules);

        // KBC branding footer
        JLabel footer = new JLabel("KBC Online — Kaun Banega Crorepati", SwingConstants.CENTER);
        footer.setBounds(30, 400, 390, 16);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(200, 200, 200));
        panel.add(footer);

        return panel;
    }

    // ── Button styles ─────────────────────────────────────────────────────────
    private void styleSolidBtn(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(30, 144, 255));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(((JButton) c).getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, c.getWidth(), c.getHeight(), 10, 10));
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    private void styleOutlineBtn(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(80, 80, 80));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnExit) {
            System.exit(0);
        } else {
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                tfName.repaint();
                JOptionPane.showMessageDialog(this,
                    "Please enter your name to continue.",
                    "Name required", JOptionPane.WARNING_MESSAGE);
                tfName.requestFocusInWindow();
            } else {
                setVisible(false);
                new Rules(name);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Fallback logo circle (used if fp.png is not found)
    // ─────────────────────────────────────────────────────────────────────────
    static class LogoCircle extends JPanel {
        LogoCircle() {
            setPreferredSize(new Dimension(110, 110));
            setMaximumSize(new Dimension(110, 110));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillOval(5, 5, 100, 100);
            g2.setColor(new Color(255, 215, 0, 160));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(5, 5, 100, 100);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            FontMetrics fm = g2.getFontMetrics();
            String e = "🎯";
            g2.drawString(e, (110 - fm.stringWidth(e)) / 2, (110 + fm.getAscent()) / 2 - 6);
            g2.dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Decorative dot strip
    // ─────────────────────────────────────────────────────────────────────────
    static class DotsPanel extends JPanel {
        DotsPanel() { setPreferredSize(new Dimension(56, 14)); setMaximumSize(new Dimension(56, 14)); setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int[] x = {0, 18, 36};
            for (int i = 0; i < 3; i++) {
                g2.setColor(i == 0 ? Color.WHITE : new Color(255, 255, 255, 90));
                g2.fillOval(x[i], 2, 10, 10);
            }
            g2.dispose();
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}