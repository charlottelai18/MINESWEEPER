import java.util.Scanner;

public class Minesweeper {
    private static final int SIZE = 10;
    private static final int MINES = 10;
    private final boolean[][] mines = new boolean[SIZE][SIZE];
    private final boolean[][] revealed = new boolean[SIZE][SIZE];
    private final int[][] counts = new int[SIZE][SIZE];

    public static void main(String[] args) {
        new Minesweeper().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Minesweeper!");

        placeMines();
        computeCounts();

        render(false);

        while (true) {
            System.out.print("Enter coordinate (row col) or 'quit': ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("quit"))
                break;

            int[] rc = parseCoordinate(line);
            if (rc == null) {
                System.out.println("Invalid input. Use: row col (example: 3 7)");
                continue;
            }

            int r = rc[0];
            int c = rc[1];

            if (!inBounds(r, c)) {
                System.out.println("Out of bounds. Rows/cols must be 0 to " + (SIZE - 1));
                continue;
            }

            if (revealed[r][c]) {
                System.out.println("Already revealed.");
                continue;
            }

            System.out.println("Valid coordinate: " + r + " " + c);
            revealed[r][c] = true;
            render(false);

        }

        sc.close();
    }

    private void render(boolean showMines) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                if (revealed[r][c]) {
                    int n = counts[r][c];
                    System.out.print("[" + (n == 0 ? " " : n) + "]");
                } else if (showMines && mines[r][c]) {
                    System.out.print("[*]");
                } else {
                    System.out.print("[ ]");
                }

            }
            System.out.println();
        }
    }

    private void placeMines() {
        java.util.Random rand = new java.util.Random();
        int placed = 0;
        while (placed < MINES) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            if (!mines[r][c]) {
                mines[r][c] = true;
                placed++;
            }
        }
    }

    private void computeCounts() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (mines[r][c]) {
                    counts[r][c] = -1;
                } else {
                    counts[r][c] = countAdjacentMines(r, c);
                }
            }
        }
    }

    private int countAdjacentMines(int r, int c) {
        int total = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0)
                    continue;
                int nr = r + dr, nc = c + dc;
                if (inBounds(nr, nc) && mines[nr][nc])
                    total++;
            }
        }
        return total;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private int[] parseCoordinate(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2)
            return null;

        try {
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            return new int[] { r, c };
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
