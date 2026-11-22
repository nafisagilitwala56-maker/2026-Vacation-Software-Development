import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AlgorithmArena extends JPanel {

    private int[] array = new int[50];
    private int compareIndex = -1;
    private int swapIndex = -1;
    private int comparisons = 0;
    private int swaps = 0;

    private String currentAlgorithm = "Ready";
    private boolean sorting = false;

    private Timer timer;
    private int outerIndex = 0;
    private int innerIndex = 0;

    private final JLabel statusLabel;
    private final JLabel statsLabel;
    private final JButton bubbleButton;
    private final JButton selectionButton;
    private final JButton insertionButton;
    private final JButton resetButton;

    public AlgorithmArena() {
        setPreferredSize(new Dimension(900, 500));
        setBackground(new Color(13, 17, 23));
        generateArray();

        statusLabel = new JLabel("Algorithm: Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        statsLabel = new JLabel("Comparisons: 0 | Swaps: 0");
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        bubbleButton = createButton("Bubble Sort");
        selectionButton = createButton("Selection Sort");
        insertionButton = createButton("Insertion Sort");
        resetButton = createButton("Generate New Array");

        bubbleButton.addActionListener(e -> startSorting("Bubble Sort"));
        selectionButton.addActionListener(e -> startSorting("Selection Sort"));
        insertionButton.addActionListener(e -> startSorting("Insertion Sort"));
        resetButton.addActionListener(e -> resetArray());

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(22, 27, 34));
        controlPanel.add(bubbleButton);
        controlPanel.add(selectionButton);
        controlPanel.add(insertionButton);
        controlPanel.add(resetButton);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(22, 27, 34));
        infoPanel.add(statusLabel);
        infoPanel.add(statsLabel);

        JFrame frame = new JFrame("Algorithm Arena — Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(this, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(35, 134, 54));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;
    }

    private void generateArray() {
        Random random = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(400) + 20;
        }

        comparisons = 0;
        swaps = 0;
        compareIndex = -1;
        swapIndex = -1;
        outerIndex = 0;
        innerIndex = 0;
        currentAlgorithm = "Ready";
        sorting = false;

        if (statusLabel != null) {
            statusLabel.setText("Algorithm: Ready");
            statsLabel.setText("Comparisons: 0 | Swaps: 0");
        }

        repaint();
    }

    private void startSorting(String algorithm) {
        if (sorting) {
            return;
        }

        sorting = true;
        currentAlgorithm = algorithm;
        outerIndex = 0;
        innerIndex = 0;

        statusLabel.setText("Algorithm: " + algorithm);

        timer = new Timer(20, e -> performSortingStep(algorithm));
        timer.start();
    }

    private void performSortingStep(String algorithm) {
        if (algorithm.equals("Bubble Sort")) {
            bubbleSortStep();
        } else if (algorithm.equals("Selection Sort")) {
            selectionSortStep();
        } else if (algorithm.equals("Insertion Sort")) {
            insertionSortStep();
        }

        statsLabel.setText(
                "Comparisons: " + comparisons + " | Swaps: " + swaps
        );

        repaint();
    }

    private void bubbleSortStep() {
        if (outerIndex >= array.length - 1) {
            finishSorting();
            return;
        }

        if (innerIndex >= array.length - 1 - outerIndex) {
            outerIndex++;
            innerIndex = 0;
            return;
        }

        compareIndex = innerIndex;
        swapIndex = innerIndex + 1;
        comparisons++;

        if (array[innerIndex] > array[innerIndex + 1]) {
            swap(innerIndex, innerIndex + 1);
            swaps++;
        }

        innerIndex++;
    }

    private void selectionSortStep() {
        if (outerIndex >= array.length - 1) {
            finishSorting();
            return;
        }

        if (innerIndex == 0) {
            innerIndex = outerIndex + 1;
        }

        if (innerIndex >= array.length) {
            swap(outerIndex, findMinimumIndex(outerIndex));
            swaps++;
            outerIndex++;
            innerIndex = 0;
            return;
        }

        compareIndex = innerIndex;
        comparisons++;
        innerIndex++;
    }

    private int findMinimumIndex(int startIndex) {
        int minimumIndex = startIndex;

        for (int i = startIndex + 1; i < array.length; i++) {
            if (array[i] < array[minimumIndex]) {
                minimumIndex = i;
            }
        }

        return minimumIndex;
    }

    private void insertionSortStep() {
        if (outerIndex >= array.length) {
            finishSorting();
            return;
        }

        if (innerIndex == 0) {
            innerIndex = outerIndex;
        }

        if (innerIndex <= 0) {
            outerIndex++;
            innerIndex = 0;
            return;
        }

        compareIndex = innerIndex;
        swapIndex = innerIndex - 1;
        comparisons++;

        if (array[innerIndex] < array[innerIndex - 1]) {
            swap(innerIndex, innerIndex - 1);
            swaps++;
            innerIndex--;
        } else {
            outerIndex++;
            innerIndex = 0;
        }
    }

    private void swap(int firstIndex, int secondIndex) {
        int temporary = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temporary;
    }

    private void finishSorting() {
        timer.stop();
        sorting = false;
        compareIndex = -1;
        swapIndex = -1;
        statusLabel.setText("Algorithm: " + currentAlgorithm + " — Complete");
    }

    private void resetArray() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        generateArray();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int barWidth = panelWidth / array.length;

        for (int i = 0; i < array.length; i++) {
            int barHeight = array[i];
            int x = i * barWidth;
            int y = panelHeight - barHeight - 40;

            if (i == compareIndex || i == swapIndex) {
                graphics.setColor(new Color(248, 81, 73));
            } else {
                graphics.setColor(new Color(88, 166, 255));
            }

            graphics.fillRect(x, y, barWidth - 2, barHeight);
        }

        graphics.setColor(Color.WHITE);
        graphics.drawString(
                "Current Algorithm: " + currentAlgorithm,
                20,
                panelHeight - 15
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AlgorithmArena::new);
    }
}
