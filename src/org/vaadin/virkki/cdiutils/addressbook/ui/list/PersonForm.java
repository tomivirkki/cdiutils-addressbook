package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
@SessionScoped
public class PersonForm extends ViewComponent {
	/*
	 * With @Preconfigured annotation you can define Vaadin component attributes
	 * (immediateness, height, caption and so on..) that are applied
	 * injection-time. captionKey = "save", states that the caption of the
	 * injected button should be the text received from
	 * TextBundle-implementation (Lang in the case of this project, the texts
	 * are in AddressBookLang_en_US.properties) with the key "save".
	 */
	@Inject
	@Preconfigured(captionKey = "save")
	private Button saveButton;
	@Inject
	@Preconfigured(captionKey = "cancel")
	private Button cancelButton;
	@Inject
	@Preconfigured(captionKey = "edit")
	private Button editButton;
	@Inject
	@Preconfigured(writeTrough = false)
	private Form form;

	private Collection<String> cityOptions;

	public void init() {
		setCompositionRoot(form);
		initFieldFactory();
		constructFooter();
	}

	private void initFieldFactory() {
		form.setFormFieldFactory(new DefaultFieldFactory() {
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext) {
				Field field = new TextField();

				if (propertyId.equals(Person.Fields.city.name())) {
					field = new ComboBox(getText("person-city"), cityOptions);

				} else if (propertyId.equals(Person.Fields.postalCode.name())) {
					field.addValidator(new RegexpValidator("[1-9][0-9]{4}", getText("personform-error-postalcode")));
					field.setRequired(true);

				} else if (propertyId.equals(Person.Fields.email.name())) {
					field.addValidator(new EmailValidator(getText("personform-error-email")));
					field.setRequired(true);

				} else {
					field = super.createField(item, propertyId, uiContext);
				}

				if (field instanceof AbstractTextField) {
					((AbstractTextField) field).setNullRepresentation("");
				}
				field.setWidth(200.f, UNITS_PIXELS);
				field.setCaption(getText("person-" + String.valueOf(propertyId).toLowerCase()));
				return field;
			}
		});
	}

	private void constructFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.setVisible(false);

		saveButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (form.isValid()) {
					form.commit();
					fireViewEvent(ListPresenter.SAVE_PERSON, ListView.class, getItemPerson());
				}
			}
		});
		footer.addComponent(saveButton);

		cancelButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ListPresenter.CANCEL_EDIT, ListView.class, null);
			}
		});
		footer.addComponent(cancelButton);

		editButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * Clicking the edit button only fires an event and nothing
				 * more. The presenter which observes the event will decide what
				 * happens next.
				 */
				fireViewEvent(ListPresenter.EDIT_PERSON, ListView.class, null);
			}
		});
		footer.addComponent(editButton);

		form.setFooter(footer);
	}

	public void setItemDataSource(Item newDataSource) {
		if (newDataSource != null) {
			form.setItemDataSource(newDataSource, Arrays.asList(PersonList.NATURAL_COL_ORDER));
		}
		form.getFooter().setVisible(newDataSource != null);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		saveButton.setVisible(!readOnly);
		cancelButton.setVisible(!readOnly);
		editButton.setVisible(readOnly);
		form.setReadOnly(readOnly);
	}

	public void editNewContact(Person person) {
		setItemDataSource(new BeanItem<Person>(person));
		setReadOnly(false);
	}

	public void setCityOptions(Collection<String> cityOptions) {
		this.cityOptions = cityOptions;
	}

	@SuppressWarnings("unchecked")
	private Person getItemPerson() {
		return ((BeanItem<Person>) form.getItemDataSource()).getBean();
	}

	public void cancel() {
		if (!getItemPerson().isPersistent()) {
			form.setItemDataSource(null);
		} else {
			form.discard();
		}
		setReadOnly(true);
	}

}