package miun.android.test;

import miun.android.HelpActivity;
import miun.android.StartupActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;

public class StartupActivityTest extends
		ActivityInstrumentationTestCase2<StartupActivity> {
	
	private Solo mSolo;
	private StartupActivity mActivity;

	public StartupActivityTest(String name) {
		super(StartupActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mSolo = new Solo(getInstrumentation(),mActivity);
	}

	protected void tearDown() throws Exception {
		try {
			mSolo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		mActivity.finish();
		super.tearDown();
	}
	
	//Test preconditions - e.g. the activity is not null
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mSolo);
	}
	
	public void testOptionsMenuInvisibleOnStartup() {
		fail("No Idea to check if the whole Menu is invisible");
	}
	
	public void testOptionsMenuEntries() {
		mSolo.sendKey(Solo.MENU);
		assertTrue("Gallery entry not found",mSolo.searchText("Gallery"));
		assertTrue("Help Entry not found",mSolo.searchText("Help"));
		assertTrue("Exit Entry not found",mSolo.searchText("Exit"));
	}
	/*
	public void testGalleryButtonFunction() {
		mSolo.sendKey(Solo.MENU);
		mSolo.clickOnText("Gallery");
		fail(mSolo.getCurrentActivity().toString());
		mSolo.
		//mSolo.assertCurrentActivity("HelpActivity not Called", HelpActivity.class);
	}
	*/
	public void testHelpButtonFunction() {
		mSolo.sendKey(Solo.MENU);
		mSolo.clickOnText("Help");
		mSolo.assertCurrentActivity("HelpActivity not Called", HelpActivity.class);
		mSolo.goBack();
	}
	
	public void testExitButtonFunction() {
		mSolo.sendKey(Solo.MENU);
		mSolo.clickOnText("Exit");
		assertTrue(mSolo.getAllOpenedActivities().isEmpty());
	}
	
}
