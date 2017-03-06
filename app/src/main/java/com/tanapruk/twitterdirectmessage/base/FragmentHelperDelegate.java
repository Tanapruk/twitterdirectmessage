package com.tanapruk.twitterdirectmessage.base;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tanapruk.twitterdirectmessage.R;

import java.util.List;

/**
 * Created by trusttanapruk on 9/21/2016.
 */

public class FragmentHelperDelegate {
    final private FragmentActivity fragmentActivity;
    private int containerLayoutRes = -1;

    public FragmentHelperDelegate(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
//        throw new NullPointerException("No Fragment attached.");
    }

    public void bind(int containerLayoutRes) {
        this.containerLayoutRes = containerLayoutRes;
    }

    public void setAnimation() {

    }

    public Fragment getCurrentFragment() {
        throwNullIfNotBind();
        return getVisibleFragment();
    }

    private void throwNullIfNotBind() {
        if (containerLayoutRes == -1) {
            throw new NullPointerException("bind() first.");
        }
    }

    @SuppressLint("CommitTransaction")
    private FragmentTransaction getBase() {
        return fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
    }


    private FragmentTransaction getBaseAnimation() {
        return getBase();
    }


    public void handleBackButton() {
        if (fragmentActivity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            fragmentActivity.getSupportFragmentManager().popBackStack();
        } else {
            fragmentActivity.finish();
        }
    }

    public void addFragment(Fragment fragment, boolean backStack) {
        if( backStack ){
            getBase()
                    .add(containerLayoutRes, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }else{
            getBase()
                    .add(containerLayoutRes, fragment)
                    .commit();
        }
    }

    public void addFragmentWithBaseAnimation(Fragment fragment, boolean backStack) {
        if (backStack) {
            getBaseAnimation()
                    .add(containerLayoutRes, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }else{
            getBaseAnimation()
                    .add(containerLayoutRes, fragment)
                    .commit();
        }
    }


    private FragmentTransaction getReplaceTransaction(Fragment fragment, boolean animate) {
        throwNullIfNotBind();
        String TAG = fragment.getClass().getSimpleName();
        return animate ? getBaseAnimation().replace(containerLayoutRes, fragment, TAG) : getBase().replace(containerLayoutRes, fragment, TAG);
    }


    public boolean contain(String tag) {
        if (fragmentActivity.getSupportFragmentManager().getFragments() == null || fragmentActivity.getSupportFragmentManager().getFragments().size() == 0) {
            return false;
        }

        for (Fragment eachFragment : fragmentActivity.getSupportFragmentManager().getFragments()) {
            if (eachFragment != null && eachFragment.getTag() != null && eachFragment.getTag().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public void clearBackStackTo(int index) {
        FragmentManager.BackStackEntry entry = fragmentActivity.getSupportFragmentManager().getBackStackEntryAt(index);
        fragmentActivity.getSupportFragmentManager().popBackStackImmediate(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearAllBackStacks() {
        if (fragmentActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            clearBackStackTo(0);
        }

    }

    public void goBack() {
        fragmentActivity.getSupportFragmentManager().popBackStack();
    }

    public void goBackToFragment(String tag) {
        if (contain(tag)) {
            fragmentActivity.getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void openFragmentAndWontComeBack(Fragment fragment, boolean animate) {
        getReplaceTransaction(fragment, animate)
                .commit();
    }



    public void openFragmentAndWontComeBack(Fragment fragment) {
        openFragmentAndWontComeBack(fragment, true);
    }

    private void openFragmentAddToBackStack(Fragment fragment, boolean animate) {
        getReplaceTransaction(fragment, animate)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void openFragmentAndWontComeBackNoAnimation(Fragment fragment) {
        openFragmentAndWontComeBack(fragment, false);
    }


    public void openFragmentAddToBackStack(Fragment fragment) {
        openFragmentAddToBackStack(fragment, true);
    }

    public void openFragmentAddToBackStackNoAnimation(Fragment fragment) {
        openFragmentAddToBackStack(fragment, false);
    }

}
