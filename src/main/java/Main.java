import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;


    public Main(){
        reset();
//        snake = new Snake();
//        fruit = new Fruit();

//        direction = "Right";
        addKeyListener(this);
//        allowKeyPress = true;
    }
    private void setTimer(){
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                repaint();
            }
        },0,this.speed);

    }
    private void reset(){
        score = 0;
        if(snake != null){
            snake.getSnakeBody().clear();

        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit= new Fruit();
        setTimer();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }

    @Override
    public void paintComponent(Graphics g){

        /*
            TODO
            check if the snake bite itself

         */
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node head =snake_body.get(0);
        for(int i =1;i<snake.getSnakeBody().size();i++){
            if(snake_body.get(i).x == head.x && snake_body.get(i).y == head.y){
                allowKeyPress = false;
                t.cancel();;
                t.purge();
                int response = JOptionPane.showOptionDialog(
                        this
                        ,"Game over!!! would you like to start over???"
                        ,"Game over!"
                        ,JOptionPane.YES_NO_OPTION
                        ,JOptionPane.INFORMATION_MESSAGE
                        ,null
                        ,null
                        ,JOptionPane.YES_OPTION
                );
                switch (response){
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_NO_OPTION:
                        reset();
                        return;
                }


            }
        }



//        System.out.println("We are calling paint component...");
        //draw a blank background
        g.fillRect(0,0,width,height);
        fruit.drawFruit(g);
        snake.drawSnake(g);


        //remove snake tail and put in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        //right, x += cell_size
        //left, x -= cell_size
        //up, y-= cell_size
        //down, y += cell_size
        if(direction.equals("Left")){
            snakeX -= CELL_SIZE;
        }else if(direction.equals("Up")){
            snakeY -= CELL_SIZE;
        }
        else if(direction.equals("Right")){
            snakeX += CELL_SIZE;
        }
        else if(direction.equals("Down")){
            snakeY += CELL_SIZE;
        }
        Node newHead = new Node(snakeX,snakeY);
        //check if the snake eats the fruit

        if(snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY() ){
//            System.out.println("Eating the fruit.");
        /*TODO
           1.set fruit to new location
           2.draw fruit
           3.score ++
         */
            fruit.setNewLocation(snake);
            fruit.drawFruit(g);


        }else{
            snake.getSnakeBody().remove(snake.getSnakeBody().size()-1);
        }




        snake.getSnakeBody().add(0,newHead);


        allowKeyPress= true;
        requestFocusInWindow();




    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(allowKeyPress){
            if (e.getKeyCode()==37 && !direction.equals("Right")){
                direction = "Left";
            }else if(e.getKeyCode() == 38 && !direction.equals("Down")){
                direction = "Up";
            }else if(e.getKeyCode() == 39 && !direction.equals("Left")){
                direction = "Right";
            }else if(e.getKeyCode() == 40 && !direction.equals("Up")){
                direction = "Down";
            }
            allowKeyPress = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }




    public static void main(String[] args) {
        JFrame window = new JFrame("Snake game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }



}
