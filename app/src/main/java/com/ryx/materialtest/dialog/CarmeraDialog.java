package com.ryx.materialtest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ryx.materialtest.R;

/**
 * Created by Ryx on 2019/1/4.
 */
public class CarmeraDialog extends Dialog implements View.OnClickListener{

    private Button carmeraButton;
    private Button cancleButton;
    private Button selectButton;
    public CarmeraListener carmeraListener;
    private Context mContext;

    public CarmeraDialog(Context context) {
        super(context);
    }

    public CarmeraDialog(Context context, CarmeraListener carmeraListener) {
        super(context, R.style.DialogStyle);
        this.mContext = context;
        this.carmeraListener = carmeraListener;
    }


    public interface CarmeraListener{
        public void onClick(View view);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        carmeraButton = (Button) findViewById(R.id.do_button);
        cancleButton = (Button) findViewById(R.id.cancle_button);
        selectButton = (Button) findViewById(R.id.select_img_button);

        cancleButton.setOnClickListener(this);
        carmeraButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        carmeraListener.onClick(v);
    }

//
//    public CarmeraDialog(Context context, View.OnClickListener carmeraListener, View.OnClickListener selectListener) {
//        super(context, R.style.DialogStyle);
//        setContentView(R.layout.dialog);
//
//        setCancelable(false);
//        setCanceledOnTouchOutside(false);
//
//        findViewById(R.id.do_button).setOnClickListener(carmeraListener);
//        findViewById(R.id.select_img_button).setOnClickListener(selectListener);
//        findViewById(R.id.cancle_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void show() {
//        super.show();
//    }
}
