import java.util.Scanner;

public class Minesweeper {
    private static final int SIZE = 10;
    private static final int MINES = 10;

    public static void main(String[] args) {
        new Minesweeper().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Minesweeper!");
        renderPlaceholder();

        while (true) {
            System.out.print("Enter coordinate (row col) or 'quit': ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("quit"))
                break;

            System.out.println("TODO: handle input: " + line);
            renderPlaceholder();
        }
    }

    private void renderPlaceholder() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++)
                System.out.print("[ ]");
            System.out.println();
        }
    }
}
