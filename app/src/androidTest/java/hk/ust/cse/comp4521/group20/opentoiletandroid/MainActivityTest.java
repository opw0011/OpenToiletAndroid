package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by opw on 24/5/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void navigationDrawerOpenTest() throws InterruptedException {
        //onView(withId(R.id.update)).perform(click());
        Thread.sleep(2000);

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
    }

    @Test
    public void navigationDrawerClickHomeTest() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer
        Thread.sleep(1000);
        onView(withText("Home")).perform(click());
    }

    @Test
    public void navigationDrawerClickFindToiletTest() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer
        Thread.sleep(1000);
        onView(withText("Find Toilets")).perform(click());
    }

    @Test
    public void navigationDrawerClickBookmarkTest() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer
        Thread.sleep(1000);
        onView(withText("Bookmarks")).perform(click());
    }

    @Test
    public void navigationDrawerClickSOSTest() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer
        Thread.sleep(1000);
        onView(withText("SOS")).perform(click());
    }

    @Test
    public void navigationDrawerClickSettingsTest() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer
        Thread.sleep(1000);
        onView(withText("Settings")).perform(click());
    }
}
