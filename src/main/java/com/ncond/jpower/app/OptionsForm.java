package com.ncond.jpower.app;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class OptionsForm extends Form implements ClickListener {

	private Button save = new Button("Save", (ClickListener) this);
	private Button cancel = new Button("Cancel", (ClickListener) this);

//	private final ComboBox alg = new ComboBox();
//	private final TextField iterMax = new TextField();
//	private final CheckBox dc = new CheckBox();

	private final JPowerApplication app;

	public OptionsForm(final JPowerApplication app) {
		this.app = app;

		setWriteThrough(false);  // explicit save

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		setFooter(footer);

//		alg.setNewItemsAllowed(false);
//		alg.setNullSelectionAllowed(false);
//		for (PowerFlowAlg pfAlg : PowerFlowAlg.values()) {
//			alg.addItem(pfAlg);
//		}

		setFormFieldFactory(new DefaultFieldFactory() {
			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field field;
//				if (propertyId.equals("PFALG")) {
//					field = alg;
//				} else {
					field = super.createField(item,
							propertyId, uiContext);
//				}

				/*
				 * Set null representation of all text fields to
				 * empty
				 */
				if (field instanceof TextField) {
					((TextField) field).setNullRepresentation("");
				}

				field.setWidth("200px");

				/* Set the correct caption to each field */
				for (int i = 0; i < DatabaseHelper.NATURAL_COL_ORDER_OPT.length; i++) {
					if (DatabaseHelper.NATURAL_COL_ORDER_OPT[i].equals(propertyId)) {
						field.setCaption(DatabaseHelper.COL_HEADERS_ENGLISH_OPT[i]);
					}
				}
				return field;
			}
		});
	}

	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();
		if (source == save) {
			/*
			 * If the given input is not valid there is no point in
			 * continuing
			 */
			if (!isValid()) {
				return;
			}
			commit();
		} else if (source == cancel) {
			discard();
		}
	}

	@Override
	public void setItemDataSource(Item newDataSource) {
		if (newDataSource != null) {
			setReadOnly(false);
			List<Object> orderedProperties = Arrays.asList(DatabaseHelper.NATURAL_COL_ORDER_OPT);
			super.setItemDataSource(newDataSource, orderedProperties);
			/* Select correct city from the cities ComboBox */
//			if (newDataSource.getItemProperty("PFALG").getValue() != null) {
//				alg.select(new RowId(
//						new Object[] { newDataSource
//								.getItemProperty(
//										"PFALG")
//								.getValue() }));
//			} else {
//				alg.select(alg.getItemIds().iterator().next());
//			}
			//setReadOnly(true);
//			getFooter().setEnabled(true);
		} else {
			super.setItemDataSource(null);
//			getFooter().setEnabled(false);
		}
	}

	@Override
	public void commit() throws Buffered.SourceException {
		/*
		 * Commit the data entered to the person form to the actual
		 * item.
		 */
		super.commit();
		/* Commit changes to the database. */
		try {
			app.getDbHelp().getOptionsContainer().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setReadOnly(true);
	}

	@Override
	public void discard() throws Buffered.SourceException {
		super.discard();
		/* On discard roll back the changes. */
		try {
			app.getDbHelp().getOptionsContainer().rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/* Clear the form */
		setItemDataSource(null);
//		setReadOnly(true);
	}

}
