package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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
    private int highest_score;
    String desktop = System.getProperty("user.home");
    String myFile = desktop + "filename.txt";


    public Main(){
        read_highest_score();
        reset();
//        snake = new com.company.Snake();
//        fruit = new com.company.Fruit();

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
                        ,"Game over!!! Your score is " +score +". The highest score was " + highest_score + "would you like to start over???"
                        ,"Game over!"
                        ,JOptionPane.YES_NO_OPTION
                        ,JOptionPane.INFORMATION_MESSAGE
                        ,null
                        ,null
                        ,JOptionPane.YES_OPTION
                );
                write_a_file(score);
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

        /*
            概念；
            如果向右，right, x += cell_size
            如果向左，left, x -= cell_size
            如果向上，up, y-= cell_size
            如果向下，down, y += cell_size

         */

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
        /* 概念；
           1.set fruit to new location 新增水果新座標(包含一個避免水果產生到蛇身上的方法)
           2.draw fruit 畫出水果
           3.score ++ 分數加一
         */
            fruit.setNewLocation(snake);
            fruit.drawFruit(g);
            score ++;


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
        JFrame window = new JFrame("com.company.Snake game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    public void read_highest_score(){
        try{
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            highest_score = myReader.nextInt();
            myReader.close();
        }catch(FileNotFoundException e){
            highest_score = 0;
            try{
                File myObj = new File("filename.txt");
                if(myObj.createNewFile()){
                    System.out.println("File created: " + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + 0);

            }catch (IOException err){
                System.out.println("An error occurred.");
                err.printStackTrace();
            }
        }
    }

    public void write_a_file(int score){
        try{
            FileWriter myWriter = new FileWriter("filename.txt");
            if(score > highest_score){
                System.out.println("rewriting score...");
                myWriter.write("" + score);
                highest_score = score;
            }else{
                myWriter.write("" + highest_score);
            }
            myWriter.close();
        }catch (IOException err){
            err.printStackTrace();
        }

    }



}
