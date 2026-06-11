package kbc.clone;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Quiz extends JFrame {

    private static final String[][] QUESTIONS = {
        {"Which tool is used to find and fix bugs in Java programs?",
            "JVM", "JDB", "JDK", "JRE"},
        {"What is the return type of hashCode() in the Object class?",
            "int", "Object", "long", "void"},
        {"Which package contains the Random class?",
            "java.util", "java.lang", "java.awt", "java.io"},
        {"An interface with no fields or methods is known as?",
            "Runnable Interface", "Abstract Interface", "Marker Interface", "CharSequence Interface"},
        {"Where is a String stored when created with the new operator?",
            "Stack", "String Pool", "Random Storage", "Heap Memory"},
        {"Which of the following is a marker interface?",
            "Runnable", "Remote", "Readable", "Result"},
        {"Which keyword is used for accessing features of a package?",
            "import", "package", "extends", "export"},
        {"In Java, JAR stands for?",
            "Java Archive Runner", "Java Archive", "Java Application Resource", "Java Application Runner"},
        {"Which of the following is a mutable class in Java?",
            "java.lang.StringBuilder", "java.lang.Short", "java.lang.Byte", "java.lang.String"},
        {"Which feature leads to the portability and security of Java?",
            "Bytecode executed by JVM", "Applet makes code secure",
            "Use of exception handling", "Dynamic binding between objects"}
    };

    private static final String[] CORRECT = {
        "JDB", "int", "java.util", "Marker Interface", "Heap Memory",
        "Remote", "import", "Java Archive", "java.lang.StringBuilder",
        "Bytecode executed by JVM"
    };

    // Palette
    private static final Color BG_DEEP       = new Color(5, 8, 24);
    private static final Color BG_CARD       = new Color(12, 20, 50);
    private static final Color BORDER_SUBTLE = new Color(45, 75, 145);
    private static final Color BORDER_GLOW   = new Color(80, 130, 220);
    private static final Color GOLD          = new Color(255, 205, 50);
    private static final Color GOLD_DIM      = new Color(190, 145, 25);
    private static final Color GOLD_PALE     = new Color(255, 230, 130);
    private static final Color OPT_BG        = new Color(14, 28, 72);
    private static final Color OPT_HOV       = new Color(22, 44, 105);
    private static final Color OPT_SEL       = new Color(35, 65, 155);
    private static final Color TEXT_BRIGHT   = new Color(235, 240, 255);
    private static final Color TEXT_DIM      = new Color(90, 110, 160);
    private static final Color BTN_BLUE      = new Color(25, 120, 230);
    private static final Color BTN_AMBER     = new Color(195, 120, 0);
    private static final Color BTN_PURPLE    = new Color(110, 35, 190);
    private static final Color BTN_GREEN     = new Color(20, 130, 55);
    private static final Color RED_URGENT    = new Color(240, 70, 70);

    private static final String[] PRIZES = {
        "₹1,000","₹2,000","₹3,000","₹5,000","₹10,000",
        "₹20,000","₹40,000","₹80,000","₹1,60,000","₹3,20,000"
    };

    // UI
    private JLabel   qnoLabel, qLabel, timerLabel, progressLabel, scoreValueLabel;
    private JPanel[] optPanels  = new JPanel[4];
    private JLabel[] optLabels  = new JLabel[4];
    private JLabel[] optLetters = new JLabel[4];
    private int      selectedOpt = -1;
    private JButton  btnNext, btnSubmit;
    private JButton  btn5050, btnDoubleDip, btnExtraTime;
    private JPanel   timerArcPanel;
    private JPanel   progressBarFill;

    // State
    private int     questionIndex    = 0;
    private int     score            = 0;
    private int     timeLeft         = 20;
    private boolean doubleDipActive  = false;
    private boolean doubleDipUsed1st = false;
    private final   String name;
    private Timer   countdown;

    Quiz(String name) {
        this.name = name;
        setTitle("KBC Online – Java Quiz");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Custom content pane that paints the starfield background
        JPanel contentPane = new JPanel(null) {
            private final java.util.Random rng = new java.util.Random(42);
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background gradient
                g2.setPaint(new RadialGradientPaint(
                    new Point(getWidth()/2, getHeight()/2),
                    Math.max(getWidth(), getHeight()) * 0.75f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(10, 18, 55), BG_DEEP}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Stars
                for (int i = 0; i < 200; i++) {
                    int x = rng.nextInt(1440), y = rng.nextInt(900);
                    int alpha = 50 + rng.nextInt(120);
                    float sz = rng.nextFloat() * 1.6f + 0.4f;
                    g2.setColor(new Color(200, 215, 255, alpha));
                    g2.fillOval(x, y, (int)sz + 1, (int)sz + 1);
                }
                g2.dispose();
            }
        };
        contentPane.setBackground(BG_DEEP);
        setContentPane(contentPane);

        buildBanner();
        buildProgressBar();
        buildQuestionArea();
        buildOptionsArea();
        buildRightPanel();

        loadQuestion();
        startCountdown();
        setVisible(true);
    }

    // Banner – pure code, no image
    private void buildBanner() {
        JPanel banner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                g2.setPaint(new LinearGradientPaint(0, 0, getWidth(), 0,
                    new float[]{0f, 0.35f, 0.65f, 1f},
                    new Color[]{new Color(8,14,40), new Color(18,36,90),
                                new Color(14,28,76), new Color(5,9,30)}));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Radial glow centre
                g2.setPaint(new RadialGradientPaint(
                    new Point(getWidth()/2, getHeight()/2), 300,
                    new float[]{0f, 1f},
                    new Color[]{new Color(70,120,220,60), new Color(0,0,0,0)}));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Top gold hairline
                g2.setColor(new Color(255, 205, 50, 130));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(0, 0, getWidth(), 0);

                // Bottom gold hairline
                g2.setColor(new Color(255, 205, 50, 60));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);

                // Diamond decorations along bottom
                g2.setColor(new Color(255, 205, 50, 45));
                g2.setStroke(new BasicStroke(1f));
                for (int x = 50; x < getWidth(); x += 70) {
                    int cy = getHeight() - 12;
                    int[] xs = {x, x+5, x, x-5};
                    int[] ys = {cy-5, cy, cy+5, cy};
                    g2.fillPolygon(xs, ys, 4);
                }

                // Subtitle
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(255, 205, 50, 155));
                FontMetrics fmSub = g2.getFontMetrics();
                String sub = "KAUN BANEGA CROREPATI  ·  JAVA EDITION";
                g2.drawString(sub, (getWidth() - fmSub.stringWidth(sub)) / 2, 30);

                // Main title glow layers
                String title = "KBC ONLINE";
                g2.setFont(new Font("Georgia", Font.BOLD, 58));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(title)) / 2;
                int ty = 100;
                for (int p = 1; p <= 3; p++) {
                    g2.setColor(new Color(255, 200, 50, 14));
                    g2.drawString(title, tx - p, ty);
                    g2.drawString(title, tx + p, ty);
                    g2.drawString(title, tx, ty - p);
                    g2.drawString(title, tx, ty + p);
                }
                // Gold text
                g2.setColor(GOLD);
                g2.drawString(title, tx, ty);
                // Sheen
                g2.setColor(new Color(255, 250, 200, 80));
                g2.drawString(title, tx + 1, ty - 1);

                // Player chip
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.setColor(new Color(160, 180, 220));
                g2.drawString("Player: " + name, 28, getHeight() - 10);

                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setBounds(0, 0, 1440, 130);
        add(banner);
    }

    // Thin progress bar
    private void buildProgressBar() {
        JPanel track = new JPanel(null);
        track.setBackground(new Color(20, 30, 70));
        track.setBounds(0, 130, 1100, 4);

        progressBarFill = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new LinearGradientPaint(0, 0, getWidth(), 0,
                    new float[]{0f, 0.6f, 1f},
                    new Color[]{GOLD_DIM, GOLD, GOLD_PALE}));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        progressBarFill.setOpaque(false);
        progressBarFill.setBounds(0, 0, 0, 4);
        track.add(progressBarFill);
        add(track);
    }

    private void updateProgressBar() {
        int fillW = (int)((double)(questionIndex + 1) / QUESTIONS.length * 1100);
        progressBarFill.setBounds(0, 0, fillW, 4);
        progressBarFill.repaint();
    }

    // Question card
    private void buildQuestionArea() {
        JPanel card = new RoundPanel(16, BG_CARD, BORDER_SUBTLE);
        card.setBounds(28, 148, 1060, 84);
        card.setLayout(null);
        add(card);

        qnoLabel = new JLabel("Q1.");
        qnoLabel.setBounds(16, 16, 60, 50);
        qnoLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        qnoLabel.setForeground(GOLD);
        card.add(qnoLabel);

        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BORDER_SUBTLE);
                g.fillRect(0, 6, 1, getHeight() - 12);
            }
        };
        sep.setOpaque(false);
        sep.setBounds(82, 8, 1, 68);
        card.add(sep);

        qLabel = new JLabel();
        qLabel.setBounds(96, 14, 950, 56);
        qLabel.setFont(new Font("Segoe UI", Font.PLAIN, 21));
        qLabel.setForeground(TEXT_BRIGHT);
        card.add(qLabel);

        progressLabel = new JLabel("Question 1 of 10");
        progressLabel.setBounds(96, 63, 300, 14);
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        progressLabel.setForeground(TEXT_DIM);
        card.add(progressLabel);
    }

    // Options
    private void buildOptionsArea() {
        String[] letters = {"A", "B", "C", "D"};
        int[] cols = {28, 546};
        int[] rows = {248, 344};
        int w = 504, h = 80;

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            JPanel card = new RoundPanel(14, OPT_BG, BORDER_SUBTLE);
            card.setBounds(cols[i % 2], rows[i / 2], w, h);
            card.setLayout(null);
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            optPanels[i] = card;

            JLabel letter = new JLabel(letters[i], SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new RadialGradientPaint(
                        new Point(getWidth()/2, getHeight()/2), getWidth()/2,
                        new float[]{0f, 1f},
                        new Color[]{new Color(100,70,0,210), new Color(55,38,0,190)}));
                    g2.fillOval(1, 1, getWidth()-2, getHeight()-2);
                    g2.setColor(GOLD_DIM);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawOval(1, 1, getWidth()-2, getHeight()-2);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            letter.setBounds(14, 19, 42, 42);
            letter.setFont(new Font("Segoe UI", Font.BOLD, 15));
            letter.setForeground(GOLD);
            letter.setOpaque(false);
            optLetters[i] = letter;
            card.add(letter);

            JLabel lbl = new JLabel();
            lbl.setBounds(68, 19, 422, 42);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 19));
            lbl.setForeground(TEXT_BRIGHT);
            optLabels[i] = lbl;
            card.add(lbl);

            MouseAdapter ma = new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if (card.isEnabled() && selectedOpt != idx) card.setBackground(OPT_HOV);
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (selectedOpt != idx) card.setBackground(OPT_BG);
                }
                @Override public void mouseClicked(MouseEvent e) {
                    if (!card.isEnabled()) return;
                    selectOption(idx);
                }
            };
            card.addMouseListener(ma);
            lbl.addMouseListener(ma);
            letter.addMouseListener(ma);
            add(card);
        }
    }

    private void selectOption(int idx) {
        if (selectedOpt >= 0 && selectedOpt < 4) {
            optPanels[selectedOpt].setBackground(OPT_BG);
            ((RoundPanel) optPanels[selectedOpt]).setBorderColor(BORDER_SUBTLE);
            optLetters[selectedOpt].setForeground(GOLD);
            optLabels[selectedOpt].setForeground(TEXT_BRIGHT);
        }
        selectedOpt = idx;
        optPanels[idx].setBackground(OPT_SEL);
        ((RoundPanel) optPanels[idx]).setBorderColor(BORDER_GLOW);
        optLetters[idx].setForeground(GOLD_PALE);
        optLabels[idx].setForeground(Color.WHITE);
    }

    // Right panel
    private void buildRightPanel() {
        timerArcPanel = new TimerArcPanel();
        timerArcPanel.setBounds(1110, 148, 150, 150);
        add(timerArcPanel);

        timerLabel = new JLabel("20", SwingConstants.CENTER);
        timerLabel.setBounds(1110, 148, 150, 150);
        timerLabel.setFont(new Font("Georgia", Font.BOLD, 40));
        timerLabel.setForeground(GOLD);
        add(timerLabel);

        JLabel secLbl = new JLabel("seconds left", SwingConstants.CENTER);
        secLbl.setBounds(1110, 295, 150, 16);
        secLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        secLbl.setForeground(TEXT_DIM);
        add(secLbl);

        btnNext = styledBtn("Next  ▶", BTN_BLUE);
        btnNext.setBounds(1100, 325, 235, 46);
        btnNext.addActionListener(e -> onNext());
        add(btnNext);

        btnSubmit = styledBtn("Submit  ✔", new Color(15, 150, 70));
        btnSubmit.setBounds(1100, 381, 235, 46);
        btnSubmit.addActionListener(e -> onSubmit());
        btnSubmit.setEnabled(false);
        add(btnSubmit);

        // Lifelines header
        JPanel llHead = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,205,50,22));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(GOLD_DIM);
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.setColor(GOLD);
                FontMetrics fm = g2.getFontMetrics();
                String s = "LIFELINES";
                g2.drawString(s, (getWidth()-fm.stringWidth(s))/2, 17);
                g2.dispose();
            }
        };
        llHead.setOpaque(false);
        llHead.setBounds(1100, 442, 235, 26);
        add(llHead);

        btn5050 = lifelineBtn("50 : 50", "Removes 2 wrong options", BTN_AMBER);
        btn5050.setBounds(1100, 478, 235, 54);
        btn5050.addActionListener(e -> onFiftyFifty());
        add(btn5050);

        btnDoubleDip = lifelineBtn("Double Dip", "Two chances to answer", BTN_PURPLE);
        btnDoubleDip.setBounds(1100, 542, 235, 54);
        btnDoubleDip.addActionListener(e -> onDoubleDip());
        add(btnDoubleDip);

        btnExtraTime = lifelineBtn("Extra Time", "+30 seconds on clock", BTN_GREEN);
        btnExtraTime.setBounds(1100, 606, 235, 54);
        btnExtraTime.addActionListener(e -> onExtraTime());
        add(btnExtraTime);

        // Score panel
        JPanel scorePanel = new RoundPanel(12, new Color(12, 20, 52), BORDER_SUBTLE);
        scorePanel.setLayout(null);
        scorePanel.setBounds(1100, 676, 235, 76);
        add(scorePanel);

        JLabel scoreTitleLbl = new JLabel("CURRENT SCORE", SwingConstants.CENTER);
        scoreTitleLbl.setBounds(0, 10, 235, 16);
        scoreTitleLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        scoreTitleLbl.setForeground(TEXT_DIM);
        scorePanel.add(scoreTitleLbl);

        scoreValueLabel = new JLabel("₹ 0", SwingConstants.CENTER);
        scoreValueLabel.setBounds(0, 26, 235, 38);
        scoreValueLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        scoreValueLabel.setForeground(GOLD);
        scorePanel.add(scoreValueLabel);
    }

    // Timer
    private void startCountdown() {
        if (countdown != null) countdown.stop();
        countdown = new Timer(1000, e -> {
            timeLeft--;
            refreshTimer();
            if (timeLeft < 0) { countdown.stop(); timeUp(); }
        });
        countdown.start();
        refreshTimer();
    }

    private void refreshTimer() {
        timerLabel.setText(Math.max(timeLeft, 0) + "");
        timerLabel.setForeground(timeLeft <= 5 ? RED_URGENT : GOLD);
        timerArcPanel.repaint();
    }

    private void timeUp() {
        JOptionPane.showMessageDialog(this, "Time's up! Moving to next question.",
            "⏱ Time's Up", JOptionPane.WARNING_MESSAGE);
        advanceQuestion();
    }

    // Handlers
    private void onNext() {
        if (doubleDipActive && !doubleDipUsed1st) {
            if (selectedOpt < 0) {
                JOptionPane.showMessageDialog(this, "Select an answer to use Double Dip.",
                    "Double Dip", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!optLabels[selectedOpt].getText().equals(CORRECT[questionIndex])) {
                doubleDipUsed1st = true;
                optPanels[selectedOpt].setEnabled(false);
                optPanels[selectedOpt].setBackground(new Color(60, 18, 18));
                optLabels[selectedOpt].setForeground(new Color(100, 70, 70));
                optLetters[selectedOpt].setForeground(new Color(100, 70, 70));
                selectedOpt = -1;
                JOptionPane.showMessageDialog(this, "Wrong! You have one more attempt.",
                    "Double Dip", JOptionPane.WARNING_MESSAGE);
                return;
            }
            doubleDipActive = doubleDipUsed1st = false;
        }
        advanceQuestion();
    }

    private void onSubmit() {
        countdown.stop();
        if (selectedOpt >= 0 && optLabels[selectedOpt].getText().equals(CORRECT[questionIndex]))
            score += 10;
        setVisible(false);
        new Score(name, score);
    }

    private void onFiftyFifty() {
        int removed = 0;
        for (int i = 0; i < 4 && removed < 2; i++) {
            if (!optLabels[i].getText().equals(CORRECT[questionIndex]) && optPanels[i].isEnabled()) {
                optPanels[i].setEnabled(false);
                optPanels[i].setBackground(new Color(14, 20, 42));
                optLabels[i].setForeground(new Color(55, 70, 100));
                optLetters[i].setForeground(new Color(55, 70, 100));
                removed++;
            }
        }
        btn5050.setEnabled(false);
        btn5050.setBackground(new Color(60, 50, 20));
    }

    private void onDoubleDip() {
        doubleDipActive = true;
        doubleDipUsed1st = false;
        btnDoubleDip.setEnabled(false);
        btnDoubleDip.setBackground(new Color(45, 25, 75));
        JOptionPane.showMessageDialog(this, "Double Dip activated! You may answer twice.",
            "Double Dip", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onExtraTime() {
        timeLeft += 30;
        refreshTimer();
        btnExtraTime.setEnabled(false);
        btnExtraTime.setBackground(new Color(15, 50, 25));
        JOptionPane.showMessageDialog(this, "+30 seconds added!", "Extra Time",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void advanceQuestion() {
        countdown.stop();
        doubleDipActive = doubleDipUsed1st = false;

        if (selectedOpt >= 0 && optLabels[selectedOpt].getText().equals(CORRECT[questionIndex]))
            score += 10;

        scoreValueLabel.setText(PRIZES[Math.min(score / 10, PRIZES.length - 1)]);

        questionIndex++;
        if (questionIndex >= QUESTIONS.length) { setVisible(false); new Score(name, score); return; }
        if (questionIndex == QUESTIONS.length - 1) { btnNext.setEnabled(false); btnSubmit.setEnabled(true); }

        for (int i = 0; i < 4; i++) {
            optPanels[i].setEnabled(true);
            optPanels[i].setBackground(OPT_BG);
            ((RoundPanel) optPanels[i]).setBorderColor(BORDER_SUBTLE);
            optLabels[i].setForeground(TEXT_BRIGHT);
            optLetters[i].setForeground(GOLD);
        }
        selectedOpt = -1;
        timeLeft = 20;
        loadQuestion();
        updateProgressBar();
        startCountdown();
    }

    private void loadQuestion() {
        qnoLabel.setText("Q" + (questionIndex + 1) + ".");
        qLabel.setText(QUESTIONS[questionIndex][0]);
        progressLabel.setText("Question " + (questionIndex + 1) + " of " + QUESTIONS.length);
        for (int i = 0; i < 4; i++) {
            optLabels[i].setText(QUESTIONS[questionIndex][i + 1]);
            optPanels[i].setBackground(OPT_BG);
            optLabels[i].setForeground(TEXT_BRIGHT);
            optLetters[i].setForeground(GOLD);
            optLetters[i].repaint();
        }
        updateProgressBar();
    }

    // Button factories
    private JButton styledBtn(String label, Color bg) {
        JButton b = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = isEnabled() ? getBackground() : getBackground().darker().darker();
                g2.setPaint(new LinearGradientPaint(0, 0, 0, getHeight(),
                    new float[]{0f, 1f}, new Color[]{c.brighter(), c}));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                if (isEnabled()) {
                    g2.setColor(new Color(255,255,255,28));
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1f,getHeight()-1f,10,10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton lifelineBtn(String title, String sub, Color bg) {
        JButton b = new JButton("<html><center><b>" + title + "</b><br>"
            + "<span style='font-size:10px'>" + sub + "</span></center></html>") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = isEnabled() ? getBackground() : new Color(30, 35, 55);
                g2.setPaint(new LinearGradientPaint(0, 0, 0, getHeight(),
                    new float[]{0f,1f}, new Color[]{c.brighter(), c}));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),12,12));
                if (isEnabled()) {
                    g2.setColor(GOLD_DIM);
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1f,getHeight()-1f,12,12));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // Inner: RoundPanel with mutable border
    static class RoundPanel extends JPanel {
        private final int radius;
        private Color borderColor;
        RoundPanel(int r, Color bg, Color border) {
            this.radius = r;
            this.borderColor = border;
            setBackground(bg);
            setOpaque(false);
        }
        void setBorderColor(Color c) { this.borderColor = c; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),radius,radius));
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0.6f,0.6f,getWidth()-1.2f,getHeight()-1.2f,radius,radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Inner: timer arc
    class TimerArcPanel extends JPanel {
        TimerArcPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth()/2, cy = getHeight()/2, r = 58;
            // Outer glow ring
            g2.setColor(new Color(30, 45, 100, 100));
            g2.setStroke(new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(cx-r, cy-r, r*2, r*2);
            // Track
            g2.setColor(new Color(22, 32, 75));
            g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(cx-r, cy-r, r*2, r*2);
            // Arc
            int pct = Math.max(0, Math.min(timeLeft, 20));
            int angle = (int)(360.0 * pct / 20);
            Color arc = timeLeft <= 5 ? RED_URGENT : GOLD;
            // Arc glow
            g2.setColor(new Color(arc.getRed(), arc.getGreen(), arc.getBlue(), 55));
            g2.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(cx-r, cy-r, r*2, r*2, 90, -angle);
            // Arc fill
            g2.setColor(arc);
            g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(cx-r, cy-r, r*2, r*2, 90, -angle);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String player = JOptionPane.showInputDialog(null,
                "Enter your name:", "KBC Quiz", JOptionPane.QUESTION_MESSAGE);
            if (player == null || player.isBlank()) player = "Player";
            new Quiz(player);
        });
    }
}