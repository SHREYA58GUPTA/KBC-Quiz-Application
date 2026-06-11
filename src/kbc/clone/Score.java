package kbc.clone;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Score extends JFrame implements ActionListener {

    Score(String name, int score) {
        setTitle("KBC – Results");
        setBounds(350, 100, 700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ── Main panel ────────────────────────────────────────────────────────
        JPanel main = new JPanel();
        main.setBackground(Color.WHITE);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // ── Trophy circle ─────────────────────────────────────────────────────
        TrophyPanel trophy = new TrophyPanel();
        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(trophy);
        main.add(Box.createVerticalStrut(18));

        // ── Heading ───────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Thank you, " + name + "!");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(30, 30, 30));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(heading);

        JLabel subheading = new JLabel("for playing KBC Java Quiz");
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subheading.setForeground(new Color(120, 120, 120));
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(subheading);
        main.add(Box.createVerticalStrut(24));

        // ── Score card ────────────────────────────────────────────────────────
        ScoreCard card = new ScoreCard(score);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(260, 110));
        card.setPreferredSize(new Dimension(260, 110));
        main.add(card);
        main.add(Box.createVerticalStrut(18));

        // ── Badge ─────────────────────────────────────────────────────────────
        JLabel badge = makeBadge(score);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(badge);
        main.add(Box.createVerticalStrut(16));

        // ── Progress bar ──────────────────────────────────────────────────────
        JLabel barLabel = new JLabel(score / 10 + " of 10 correct");
        barLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        barLabel.setForeground(new Color(110, 110, 110));
        barLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(barLabel);
        main.add(Box.createVerticalStrut(6));

        ProgressBar bar = new ProgressBar(score);
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bar.setMaximumSize(new Dimension(380, 12));
        bar.setPreferredSize(new Dimension(380, 12));
        main.add(bar);
        main.add(Box.createVerticalStrut(26));

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btns.setBackground(Color.WHITE);
        btns.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playAgain = makeBtn("▶  Play Again", new Color(30, 144, 255), Color.WHITE);
        playAgain.addActionListener(this);
        btns.add(playAgain);

        JButton quit = makeBtn("Quit", new Color(240, 240, 240), new Color(50, 50, 50));
        quit.addActionListener(e -> System.exit(0));
        btns.add(quit);

        main.add(btns);

        add(main, BorderLayout.CENTER);

        // Animate bar after frame shows
        Timer t = new Timer(50, null);
        final int[] pct = {0};
        t.addActionListener(e -> {
            if (pct[0] >= score) { t.stop(); return; }
            pct[0] = Math.min(pct[0] + 2, score);
            bar.setProgress(pct[0]);
        });

        setVisible(true);
        t.start();
    }

    // ── ActionListener ────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Login();
    }

    // ── Badge factory ─────────────────────────────────────────────────────────
    private JLabel makeBadge(int score) {
        String text;
        Color bg, fg;
        if      (score == 100) { text = "★ Perfect Score!";        bg = new Color(250,238,218); fg = new Color(133,79,11);  }
        else if (score >= 80)  { text = "✦ Excellent";             bg = new Color(250,238,218); fg = new Color(133,79,11);  }
        else if (score >= 60)  { text = "✓ Good Job";              bg = new Color(234,243,222); fg = new Color(59,109,17);  }
        else if (score >= 40)  { text = "→ Keep Practising";       bg = new Color(230,241,251); fg = new Color(24,95,165);  }
        else                   { text = "↺ Better Luck Next Time"; bg = new Color(250,236,231); fg = new Color(153,60,29);  }

        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(fg);
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        return lbl;
    }

    // ── Button factory ────────────────────────────────────────────────────────
    private JButton makeBtn(String label, Color bg, Color fg) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 42));
        return b;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Inner panel: trophy circle
    // ─────────────────────────────────────────────────────────────────────────
    static class TrophyPanel extends JPanel {
        TrophyPanel() {
            setPreferredSize(new Dimension(110, 110));
            setMaximumSize(new Dimension(110, 110));
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Circle fill
            g2.setColor(new Color(250, 238, 218));
            g2.fillOval(5, 5, 100, 100);
            // Circle border
            g2.setColor(new Color(239, 159, 39));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(5, 5, 100, 100);
            // Trophy emoji via font
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
            FontMetrics fm = g2.getFontMetrics();
            String t = "🏆";
            int tx = (110 - fm.stringWidth(t)) / 2;
            int ty = (110 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(t, tx, ty);
            g2.dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Inner panel: rounded score card
    // ─────────────────────────────────────────────────────────────────────────
    static class ScoreCard extends JPanel {
        private final int score;
        ScoreCard(int score) {
            this.score = score;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Card background
            g2.setColor(new Color(247, 247, 247));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
            g2.setColor(new Color(210, 210, 210));
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
            // Label
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(new Color(140, 140, 140));
            FontMetrics fmS = g2.getFontMetrics();
            String lbl = "YOUR SCORE";
            g2.drawString(lbl, (getWidth() - fmS.stringWidth(lbl)) / 2, 28);
            // Score number
            g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
            g2.setColor(new Color(25, 25, 25));
            FontMetrics fmB = g2.getFontMetrics();
            String snum = String.valueOf(score);
            int sx = (getWidth() - fmB.stringWidth(snum) - 42) / 2;
            g2.drawString(snum, sx, 80);
            // "/100"
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            g2.setColor(new Color(170, 170, 170));
            g2.drawString("/ 100", sx + fmB.stringWidth(snum) + 6, 80);
            g2.dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Inner panel: animated progress bar
    // ─────────────────────────────────────────────────────────────────────────
    static class ProgressBar extends JPanel {
        private int progress = 0;
        ProgressBar(int max) { setOpaque(false); }
        void setProgress(int p) { this.progress = p; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            // Track
            g2.setColor(new Color(220, 220, 220));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, h, h));
            // Fill
            int fw = (int)(w * progress / 100.0);
            if (fw > 0) {
                g2.setColor(new Color(239, 159, 39));
                g2.fill(new RoundRectangle2D.Float(0, 0, fw, h, h, h));
            }
            g2.dispose();
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Score("Arjun", 70));
    }
}