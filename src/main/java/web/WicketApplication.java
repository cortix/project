package web;



import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.prettyprint.PrettifyCssResourceReference;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.prettyprint.PrettifyJavaScriptReference;
import de.agilecoders.wicket.core.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.jqueryui.*;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

import de.agilecoders.wicket.webjars.WicketWebjars;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import org.springframework.stereotype.Component;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import web.order.HomePage;


import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: HasanCelik
 * Date: 18.11.2013
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */

@Component(value = "wicketApplication")
public class WicketApplication extends WebApplication  {

    private static final String DEFAULT_ENCODING = "UTF-8";
    public static final int LANGUAGE_COOKIE_AGE = 60 * 60 * 24 * 31 * 6;
    public static final String LANGUAGE_COOKIE_NAME = "LANG";

    protected void init() {
        super.init();
        getMarkupSettings().setDefaultMarkupEncoding(DEFAULT_ENCODING);
        getRequestCycleSettings().setResponseRequestEncoding(DEFAULT_ENCODING);
        if (getConfigurationType().equals(RuntimeConfigurationType.DEPLOYMENT)) {
            getMarkupSettings().setStripWicketTags(true);
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setCompressWhitespace(true);
        }
        getResourceSettings().getResourceFinders().add(
                new WebApplicationPath(getServletContext(), "src/main/java"));
        getResourceSettings().getResourceFinders().add(
                new WebApplicationPath(getServletContext(), "src/main/resources"));
        // using Wicket Spring component injector...
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        configureBootstrap();
        configureResourceBundles();
        new AnnotatedMountScanner().scanPackage("web").mount(this);
    }

    /**
     * configure all resource bundles (css and js)
     */
    private void configureResourceBundles() {
        ResourceBundles bundles = getResourceBundles();
        bundles.addJavaScriptBundle(WicketApplication.class, "core.js",
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getJQueryReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketEventReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketAjaxReference(),
                ModernizrJavaScriptReference.instance()
        );



        getResourceBundles().addJavaScriptBundle(WicketApplication.class, "bootstrap-extensions.js",
                JQueryUICoreJavaScriptReference.instance(),
                JQueryUIWidgetJavaScriptReference.instance(),
                JQueryUIMouseJavaScriptReference.instance(),
                JQueryUIDraggableJavaScriptReference.instance(),
                JQueryUIResizableJavaScriptReference.instance(),
                Html5PlayerJavaScriptReference.instance()
        );

        bundles.addCssBundle(WicketApplication.class, "bootstrap-extensions.css",
                Html5PlayerCssReference.instance(),
                OpenWebIconsCssReference.instance()
        );

    }

    /**
     * configures wicket-bootstrap and installs the settings.
     */
    private void configureBootstrap() {
        WicketWebjars.install(this);
        final ThemeProvider themeProvider = new BootswatchThemeProvider(BootswatchTheme.Spacelab) {{
        }};

        final IBootstrapSettings settings = new BootstrapSettings();

        settings.setJsResourceFilterName("footer-container")
                .setThemeProvider(themeProvider);
        Bootstrap.install(this, settings);

        BootstrapLess.install(this);
    }

    @Override
    public Session newSession(Request request, Response response) {

        Session session = super.newSession(request, response);
        session = trySetLanguageFromCookie(session, request, response);

        return session;
    }

    private Session trySetLanguageFromCookie(Session session, Request request, Response response) {

        List<Cookie> cookies = ((WebRequest) request).getCookies();

        if (cookies == null || cookies.isEmpty()) {
            return session;
        }


        for (Cookie cookie : cookies) {
            if (LANGUAGE_COOKIE_NAME.equals(cookie.getName())) {
                session.setLocale(new Locale(cookie.getValue()));

                cookie.setMaxAge(LANGUAGE_COOKIE_AGE);
                ((WebResponse)response).addCookie(cookie);
                break;
            }
        }

        return session;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }
}

