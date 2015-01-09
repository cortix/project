package web;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import web.management.ManagementPage;

/**
 * Created by HasanCelik on 09.01.2015.
 */
public abstract class BasePage2 extends BasePage {

    public BasePage2 (PageParameters parameters){
        super(parameters);
    }
    protected BookmarkablePageLink brandLink(String id) {
        return new BookmarkablePageLink(id, ManagementPage.class);
    }
}
