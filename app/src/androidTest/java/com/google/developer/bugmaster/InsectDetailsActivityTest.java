package com.google.developer.bugmaster;

import android.content.Intent;
import android.database.Cursor;
import android.support.test.espresso.core.deps.guava.base.Objects;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.developer.bugmaster.InsectDetailsActivity.INSECT_ID;

//Testing Task 2
@RunWith(AndroidJUnit4.class)
public class InsectDetailsActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            InsectDetailsActivity.class, false, false);

    public static BoundedMatcher<View, TextView> hasTheRightText(final String text) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView textView) {
                return (Objects.equal(textView.getText(), text));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has the right text");
            }
        };
    }

    @Test
    public void ensureInsectTextIsDisplayed() {

        //We get an insect
        Cursor mCursor = DatabaseManager.getInstance(getInstrumentation().getTargetContext()).queryAllInsects(null);
        mCursor.moveToFirst();
        Insect insect = new Insect(mCursor);
        Intent intent = new Intent();
        //We pass it to the activity
        intent.putExtra(INSECT_ID, insect.id);
        mActivityRule.launchActivity(intent);
        //We check if the activity displays the text contents of the insect we passed to it
        onView(withId(R.id.common_name_detail)).check(matches(hasTheRightText(insect.name)));
        onView(withId(R.id.scientific_name_detail)).check(matches(hasTheRightText(insect.scientificName)));
        onView(withId(R.id.classification)).check(matches(hasTheRightText(insect.classification)));
    }


}