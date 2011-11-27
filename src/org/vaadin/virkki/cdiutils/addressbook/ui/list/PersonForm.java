package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifierImpl;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

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
public class PersonForm extends Form {
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;

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
	private Lang lang;

	private Collection<String> cityOptions;

	public void init() {
		setWriteThrough(false);
		initFieldFactory();
		constructFooter();
	}

	private void initFieldFactory() {
		setFormFieldFactory(new DefaultFieldFactory() {
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext) {
				Field field = new TextField();

				if (propertyId.equals(Person.Fields.city.name())) {
					field = new ComboBox(lang.getText("person-city"), cityOptions);

				} else if (propertyId.equals(Person.Fields.postalCode.name())) {
					field.addValidator(new RegexpValidator("[1-9][0-9]{4}", lang.getText("personform-error-postalcode")));
					field.setRequired(true);

				} else if (propertyId.equals(Person.Fields.email.name())) {
					field.addValidator(new EmailValidator(lang.getText("personform-error-email")));
					field.setRequired(true);

				} else {
					field = super.createField(item, propertyId, uiContext);
				}

				if (field instanceof AbstractTextField) {
					((AbstractTextField) field).setNullRepresentation("");
				}
				field.setWidth(200.f, UNITS_PIXELS);
				field.setCaption(lang.getText("person-" + String.valueOf(propertyId).toLowerCase()));
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
				if (isValid()) {
					commit();
					fireViewEvent(ListPresenter.SAVE_PERSON, getItemPerson());
				}
			}
		});
		footer.addComponent(saveButton);

		cancelButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ListPresenter.CANCEL_EDIT, null);
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
				fireViewEvent(ListPresenter.EDIT_PERSON, null);
			}
		});
		footer.addComponent(editButton);

		setFooter(footer);
	}

	@Override
	public void setItemDataSource(Item newDataSource) {
		if (newDataSource != null) {
			super.setItemDataSource(newDataSource, Arrays.asList(PersonList.NATURAL_COL_ORDER));
		}
		getFooter().setVisible(newDataSource != null);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		saveButton.setVisible(!readOnly);
		cancelButton.setVisible(!readOnly);
		editButton.setVisible(readOnly);
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
		return ((BeanItem<Person>) getItemDataSource()).getBean();
	}

	public void cancel() {
		if (!getItemPerson().isPersistent()) {
			setItemDataSource(null);
		} else {
			discard();
		}
		setReadOnly(true);
	}

	private void fireViewEvent(String methodIdentifier, Object primaryParameter) {
		viewEvent.select(new EventQualifierImpl(methodIdentifier, ListView.class) {}).fire(new ParameterDTO(primaryParameter));
	}

}