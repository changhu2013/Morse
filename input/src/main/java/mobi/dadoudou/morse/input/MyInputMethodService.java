package mobi.dadoudou.morse.input;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MyInputMethodService extends InputMethodService {

    private List<String> suggestionlist; // 当前候选词表
    private Hashtable<String, List<String>> data; // 词典数据
    private CandidateView candView;

    private KeyboardView keyView;

    /*
    public void onInitializeInterface() {
        //InputMethodService在启动时，系统会调用该方法，具体内容下回再表
        // 初始化 词典数据
        data = new Hashtable<String, List<String>>();
        List<String> list = new ArrayList<String>();
        list.add("if(){}");
        list.add("if(){}else if(){}");
        list.add("if(){}else{}");
        data.put("if", list);

        list = new ArrayList<String>();
        list.add("while(){}");
        data.put("while", list);
    }

    public View onCreateInputView() {
        System.out.println("---->onCreateInputView");
        keyView = new KeyboardView(this);
        System.out.println("---->onCreateInputView " + keyView);
        return keyView;
    }

    @Override
    public View onCreateCandidatesView() {
        candView = new CandidateView(this);
        candView.setService(this);
        return candView;
    }


    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
    }

    */
    public void pickSuggestionManually(int mSelectedIndex) {
        getCurrentInputConnection().commitText(suggestionlist.get(mSelectedIndex), 0); // 往输入框输出内容
        setCandidatesViewShown(false); // 隐藏 CandidatesView
    }

    class KeyboardView extends RelativeLayout {

        public KeyboardView(Context context) {
            super(context);

            Button btIf = new Button(context);
            btIf.setText("1");
            btIf.setId(1);
            RelativeLayout.LayoutParams lpIf = new RelativeLayout.LayoutParams(100, 50);
            lpIf.addRule(RelativeLayout.CENTER_HORIZONTAL);

            btIf.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    setCandidatesViewShown(true); // 显示 CandidateView
                    MyInputMethodService.this.onKey("if"); // 将点击按钮的值传回给 InputMethodService

                    System.out.println("点击1");

                }
            });
            addView(btIf, lpIf);

            Button btWhile = new Button(context);
            btWhile.setText("0");
            RelativeLayout.LayoutParams lpWhile = new RelativeLayout.LayoutParams(100, 50);
            lpWhile.addRule(RelativeLayout.BELOW, 1);
            lpWhile.addRule(RelativeLayout.ALIGN_LEFT, 1);

            btWhile.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    setCandidatesViewShown(true);
                    MyInputMethodService.this.onKey("while");
                    System.out.println("点击0");
                }
            });

            addView(btWhile, lpWhile);
        }
    }

    private void onKey(String text) {
        suggestionlist = data.get(text);
        candView.setSuggestions(suggestionlist);
    }


    class CandidateView extends RelativeLayout {
        TextView tv;                 // 中间显示候选词
        Button btLeft, btRight; // 左右按钮
        MyInputMethodService listener;         // helloIme 用于返回选中的 候选词下标
        List<String> suggestions; // 候选词列表， KeyboardView 不同的键按下后会设置相关的列表
        int mSelectedIndex = -1;  // 当前 候选词下标

        public CandidateView(Context context) {
            super(context);

            tv = new TextView(context);
            tv.setId(1);
            RelativeLayout.LayoutParams lpCenter = new RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpCenter.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(tv, lpCenter);
            tv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    listener.pickSuggestionManually(mSelectedIndex);
                }
            });

            btLeft = new Button(context);
            btLeft.setText("<");
            btLeft.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    mSelectedIndex = mSelectedIndex > 0 ? (mSelectedIndex - 1) : 0;
                    tv.setText(suggestions.get(mSelectedIndex));
                }
            });

            RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpLeft.addRule(RelativeLayout.LEFT_OF, 1);
            addView(btLeft, lpLeft);

            btRight = new Button(context);
            btRight.setText(">");
            btRight.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    mSelectedIndex = mSelectedIndex >= suggestions.size() - 1 ? suggestions.size() - 1 : mSelectedIndex + 1;
                    tv.setText(suggestions.get(mSelectedIndex));
                }
            });
            RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpRight.addRule(RelativeLayout.RIGHT_OF, 1);
            addView(btRight, lpRight);
        }

        public void setService(MyInputMethodService listener) {
            this.listener = listener;
        }

        public void setSuggestions(List<String> suggestions) {
            mSelectedIndex = 0;
            tv.setText(suggestions.get(mSelectedIndex));
            this.suggestions = suggestions;
        }
    }

}


