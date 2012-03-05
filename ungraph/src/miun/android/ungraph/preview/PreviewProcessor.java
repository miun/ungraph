package miun.android.ungraph.preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import miun.android.ungraph.FileNotSupportedException;
import miun.android.ungraph.process.GraphProcessingActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.YuvImage;
import android.net.Uri;
import android.util.Log;

public class PreviewProcessor {
	private static final String FILENAME = "TEMPFILE";
	//constants defining the image size that the bitmap should be. - Is sometimes value + 1/3*value
	private static final int TRY_X = 1024;
	private static final int TRY_Y = 768;
	
	private Activity context;
	
	/**
	 * This constructor will be used after loading a file from an filechooser
	 * @throws FileNotFoundException 
	 * @throws FileNotSupportedException 
	 */
	public PreviewProcessor(Uri source, Activity context) throws FileNotFoundException, FileNotSupportedException{
		this.context = context;
		InputStream is = null;
    	Bitmap bm = null;
    	is = context.getContentResolver().openInputStream(source);
			BitmapFactory.Options oScale = new BitmapFactory.Options();
			oScale.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is,null,oScale);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (oScale.outHeight <= 0 || oScale.outWidth <= 0)
				throw new FileNotSupportedException("This File type is not supported");
			BitmapFactory.Options oImage = new BitmapFactory.Options();
			oImage.inSampleSize = calculateInSampleSize(oScale.outWidth, oScale.outHeight);
			is = context.getContentResolver().openInputStream(source);
			bm = BitmapFactory.decodeStream(is,null,oImage);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.startIntent(this.saveBitmapToTempFile(bm));
	}
	
	/**
	 * This constructor will be used to manage a the input from the camerapreview as matrix 
	 * @param pic
	 */
	public PreviewProcessor(byte[] pic, Activity context){
		this.context = context;
		BitmapFactory.Options oScale = new BitmapFactory.Options();
		oScale.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(pic, 0, pic.length, oScale);
		BitmapFactory.Options oImage = new BitmapFactory.Options();
		oImage.inSampleSize = calculateInSampleSize(oScale.outWidth, oScale.outHeight);
		Bitmap bm = BitmapFactory.decodeByteArray(pic, 0, pic.length, oImage);
		this.startIntent(this.saveBitmapToTempFile(bm));
	}
	
	public PreviewProcessor(Mat mat, Activity context) {
		float factor = TRY_X / mat.width();
		Mat m = new Mat();
		Bitmap bm = Bitmap.createBitmap(Math.round(mat.width()*factor),
				Math.round(mat.height()*factor),
				Bitmap.Config.RGB_565);
		Utils.matToBitmap(mat, bm);
		saveBitmapToTempFile(bm);
	}
	
	private int calculateInSampleSize(int x, int y) {
    	int result = 1;
    	while (x>TRY_X || y>TRY_Y){
    		result++;
    		x /= result;
    		y /= result;
    	}
    	return result;
    }
	
	private Uri saveBitmapToTempFile(Bitmap bm) {
		File f = new File(context.getFilesDir(),FILENAME);
		OutputStream out;
		try {
			out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			bm.recycle();
			bm = null;
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Uri.fromFile(f);
	}
	
	private void startIntent(Uri tempFile) {
		Intent intent = new Intent(context, GraphProcessingActivity.class);
		intent.setData(tempFile);
		context.startActivity(intent);
		context.finish();
	}
}
