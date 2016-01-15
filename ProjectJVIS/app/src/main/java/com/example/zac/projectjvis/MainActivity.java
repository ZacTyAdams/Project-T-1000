package com.example.zac.projectjvis;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.util.Properties;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button)findViewById(R.id.button);
        final TextView text = (TextView)findViewById(R.id.text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            String out = executeRemoteCommand("pi", "raspberry", "192.168.2.156", 22);
                            System.out.println(out);

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        return null;
                    }
                }.execute(1);
            }
        });
    }
    public String executeRemoteCommand(String username, String password, String hostname, int port) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        //Attempting to avaoid confirmation of RSA key
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        //initial connection
        session.connect();

        //opening ssh channel
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        //command to execute
        channelssh.setCommand("ls");
        channelssh.connect();

        channelssh.disconnect();

        return baos.toString();
    }
}
