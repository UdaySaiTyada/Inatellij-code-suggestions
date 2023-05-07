import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.ListPopupStep;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;
import java.util.List;

public class GetAnswer2Action extends AnAction{
    @Override
    public void actionPerformed(AnActionEvent event)
    {
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
        String currentLineText = document.getText(new TextRange(lineStartOffset, lineEndOffset));

        if (currentLineText != null && !currentLineText.isEmpty()) {
            if (currentLineText.contains("//")) {
                String query = currentLineText.split("//")[1];
                List<LookupElement> lookupElements = new ArrayList<>();
                lookupElements.add(LookupElementBuilder.create("int c = a + b;"));
                lookupElements.add(LookupElementBuilder.create("int add = a + b;"));


                // Show the ListPopup
                ListPopupStep<LookupElement> step = new BaseListPopupStep<LookupElement>("Suggestions", lookupElements) {
                    @Override
                    public PopupStep onChosen(LookupElement selectedValue, boolean finalChoice) {
                        // handle the selected element
                        WriteCommandAction.writeCommandAction(getEventProject(event))
                                .run(() -> {
                                    EditorModificationUtil.insertStringAtCaret(editor, "\n\t\t"+selectedValue.getLookupString());
                                });
                        return finalChoice ? null : this;
                    }
                };

                // Show the ListPopupStep in the editor
                JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(event.getDataContext());
            }
        }

    }

    public void insert(AnActionEvent event) {

    }
}
