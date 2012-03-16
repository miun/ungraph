package miun.android.ungraph.preview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class CameraButton extends BroadcastReceiver {
	Context mContext;
	CameraButtonReceiver mReceiver;
	
	public CameraButton(Context context,CameraButtonReceiver receiver) {
		assert(context != null);
		assert(receiver != null);
		
		mContext = context;
		mReceiver = receiver;
	}
	
	public void registerCameraButton() {
		//Register broadcast receiver
		mContext.registerReceiver(this,new IntentFilter("android.intent.action.CAMERA_BUTTON"));
	}
	
	public void unregisterCameraButton() {
		//Unregister receiver
		mContext.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//Forward
		onCameraButton();
		abortBroadcast();
	}
	
	//Forward button press event
	private void onCameraButton() {
		mReceiver.onCameraButtonPressed();
	}
}
