package edu.purdue.hughe127;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

/**
 * This fragment is the "page" where the user inputs information about the
 * request, he/she wishes to send.
 *
 * @author YL
 */
public class ClientFragment extends Fragment implements OnClickListener {

	public Spinner s;

	public Spinner s2;

	public String name;

	public EditText nameStuff;

	public RadioButton priority0;
	public RadioButton priority1;
	public RadioButton priority2;

	public int priority;

	public String dest;

	public String fromPlace;

	public String command;

	/**
	 * Activity which have to receive callbacks.
	 */
	private SubmitCallbackListener activity;

	/**
	 * Creates a ProfileFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the submit Button.
	 * 
	 *            ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	 * 
	 * @return the fragment initialized.
	 */
	// ** DO NOT CREATE A CONSTRUCTOR FOR ProfileFragment **
	public static ClientFragment newInstance(SubmitCallbackListener activity) {
		ClientFragment f = new ClientFragment();

		f.activity = activity;
		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.client_fragment_layout,
				container, false);

		s = (Spinner) view.findViewById(R.id.spinner);
		s2 = (Spinner) view.findViewById(R.id.spinner2);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.locations,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.locationsTo,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		s.setAdapter(adapter);
		s2.setAdapter(adapter2);

		/**
		 * Register this fragment to be the OnClickListener for the submit
		 * Button.
		 */
		view.findViewById(R.id.bu_submit).setOnClickListener(this);

		priority0 = (RadioButton) view.findViewById(R.id.priority0Button);

		priority1 = (RadioButton) view.findViewById(R.id.priority1Button);

		priority2 = (RadioButton) view.findViewById(R.id.priority2Button);

		priority1.setChecked(true);

		nameStuff = (EditText) view.findViewById(R.id.name);

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {

		name = nameStuff.getText().toString();

		if (priority0.isChecked()) {
			priority = 0;
		} else if (priority1.isChecked()) {
			priority = 1;
		} else {
			priority = 2;
		}

		fromPlace = s.getSelectedItem().toString();

		if (fromPlace.equals("Class of 50")) {
			fromPlace = "CL50";
		} else if (fromPlace.equals("Electrical Engineering")) {
			fromPlace = "EE";
		} else if (fromPlace.equals("Purdue Memorial Union")) {
			fromPlace = "PMU";
		} else if (fromPlace.equals("Lawson Hall")) {
			fromPlace = "LWSN";
		} else if (fromPlace.equals("Purdue University Student Health Center")) {
			fromPlace = "PUSH";
		}

		dest = s2.getSelectedItem().toString();

		if (dest.equals("Class of 50")) {
			dest = "CL50";
		} else if (dest.equals("Electrical Engineering")) {
			dest = "EE";
		} else if (dest.equals("Purdue Memorial Union")) {
			dest = "PMU";
		} else if (dest.equals("Lawson Hall")) {
			dest = "LWSN";
		} else if (dest.equals("Purdue University Student Health Center")) {
			dest = "PUSH";
		} else if (dest.equals("Any of the above locations")) {
			dest = "*";
		}

		command = String.format("%s,%s,%s,%s\n", name, fromPlace, dest, priority);

		if (dest.equals(fromPlace)) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle("Error!");
			alertDialog.setMessage("To should not be the same as from!");
			alertDialog.show();
			return;
		}

		if (name.equals("") || name.contains(",")) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle("Error!");
			alertDialog.setMessage("That really isn't your name, is it?");
			alertDialog.show();
			return;
		}

		if (!(priority == 2) && dest.equals("*")) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle("Error!");
			alertDialog.setMessage("Invalid command!");
			alertDialog.show();
			return;
		}


		this.activity.onSubmit();
	}
}
