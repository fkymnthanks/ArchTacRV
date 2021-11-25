package window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SampleToolWindowFactory implements ToolWindowFactory {
    public SampleToolWindow window = null;
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        try {
            window = new SampleToolWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentFactory factory = ContentFactory.SERVICE.getInstance();
        Content content = factory.createContent(window.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
