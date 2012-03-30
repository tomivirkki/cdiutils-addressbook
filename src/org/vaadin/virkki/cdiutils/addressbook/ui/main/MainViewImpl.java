package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.ui.test.TestViewImpl;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainViewImpl extends AbstractView implements MainViev, Handler {
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

    // For testing
    @Inject
    private Instance<TestViewImpl> testView;
    private final Action actionTestcontent = new ShortcutAction("Alt+C",
            ShortcutAction.KeyCode.C,
            new int[] { ShortcutAction.ModifierKey.ALT });

    @Override
    protected final void initView() {
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
        horizontalSplit.setSplitPosition(200, UNITS_PIXELS);

        helpWindow.init();
        sharingOptions.init();

        tree.get().setValue(MainPresenter.SHOW_ALL);

        getContextWindow().addActionHandler(this);
    }

    @Override
    public final void setView(final Class<? extends View> viewClass,
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
    public final void showHelpWindow() {
        if (helpWindow.getParent() == null) {
            getContextWindow().addWindow(helpWindow);
        }
    }

    @Override
    public final void showShareWindow() {
        if (sharingOptions.getParent() == null) {
            getContextWindow().addWindow(sharingOptions);
        }
    }

    @Override
    public final void addSearchToTree(final SearchFilter searchFilter) {
        tree.get().addSearchToTree(searchFilter);
    }

    @Override
    public final Action[] getActions(final Object target, final Object sender) {
        return new Action[] { actionTestcontent };
    }

    @Override
    public final void handleAction(final Action action, final Object sender,
            final Object target) {
        if (action == actionTestcontent) {
            horizontalSplit.setSecondComponent(testView.get());
            testView.get().openView();
        }
    }
}
