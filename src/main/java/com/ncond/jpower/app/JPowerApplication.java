/*
 * Copyright 2012 Richard Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ncond.jpower.app;

import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class JPowerApplication extends Application implements
		ValueChangeListener, ClickListener {

	private static final long serialVersionUID = -5582875605614621365L;

	private final Button options = new Button("Options");

	private VerticalLayout view = null;
	private OptionsForm optionsForm = null;

	private CaseTable caseTable = null;

	private final DatabaseHelper dbHelp = new DatabaseHelper();

	@Override
	public void init() {
		buildMainLayout();
		setMainComponent(getCaseTable());
	}

	private void buildMainLayout() {
		setMainWindow(new Window("JPower"));

		VerticalLayout master = new VerticalLayout();
		master.setSizeFull();

		view = new VerticalLayout();
		master.addComponent(view);
		master.setComponentAlignment(view, Alignment.TOP_CENTER);

		view.setWidth("600px");

		view.addComponent(createToolbar());

	        getMainWindow().setContent(master);
	}

	private HorizontalLayout createToolbar() {
		HorizontalLayout bar = new HorizontalLayout();
		bar.addComponent(new Label("JPOWER"));

		bar.addComponent(options);
		bar.setComponentAlignment(options, Alignment.MIDDLE_RIGHT);
		options.addListener((ClickListener) this);

		bar.setWidth("100%");
		return bar;
	}

	private CaseTable getCaseTable() {
		if (caseTable == null)
			caseTable = new CaseTable(this);
		return caseTable;
	}

	private void setMainComponent(Component c) {
		if (view.getComponentCount() == 1) {
			view.addComponent(c, 1);
		} else {
			view.replaceComponent(view.getComponent(1), c);
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();

		if (source == options) {
			showOptionsWindow();
		}
	}

	private void showOptionsWindow() {
		getMainWindow().addWindow(getOptionsWindow());
	}

	private Window getOptionsWindow() {
		if (optionsForm == null) {
			optionsForm = new OptionsForm(this);
		}
		final Window dialog = new Window("Options");
		dialog.addComponent(optionsForm);
		dialog.setModal(true);

		Object ID = dbHelp.getOptionsContainer().getIdByIndex(0);
		optionsForm.setItemDataSource(dbHelp.getOptionsContainer().getItem(ID));

		return dialog;
	}

	public DatabaseHelper getDbHelp() {
		return dbHelp;
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty();
		if (property == caseTable) {
			//
		}
	}

}
