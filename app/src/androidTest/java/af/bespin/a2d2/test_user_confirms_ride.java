package af.bespin.a2d2;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import af.bespin.a2d2.controllers.Rider_RequestRide;
import af.bespin.a2d2.controllers.Rider_RideStatus;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;

public class test_user_confirms_ride {
    @Rule
    public ActivityTestRule<Rider_RequestRide> mRequestRideActivity = new ActivityTestRule<>(Rider_RequestRide.class);


    private Rider_RequestRide mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;
    private TextInputEditText mPhoneNumberEditText;
    private TextInputEditText mNameEditText;
    private TextInputLayout mPhoneNumberInputLayout;
    private MaterialButton mRequestDriverButton;
    private Instrumentation.ActivityMonitor mRideStatusMonitor;
    @Nullable
    private static String errorMessage;

    @Before
    public void setUp() {
        mActivity = mRequestRideActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(getInstrumentation());
        mRequestDriverButton = mActivity.findViewById(R.id.button_request_driver);
        mPhoneNumberInputLayout = (TextInputLayout) mActivity.findViewById(R.id.activity_request_ride_phone_number_text_input_layout);
        mPhoneNumberEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_phone_number_text_edit);
        mNameEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_name_text_edit);
        mRideStatusMonitor = mInstrumentation.addMonitor(Rider_RideStatus.class.getName(), null, false);
    }

    //ensures the request button exists
    @Test
    public void hasRequestDriverButton() {
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);
    }


    @Test
    public void clickConfirmButton_rideStatusPageOpens() {


        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);

        getInstrumentation().runOnMainSync(()->{
            mNameEditText.setText("Scott Yamamoto");
            mPhoneNumberEditText.setText("8087386925");

            buttonRequestDriver.performClick();
        });

        onView(withText(R.string.dialog_okay))
                .inRoot(withDecorView(IsNot.not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed())).perform(click());

        Activity mRideStatusActivity = mInstrumentation.waitForMonitorWithTimeout(mRideStatusMonitor, 1000);
        assertNotNull(mRideStatusActivity);
    }

}
