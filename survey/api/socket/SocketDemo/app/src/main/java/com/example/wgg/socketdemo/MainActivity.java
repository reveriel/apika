package com.example.wgg.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button btnGet = null;
    TextView textInfo = null;
    String line, response = "";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            textInfo.setText(response);
            btnGet.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInfo = (TextView)findViewById(R.id.textInfo);
        btnGet = (Button)findViewById(R.id.btnGet);

        btnGet.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                textInfo.setText("Waiting...");
                btnGet.setEnabled(false);
                new Thread(new Runnable(){
                    public void run() {
                        response = SocketHttpGetTest();
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    // 一个Android Socket 的最简单的例子
    // 函数SocketHttpTest() 向202.38.64.40(test.ustc.edu.cn) 发送一个HTTP请求
    // 获得响应后将原始响应报文作为字符串返回
    private String SocketHttpGetTest() {
        String msg = "";
        try{
            // 创建Socket对象，设置通信对方的IP和端口
            Socket socket = new Socket("202.38.64.40",80);
            // 获取该Socket的输入输出流
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream reader = new DataInputStream( socket.getInputStream());
            // 发送请求报文(HTTP请求头)
            writer.writeUTF("GET / HTTP/1.1\n" +
                    "Host: test.ustc.edu.cn\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 "+
                    "(KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36\n" +
                    "Accept: text/html\n" +
                    "Accept-Encoding: deflate, sdch\n" +
                    "Accept-Language: zh-CN,zh;q=0.8\n\n");
            // 接受32行响应数据
            while((line=reader.readLine())!=null){
                msg += line + "\n";
            }
            socket.close();
        }catch (Exception ex) {
            return "Network Error: " + ex.toString();
        }
        return msg;
    }

}
