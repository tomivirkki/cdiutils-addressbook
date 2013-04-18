package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.TestView;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchViewImpl;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.cdi.UIScoped;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class MainViewImpl extends AbstractView implements MainViev {
    @Inject
    private Instance<NavigationTree> tree;
    @Inject
    private Instance<Toolbar> toolbar;
    @Inject
    @Preconfigured
    private HorizontalSplitPanel horizontalSplit;

    // Views
    @Inject
    private Instance<ListViewImpl> listView;
    @Inject
    private Instance<SearchViewImpl> searchView;

    // Windows
    @Inject
    private HelpWindow helpWindow;
    @Inject
    private SharingOptions sharingOptions;

    // Test
    @Inject
    private TestView testView;

    @Override
    protected void initView() {
        setSizeFull();

        final VerticalLayout mainLayout = new VerticalLayout();
        setCompositionRoot(mainLayout);
        mainLayout.setSizeFull();

        toolbar.get().init();
        mainLayout.addComponent(toolbar.get());

        mainLayout.addComponent(horizontalSplit);
        mainLayout.setExpandRatio(horizontalSplit, 1);

        tree.get().init();
        horizontalSplit.setFirstComponent(tree.get());
        horizontalSplit.setSplitPosition(200, Unit.PIXELS);

        helpWindow.init();
        sharingOptions.init();

        tree.get().setValue(MainPresenter.SHOW_ALL);

        UI.getCurrent().addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(final ClickEvent event) {
                if (event.isCtrlKey()) {
                    horizontalSplit.setSecondComponent(testView);
                }
            }
        });
    }

    @Override
    public void setView(final Class<? extends View> viewClass,
            final boolean selectInNavigationTree) {
        AbstractView view = null;
        if (SearchView.class.isAssignableFrom(viewClass)) {
            view = searchView.get();
        } else if (ListView.class.isAssignableFrom(viewClass)) {
            view = listView.get();
        }
        horizontalSplit.setSecondComponent(view);

        if (selectInNavigationTree) {
            tree.get().setSelectedView(viewClass);
        }

        view.openView();
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
        tree.get().addSearchToTree(searchFilter);
    }

}
