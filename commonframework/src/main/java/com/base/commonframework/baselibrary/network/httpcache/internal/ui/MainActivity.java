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

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.base.commonframework.R;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends BaseHttpCacheActivity implements TransactionListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpcache_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TransactionListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(HttpTransaction transaction) {
        TransactionActivity.start(this, transaction.getId());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}
