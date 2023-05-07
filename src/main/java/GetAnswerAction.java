import com.ibm.icu.impl.CacheBase;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import com.intellij.codeInsight.completion.*;


import javax.annotation.processing.Completion;
import java.util.UUID;

public class GetAnswerAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        WriteCommandAction.writeCommandAction(getEventProject(event))
                .run(() -> getCommentInCurrentLine(event));
    }

    public void insert(AnActionEvent event) {
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        String randomString = UUID.randomUUID().toString();
        EditorModificationUtil.insertStringAtCaret(editor, randomString);
    }
    public void insertOrangeString(AnActionEvent event) {
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        String randomString = "pillapichakai";
        Document document = editor.getDocument();
        int offset = editor.getCaretModel().getOffset();
        MarkupModel markupModel = editor.getMarkupModel();

        TextAttributes attributes = new TextAttributes();
        attributes.setForegroundColor(JBColor.MAGENTA);

        RangeHighlighter rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(
                offset,
                offset + randomString.length(),
                HighlighterLayer.CARET_ROW,
                attributes,
                HighlighterTargetArea.EXACT_RANGE
        );

        rangeHighlighter.setGreedyToLeft(true);
        rangeHighlighter.setGreedyToRight(true);

        document.insertString(offset, randomString);
        editor.getCaretModel().moveToOffset(offset + randomString.length());
    }
    public void insertSelectedString(AnActionEvent event) {
        Project project = getEventProject(event);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        CaretModel caretModel = editor.getCaretModel();
        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();
            // get the offset of a selected text
            int offset = selectionModel.getSelectionStart();
            // get the length of a selected text
            int length = selectionModel.getSelectionEnd() - selectionModel.getSelectionStart();

            // move the caret to selectionModel.getSelectionEnd()
            caretModel.moveToOffset(offset + length);

            if (selectedText != null && !selectedText.isEmpty()) {
                EditorModificationUtil.insertStringAtCaret(editor, "The selected string is "+selectedText);
            }
        }
    }
    public void getCommentInCurrentLine(AnActionEvent event) {
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
            if (currentLineText.contains("//")){
                String query = currentLineText.split("//")[1];
                EditorModificationUtil.insertStringAtCaret(editor, "\n// The Query is "+query);
            }
        }
    }

}
