package com.ryx.materialtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.materialtest.R;
import com.ryx.materialtest.bean.ChangeUserBean;
import com.ryx.materialtest.utils.Constans;
import com.ryx.materialtest.utils.InfoPrefs;

/**
 * Created by Ryx on 2019/1/7.
 */
public class MailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CHANGE_USER_MAIL = 15;
    private RelativeLayout userMail;
    private ChangeUserBean bean;
    private TextView textView_user_mail;
    private ImageView mailImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        textView_user_mail = findViewById(R.id.user_mail_text);
        InfoPrefs.init("user_info");
        userMail = (RelativeLayout) findViewById(R.id.user_mail);
        mailImg = (ImageView) findViewById(R.id.mail_back);

        userMail.setOnClickListener(this);
        mailImg.setOnClickListener(this);

        refresh();
    }
    public void refresh(){

        String mail = InfoPrefs.getData(Constans.UserInfo.MAIL);
        textView_user_mail.setText(mail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_USER_MAIL:
                    String returnMail = data.getStringExtra("mail_return");
                    InfoPrefs.setData(Constans.UserInfo.MAIL,returnMail);
                    textView_user_mail.setText(InfoPrefs.getData(Constans.UserInfo.MAIL));
                default:
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mail_back:
                finish();
                break;
            case R.id.user_mail:
                bean = new ChangeUserBean();
                bean.setMaileInfo(InfoPrefs.getData(Constans.UserInfo.MAIL));

                Intent i = new Intent();
                i.setClass(MailActivity.this,EditUserMailActivity.class);
                i.putExtra("mail",bean);
                startActivityForResult(i,REQUEST_CHANGE_USER_MAIL);
                break;

        }
    }
}
