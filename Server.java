import java.io.*;
import java.net.*;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Server extends JFrame{
    ServerSocket server;
    Socket socket;

    // declaring swing components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    BufferedReader br;
    PrintWriter out;
    //Constructor..
    public Server(){
        try {
            server = new ServerSocket(5556);
            System.out.println("server is ready to accept connection..");
            System.out.println("waitng...");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWritting();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    // System.out.println("enter is pressed");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Server :"+contentToSend+"\n");
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
            
        });
    }
    private void createGUI() {
        // gui codes
        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("c.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        
        // setting layout of frame
        this.setLayout(new BorderLayout());
        // adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }
    public void startReading(){
        // thread to read data
        Runnable r1 = ()->{
            System.out.println("reader started..");
            try {
                while(true){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        // System.out.println("Client terminated the chat...");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client : "+msg);
                    messageArea.append("Client : "+msg+"\n");
                }                
            }catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
            }
        };
        new Thread(r1).start(); 
    }
    public void startWritting(){
        // thread to write data
        Runnable r2 = ()->{
            System.out.println("writter started...");
            try {
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            
        };
        new Thread(r2).start();   
    }
    public static void main(String[] args) {
        System.out.println("this is server going to start server");
        new Server();

    }
}