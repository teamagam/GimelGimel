package com.teamagam.gimelgimel;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.teamagam.gimelgimel.app.mainActivity.MainActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public ApplicationTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void test_shouldShowBookTitle() throws Exception {
        TextView titleText = (TextView) solo.getView(R.id.title);
        assertEquals(View.VISIBLE, titleText.getVisibility());
        solo.sleep(1000);
        assertEquals(titleText.getText().toString().startsWith(solo.getString(R.string.app_name)), true);
    }
}
