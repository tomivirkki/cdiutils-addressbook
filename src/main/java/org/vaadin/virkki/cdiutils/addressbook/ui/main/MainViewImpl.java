package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractView;
import org.vaadin.addon.cdimvp.View;
import org.vaadin.addon.cdiproperties.annotation.HorizontalSplitPanelProperties;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class MainViewImpl extends AbstractView implements MainViev {
    @Inject
    private NavigationTree tree;
    @Inject
    private Instance<Toolbar> toolbar;
    @Inject
    @HorizontalSplitPanelProperties
    private HorizontalSplitPanel horizontalSplit;

    @Inject
    private Instance<View> views;

    // Windows
    @Inject
    private HelpWindow helpWindow;
    @Inject
    private SharingOptions sharingOptions;

    @PostConstruct
    protected void initView() {
        setSizeFull();

        final VerticalLayout mainLayout = new VerticalLayout();
        setCompositionRoot(mainLayout);
        mainLayout.setSizeFull();

        mainLayout.addComponent(toolbar.get());

        mainLayout.addComponent(horizontalSplit);
        mainLayout.setExpandRatio(horizontalSplit, 1);

        if (horizontalSplit.getFirstComponent() == null) {
            horizontalSplit.setFirstComponent(tree);
        }
        horizontalSplit.setSplitPosition(200, Unit.PIXELS);
    }

    @Override
    public void setView(final Class<? extends View> viewClass,
            final boolean selectInNavigationTree) {
        View view = views.select(viewClass).get();
        horizontalSplit.setSecondComponent((Component) view);

        if (selectInNavigationTree) {
            tree.setSelectedView(viewClass);
        }

        view.enter();
    }

    @Override
    public void showHelpWindow() {
        if (helpWindow.getParent() == null) {
            UI.getCurrent().addWindow(helpWindow);
        }
    }

    @Override
    public void showShareWindow() {
        if (sharingOptions.getParent() == null) {
            UI.getCurrent().addWindow(sharingOptions);
        }
    }

    @Override
    public void addSearchToTree(final SearchFilter searchFilter) {
        tree.addSearchToTree(searchFilter);
    }

    @Override
    public void enter() {
        super.enter();
        fireViewEvent(MainPresenter.SHOW_ALL, null);
    }

}
