package web;

import base.ApplicationJavaScript;
import base.DocsCssResourceReference;
import base.FixBootstrapStylesCssResourceReference;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import web.order.Footer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HasanCelik on 06.01.2015.
 */
public abstract class BasePage<T> extends GenericWebPage<T> {

    public BasePage(final PageParameters parameters){
        super(parameters);
        add(new HtmlTag("html"));
        add(newNavbar("navbar"));
        add(new Footer("footer"));
        add(new Code("code-internal"));
    }

    @Override protected void onInitialize() {
        super.onInitialize();
//        add(brandLink("brandLink"));
    }
    protected abstract AbstractLink brandLink(String id);

    protected Navbar newNavbar(String markupId) {
        Navbar navbar = new Navbar(markupId) {
            @Override
            protected Component newBrandNameLink(String componentId)
            {
                AbstractLink brandLink = brandLink(componentId);
                brandLink.setOutputMarkupPlaceholderTag(true);

                brandLink.add(newBrandLabel("brandLabel"));
                brandLink.add(newBrandImage("brandImage"));
                return brandLink;
            }

            @Override
            protected TransparentWebMarkupContainer newCollapseContainer(String componentId) {
                TransparentWebMarkupContainer container = super.newCollapseContainer(componentId);
                container.add(new CssClassNameAppender("bs-navbar-collapse"));
                return container;
            }
        };
        navbar.setPosition(Navbar.Position.TOP);
        //navbar.setInverted(false);
        navbar.setBrandName(Model.of("Project"));

        DropDownButton dropdown = new NavbarDropDownButton(new StringResourceModel("choose_language", this, null)) {
            @Override
            public boolean isActive(Component item) {
                return false;
            }

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                List<AbstractLink> languages = new ArrayList<AbstractLink>();
                //languages.add(newLanguageOption(buttonMarkupId, "tr"));
                //languages.add(newLanguageOption(buttonMarkupId, "gb"));
                return languages;
            }
        }.setIconType(GlyphIconType.globe);

        navbar.addComponents(new ImmutableNavbarComponent(dropdown, Navbar.ComponentPosition.RIGHT));

        return navbar;
    }

    /**
     * sets the theme for the current user.
     *
     * @param pageParameters current page parameters
     */
    private void configureTheme(PageParameters pageParameters) {
        StringValue theme = pageParameters.get("theme");

        if (!theme.isEmpty()) {
            IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
            settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
        }
    }

    protected ITheme activeTheme() {
        IBootstrapSettings settings = Bootstrap.getSettings(getApplication());

        return settings.getActiveThemeProvider().getActiveTheme();
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        configureTheme(getPageParameters());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(FixBootstrapStylesCssResourceReference.INSTANCE));
        response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE), "footer-container"));

        if ("google".equalsIgnoreCase(activeTheme().name())) {
            response.render(CssHeaderItem.forReference(DocsCssResourceReference.GOOGLE));
        }
    }
}
