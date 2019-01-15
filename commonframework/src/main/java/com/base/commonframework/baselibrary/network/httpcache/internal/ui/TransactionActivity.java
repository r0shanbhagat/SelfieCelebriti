/*
 * Copyright (C) 2017 Jeff Gilfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.commonframework.baselibrary.network.httpcache.internal.ui;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.base.commonframework.R;
import com.base.commonframework.baselibrary.listener.AbstractOnPageChangedListener;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpCacheContentProvider;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.LocalCupboard;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;


public class TransactionActivity extends BaseHttpCacheActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_TRANSACTION_ID = "transaction_id";

    private static int selectedTabPosition = 0;
    TextView title;
    Adapter adapter;
    private long transactionId;
    private HttpTransaction transaction;

    public static void start(Context context, long transactionId) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(ARG_TRANSACTION_ID, transactionId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpcache_activity_transaction);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.toolbar_title);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        transactionId = getIntent().getLongExtra(ARG_TRANSACTION_ID, 0);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(ContentUris.withAppendedId(HttpCacheContentProvider.TRANSACTION_URI, transactionId));
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        transaction = LocalCupboard.getInstance().withCursor(data).get(HttpTransaction.class);
        populateUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void populateUI() {
        if (transaction != null) {
            title.setText(transaction.getMethod() + " " + transaction.getPath());
            for (TransactionFragment fragment : adapter.fragments) {
                fragment.transactionUpdated(transaction);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new TransactionOverviewFragment(), getString(R.string.httpCacheOverview));
        adapter.addFragment(TransactionPayloadFragment.newInstance(TransactionPayloadFragment.TYPE_REQUEST), getString(R.string.httpCacheRequest));
        adapter.addFragment(TransactionPayloadFragment.newInstance(TransactionPayloadFragment.TYPE_RESPONSE), getString(R.string.httpCacheResponse));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new AbstractOnPageChangedListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
            }
        });
        viewPager.setCurrentItem(selectedTabPosition);
    }

    private void share(String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
    }

    static class Adapter extends FragmentPagerAdapter {
        final List<TransactionFragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(TransactionFragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
