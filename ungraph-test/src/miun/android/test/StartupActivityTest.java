package miun.android.test;

import miun.android.test.mock.MockFileChooser;
import miun.android.ungraph.help.HelpActivity;
import miun.android.ungraph.preview.PreviewActivity;

import miun.android.ungraph.process.GraphProcessingActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class StartupActivityTest extends
		ActivityInstrumentationTestCase2<PreviewActivity> {
	
	private Solo mSolo;
	private PreviewActivity mActivity;

	public StartupActivityTest() {
		super(PreviewActivity.class);
	}
	
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
	
	public void testReceiveResultCancelledFromFileChooser() {
		Intent intent = new Intent();
		intent.putExtra("simu", MockFileChooser.CANCELLED);
		intent.setAction(".mock.startfc");
		mActivity.startActivityForResult(intent,PreviewActivity.PICK_IMAGE);
		mSolo.waitForActivity("MockFileChooser",1);
		assertTrue(mSolo.searchText(mSolo.getString(miun.android.R.string.no_file_selected)));
		mSolo.assertCurrentActivity("StartupActivity not active after cancel from filechooser", PreviewActivity.class);
	}
	
	public void testReceiveWrongFileTypeFromFileChooser() {
		Intent intent = new Intent();
		intent.putExtra("simu", MockFileChooser.UNSUPPORTED_FILE);
		intent.setAction(".mock.startfc");
		mActivity.startActivityForResult(intent,PreviewActivity.PICK_IMAGE);
		mSolo.waitForActivity("MockFileChooser",1);
		assertTrue(mSolo.searchText(mSolo.getString(miun.android.R.string.wrong_file_type)));
		mSolo.clickOnText(mSolo.getString(android.R.string.ok));
		mSolo.assertCurrentActivity("StartupActivity not active after cancel from filechooser", PreviewActivity.class);
	}
	
	public void testReceivePictureFromFileChooser() {
		Intent intent = new Intent();
		intent.putExtra("simu", MockFileChooser.SUPPORTED_FILE);
		intent.setAction(".mock.startfc");
		mActivity.startActivityForResult(intent,PreviewActivity.PICK_IMAGE);
		mSolo.waitForActivity("MockFileChooser",1);
		mSolo.assertCurrentActivity("Evaluation Activity not called after file load", GraphProcessingActivity.class);
		mSolo.goBack();
	}
	
}
