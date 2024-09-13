import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Paint extends JFrame {

    JButton pencilBtn, rectangleBtn, fillRectangleBtn, circleBtn, fillCircleBtn, lineBtn, backgroundBtn, clearBtn, colorBtn;
    JPanel toolButtons;

    int prevX, prevY, currentX, currentY, diameter;
    int choice = 1;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem newFile, openFile, saveFile;


    Color color;
    MyPanel drawingPanel;
    BufferedImage bufferedImage;

    public JButton createButton(String actionCommand, String iconPath) {
        ImageIcon iconTemp = new ImageIcon(iconPath);
        JButton button = new JButton("", iconTemp);
        button.setPreferredSize(new Dimension(40, 40));
        button.setActionCommand(actionCommand);
        button.addActionListener(new ButtonClickListener());
        toolButtons.add(button);
        if (Objects.equals(actionCommand, "7"))
            toolButtons.add(button, BorderLayout.EAST);
        if (Objects.equals(actionCommand, "8"))
            toolButtons.add(button, BorderLayout.WEST);
        return button;
    }

    class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("1")) {
                choice = 1;
            } else if (command.equals("2")) {
                choice = 2;
            } else if (command.equals("3")) {
                choice = 3;
            } else if (command.equals("4")) {
                choice = 4;
            } else if (command.equals("5")) {
                choice = 5;
            } else if (command.equals("6")) {
                choice = 6;
            }
        }
    }

    public JMenuItem createMenuItem(String menuItemName, String actionCommand) {
        JMenuItem menuItem = new JMenuItem(menuItemName);
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(new ButtonClickListener());
        fileMenu.add(menuItem);
        return menuItem;
    }


    class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    Paint() {

        setTitle("Paint");
        setBounds(300, 300, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        color = Color.BLACK;

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        toolButtons = new JPanel();
        container.add(toolButtons, BorderLayout.NORTH);

        colorBtn = createButton("0", "src/colorChooser.png");

        pencilBtn = createButton("1", "src/pencil.png");
        rectangleBtn = createButton("2", "src/rectangle.png");
        fillRectangleBtn = createButton("3", "src/fillRectangle.png");
        circleBtn = createButton("4","src/circle.png");
        fillCircleBtn = createButton("5","src/fillCircle.png");
        lineBtn = createButton("6", "src/line.png");
        backgroundBtn = createButton( "0", "src/background.png");

        clearBtn = createButton( "0", "src/clear.png");


        menuBar = new JMenuBar();
        fileMenu = new JMenu("Plik");
        menuBar.add(fileMenu);
        newFile = createMenuItem("Nowy", "0");
        openFile = createMenuItem("Otwórz", "0");
        saveFile = createMenuItem("Zapisz", "0");



        setJMenuBar(menuBar);

        bufferedImage = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g2d.dispose();

        drawingPanel = new MyPanel();
        drawingPanel.setBackground(Color.WHITE);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setColor(color);

                if (choice == 2) { //prostokat
                    int width = Math.abs(currentX - prevX);
                    int height = Math.abs(currentY - prevY);
                    int x = Math.min(prevX, currentX);
                    int y = Math.min(prevY, currentY);
                    g2d.drawRect(x, y, width, height);
                }
                else if (choice == 3) { // wypełniony prostokąt
                    int width = Math.abs(currentX - prevX);
                    int height = Math.abs(currentY - prevY);
                    int x = Math.min(prevX, currentX);
                    int y = Math.min(prevY, currentY);
                    g2d.fillRect(x, y, width, height);
                }
                else if (choice == 4) { //koło
                    diameter = Math.max(Math.abs(currentX - prevX), Math.abs(currentY - prevY));
                    int x = Math.min(prevX, currentX);
                    int y = Math.min(prevY, currentY);
                    g2d.drawOval(x, y, diameter, diameter);
                }
                else if (choice == 5) { //wypełnione koło
                    diameter = Math.max(Math.abs(currentX - prevX), Math.abs(currentY - prevY));
                    int x = Math.min(prevX, currentX);
                    int y = Math.min(prevY, currentY);
                    g2d.fillOval(x, y, diameter, diameter);
                }
                else if (choice == 6) { //linia
                    g2d.drawLine(prevX, prevY, currentX, currentY);
                }

                g2d.dispose();
                drawingPanel.repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();

                if (choice == 1) { // Ołówek
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.setColor(color);
                    g2d.drawLine(prevX, prevY, currentX, currentY);
                    g2d.dispose();
                    prevX = currentX;
                    prevY = currentY;
                    drawingPanel.repaint();
                }
            }
        });

        drawingPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                BufferedImage newImage = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = newImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
                g2d.drawImage(bufferedImage, 0, 0, null);
                g2d.dispose();
                bufferedImage = newImage;

                drawingPanel.repaint();
            }
        });

        backgroundBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics2D g2d = bufferedImage.createGraphics();
                color = JColorChooser.showDialog(null, "Wybór koloru", Color.BLACK);
                g2d.setColor(color);
                g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
                g2d.dispose();
                drawingPanel.repaint();
            }
        });

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
                g2d.dispose();
                drawingPanel.repaint();
            }
        });

        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = JColorChooser.showDialog(null, "Wybór koloru", Color.BLACK);
            }
        });

        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
                g2d.dispose();
                drawingPanel.repaint();
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Zapisz obraz");
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    try {
                        ImageIO.write(bufferedImage, "jpg", fileToSave);
                        System.out.println("Obrazek zapisany jako: " + fileToSave.getAbsolutePath());
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Otwórz obraz");
                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();
                    try {
                        bufferedImage = ImageIO.read(fileToOpen);
                        drawingPanel.repaint();
                        System.out.println("Obrazek otwarty: " + fileToOpen.getAbsolutePath());
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        getContentPane().add(drawingPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Paint();
    }
}