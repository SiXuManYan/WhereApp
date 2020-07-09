package com.jcs.where.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class WorksSizeCheckUtil {
    //发布作品时候填写尺寸的监听器
    static IEditTextChangeListener mChangeListener;

    public static void setChangeListener(IEditTextChangeListener changeListener) {
        mChangeListener = changeListener;
    }


    /**
     * 检测输入框是否都输入了内容
     * 从而改变按钮的是否可点击
     * 以及输入框后面的X的可见性，X点击删除输入框的内容
     */
    public static class textChangeListener {
        private TextView button;
        private EditText[] editTexts;

        public textChangeListener(TextView button) {
            this.button = button;
        }

        public textChangeListener addAllEditText(EditText... editTexts) {
            this.editTexts = editTexts;
            initEditListener();
            return this;
        }


        private void initEditListener() {
            for (EditText editText : editTexts) {
                editText.addTextChangedListener(new textChange());
            }
        }


        /**
         * edit输入的变化来改变按钮的是否点击
         */
        private class textChange implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkAllEdit()) {
                    mChangeListener.textChange(true);
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                    mChangeListener.textChange(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        }

        /**
         * 检查所有的edit是否输入了数据
         *
         * @return
         */
        private boolean checkAllEdit() {
            for (EditText editText : editTexts) {
                if (!TextUtils.isEmpty(editText.getText() + "")) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}