package songpatechnicalhighschool.motivation.eyefriend.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import songpatechnicalhighschool.motivation.eyefriend.Fragment.QuickFragment;
import songpatechnicalhighschool.motivation.eyefriend.Fragment.ScanFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int size;

    public PageAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                QuickFragment quickFragment = new QuickFragment();
                return quickFragment;
            case 1:
                ScanFragment scanFragment = new ScanFragment();
                return scanFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
