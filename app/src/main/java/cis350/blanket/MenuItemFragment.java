package cis350.blanket;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by davidhb on 4/2/2015.
 */
// Fragment containing a simple view
public class MenuItemFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static int menuIndex;

    // Returns a new instance of this fragment for the given section number
    public static MenuItemFragment newInstance(int sectionNumber) {

        MenuItemFragment fragment = new MenuItemFragment();
        fragment.menuIndex = sectionNumber - 1;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    // Empty constructor
    public MenuItemFragment() {
    }

    @Override
    // Set the view based on what you selected
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        // Show the QR code scan button if we're on that menu option
        if (menuIndex == 1) {
            rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
