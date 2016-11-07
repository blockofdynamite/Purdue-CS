package edu.purdue.hughe127;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment is the "page" where the application display the log from the
 * server and wait for a match.
 *
 * @author YL
 */
public class MatchFragment extends Fragment implements OnClickListener {

	private static final String DEBUG_TAG = "DEBUG";

	/**
	 * Activity which have to receive callbacks.
	 */
	private StartOverCallbackListener activity;

	/**
	 * AsyncTask sending the request to the server.
	 */
	private Client client;

	/**
	 * Coordinate of the server.
	 */
	private String host;
	private int port;

	/**
	 * Command the user should send.
	 */
	private String command;

	private boolean validIP;

	private boolean validPort;

	TextView partnerName;

	TextView log;

	TextView partnerOrig;

	TextView partnerDest;

	TextView congrats;

	Socket s;

	String name;

	boolean success;

	String reply;

	String status;

	String status2;

	int count = 0;

	boolean goodCommand = true;

	boolean safeToExit = false;

	// Class methods
	/**
	 * Creates a MatchFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the start over
	 *            Button.
	 * @param host
	 *            address or IP address of the server.
	 * @param port
	 *            port number.
	 * 
	 * @param command
	 *            command you have to send to the server.
	 * 
	 * @return the fragment initialized.
	 */

	// ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	public static MatchFragment newInstance(StartOverCallbackListener activity, String host, int port, String command, boolean validPort, boolean validIP) {
		MatchFragment f = new MatchFragment();

		f.activity = activity;
		f.host = host;
		f.port = port;
		f.command = command;
		f.validIP = validPort;
		f.validPort = validPort;

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

		View view = inflater.inflate(R.layout.match_fragment_layout, container,
				false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		log = (TextView) view.findViewById(R.id.log);

		partnerName = (TextView) view.findViewById(R.id.partnerName);

		partnerOrig = (TextView) view.findViewById(R.id.partnerOrig);

		partnerDest = (TextView) view.findViewById(R.id.partnerDest);

		congrats = (TextView) view.findViewById(R.id.Congrats);



		/**
		 * Launch the AsyncTask
		 */
		this.client = new Client();
		this.client.execute("");

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		/**
		 * Close the AsyncTask if still running.
		 */
		this.client.close();

		/**
		 * Notify the Activity.
		 */
		this.activity.onStartOver();
	}

	class Client extends AsyncTask<String, String, String> implements Closeable {

		/**
		 * NOTE: you can access MatchFragment field from this class:
		 * 
		 * Example: The statement in doInBackground will print the message in
		 * the Eclipse LogCat view.
		 */

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {
			Log.d(DEBUG_TAG, String
					.format("The Server at the address %s uses the port %d",
							host, port));

			Log.d(DEBUG_TAG, String.format(
					"The Client will send the command: %s", command));

			if (!validIP || !validPort) {
				success = false;
				return "Invalid port or host";
			}

			try {
				s = new Socket();
				System.out.println(host + " " + port);
				s.connect(new InetSocketAddress(host, port), 5000);
				success = true;
				status = "Successfully connected to server!";
				publishProgress();
			} catch (IOException e) {
				success = false;
				return "Couldn't connect to server! Did you enter the IP or port wrong?";
			}

			PrintWriter pw;

			try {
				pw = new PrintWriter(s.getOutputStream(), true);
				pw.println(command);
				status2 = "Successfully sent command to server!";
				publishProgress();
			} catch (IOException e) {
				return "Couldn't send command! Did you lose internet?";
			}

			BufferedReader in;
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {
				return "Error on establishing input stream! Did you lose internet?";
			}

			try {
				reply = in.readLine();
				pw.println(":ACK");
				pw.println(":ACK");
				pw.flush();
				Log.d(DEBUG_TAG, reply);
			} catch (IOException e) {
				return "Error on getting reply from server! Did you lose internet?";
			} catch (NullPointerException e) {
				return "Error on getting reply from server! Did you lose internet?";
			}

			try {
				s.close();
			} catch (IOException e) {
				return "I honestly don't know what happened. Please cry for me!";
			}

			if (reply.equals("ERROR: invalid request")) {
				goodCommand = false;
				return "Invalid request!";
			} else {
				safeToExit = true;
				return "A match was found! Congrats!";
			}
		}

		public void close() {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.d(DEBUG_TAG, "NullPointer: Socket was never opened due to invalid port or host");
			}
			partnerName.setText("Waiting...");
			partnerOrig.setText("Waiting...");
			partnerDest.setText("Waiting...");
			congrats.setText("Waiting...");
			log.setText("");
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		/**
		 * Method executed just before the task.
		 */
		@Override
		protected void onPreExecute() {
			if (!host.matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")) {
				validIP = false;
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setTitle("Error!");
				alertDialog.setMessage("Invalid IP!");
				alertDialog.show();
				return;
			} else {
				validIP = true;
			}
			if (!(port >= 1) || !(port <= 65535)) {
				validPort = false;
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setTitle("Error!");
				alertDialog.setMessage("Invalid port!");
				alertDialog.show();
				return;
			} else {
				validPort = true;
			}
			log.append("[" + Calendar.getInstance().getTime() + "] " + "Connecting to server: " + host + " on port: " + port + "\n\n");
		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {

			if (!success) {
				log.append("[" + Calendar.getInstance().getTime() + "] " + result);
				partnerName.setText("Err: connection");
				partnerOrig.setText("Err: connection");
				partnerDest.setText("Err: connection");
				congrats.setText("Err: connection");
				return;
			}

			else  if (goodCommand && safeToExit) {
				String one = null;
				String two = null;
				String three = null;
				try {
					int commaOne = reply.indexOf(',') + 1;
					int commaTwo = reply.indexOf(',', commaOne) + 1;
					int commaThree = reply.indexOf(',', commaTwo) + 1;

					one = reply.substring(10, commaOne - 1);
					two = reply.substring(commaOne, commaTwo - 1);
					three = reply.substring(commaTwo, commaThree - 1);
					log.append("[" + Calendar.getInstance().getTime() + "] " + result);
				} catch (StringIndexOutOfBoundsException e) {
					log.append("[" + Calendar.getInstance().getTime() + "] " + "Connection Reset!");
					partnerName.setText("Err: reset");
					partnerOrig.setText("Err: reset");
					partnerDest.setText("Err: reset");
					congrats.setText("Err: reset");
					return;
				}
				partnerName.setText(one);
				partnerOrig.setText(two);
				partnerDest.setText(three);
				congrats.setText("A match was found!");
			} else {
				log.append("[" + Calendar.getInstance().getTime() + "] " + "Unknown Error!");
				partnerName.setText("Err: unknown");
				partnerOrig.setText("Err: unknown");
				partnerDest.setText("Err: unknown");
				congrats.setText("Err: unknown");
			}
		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@Override
		protected void onProgressUpdate(String... result) {
			if (count == 0) {
				log.append("[" + Calendar.getInstance().getTime() + "] " + status + "\n\n");
			} else {
				log.append("[" + Calendar.getInstance().getTime() + "] " + status2 + "\n\n");
			}
			count++;
		}
	}
}
