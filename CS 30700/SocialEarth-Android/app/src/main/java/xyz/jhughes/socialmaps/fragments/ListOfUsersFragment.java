package xyz.jhughes.socialmaps.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

// import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.adapters.UsersAdapter;
import xyz.jhughes.socialmaps.enums.TypeOfUserList;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfUsersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "typeOfUserList";
    private static final String ARG_PARAM2 = "primaryUsername";

    private TypeOfUserList type;
    private String primaryUsername;

    private OnFragmentInteractionListener mListener;
    private boolean instantiate;
    private UsersAdapter adapter;

    public ListOfUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type The type of list that should be displayed.
     * @return A new instance of fragment ListOfUsersFragment.
     */
    public static ListOfUsersFragment newInstance(TypeOfUserList type, String username) {
        ListOfUsersFragment fragment = new ListOfUsersFragment();
        Bundle args = new Bundle();
        switch(type) {
            case FOLLOWERS:
                args.putInt(ARG_PARAM1, 0);
                break;
            case FOLLOWING:
                args.putInt(ARG_PARAM1, 1);
                break;
            case NEW_FOLLOWER:
                args.putInt(ARG_PARAM1, 2);
                break;
        }
        args.putString(ARG_PARAM2, username);
        fragment.setArguments(args);
        return fragment;
    }

    private TypeOfUserList intToType(int i) {
        switch(i) {
            case 0:
                return TypeOfUserList.FOLLOWERS;
            case 1:
                return TypeOfUserList.FOLLOWING;
            case 2:
                return TypeOfUserList.NEW_FOLLOWER;
            default:
                throw new RuntimeException("Improper conversion from int to type.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        instantiate = false;
        super.onCreate(savedInstanceState);
        this.type = intToType(getArguments().getInt(ARG_PARAM1)); // may throw null pointer, but idc
        this.primaryUsername = getArguments().getString(ARG_PARAM2);
        switch (this.type) {
            case FOLLOWERS:
                new GetListOfUsersTask().execute(Command.CommandType.GET_USERS_THAT_FOLLOW_USER);
                break;
            case FOLLOWING:
                new GetListOfUsersTask().execute(Command.CommandType.GET_USERS_THAT_USER_FOLLOWS);
                break;
            case NEW_FOLLOWER:
                instantiate = true;
                break;
        }
        System.out.println("************************** Fragment.onCreate() concluded");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_users, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void gotoUserActivity(String username) {
        if (mListener != null) {
            mListener.onFragmentInteraction(username);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void clear() {
        if (this.instantiate) {
            this.instantiate = false;
            RecyclerView rvUsers = (RecyclerView) this.getView().findViewById(R.id.rvUsers);
            adapter = new UsersAdapter(new ArrayList<User>(), this.type, getContext());
            rvUsers.setAdapter(adapter);
            rvUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        } else {
            this.adapter.clear();
        }
    }

    public void updateListOfUsers(String prefix) {
        adapter.updateBasedOnPrefix(prefix);
    }

    private void makeListOfUsers(ArrayList<User> listOfUsers) {
        RecyclerView rvUsers = (RecyclerView) this.getView().findViewById(R.id.rvUsers);
        adapter = new UsersAdapter(listOfUsers, this.type, getContext());
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // originally Uri, change back if things are fucked up
        void onFragmentInteraction(String username);
    }

    private class GetListOfUsersTask extends AsyncTask<Command.CommandType, Void, Response> {

        protected Response doInBackground(Command.CommandType... commandType) {
            User user = new User(null, ListOfUsersFragment.this.primaryUsername, null, null, null, null, null);
            Command command = new Command(commandType[0], null, user, null, null);
            return Server.sendCommandToServer(command, getContext());
        }

        protected void onPostExecute(Response response) {
            try {
                if (response == null)
                    Alert.networkError(getContext());
                else if(!response.isBool())
                    Alert.generalError(getContext());
                else if (response.getListOfUsers().size() == 0)
                    Alert.createAlertDialog("Follows", "You don't follow anyone. Try following someone!", getContext());
                else {
                    ArrayList<User> users = response.getListOfUsers();
                    makeListOfUsers(users);
                }
            } catch (NullPointerException e) {
                Alert.generalError(getContext());
            }
        }
    }
}
