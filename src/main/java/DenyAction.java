import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public class DenyAction extends AnAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        WriteCommandAction.writeCommandAction(getEventProject(event))
                .run(() -> insert(event));
    }
    public void insert(AnActionEvent event)
    {
        WriteCommandAction.writeCommandAction(getEventProject(event))
                .run(() -> removeCommentInCurrentLine(event));
    }
    public void removeCommentInCurrentLine(AnActionEvent event) {
        Project project = getEventProject(event);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        CaretModel caretModel = editor.getCaretModel();
        Document document = editor.getDocument();

        // Get the current caret position
        int offset = caretModel.getOffset();

        // Get the line number of the current caret position
        int lineNumber = document.getLineNumber(offset);

        // Get the text of the current line
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        int lineEndOffset = document.getLineEndOffset(lineNumber);
        document.deleteString(lineStartOffset, lineEndOffset);
    }
}
