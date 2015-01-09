package web.order;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;
import web.BasePage;

import java.io.IOException;

/**
 * Created by HasanCelik on 06.01.2015.
 */
@MountPath(value = "/index")
public class HomePage extends BasePage {
    public HomePage(PageParameters parameters) throws IOException {
        super(new PageParameters());
        add(new HomeContentPanel("HomeContentPanel"));
    }
}
