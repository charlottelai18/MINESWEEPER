import java.util.*;

public class Minesweeper {
    private static final int SIZE = 10;
    private static final int MINES = 10;

    private final boolean[][] mines = new boolean[SIZE][SIZE];
    private final boolean[][] revealed = new boolean[SIZE][SIZE];
    private final int[][] counts = new int[SIZE][SIZE];

    private boolean gameOver = false;
    private boolean won = false;

    public static void main(String[] args) {
        new Minesweeper().run();
    }

    private void run() {
        placeMines();
        computeCounts();

        Scanner sc = new Scanner(System.in);

        while (!gameOver) {
            render(false);

            System.out.print("Enter coordinate (row col) e.g. '3 7': ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
                System.out.println("Bye!");
                return;
            }

            int[] rc = parseTwoInts(line);
            if (rc == null) {
                System.out.println("Invalid input. Use: row col (0-9)");
                continue;
            }

            int r = rc[0], c = rc[1];
            if (!inBounds(r, c)) {
                System.out.println("Out of bounds. Rows/cols are 0 to 9.");
                continue;
            }

            if (revealed[r][c]) {
                System.out.println("Already revealed.");
                continue;
            }

            // If they hit a mine -> lose
            if (mines[r][c]) {
                gameOver = true;
                render(true);
                System.out.println("boom!");
                return;
            }

            // Reveal chosen square (with cascade for zeros)
            revealCascade(r, c);

            // Check win condition
            if (allNonMinesRevealed()) {
                gameOver = true;
                won = true;
                render(true);
                System.out.println("You win!");
                return;
            }
        }
    }

    // ----------------------------
    // Board generation
    // ----------------------------

    private void placeMines() {
        Random rand = new Random();
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
                    counts[r][c] = -1; // optional marker
                    continue;
                }
                counts[r][c] = countAdjacentMines(r, c);
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

    // ----------------------------
    // Reveal logic
    // ----------------------------

    private void revealCascade(int startR, int startC) {
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[] { startR, startC });

        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int r = cur[0], c = cur[1];

            if (!inBounds(r, c))
                continue;
            if (revealed[r][c])
                continue;
            if (mines[r][c])
                continue;

            revealed[r][c] = true;

            // If this is a 0, reveal neighbors too
            if (counts[r][c] == 0) {
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0)
                            continue;
                        int nr = r + dr, nc = c + dc;
                        if (inBounds(nr, nc) && !revealed[nr][nc] && !mines[nr][nc]) {
                            q.addLast(new int[] { nr, nc });
                        }
                    }
                }
            }
        }
    }

    private boolean allNonMinesRevealed() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (!mines[r][c] && !revealed[r][c])
                    return false;
            }
        }
        return true;
    }

    // ----------------------------
    // Rendering
    // ----------------------------

    private void render(boolean showMines) {
        System.out.println();
        System.out.print("    ");
        for (int c = 0; c < SIZE; c++)
            System.out.print(c + "   ");
        System.out.println();

        for (int r = 0; r < SIZE; r++) {
            System.out.println("  " + "+---".repeat(SIZE) + "+");
            System.out.print(r + " |");

            for (int c = 0; c < SIZE; c++) {
                String cell;

                if (revealed[r][c]) {
                    int n = counts[r][c];
                    cell = (n == 0) ? " " : String.valueOf(n);
                } else if (showMines && mines[r][c]) {
                    cell = "*";
                } else {
                    cell = " ";
                }

                System.out.print(" " + cell + " |");
            }
            System.out.println();
        }
        System.out.println("  " + "+---".repeat(SIZE) + "+");
        System.out.println();
    }

    // ----------------------------
    // Helpers
    // ----------------------------

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private int[] parseTwoInts(String line) {
        String[] parts = line.split("\\s+");
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
