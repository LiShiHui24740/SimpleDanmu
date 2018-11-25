package com.airland.datastruct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airland.simpledanmuku.message.ISimpleMessageAdapter;
import com.airland.simpledanmuku.widget.SimpleDanmuView;
import com.airland.simpledanmuku.widget.SimpleItemBaseView;
import com.airland.simpledanmuku.widget.SimpleItemPictureView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView danmu, welcome;
    private SimpleDanmuView simpleDanmuView;
    private SimpleWelcomeView simpleWelcomeView;
    private String[] strs_Danmu = {"lalallalala", "hahahahahahahahahahahah", "wqijeiwdjkdkalsdalskdjalksdjlasdald"};
    private String[] strs_welcome = {"李木子", "撒拉弗", "库德乐"};
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        danmu = findViewById(R.id.id_danmu);
        welcome = findViewById(R.id.id_welcome);

        simpleDanmuView = findViewById(R.id.id_sd);
        simpleWelcomeView = findViewById(R.id.id_sw);

        simpleDanmuView.setMessageAdapter(new ISimpleMessageAdapter<TestMessage>() {
            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public SimpleItemBaseView getView(TestMessage testMessage) {
                SimpleItemPictureView simpleItemPictureView = new SimpleItemPictureView(MainActivity.this);
                simpleItemPictureView.getContentView().setText(testMessage.getContent());
                return simpleItemPictureView;
            }
        });

        simpleWelcomeView.setMessageAdapter(new ISimpleMessageAdapter<TestMessage>() {
            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public SimpleItemBaseView getView(TestMessage testMessage) {
                SimpleItemWelcomeView simpleItemWelcomeView = new SimpleItemWelcomeView(MainActivity.this);
                simpleItemWelcomeView.getNameView().setText(testMessage.getContent());
                return simpleItemWelcomeView;
            }
        });

        danmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMessage testMessage = new TestMessage();
                int index = random.nextInt(3);
                testMessage.setContent(strs_Danmu[index]);
                simpleDanmuView.addInMessageQueue(testMessage);
            }
        });

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMessage testMessage = new TestMessage();
                int index = random.nextInt(3);
                testMessage.setContent(strs_welcome[index]);
                simpleWelcomeView.addInMessageQueue(testMessage);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleDanmuView.endDealWithMessage();
        simpleWelcomeView.endDealWithMessage();
    }
}
