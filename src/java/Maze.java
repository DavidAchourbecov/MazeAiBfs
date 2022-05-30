 import javax.swing.*;

 import java.awt.*;
import java.util.ArrayList;
 import java.util.LinkedList;
 import java.util.Random;
 import java.util.Queue;
 import java.util.Stack;

 public class Maze extends JFrame {

    private int[][] values;
    private boolean[][] visited;
    private int startRow;
    private int startColumn;
    private ArrayList<JButton> buttonList;// [1,2,3,4,5,6,7,8,9,0,]
    private int rows;
    private int columns;
    private boolean backtracking;
    private int algorithm;


    public boolean isObstacle (Node node){
        return values[node.getX()][node.getY()] == Definitions.OBSTACLE;
    }

    public boolean isVisited (Node currentNode){

        return visited[currentNode.getX()][currentNode.getY()];
    }

    public Maze(int algorithm, int size, int startRow, int startColumn) {
        this.algorithm = algorithm;
        Random random = new Random();
        this.values = new int[size][];
        for (int i = 0; i < values.length; i++) {
            int[] row = new int[size];
            for (int j = 0; j < row.length; j++) {
                if (i > 1 || j > 1) {
                    row[j] = random.nextInt(8) % 7 == 0 ? Definitions.OBSTACLE : Definitions.EMPTY;
                } else {
                    row[j] = Definitions.EMPTY;
                }
            }
            values[i] = row;
        }
        values[0][0] = Definitions.EMPTY;
        values[size - 1][size - 1] = Definitions.EMPTY;
        this.visited = new boolean[this.values.length][this.values.length];
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.buttonList = new ArrayList<>();
        this.rows = values.length;
        this.columns = values.length;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocationRelativeTo(null);
        GridLayout gridLayout = new GridLayout(rows, columns);
        this.setLayout(gridLayout);
        for (int i = 0; i < rows * columns; i++) {
            int value = values[i / rows][i % columns];
            JButton jButton = new JButton(String.valueOf(i));
            if (value == Definitions.OBSTACLE) {
                jButton.setBackground(Color.BLACK);
            } else {
                jButton.setBackground(Color.WHITE);
            }
            this.buttonList.add(jButton);
            this.add(jButton);
        }
        this.setVisible(true);
        this.setSize(Definitions.WINDOW_WIDTH, Definitions.WINDOW_HEIGHT);
        this.setResizable(false);
    }
    //
    public void checkWayOut() {
        new Thread(() -> {
            boolean result = false;
            switch (this.algorithm) {
                case Definitions.ALGORITHM_BFS:
                    result = startBsf(new Node(0,0));
                    break;
            }
            JOptionPane.showMessageDialog(null,  result ? "FOUND SOLUTION" : "NO SOLUTION FOR THIS MAZE");

        }).start();
    }


    public void setSquareAsVisited(int x, int y, boolean visited) {
        try {
            if (visited) {
                if (this.backtracking) {
                    Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE * 5);
                    this.backtracking = false;
                }
                this.visited[x][y] = true;
                for (int i = 0; i < this.visited.length; i++) {
                    for (int j = 0; j < this.visited[i].length; j++) {
                        if (this.visited[i][j]) {
                            if (i == x && y == j) {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.RED);
                            } else {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.BLUE);
                            }
                        }
                    }
                }
            } else {
                this.visited[x][y] = false;
                this.buttonList.get(x * this.columns + y).setBackground(Color.WHITE);
                Thread.sleep(Definitions.PAUSE_BEFORE_BACKTRACK);
                this.backtracking = true;
            }
            if (!visited) {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE / 4);
            } else {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public ArrayList<Node> getNeighbors(Node currentNode){
         ArrayList<Node> listToReturn = new ArrayList<>();


         if(currentNode.getY() + 1 != this.values.length ){
             Node neighbor = new Node(currentNode.getX(), currentNode.getY() + 1);
             if(!isObstacle(neighbor)) listToReturn.add(neighbor);
         }



         if(currentNode.getY() != 0  ){
             Node neighbor = new Node(currentNode.getX(), currentNode.getY() - 1);
             if(!isObstacle(neighbor)) listToReturn.add(neighbor);
         }


        if (currentNode.getX() != 0 ){
            Node neighbor =new Node(currentNode.getX() -1,currentNode.getY());
            if(!isObstacle(neighbor)) listToReturn.add(neighbor);

        }

        
         if (currentNode.getX() + 1 != this.values.length ){
             Node neighbor =new Node(currentNode.getX() +1 , currentNode.getY());
             if(!isObstacle(neighbor)) listToReturn.add(neighbor);
         }

         return listToReturn;
     }

     public boolean startBsf(Node start) {
         boolean result = false;
         Queue<Node> queue = new LinkedList<>();
         setSquareAsVisited(start.getX(),start.getY(),true);
         queue.add(start);

         while (!queue.isEmpty()) {
             Node currentNode = queue.remove();

             if(currentNode.getX() == this.values.length - 1 && currentNode.getY() == this.values.length - 1 )
                 result = true;

             for (Node neighbor : getNeighbors(currentNode)) {
                 if (!isVisited(neighbor)) {
                     setSquareAsVisited(neighbor.getX(),neighbor.getY(),true);
                     queue.add(neighbor);
                 }
             }
         }
        return result;
     }




     }




