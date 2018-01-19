package com.at.cancerbero.app.fragments.node;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.at.cancerbero.CancerberoApp.R;
import com.at.cancerbero.app.fragments.AppFragment;
import com.at.cancerbero.app.fragments.installation.InstallationFragment;
import com.at.cancerbero.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.StreamSupport;

public class NodeFragment extends AppFragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private static final String TAB_SELECTED = "TAB_SELECTED";

    private final List<TabFragment> items = new ArrayList<>();

    private String nodeId;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private SectionsPagerAdapter sectionsPagerAdapter;


    @Override
    public View onCreateViewApp(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_node, container, false);

        getSupportActionBar().show();

        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.pager_viewer);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(this);

        tabLayout = view.findViewById(R.id.layout_tabs);
        tabLayout.removeAllTabs();
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.addOnTabSelectedListener(this);

        selectTab(0);
        restoreFromBundle(savedInstanceState);

        getMainActivity().setActivityTitle(R.string.title_node);

        return view;
    }

    @Override
    public void onCreateOptionsMenuApp(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            loadNode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (nodeId != null) {
            outState.putString("nodeId", nodeId);
        }

        outState.putInt(TAB_SELECTED, tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        restoreFromBundle(savedInstanceState);
    }

    private TabLayout.Tab createTab(TabLayout tabLayout, Integer title, Integer icon, Class<? extends TabFragment> tabFragmentClass) {
        TabLayout.Tab result = tabLayout.newTab();

        if (title != null) {
            result.setText(title);
        }

        if (icon != null) {
            result.setIcon(icon);
        }

        try {
            TabFragment fragment = tabFragmentClass.newInstance();
            fragment.setNodeFragment(this);
            items.add(fragment);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void clearTabs() {
        tabLayout.removeAllTabs();
        items.clear();
    }

    private void showItem(Node node) {
        getMainActivity().setActivityTitle(node.name);

        viewPager.setCurrentItem(-1);
        clearTabs();

        if (node.modules.alarm != null) {
            tabLayout.addTab(createTab(tabLayout, R.string.title_tab_alarm, R.drawable.ic_settings_remote_black_24dp, TabAlarmFragment.class), false);
        }
        if (node.modules.card != null) {
            tabLayout.addTab(createTab(tabLayout, R.string.title_tab_card, R.drawable.ic_payment_black_24dp, TabCardFragment.class), false);
        }

        sectionsPagerAdapter.notifyDataSetChanged();
        selectTab(0);
        viewPager.setCurrentItem(0);

        StreamSupport.stream(items).forEach(i -> i.showItem(node));
    }

    void loadNode() {
        if (nodeId != null) {
            setRefreshing(true);
            getMainService().getInstallationService().loadNode(nodeId).handle((node, t) -> {
                runOnUiThread(() -> {
                    if (t != null) {
                        showToast(R.string.label_unable_to_load_node);
                    } else {
                        showItem(node);
                    }
                    setRefreshing(false);
                });
                return null;
            });
        }
    }

    private void restoreFromBundle(Bundle extras) {
        if (extras != null) {
            if (extras.containsKey("nodeId")) {
                nodeId = extras.getString("nodeId");
                loadNode();
            }

            if (extras.containsKey("TAB_SELECTED")) {
                selectTab(extras.getInt(TAB_SELECTED, 0));
            }
        }
    }

    private void selectTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);

        if (tab != null) {
            tab.select();
        }

    }

    @Override
    public boolean onBackPressed() {
        changeFragment(InstallationFragment.class);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(-1);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}