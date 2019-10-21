package com.airland.datastruct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airland.simpledanmuku.message.AbstractMessage;
import com.airland.simpledanmuku.message.ISimpleMessageAdapter;
import com.airland.simpledanmuku.widget.SimpleDanmuView;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;
import com.airland.simpledanmuku.widget.itemviews.SimpleItemPictureView;
import com.airland.simpledanmuku.widget.itemviews.SimpleItemTextView;

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
        Log.d("lishihuiya", "onCreate");
        danmu = findViewById(R.id.id_danmu);
        welcome = findViewById(R.id.id_welcome);

        simpleDanmuView = findViewById(R.id.id_sd);
        simpleWelcomeView = findViewById(R.id.id_sw);
        simpleDanmuView.setMessageAdapter(new ISimpleMessageAdapter<AbstractMessage>() {
            @Override
            public SimpleItemBaseView getView(AbstractMessage testMessage) {
                if (testMessage.getMsgType()==1){
                    SimpleItemPictureView simpleItemPictureView = new SimpleItemPictureView(MainActivity.this);
                    simpleItemPictureView.getContentView().setText(((TestMessage)testMessage.getBean()).getContent());
                    return simpleItemPictureView;
                }else{
                    SimpleItemTextView simpleItemTextView = new SimpleItemTextView(MainActivity.this);
                    simpleItemTextView.getTextView().setText(((TestMessage)testMessage.getBean()).getContent());
                    return simpleItemTextView;
                }

            }
        });


        simpleWelcomeView.setMessageAdapter(new ISimpleMessageAdapter<AbstractMessage>() {
            @Override
            public SimpleItemBaseView getView(AbstractMessage testMessage) {
                SimpleItemWelcomeView simpleItemWelcomeView = new SimpleItemWelcomeView(MainActivity.this);
                simpleItemWelcomeView.getNameView().setText(((TestMessage)testMessage.getBean()).getContent());
                return simpleItemWelcomeView;
            }
        });

        danmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbstractMessage abstractMessage = new AbstractMessage();
                TestMessage testMessage = new TestMessage();
                int index = random.nextInt(3);
                if (index == 2)
                    abstractMessage.setMsgType(2);
                else
                    abstractMessage.setMsgType(1);
                testMessage.setContent(strs_Danmu[index]);
                abstractMessage.setBean(testMessage);
                simpleDanmuView.addInMessageQueue(abstractMessage);
            }
        });

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMessage testMessage = new TestMessage();
                int index = random.nextInt(3);
                testMessage.setContent(strs_welcome[index]);
                AbstractMessage abstractMessage = new AbstractMessage();
                abstractMessage.setBean(testMessage);
                simpleWelcomeView.addInMessageQueue(abstractMessage);
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Log.d("lishihuiya", "程序退出");
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lishihuiya", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lishihuiya", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lishihuiya", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lishihuiya", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lishihuiya", "onDestroy");
        simpleDanmuView.endDealWithMessage();
        simpleWelcomeView.endDealWithMessage();
    }
}
