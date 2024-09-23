package com.qiaolu;

import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.security.PublicKey;
import java.util.Objects;
import java.util.Random;

public class MainFrame extends JFrame implements KeyListener, ActionListener, MouseListener { // 继承JFrame，有利于代码扩展，实现自己的功能
    int[][] data = new int[4][4];

    int loseFlag = 1;
    int score = 0;

    String theme = "A-";

    // 创建JmenuItem（大蒜）
    JMenuItem item1 = new JMenuItem("经典");
    JMenuItem item2 = new JMenuItem("宝石");
    JMenuItem item3 = new JMenuItem("糖果");

    JLabel loseLable;



    public MainFrame(){
        initFrame();
        initData();
        paintView();
        setVisible(true);
    }

    public void initData() {
        generatorNum();
        generatorNum();
    }

    public void restartGame(){
        loseFlag = 1;
        score = 0;
        data = new int[4][4];
        initData();
        paintView();
    }


    public void initMenu() {
        // 创建JmenuBar（木棍）
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("换肤");
        JMenu menu2 = new JMenu("关于我们");


        menuBar.add(menu1);
        menuBar.add(menu2);

        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);

        item1.addActionListener(this);
        item2.addActionListener(this);
        item3.addActionListener(this);

        setJMenuBar(menuBar);

    }

    /**
     * 初始化窗体；子类直接调用父类方法，没有重写，可以直接省略super
     */
    public void initFrame(){
        // 调用成员方法，设置窗口宽高
        setSize(514,538);
        // 调用成员方法，设置窗体居中
        setLocationRelativeTo(null);
        // 调用成员方法，设置窗体置顶
        setAlwaysOnTop(true);
        // 设置关闭模式
        setDefaultCloseOperation(3);
        // 设置窗体标题
        setTitle("2048小游戏");
        setLayout(null);
        this.addKeyListener(this);
        initMenu();
    }

    /**
     * 游戏界面绘制
     */
    public void paintView() {
        getContentPane().removeAll();

        if (loseFlag == 2) {
            loseLable = new JLabel(new ImageIcon(this.getClass().getResource("/images/" + theme + "gameover.png")));
            loseLable.setBounds(98, 100,334, 334);
            getContentPane().add(loseLable);
            loseLable.addMouseListener(this);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("/images/" + theme + data[i][j] + ".png");
                URL imageUrl = this.getClass().getResource("/images/" + theme + data[i][j] + ".png");
                if (imageUrl != null) {
                    JLabel image = new JLabel(new ImageIcon(imageUrl));
                    image.setBounds(50 + 100 * j, 50 + 100 * i, 100, 100);
                    super.getContentPane().add(image);
                } else {
                    System.out.println(data[i][j] + "没有图片");
                }
            }
        }

        JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("/images/" + theme + "background.png")));
        background.setBounds(40, 40, 420, 420);
        super.getContentPane().add(background);

        JLabel scoreJlable = new JLabel("得分：" + score);
        scoreJlable.setBounds(50, 20, 100, 20);
        getContentPane().add(scoreJlable);

        getContentPane().repaint();
    }

    /**
     * 无法监听到上下左右安检，无需关注
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 键盘被按下时，所触发的方法，在这个方法中区分上下左右按键
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case 37:
                moveToLeft();
                generatorNum();
                break;
            case 38:
                moveToTop();
                generatorNum();
                break;
            case 39:
                moveToRight();
                generatorNum();
                break;
            case 40:
                moveToBottom();
                generatorNum();
                break;
            default:
                return;
        }
        check();
        paintView();
    }

    public void check(){
        if(checkLeft() == false && checkTop() == false && checkRight() == false && checkBottom() == false) {
            loseFlag = 2;
        }
    }

    /**
     * 用于二维数组的数据拷贝
     * @param src 原数组
     * @param dest 目标数组
     */
    public void copyArray(int[][] src, int[][] dest) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                dest[i][j] = src[i][j];
            }
        }
    }

    /**
     * 判断是否可以进行下移动
     * @return
     */
    public boolean checkBottom(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        // 进行了移动，后面需要复原数据
        moveToBottom();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 元素有一个是不相同的，就代表数据可以变化，数据可以移动
                if (data[i][j] != newArr[i][j]) {
                    flag = true;
                    break lo;
                }
            }
        }

        // 确定信息后，恢复原数据
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 判断是否可以进行右移动
     * @return
     */
    public boolean checkRight(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        // 进行了移动，后面需要复原数据
        moveToRight();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 元素有一个是不相同的，就代表数据可以变化，数据可以移动
                if (data[i][j] != newArr[i][j]) {
                    flag = true;
                    break lo;
                }
            }
        }

        // 确定信息后，恢复原数据
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 判断是否可以进行上移动
     * @return
     */
    public boolean checkTop(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        // 进行了移动，后面需要复原数据
        moveToTop();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 元素有一个是不相同的，就代表数据可以变化，数据可以移动
                if (data[i][j] != newArr[i][j]) {
                    flag = true;
                    break lo;
                }
            }
        }

        // 确定信息后，恢复原数据
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 判断是否可以进行左移动
     * @return
     */
    public boolean checkLeft(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        // 进行了移动，后面需要复原数据
        moveToLeft();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 元素有一个是不相同的，就代表数据可以变化，数据可以移动
                if (data[i][j] != newArr[i][j]) {
                    flag = true;
                    break lo;
                }
            }
        }

        // 确定信息后，恢复原数据
        copyArray(newArr, data);
        return flag;
    }


    public void moveToBottom() {
        clockwise();
        moveToLeft();
        anticlockwise();
    }

    public void moveToTop() {
        anticlockwise();
        moveToLeft();
        clockwise();
    }

    public void moveToRight() {
        horizontalSwap();
        moveToLeft();
        horizontalSwap();
    }

    public void moveToLeft() {
        for (int i = 0; i < data.length; i++) {
            // 后置0号元素
            int[] newArr = new int[4];
            int index = 0;
            for (int x = 0; x < data[i].length; x++) {
                if (data[i][x] != 0) {
                    newArr[index] = data[i][x];
                    index++;
                }
            }
            data[i] = newArr;
            
            // 合并元素后，后续元素前移，并在末尾补0
            for (int x = 0; x < data.length - 1; x++) {
                if (data[i][x] == data[i][x + 1]) {
                    data[i][x] *= 2;

                    score += data[i][x];

                    for (int j = x + 1; j < 3; j++) {
                        data[i][j] = data[i][j + 1];
                    }
                    data[i][3] = 0;
                }
            }
        }
    }

    /**
     * 二维数组反转
     */
    public void horizontalSwap(){
        for (int i = 0; i < data.length; i++) {
            reverseArray(data[i]);
        }
    }

    /**
     * 一维数据翻转
     * @param arr
     */
    public void reverseArray(int[] arr) {
        for(int start = 0, end = arr.length - 1; start < end; start++, end--) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
    }

    /**
     * 顺时针旋转
     */
    public void clockwise() {
        int[][] newArr = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newArr[j][3 - i] = data[i][j];
            }
        }
        data = newArr;
    }

    /**
     * 逆时针旋转数据
     */
    public void anticlockwise() {
        int[][] newArr = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newArr[3 - j][i] = data[i][j];
            }
        }
        data = newArr;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void generatorNum() {
        int[] arrI = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] arrJ = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int w = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0) {
                    arrI[w] = i;
                    arrJ[w] = j;
                    w++;
                }
            }
        }

        if (w != 0) {
            Random r = new Random();
            int index = r.nextInt(w);
            int x = arrI[index];
            int y = arrJ[index];
            data[x][y] = 2;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == item1) {
            theme = "A-";
        } else if (e.getSource() == item2) {
            theme = "B-";
        } else if (e.getSource() == item3) {
            System.out.println("换肤为糖果");
        }

        paintView();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == loseLable) {
            restartGame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
