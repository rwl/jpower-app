package com.ncond.jpower.app;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;


public class DatabaseHelper implements Serializable {

	private static final long serialVersionUID = -5220381127164418511L;

	public static final Object[] NATURAL_COL_ORDER_JPC = new Object[] {
		"NAME", "DESCRIPTION"};

	public static final String[] COL_HEADERS_ENGLISH_JPC = new String[] {
		"Name", "Description"};

	public static final Object[] NATURAL_COL_ORDER_OPT = new Object[] {
		"ALG", "ITERMAX", "PFDC"};

	public static final String[] COL_HEADERS_ENGLISH_OPT = new String[] {
		"Algorithm", "Max Iter", "DC"};

	private JDBCConnectionPool connectionPool = null;
	private SQLContainer caseContainer = null;
	private SQLContainer optionsContainer = null;

	public DatabaseHelper() {
		initConnectionPool();
	        initDatabase();
	        initContainers();
	        fillContainers();
	}

	private void initConnectionPool() {
		try {
			connectionPool = new SimpleJDBCConnectionPool(
				"org.hsqldb.jdbc.JDBCDriver",
				"jdbc:hsqldb:mem:sqlcontainer",
				"SA", "", 2, 5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initDatabase() {
		try {
			Connection conn = connectionPool.reserveConnection();
			Statement statement = conn.createStatement();
			try {
				statement.executeQuery("SELECT * FROM JPC");
				statement.executeQuery("SELECT * FROM JPOPT");
			} catch (SQLException e) {
				/*
				 * Failed, which means that the database is not
				 * yet initialized => Create the tables
				 */
				statement.execute("create table jpc (id integer generated always as identity, name varchar(64), description varchar(64), version integer default 0 not null)");
				statement.execute("alter table jpc add primary key (id)");

				statement.execute("create table jpopt (id integer generated always as identity, alg integer default 1, itermax int default 10, pfdc boolean default false, version integer default 0 not null)");
				statement.execute("alter table jpopt add primary key (id)");
			}
			statement.close();
			conn.commit();
			connectionPool.releaseConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initContainers() {
		try {
			/* TableQuery and SQLContainer for case table */
			TableQuery q1 = new TableQuery("jpc", connectionPool);
			q1.setVersionColumn("VERSION");
			caseContainer = new SQLContainer(q1);

			/* TableQuery and SQLContainer for options */
			TableQuery q2 = new TableQuery("jpopt", connectionPool);
			q2.setVersionColumn("VERSION");
			optionsContainer = new SQLContainer(q2);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fillContainers() {
		if (caseContainer.size() == 0) {

			final String cases[] = { "case4gs", "case6ww", "case9",
					"case24_ieee_rts", "case30", "case57",
					"case118", "case300" };
			for (int i = 0; i < cases.length; i++) {
				Object id = caseContainer.addItem();
				caseContainer.getContainerProperty(id, "NAME")
						.setValue(cases[i]);
			}
			try {
				caseContainer.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (optionsContainer.size() == 0) {
			Object id = optionsContainer.addItem();
		}
	}

	public SQLContainer getCaseContainer() {
		return caseContainer;
	}

	public SQLContainer getOptionsContainer() {
		return optionsContainer;
	}

}
