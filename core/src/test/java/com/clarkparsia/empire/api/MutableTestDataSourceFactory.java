/*
 * Copyright (c) 2009-2013 Clark & Parsia, LLC. <http://www.clarkparsia.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarkparsia.empire.api;

import com.clarkparsia.empire.ds.DataSourceFactory;
import com.clarkparsia.empire.ds.DataSource;
import com.clarkparsia.empire.ds.DataSourceException;
import com.clarkparsia.empire.ds.Alias;

import com.complexible.common.openrdf.repository.Repositories;
import com.google.common.base.Splitter;
import org.openrdf.repository.Repository;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * <p>DataSourceFactory implementation which creates an instance of a MutableDataSource for testing.</p>
 *
 * @author Michael Grove
 */
@Alias("test-source")
public final class MutableTestDataSourceFactory implements DataSourceFactory {
	private static Map<String, DataSource> mSourceCache = new HashMap<String, DataSource>();

    @Override
	public boolean canCreate(final Map<String, Object> theMap) {
		return true;
	}

    @Override
	public DataSource create(final Map<String, Object> theMap) throws DataSourceException {
		// tests should reuse the same source.
		if (theMap.containsKey("files")
			&& mSourceCache.containsKey(theMap.get("files"))
			&& (!theMap.containsKey("use.cache") || theMap.get("use.cache").toString().equalsIgnoreCase("true"))) {
			return mSourceCache.get(theMap.get("files"));
		}

		Repository aRepo = Repositories.createInMemoryRepo();

		if (theMap.containsKey("files")) {
			for (String aFile : Splitter.on(",").omitEmptyStrings().trimResults().split(theMap.get("files").toString())) {
				try {
					Repositories.add(aRepo, new File(aFile));
				}
				catch (Exception e) {
					throw new DataSourceException("Error reading file: " + aFile, e);
				}
			}
		}

		DataSource aSource = new MutableTestDataSource(aRepo);

        if (theMap.containsKey("files")) {
		    mSourceCache.put(theMap.get("files").toString(), aSource);
        }

		return aSource;
	}
}
