package kbc.clone;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Rules extends JFrame implements ActionListener {

    private final String name;
    private JButton start, back;

    Rules(String name) {
        this.name = name;
        setTitle("KBC – Rules");
        setSize(820, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Top bar ───────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(null);
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(820, 90));

        // Blue left accent stripe
        JPanel accent = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 144, 255));
                g2.fill(new RoundRectangle2D.Float(0, 8, 5, getHeight() - 16, 5, 5));
            }
        };
        accent.setOpaque(false);
        accent.setBounds(28, 0, 10, 90);
        bar.add(accent);

        JLabel welcome = new JLabel("WELCOME,  " + name.toUpperCase());
        welcome.setBounds(48, 18, 700, 20);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        welcome.setForeground(new Color(140, 140, 140));
        bar.add(welcome);

        JLabel title = new JLabel("Kaun Banega Crorepati");
        title.setBounds(48, 38, 700, 36);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 144, 255));
        bar.add(title);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(0, 88, 820, 1);
        sep.setForeground(new Color(230, 230, 230));
        bar.add(sep);

        return bar;
    }

    // ── Center panel ──────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(18, 28, 8, 28));

        // Rules grid (2 columns)
        String[][] rules = {
            {"1", "10 questions in total"},
            {"2", "Each correct answer earns 10 points"},
            {"3", "20 seconds allowed per question"},
            {"4", "No negative marking for wrong answers"},
            {"5", "Submit after answering the last question"},
            {"6", "Your final score is displayed at the end"},
        };

        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 10));
        grid.setBackground(Color.WHITE);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String[] r : rules) grid.add(ruleCard(r[0], r[1]));
        center.add(grid);
        center.add(Box.createVerticalStrut(20));

        // Lifelines section label
        JLabel llHead = new JLabel("LIFELINES AVAILABLE");
        llHead.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        llHead.setForeground(new Color(140, 140, 140));
        llHead.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(llHead);
        center.add(Box.createVerticalStrut(8));

        // Lifeline pills row
        JPanel pills = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pills.setBackground(Color.WHITE);
        pills.setAlignmentX(Component.LEFT_ALIGNMENT);
        pills.add(lifelinePill("50-50",       "Removes 2 wrong options",       new Color(239, 159, 39)));
        pills.add(lifelinePill("Double Dip",  "2 attempts on one question",    new Color(138, 43, 226)));
        pills.add(lifelinePill("Extra Time",  "+30 seconds added",             new Color(34, 139, 34)));
        center.add(pills);

        return center;
    }

    // ── Button bar ────────────────────────────────────────────────────────────
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 14));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        back = plainBtn("← Back");
        back.addActionListener(this);
        bar.add(back);

        start = solidBtn("Start Quiz  ▶");
        start.addActionListener(this);
        bar.add(start);

        return bar;
    }

    // ── Rule card ─────────────────────────────────────────────────────────────
    private JPanel ruleCard(String number, String text) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(new Color(248, 248, 248));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        // Numbered badge
        JLabel num = new JLabel(number, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 241, 251));
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        num.setPreferredSize(new Dimension(26, 26));
        num.setMinimumSize(new Dimension(26, 26));
        num.setMaximumSize(new Dimension(26, 26));
        num.setFont(new Font("Segoe UI", Font.BOLD, 11));
        num.setForeground(new Color(30, 144, 255));
        num.setOpaque(false);

        JLabel lbl = new JLabel("<html>" + text + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(40, 40, 40));

        card.add(num, BorderLayout.WEST);
        card.add(lbl, BorderLayout.CENTER);
        return card;
    }

    // ── Lifeline pill ─────────────────────────────────────────────────────────
    private JPanel lifelinePill(String name, String desc, Color dot) {
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pill.setBackground(Color.WHITE);
        pill.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 12)
        ));

        // Colour dot
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dot);
                g2.fillOval(0, 3, 10, 10);
                g2.dispose();
            }
        };
        d.setOpaque(false);
        d.setPreferredSize(new Dimension(10, 16));

        JLabel lbl = new JLabel("<html><b>" + name + "</b>  <span style='color:#888'>" + desc + "</span></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pill.add(d);
        pill.add(lbl);
        return pill;
    }

    // ── Button helpers ────────────────────────────────────────────────────────
    private JButton solidBtn(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(30, 144, 255));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 40));
        return b;
    }

    private JButton plainBtn(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(60, 60, 60));
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 40));
        return b;
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        if (ae.getSource() == start) new Quiz(name);
        else                         new Login();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Rules("User"));
    }
}