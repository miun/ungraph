package miun.android.test;

import miun.android.ungraph.help.HelpActivity;
import miun.android.ungraph.preview.PreviewActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;

public class StartupActivityTest extends
		ActivityInstrumentationTestCase2<PreviewActivity> {
	
	private Solo mSolo;
	private PreviewActivity mActivity;

	public StartupActivityTest(String name) {
		super(PreviewActivity.class);
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
	/*
	public void testOptionsMenuInvisibleOnStartup() {
		fail("No Idea to check if the whole Menu is invisible");
	}*/
	
	public void testOptionsMenuEntries() {
		mSolo.sendKey(Solo.MENU);
		assertTrue("Gallery entry not found",mSolo.searchText("Gallery"));
		assertTrue("Help Entry not found",mSolo.searchText("Help"));
	}
	
	public void testGalleryButtonFunction() {
		mSolo.sendKey(Solo.MENU);
		mSolo.clickOnText("Gallery");
		assertTrue(mSolo.searchText(mSolo.getString(miun.android.R.string.select_picture)));
		mSolo.goBack();
	}
	
	public void testHelpButtonFunction() {
		mSolo.sendKey(Solo.MENU);
		mSolo.clickOnText("Help");
		mSolo.assertCurrentActivity("HelpActivity not Called", HelpActivity.class);
		mSolo.goBack();
	}
	
	public void testGetIntentFromOtherApplication() {
		Intent intent = new Intent();
		Uri data = Uri.parse("Ganz toller Uri String");
		//mSolo.
		//intent.setData(data);
		launchActivityWithIntent("miun.android", PreviewActivity.class, intent);
		mSolo.goBack();
	}
	
}
