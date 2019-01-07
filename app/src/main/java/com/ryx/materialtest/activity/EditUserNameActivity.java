package com.ryx.materialtest.activity;

import android.content.Intent;
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
public class EditUserNameActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView sureNameImg;
    private ImageView backImg;
    private EditText etText;
    private ChangeUserBean bean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        sureNameImg = (ImageView) findViewById(R.id.sure_neme_img);
        backImg = (ImageView) findViewById(R.id.edit_name__back);
        etText = (EditText) findViewById(R.id.info_name_et);

        sureNameImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        etText.setOnClickListener(this);
        bean = (ChangeUserBean) getIntent().getSerializableExtra("data");
        String as = bean.getNameinfo();
        etText.setText(bean.getNameinfo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_neme_img:
                Intent intent = new Intent();
                intent.putExtra("data_return",etText.getText().toString());
                setResult(RESULT_OK,intent);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.edit_name__back:
                finish();
                break;

        }
    }
}
