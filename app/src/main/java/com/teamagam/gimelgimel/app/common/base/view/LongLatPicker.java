package com.teamagam.gimelgimel.app.common.base.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.esri.EsriUtils;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.teamagam.gimelgimel.data.config.Constants.COORDINATE_SYSTEM_PREF_KEY;

public class LongLatPicker extends LinearLayout {

    private static final String LONG_LAT_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private static final String LABEL_ATTRIBUTE_STRING = "label";
    private static final int MIN_LAT_VALUE = -90;
    private static final int MAX_LAT_VALUE = 90;
    private static final int MIN_LONG_VALUE = -180;
    private static final int MAX_LONG_VALUE = 180;
    private static final int MIN_X_VALUE = 0;
    private static final int MAX_X_VALUE = (int) 1e6;
    private static final int MIN_Y_VALUE = 0;
    private static final int MAX_Y_VALUE = (int) 1e7;
    private static final NoOpListener NO_OP_LISTENER = new NoOpListener();

    @BindView(R.id.long_lat_picker_long)
    EditText mLongEditText;

    @BindView(R.id.long_lat_picker_lat)
    EditText mLatEditText;

    @BindView(R.id.long_lat_picker_text_view)
    TextView mLabelTextView;

    @Inject
    UserPreferencesRepository mUserPreferencesRepo;

    private OnValidStateChangedListener mListener;

    public LongLatPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.long_lat_picker, this);

        ButterKnife.bind(inflate, this);

        mListener = NO_OP_LISTENER;

        mLatEditText.addTextChangedListener(new ValidityTextWatcher());
        mLongEditText.addTextChangedListener(new ValidityTextWatcher());

        if (isUseUtmMode()) {
            mLongEditText.addTextChangedListener(new MinMaxTextWatcher(MIN_X_VALUE, MAX_X_VALUE));
            mLatEditText.addTextChangedListener(new MinMaxTextWatcher(MIN_Y_VALUE, MAX_Y_VALUE));
            mLongEditText.setHint(R.string.horizontal_long_lat_picker_x_hint);
            mLatEditText.setHint(R.string.horizontal_long_lat_picker_y_hint);
        } else {
            mLongEditText.addTextChangedListener(new MinMaxTextWatcher(MIN_LONG_VALUE, MAX_LONG_VALUE));
            mLatEditText.addTextChangedListener(new MinMaxTextWatcher(MIN_LAT_VALUE, MAX_LAT_VALUE));
        }

        initLabel(attrs);
    }

    public boolean hasPoint() {
        return !getLat().isNaN() && !getLong().isNaN();
    }

    public PointGeometry getPoint() {
        if (isUseUtmMode()) {
            PointGeometry point = new PointGeometry(getLat(), getLong());
            return EsriUtils.projectDomainPoint(
                    point, EsriUtils.ED50_UTM_36_N, EsriUtils.WGS_84_GEO);
        } else {
            return new PointGeometry(getLat(), getLong());
        }
    }

    public void setPoint(PointGeometry pointGeometry) {
        mLatEditText.setText(Double.toString(pointGeometry.getLatitude()));
        mLongEditText.setText(Double.toString(pointGeometry.getLongitude()));
    }

    public void setOnValidStateChangedListener(OnValidStateChangedListener listener) {
        if (listener == null) {
            mListener = NO_OP_LISTENER;
        } else {
            mListener = listener;
        }
    }

    private boolean isUseUtmMode() {
        return mUserPreferencesRepo.getBoolean(COORDINATE_SYSTEM_PREF_KEY);
    }

    private void initLabel(AttributeSet attrs) {
        String labelString = getLabelString(attrs);
        if (labelString == null || labelString.isEmpty()) {
            mLabelTextView.setVisibility(GONE);
        } else {
            mLabelTextView.setText(labelString);
        }
    }

    private String getLabelString(AttributeSet attrs) {
        int resId = attrs.getAttributeResourceValue(LONG_LAT_NAMESPACE, LABEL_ATTRIBUTE_STRING, 0);
        if (resId != 0) {
            return getContext().getResources().getString(resId);
        } else {
            return attrs.getAttributeValue(LONG_LAT_NAMESPACE, LABEL_ATTRIBUTE_STRING);
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
        return isNumeric(editTextString) ? Float.valueOf(editTextString) : Float.NaN;
    }

    private static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public interface OnValidStateChangedListener {
        void onValid();

        void onInvalid();
    }

    private static class NoOpListener implements OnValidStateChangedListener {
        @Override
        public void onValid() {

        }

        @Override
        public void onInvalid() {

        }
    }

    private static class MinMaxTextWatcher implements TextWatcher {

        private double mMin;
        private double mMax;

        MinMaxTextWatcher(double min, double max) {
            mMin = min;
            mMax = max;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (isNumeric(text)) {
                double val = Double.parseDouble(text);
                if (val > mMax) {
                    changeText(s, mMax);
                } else if (val < mMin) {
                    changeText(s, mMin);
                }
            }
        }

        private void changeText(Editable s, double number) {
            s.replace(0, s.length(), Double.toString(number));
        }
    }

    private class ValidityTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (hasPoint()) {
                mListener.onValid();
            } else {
                mListener.onInvalid();
            }
        }
    }
}