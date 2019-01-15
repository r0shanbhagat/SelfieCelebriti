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
package com.base.commonframework.baselibrary.network.httpcache.internal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;

class HttpCacheDbOpenHelper extends SQLiteOpenHelper {

    public static final String FILE_DIR = "HttpCache";
    private static final String DATABASE_NAME = "HttpCache.db";
    private static final int VERSION = 3;

    HttpCacheDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        // super(context, getDBPath(context), null, VERSION);
    }

    public static String getDBPath(Context context) {
        String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(Build.MANUFACTURER)) {
            return DATABASE_NAME;
        } else {
            return context.getExternalCacheDir() + File.separator + FILE_DIR + File.separator + DATABASE_NAME;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LocalCupboard.getAnnotatedInstance().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocalCupboard.getAnnotatedInstance().withDatabase(db).upgradeTables();
    }
}
