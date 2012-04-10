package com.ncond.jpower.app;

import com.vaadin.ui.Table;


public class CaseTable extends Table {

	private static final long serialVersionUID = 8379586223834138399L;

	public CaseTable(JPowerApplication app) {
	        setContainerDataSource(app.getDbHelp().getCaseContainer());

	        setSelectable(true);
	        setImmediate(true);
	        addListener((ValueChangeListener) app);

	        setVisibleColumns(DatabaseHelper.NATURAL_COL_ORDER);
	        setColumnHeaders(DatabaseHelper.COL_HEADERS_ENGLISH);
	}

}
