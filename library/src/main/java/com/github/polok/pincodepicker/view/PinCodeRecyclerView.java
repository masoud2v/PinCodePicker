/**
 * Copyright 2015 Marcin Polak
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.polok.pincodepicker.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.polok.pincodepicker.PinCodeListener;
import com.github.polok.pincodepicker.PinCodeValidation;
import com.github.polok.pincodepicker.R;
import com.github.polok.pincodepicker.adapter.PinCodeAdapter;
import com.github.polok.pincodepicker.adapter.RecyclerViewInsetDecoration;
import com.github.polok.pincodepicker.model.PinCodeType;

public class PinCodeRecyclerView extends android.support.v7.widget.RecyclerView {

    private GridLayoutManager layoutManager;
    private PinCodeAdapter pinCodeAdapter;

    private int pinCodeLength;

    public PinCodeRecyclerView(Context context) {
        this(context, null);
    }

    public PinCodeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCodeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinCodeRecyclerViewWidget);
        pinCodeLength = typedArray.getInt(R.styleable.PinCodeRecyclerViewWidget_pin_code_length, 0);
        int filledOutDrawableId = typedArray.getResourceId(R.styleable.PinCodeRecyclerViewWidget_pin_code_filled_out_drawable, R.drawable.ic_pin_code_check);

        int currentAnimationResId = typedArray.getResourceId(R.styleable.PinCodeRecyclerViewWidget_pin_code_animation_current, R.animator.indicator_no_animator);
        Animator animationCurrent= AnimatorInflater.loadAnimator(context, currentAnimationResId);

        PinCodeType pinCodeType = PinCodeType.typeFromName(typedArray.getString(R.styleable.PinCodeRecyclerViewWidget_pin_code_type));

        setHasFixedSize(true);

        layoutManager = new GridLayoutManager(context, 1);
        layoutManager.setSpanCount(pinCodeLength);

        setLayoutManager(layoutManager);

        pinCodeAdapter = new PinCodeAdapter(getResources(), pinCodeLength, pinCodeType, filledOutDrawableId);
        setAdapter(pinCodeAdapter);

        pinCodeAdapter.setCurrentPinCodeAnimation(animationCurrent);

        addItemDecoration(new RecyclerViewInsetDecoration(context.getResources(), R.dimen.pin_code_view_inset_default));
        setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.height = getResources().getDimensionPixelSize(R.dimen.pin_code_height);
        setLayoutParams(params);
    }

    public void setPinCodeListener(PinCodeListener pinCodeListener) {
        pinCodeAdapter.setPinCodeListener(pinCodeListener);
    }

    public void setPinCodeValidation(PinCodeValidation pinCodeValidation) {
        pinCodeAdapter.setPinCodeValidation(pinCodeValidation);
    }
}
