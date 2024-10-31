package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private ServerWindow server;
    private boolean connected;
    private String name;

    JTextArea log;
    JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    JPasswordField password;
    JButton btnLogin, btnSend;
    JPanel headerPanel;
    public ClientGUI(ServerWindow server) {
        this.server = server;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("chat client");
        setLocation(server.getX() -500, server.getY());

        cretePanel();
        setVisible(true);
    }
    public void answer(String text){
        appendLog(text);
    }
    private void appendLog(String text){
        log.append(text + "\n");
    }
    private void connectToServer(){
        if(server.connectUser(this)){
            appendLog("вы успешно подключились!\n");
            headerPanel.setVisible(false);
            connected = true;
            name=tfLogin.getText();
            String log=server.getLog();
            if (log != null){
                appendLog(log);
            }
        }else {
            appendLog("подключение не удалось");
        }
    }
    public void disconnectFromServer(){
        if(connected){
            headerPanel.setVisible(false);
            connected = false;
            server.disconnectUser(this);
            appendLog("вы были отключены от сервера!");
        }
    }
    public void message(){
        if(connected){
            String text=tfMessage.getText();
            if(!text.equals("")){
                server.message(name + ": " + text);
                tfMessage.setText("");
            }
        }else{
            appendLog("нет подключения к серверу");
        }
    }
    private void cretePanel(){
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }
    private Component createHeaderPanel(){
        headerPanel = new JPanel(new GridLayout(2,3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Admin Adminovich");
        password = new JPasswordField("123456");
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);
        headerPanel.add(new JPanel());
        return headerPanel;
    }
    private Component createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }
    private Component createFooter(){
        JPanel footerPanel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n'){
                    message();
                }
            }
        });
        btnSend=new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        footerPanel.add(tfMessage);
        footerPanel.add(btnSend, BorderLayout.EAST);
        return footerPanel;
    }
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            disconnectFromServer();
        }
        super.processWindowEvent(e);
    }



}

