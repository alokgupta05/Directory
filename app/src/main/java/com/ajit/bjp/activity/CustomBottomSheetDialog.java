package com.ajit.bjp.activity;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ajit.bjp.R;


import io.reactivex.disposables.Disposable;

/**
 * Created by o8428 on 9/6/2017.
 */

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    protected View view;
    protected Disposable disposable;
    protected FragmentActivity mFragmentActivity;

    private String TAG = getClass().getSimpleName();

    public void setCustomView(View view) {
        this.view = view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        if (view != null) {
            dialog.setContentView(view);
            setBehavior();
        }


        return dialog;
    }

    public FragmentActivity getmFragmentActivity() {
        return mFragmentActivity;
    }

    public BottomSheetBehavior getmBehavior() {
        return mBehavior;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mBehavior!=null){
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{
            collapseAndDismissFragment();
            return;
        }


        handleTouchEvent();
        if (view != null && view.findViewById(R.id.downArrowImage) != null) {
            view.findViewById(R.id.downArrowImage).setOnClickListener(
                    (View view) -> collapseAndDismissFragment()
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public String getFragmentTag() {
        return CustomBottomSheetDialog.class.getName();
    }

    public void collapseAndDismissFragment() {
        if (getmBehavior() == null)
            return;
        if (isAdded())
            dismiss();
    }

    protected void setBehavior() {
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setHideable(true);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBehavior.setPeekHeight(0);
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (isAdded())
                        dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Dismiss keyboard on slide
                if (view != null && getContext() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }
            }
        });
    }

    private void handleTouchEvent() {
        try {
            if (view != null)
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent ev) {

                        View view = getDialog().getWindow().getCurrentFocus();
                        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
                            int scrcoords[] = new int[2];
                            view.getLocationOnScreen(scrcoords);
                            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                            float y = ev.getRawY() + view.getTop() - scrcoords[1];
                            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                                ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getDialog().getWindow().getDecorView().getApplicationWindowToken()), 0);
                                getDialog().getWindow().getDecorView().requestFocus();
                            }

                        }
                        return true;
                    }
                });
        } catch (IllegalStateException e) {
            //e.printStackTrace();
          //  PbLogger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBehavior = null;
    }
}

