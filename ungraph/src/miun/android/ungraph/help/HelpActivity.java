package miun.android.ungraph.help;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miun.android.R;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class HelpActivity extends ExpandableListActivity {
	private static final String PARENT = "parent";
	private static final String CHILD = "child";
	
	private List<HashMap<String,String>> parents;
	private List<List<HashMap<String, String>>> childs;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.help);
    	parents = createParentList();
    	childs = createChildList();
        SimpleExpandableListAdapter expListAdapter = new SimpleExpandableListAdapter(
			this,
			parents, 			// Creating group List.
			R.layout.help_group_item,		// Group item layout XML.
			new String[] {PARENT},			// the key of group item.
			new int[] {R.id.row_name},		// ID of each group item.-Data under the key goes into this TextView.
			childs,				// childData describes second-level entries.
			R.layout.help_child_item,		// Layout for sub-level entries(second level).
			new String[] {CHILD},			// Keys in childData maps to display.
			new int[] {R.id.grp_child}		// Data under the keys above go into these TextViews.
		);
		
        setListAdapter(expListAdapter);		// setting the adapter in the list.

    }

	/* Creating the Hashmap for the row */
	private List<HashMap<String,String>> createParentList() {
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
	  	HashMap<String,String> m = new HashMap<String,String>();
	  	m.put(PARENT,"Help article 1");
	  	result.add(m);
	  	m = new HashMap<String,String>();
	  	m.put(PARENT,"Help article 2");
	  	result.add(m);
	  	m = new HashMap<String,String>();
	  	m.put(PARENT,"Help article 3");
	  	result.add(m);
	  	return result;
    }

	/* creatin the HashMap for the children */
	private List<List<HashMap<String, String>>> createChildList() {
    	ArrayList<List<HashMap<String, String>>> result = new ArrayList<List<HashMap<String, String>>>();
    	/*for (List<HashMap<String, String>> secList : parents){
    		
    	}*/
    	for( int i = 0 ; i < 3 ; ++i ) { // this -15 is the number of groups(Here it's fifteen)
	    	/* each group need each HashMap-Here for each group we have 3 subgroups */
	    	ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
		    HashMap<String,String> child = new HashMap<String,String>();
			child.put(CHILD, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." );
			secList.add( child );
			result.add( secList );
    	}
    	return result;
    }
}
