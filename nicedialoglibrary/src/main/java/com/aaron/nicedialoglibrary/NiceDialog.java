package com.aaron.nicedialoglibrary;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/*
new NiceDialog(context)
    .setLayoutId(R.layout.dialog)     //设置dialog布局文件
    .setTheme(R.style.MyDialog) // 设置dialog主题，默认主题继承自Theme.AppCompat.Light.Dialog
    .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
        @Override
        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
            holder.setText(R.id.title, "提示");
            holder.setText(R.id.message, "简单提示框");
        }
    })
    .setDimAmount(0.3f)  //调节灰色背景透明度[0-1]，默认0.5f
    .setGravity()     //可选，设置dialog的位置，默认居中，可通过系统Gravity的类的常量修改，例如Gravity.BOTTOM（底部），Gravity.Right（右边），Gravity.BOTTOM|Gravity.Right（右下）
    .setMargin()     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
    .setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
    .setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
    .setOutCancel(false)     //点击dialog外是否可取消，默认true
    .setAnimStyle(R.style.EnterExitAnimation)     //设置dialog进入、退出的自定义动画；根据设置的Gravity，默认提供了左、上、右、下位置进入退出的动画
    .show(getSupportFragmentManager());     //显示dialog

几个内置的默认实现：
dialog = NiceDialog.createProgressDialog(context,getSupportFragmentManager(), title);
dialog = NiceDialog.createDialogWithConfirmButton(context,getSupportFragmentManager()
        , title, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog = null;
            }
        });
dialog = NiceDialog.createDialogWithAllFunction(context,getSupportFragmentManager()
        , title, "34343", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了取消");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了ok");
            }
        });
 */
/**
 * DialogFragment 实现的对话框
 *
 * https://github.com/SheHuan/NiceDialog
 */
public class NiceDialog extends DialogFragment {
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    private int margin;//左右边距
    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.5f;//灰度深浅
    private boolean showBottom;//是否底部显示
    private boolean outCancel = true;//是否点击外部取消
    private WeakReference<Context> context;

    @StyleRes
    private int animStyle; //动画
    @LayoutRes
    protected int layoutId;//布局

    private ViewConvertListener convertListener;//视图初始化监听

    public NiceDialog(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    /**
     * 创建进度圆环对话框
     *
     * 点击不可取消，只能调用dismiss()方法取消
     * @param fragmentManager
     * @return
     */
    public static NiceDialog createProgressDialog(Context context,FragmentManager fragmentManager, String content){
        return new NiceDialog(context)
                .setLayoutId(R.layout.dialog_progress)
                .setMargin(60)
                .setOutCancel(false)
                .setConvertListener((ViewConvertListener) (holder, dialog) ->
                        holder.setText(R.id.dialog_message, content))
                .show(fragmentManager);
    }

    /**
     * 创建简单带确认按钮的对话框
     *
     * 点击外部可取消
     * @param fragmentManager
     * @param content
     * @return
     */
    public static NiceDialog createDialogWithConfirmButton(Context context,FragmentManager fragmentManager
            , String content, View.OnClickListener okListener){
        return new NiceDialog(context)
                .setLayoutId(R.layout.dialog_simple)
                .setMargin(60)
                .setOutCancel(true)
                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
                    holder.setText(R.id.dialog_message, content);
                    holder.setOnClickListener(R.id.dialog_ok,okListener);
                })
                .show(fragmentManager);
    }

    /**
     * 创建简单带确认按钮的对话框
     *
     * 点击外部可取消
     * @param fragmentManager
     * @param title
     * @param message
     * @param cancelListener
     * @param okListener
     * @return
     */
    public static NiceDialog createDialogWithAllFunction(Context context,FragmentManager fragmentManager
            , String title, String message, View.OnClickListener cancelListener
            , View.OnClickListener okListener){
        return new NiceDialog(context)
                .setLayoutId(R.layout.dialog_confirm)
                .setMargin(60)
                .setOutCancel(true)
                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
                    holder.setText(R.id.dialog_title, title);
                    holder.setText(R.id.dialog_message, message);
                    holder.setOnClickListener(R.id.dialog_cancel,cancelListener);
                    holder.setOnClickListener(R.id.dialog_ok,okListener);
                })
                .show(fragmentManager);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.NiceDialog);

        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            showBottom = savedInstanceState.getBoolean(BOTTOM);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);

            convertListener = (ViewConvertListener) savedInstanceState.getSerializable("listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        if (convertListener != null) {
            convertListener.convertView(ViewHolder.create(view), this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, margin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putBoolean(BOTTOM, showBottom);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);

        outState.putSerializable("listener", convertListener);
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation;
                }
            }

            //设置dialog宽度
            if (width == 0) {
                lp.width = getScreenWidth() - 2 * dp2px(margin);
            } else {
                lp.width = dp2px(width);
            }
            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = dp2px(height);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
    }

    private int dp2px(float dipValue) {
        final float scale = context.get().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = context.get().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 设置布局文件
     *
     * @param layoutId
     * @return
     */
    public NiceDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    /**
     * 设置视图监听器
     *
     * @param convertListener
     * @return
     */
    public NiceDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    /**
     * 设置距离
     *
     * @param margin
     * @return
     */
    public NiceDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    /**
     * 设置宽度
     *
     * @param width
     * @return
     */
    public NiceDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    public NiceDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 设置背景灰度
     *
     * @param dimAmount
     * @return
     */
    public NiceDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    /**
     * 设置是否显示在底部
     *
     * @param showBottom
     * @return
     */
    public NiceDialog setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    /**
     * 设置是否点击外区域消失
     *
     * @param outCancel
     * @return
     */
    public NiceDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    /**
     * 设置动画
     *
     * @param animStyle
     * @return
     */
    public NiceDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    /**
     * 最后调用，显示对话框
     *
     * @param manager
     * @return
     */
    public NiceDialog show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }
}
