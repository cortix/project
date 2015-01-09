package web;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import web.order.HomePage;

/**
 * Created by HasanCelik on 09.01.2015.
 */
public abstract class BasePage1 extends BasePage {
    public BasePage1(final PageParameters parameters){
        super(parameters);

    }
    protected BookmarkablePageLink brandLink(String id) {
        return new BookmarkablePageLink(id, HomePage.class);
    }
}
