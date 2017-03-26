package com.teamagam.gimelgimel.app.common.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalLongLatPicker extends LinearLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private static final String LABEL_ATTRIBUTE_STRING = "label";

    @BindView(R.id.horizontal_long_lat_picker_long)
    EditText mLongEditText;

    @BindView(R.id.horizontal_long_lat_picker_lat)
    EditText mLatEditText;

    @BindView(R.id.horizontal_long_lat_picker_text_view)
    TextView mLabelTextView;

    public HorizontalLongLatPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.horizontal_long_lat_picker, this);

        ButterKnife.bind(inflate, this);

        mLabelTextView.setText(getLabelString(attrs));
    }

    public boolean hasPoint() {
        return getLat() > 0 && getLong() > 0;
    }

    public PointGeometry getPoint() {
        return new PointGeometry(getLat(), getLong());
    }

    private String getLabelString(AttributeSet attrs) {
        int resId = attrs.getAttributeResourceValue(NAMESPACE, LABEL_ATTRIBUTE_STRING, 0);
        if (resId != 0) {
            return getContext().getResources().getString(resId);
        } else {
            return attrs.getAttributeValue(NAMESPACE, LABEL_ATTRIBUTE_STRING);
        }
    }

    private Float getLong() {
        return getNumeric(mLongEditText);
    }

    private Float getLat() {
        return getNumeric(mLatEditText);
    }

    private Float getNumeric(EditText editText) {
        String editTextString = editText.getText().toString();
        return editTextString.isEmpty() ? -1f : Float.valueOf(editTextString);
    }
}
