package moose.com.ac.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import moose.com.ac.R;
import moose.com.ac.util.DisplayUtil;
import moose.com.ac.util.Theme;

/**
 * Copyright (C) 2015 The Android Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by dell on 2015/8/20.
 */
public class FloorsView extends LinearLayout {
    private Drawable mBorder;
    private int mMaxNum;

    public FloorsView(Context context) {
        this(context, null);
    }

    public FloorsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        mMaxNum = getResources().getInteger(R.integer.max_floors_w);
    }

    public void setQuoteList(List<View> quoteList) {
        if (quoteList == null || quoteList.isEmpty()) {
            removeAllViewsInLayout();
            return;
        }
        int spacing = DisplayUtil.dip2px(getContext(), 4);
        int j = 0;
        for (int i = quoteList.size() - 1; i >= 0; i--) {
            LinearLayout.LayoutParams params = generateDefaultLayoutParams();
            int k = spacing * i;
            if (quoteList.size() > mMaxNum + 2 && i > mMaxNum) {//顶部margin的值是之前的view的高度的数目倍，以此达到效果
                k = spacing * mMaxNum;
            }
            params.leftMargin = k;
            params.rightMargin = k;
            params.topMargin = j == 0 ? k : 0;
            View v = quoteList.get(i);
            TextView floor = (TextView) v.findViewById(R.id.floor);
            floor.setText(String.valueOf(j + 1));
            addViewInLayout(v, j++, params);
        }
    }

    public void setFloorBorder(Drawable border) {
        this.mBorder = border;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {//纯粹实现边框
        if (!isPressed()) {
            final int i = getChildCount();
            if (this.mBorder == null) {
                // stroke border if above v17
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    this.mBorder = getResources().getDrawable(Theme.isNightMode() ? R.drawable.floors_border_dark : R.drawable.floors_border);
                    // else using .9 png
                else
                    this.mBorder = getResources().getDrawable(Theme.isNightMode() ? R.drawable.comment_floor_bg_dark_2 : R.drawable.comment_floor_bg_2);
            }
            if ((this.mBorder != null) && (i > 0))
                for (int j = i - 1; j >= 0; j--) {
                    View child = getChildAt(j);
                    this.mBorder.setBounds(child.getLeft(), child.getLeft(),
                            child.getRight(), child.getBottom());
                    // draw background color only once
                    if (j == i - 1) {
                        int border = DisplayUtil.dip2px(getContext(), 1);
                        Rect bounds = mBorder.copyBounds();
                        ColorDrawable drawable = new ColorDrawable(Theme.isNightMode() ? 0xFF545454 : 0xFFFFFEEE);
                        drawable.setBounds(bounds.left + border, bounds.top + border, bounds.right - border, bounds.bottom - border);
                        drawable.draw(canvas);
                    }
                    this.mBorder.draw(canvas);
                }
        }
        super.dispatchDraw(canvas);
    }
}
