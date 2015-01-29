package com.clarkparsia.empire.api;

import com.clarkparsia.empire.ds.MutableDataSource;
import com.clarkparsia.empire.ds.DataSourceException;

import com.complexible.common.openrdf.repository.Repositories;
import org.openrdf.model.Graph;
import org.openrdf.repository.Repository;

/**
 * <p>Implementation of the MutableDataSource interface for testing</p>
 *
 * @author Michael Grove
 */
public class MutableTestDataSource extends TestDataSource implements MutableDataSource {

	public MutableTestDataSource() {
        super();
	}

	public MutableTestDataSource(final Graph theGraph) {
		super(theGraph);
	}

	public MutableTestDataSource(final Repository theRepository) {
		super(theRepository);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void add(final Graph theGraph) throws DataSourceException {
		try {
			Repositories.add(getRepository(), theGraph);
		}
		catch (Exception e) {
			throw new DataSourceException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void remove(final Graph theGraph) throws DataSourceException {
		try {
			Repositories.remove(getRepository(), theGraph);
		}
		catch (Exception e) {
			throw new DataSourceException(e);
		}
	}
}
