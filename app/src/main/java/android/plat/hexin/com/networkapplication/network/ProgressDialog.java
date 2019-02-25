package android.plat.hexin.com.networkapplication.network;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.plat.hexin.com.networkapplication.R;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public class ProgressDialog extends Dialog {

    public static final    int      DIALLG_TYPE_WITH_MESSAGE = R.style.ProgressDialogStyle;
    public static final    int      DIALOG_TYPE_NO_MESSAGE = R.style.NoMessageDialogStyle;
    public static final    int      DIALOG_TYPE_WIDTH_PROGRESSBAR = R.style.ProgressBarDialogStyle;

    /**
     * Toast显示位置距离底部的偏移量
     */
    public static final int  OFFSET           = 50;

    public static final int ICONHEIGHT        =33;
    /**
     * 默认为有背景框，可以添加加载信息的对话框
     */
    private int               dialogType      = DIALLG_TYPE_WITH_MESSAGE;

    /**
     * 等待图片转动一次的时间片
     */
    private static final int ROTATE_SPAN  = 80;

    /**
     * 等待框显示界面
     */
    private View progressRootView;

    /**
     * 等待图片
     */
    private ImageView loading;

    private Handler handler      = new Handler();

    private ProcessDialogDismissListener    dialogDismissListener;

    /**
     * 执行的旋转操作
     */
    private Runnable         rotateAction = new Runnable() {

        public void run() {
            if (loading != null) {
                loading.getImageMatrix().postRotate(-20,
                        loading.getWidth() * 1.0f / 2 ,
                        loading.getHeight() * 1.0f / 2 );
                loading.invalidate();
                handler.postDelayed(rotateAction, ROTATE_SPAN);
            }
        }
    };

    public ProgressDialog(Context context) {
        super(context,R.style.ProgressDialogStyle);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.gravity = Gravity.BOTTOM;
        // 考虑手机的屏幕密度
        lp.y = (int)(OFFSET*dm.density);
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL; // 三星note5适配,更改type为TYPE_APPLICATION_PANEL，原来为TYPE_APPLICATION_ATTACHED_DIALOG  JIRA:SJCGAP-8458
        getWindow().setAttributes(lp);
        progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.view_progress_dialog, null);
        loading = (ImageView) progressRootView.findViewById(R.id.icon);
        setCanceledOnTouchOutside(true);
        setContentView(progressRootView);
    }
    /**
     * dialogType:
     * DIALLG_TYPE_WITH_MESSAGE  显示在底部，可以设置message，背景会变暗
     * DIALOG_TYPE_NO_MESSAGE    显示在中间，不可以设置message，背景不会变暗
     * DIALOG_TYPE_WIDTH_PROGRESSBAR 显示在中间，上边有loading，下边message
     * 构造函数
     */
    public ProgressDialog(Context context,int style) {
        super(context,(style==DIALLG_TYPE_WITH_MESSAGE||style == DIALOG_TYPE_NO_MESSAGE)?style:DIALLG_TYPE_WITH_MESSAGE);
        this.dialogType = style;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (this.dialogType == DIALOG_TYPE_NO_MESSAGE || this.dialogType == DIALOG_TYPE_WIDTH_PROGRESSBAR) {
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0f;
            lp.y = -(int)(ICONHEIGHT*dm.density/2);
        }
        else  {
            lp.gravity = Gravity.BOTTOM;
            // 考虑手机的屏幕密度
            lp.y = (int)(OFFSET*dm.density);
            lp.dimAmount = 0.5f;
        }
        // 考虑手机的屏幕密度
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL; // 三星note5适配,更改type为TYPE_APPLICATION_PANEL，原来为TYPE_APPLICATION_ATTACHED_DIALOG  JIRA:SJCGAP-8458
        getWindow().setAttributes(lp);
        if (this.dialogType == DIALOG_TYPE_NO_MESSAGE) {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.view_progress_dialog, null);
            progressRootView.setBackgroundResource(0);
        }else if (this.dialogType == DIALOG_TYPE_WIDTH_PROGRESSBAR) {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.view_progressbar_dialog, null);
            progressRootView.setBackgroundResource(R.drawable.middle_tip_dialog_bg);
        }else {
            progressRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.view_progress_dialog, null);
        }
        loading = (ImageView) progressRootView.findViewById(R.id.icon);
        setCanceledOnTouchOutside(true);
        setContentView(progressRootView);
    }
    /**
     * 设置提示信息
     *
     *
     * @param message
     */
    public void setMessage(String message) {
        if (dialogType == DIALOG_TYPE_NO_MESSAGE) {
            return;
        }
        ((TextView) progressRootView.findViewById(R.id.message)).setText(message);
    }

    public void setMessage(String message,int gravityType){
        if (dialogType == DIALOG_TYPE_NO_MESSAGE) {
            return;
        }
        TextView textView = (TextView) progressRootView.findViewById(R.id.message);
        textView.setGravity(gravityType);
        textView.setText(message);
    }

    public void setLoadingVisiable(int visiable){
        if(loading != null){
            loading.setVisibility(visiable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loading != null) {
            handler.postDelayed(rotateAction, ROTATE_SPAN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onRemove();
    }

    public void onRemove() {
        if (loading != null) {
            handler.removeCallbacks(rotateAction);
        }
    }

    @Override
    public void dismiss() {
        Context context = getContext();
        if(context instanceof ContextThemeWrapper){
            context = ((ContextThemeWrapper) context).getBaseContext();
        }
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                super.dismiss();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (dialogDismissListener != null){
                dialogDismissListener.onDialogDismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerDialogDismissListener(ProcessDialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }
    public void unregisterDialogDismissListener(ProcessDialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = null;
    }



    /**
     * 进度条消失的监听器
     * @author zhaoyh
     *
     */
    public interface ProcessDialogDismissListener{
        public void onDialogDismiss();
    }
}
