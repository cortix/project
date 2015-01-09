package web.management;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;
import web.BasePage;
import web.BasePage2;

import java.io.IOException;

/**
 * Created by HasanCelik on 08.01.2015.
 */
@MountPath(value = "/management")
public class ManagementPage extends BasePage2 {
    public ManagementPage(PageParameters parameters) throws IOException {
        super(new PageParameters());
        add(new ManagementContentPanel("ManagementContentPanel"));
    }
}
