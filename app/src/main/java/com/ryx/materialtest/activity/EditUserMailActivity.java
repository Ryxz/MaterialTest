package com.ryx.materialtest.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ryx.materialtest.R;
import com.ryx.materialtest.bean.ChangeUserBean;

/**
 * Created by Ryx on 2019/1/7.
 */
public class EditUserMailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mailBack;
    private ImageView mailSure;
    private EditText mailEditText;
    private ChangeUserBean bean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mail);

        mailBack = (ImageView) findViewById(R.id.edit_mail__back);
        mailSure = (ImageView) findViewById(R.id.sure_mail_img);
        mailEditText = (EditText) findViewById(R.id.info_mail_et);

        mailEditText.setOnClickListener(this);
        mailSure.setOnClickListener(this);
        mailBack.setOnClickListener(this);

        bean = (ChangeUserBean) getIntent().getSerializableExtra("mail");
        String ss = bean.getMaileInfo();
        mailEditText.setText(ss);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_mail_img:
                Intent intent = new Intent();
                String sd = mailEditText.getText().toString();
                intent.putExtra("mail_return",sd);
                setResult(RESULT_OK,intent);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.edit_mail__back:
                finish();
                break;
        }
    }
}
