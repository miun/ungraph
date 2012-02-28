package miun.android.ungraph.help;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;

public class HelpsExpandableListAdapter extends SimpleExpandableListAdapter {
	
	public HelpsExpandableListAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newChildView(boolean isLastChild, ViewGroup parent) {
	    View v = super.newChildView(isLastChild, parent);
	    v.setOnClickListener(null);
	    v.setOnLongClickListener(null);
	    return v;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	    View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
	    v.setOnClickListener(null);
	    v.setOnLongClickListener(null);
	    return v;
	}

}
