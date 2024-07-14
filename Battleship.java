import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
public class BattleshipGame 
{
    private static final int gridSize = 10;
    private static final int maxShots = 10;
    public static void display(char[][] grid) 
	{
        System.out.println("    1   2   3   4   5   6   7   8   9   10   ");
        for (int a = 0; a < gridSize; a++) 
		{
            for (int b = 0; b < gridSize; b++) 
			{
                char cell = (grid[a][b] == 'X') ? 'X' : grid[a][b];
                System.out.print("| " + cell + " ");
            }
            System.out.println("|");
            if (a < gridSize - 1)
			{
                System.out.print("  -");
                for (int b = 0; b < gridSize - 1; b++)
				{
                    System.out.print("----");
                }
                System.out.println("----");
            }
        }
        System.out.println();
    }
    private static boolean validShipPlace(char[][] grid, int size, int x, int y, char direction) 
	{
        if (direction == 'H')
		{
            if (y + size > gridSize)
                return false;

            for (int g = y; g < y + size; g++)
			{
                if (grid[x][g] != ' ')
                    return false;
            }
        } 
		else
		{
            if (x + size > gridSize)
                return false;

            for (int h = x; h < x + size; h++) 
			{
                if (grid[h][y] != ' ')
                    return false;
            }
        }
        return true;
    }
    private static void shipPlace(char[][] grid, char symbol, int size) 
	{
        Random rand = new Random();
        boolean placedShips = false;

        while (!placedShips) 
		{
            int x = rand.nextInt(gridSize);
            int y = rand.nextInt(gridSize);
            char direction = rand.nextBoolean() ? 'H' : 'V';
            if (validShipPlace(grid, size, x, y, direction)) 
			{
                if (direction == 'H') 
				{
                    for (int g = y; g < y + size; g++) 
					{
                        grid[x][g] = symbol;
                    }
                } 
				else 
				{
                    for (int h = x; h < x + size; h++) 
					{
                        grid[h][y] = symbol;
                    }
                }
                placedShips = true;
            }
        }
    }
    private static boolean validShot(int i, int j, boolean[][] shotsFired)
	{
        return !shotsFired[i][j];
    }
    private static void replaceSymbol(char[][] grid, char symbol) 
	{
        for (int a = 0; a < gridSize; a++) 
		{
            for (int b = 0; b < gridSize; b++) 
			{
                if (grid[a][b] == symbol) 
				{
                    grid[a][b] = ' ';
                }
            }
        }
    }
        private static void debug(char[][] grid, char symbol) 
	{
      //  System.out.print(symbol + ": ");

        switch (symbol) 
		{
            case 'A':
                System.out.print("Aircraft Carrier");
                break;
            case 'B':
                System.out.print("Battleship");
                break;
            case 'S':
                System.out.print("Submarine");
                break;
            case 'D':
                System.out.print("Destroyer");
                break;
            case 'P':
                System.out.print("Patrol Boat");
                break;
        }

        System.out.print(": ");
        for (int a = 0; a < gridSize; a++)
		{
            for (int b = 0; b < gridSize; b++) 
			{
                if (grid[a][b] == symbol) 
				{
                    System.out.print("(" + a + "," + b + ")");
                }
            }
        }
        System.out.println();
    }
    private static void saveHighScore(int score) 
	{
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        try (FileWriter highScoreFile = new FileWriter("high_score_file.txt", true))
		 {
            highScoreFile.write("PlayerName = " + playerName + " , " + "Score = " + score + "\n");
            System.out.println("High score saved successfully");
        } 
		catch (IOException e)
		{
            System.out.println("Error: Unable to open the high scores file");
        }
    }
    private static void displayHighScore() 
	{
        System.out.println("High Score:");

        try (BufferedReader reader = new BufferedReader(new FileReader("high_score_file.txt"))) 
		{
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } 
		catch (IOException e)
		{
            System.out.println("Error: Unable to open the high scores file.");
        }
    }
    
    public static void main(String[] args)
	 {
        boolean debugMode = false;

        System.out.println("Welcome to Battleship Game");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to play in debug mode? (1 for yes, 0 for no): ");
        debugMode = scanner.nextInt() == 1;

        System.out.println("You have 10 shots to sink the ships. Good luck");
        System.out.println();
        char[][] grid = new char[gridSize][gridSize];
        boolean[][] shotsFired = new boolean[gridSize][gridSize];
        for (int a = 0; a < gridSize; a++)
		{
            for (int b = 0; b < gridSize; b++) 
			{
                grid[a][b] = ' ';
            }
        }
        Random rand = new Random();
        shipPlace(grid, 'A', 5);
        shipPlace(grid, 'B', 4);
        shipPlace(grid, 'S', 3);
        shipPlace(grid, 'D', 2);
        shipPlace(grid, 'P', 1);
        if (debugMode)
		{
            debug(grid, 'A');
            debug(grid, 'B');
            debug(grid, 'S');
            debug(grid, 'D');
            debug(grid, 'P');
        }
        int totalPoints = 0;
        int score = 0;
        scanner.nextLine();
        for (int remainingShots = maxShots - 1; remainingShots >= 0 && totalPoints < 30; remainingShots--) 
		{
            System.out.print("Please Enter Square To Fire At From 0 to 99 (e.g 12): ");
            int input = scanner.nextInt();
            int i = input / gridSize;
            int j = input % gridSize;
            if (i >= 0 && i < gridSize && j >= 0 && j < gridSize)
			{
                if (!shotsFired[i][j]) 
				{
                    shotsFired[i][j] = true;
                    if (grid[i][j] != ' ') 
					{
                        char symbol = grid[i][j];
                        int points;
                        switch (symbol) 
						{
                            case 'A':
                                points = 2;
                                break;
                            case 'B':
                                points = 4;
                                break;
                            case 'S':
                                points = 6;
                                break;
                            case 'D':
                                points = 8;
                                break;
                            case 'P':
                                points = 10;
                                break;
                            default:
                                points = 0;
                                break;
                        }
                        String shipName;
                        switch (symbol)
						{
                            case 'A':
                                shipName = "Aircraft Carrier";
                                break;
                            case 'B':
                                shipName = "Battleship";
                                break;
                            case 'S':
                                shipName = "Submarine";
                                break;
                            case 'D':
                                shipName = "Destroyer";
                                break;
                            case 'P':
                                shipName = "Patrol Boat";
                                break;
                            default:
                                shipName = "Unknown Ship";
                                break;
                        }
                        replaceSymbol(grid, symbol);
                        System.out.println("CONGRATULATIONS You Hit a " + shipName + "," + " And you gained " + points + " points");
                        totalPoints += points;
                        System.out.println("Total points: " + totalPoints);
                        System.out.println("Remaining shots are : " + remainingShots);
                    } 
					else
					{
                        System.out.println("You Missed It ");
                        System.out.println("Remaining shots are : " + remainingShots);
                    }
                }
				else 
				{
                    System.out.println("You already fired at this square. Please Try again.");
                    System.out.println("Remaining shots are : " + remainingShots++);
                }
            } 
			else 
			{
                System.out.println("Invalid input, Please enter a Valid coordinates ");
            }
        }
        System.out.println("Game Over , You gained " + totalPoints + " points. ");
        saveHighScore(totalPoints);
        displayHighScore();
    }
}