package highlight;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.codeInsight.daemon.impl.IdentifierHighlighterPass;
import com.intellij.codeInsight.highlighting.*;
import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.find.FindManager;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesManager;
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter;
import com.intellij.find.impl.FindManagerImpl;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.model.Symbol;
import com.intellij.model.psi.impl.TargetsKt;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.UsageTargetUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighLightHandler {
    /**
     * {@link HighlightUsagesHandler#invoke(Project, Editor, PsiFile)}
     *
     * {@link IdentifierHighlighterPass#doCollectInformation(ProgressIndicator)}
     */
    public static void invoke(@NotNull Project project, @NotNull Editor editor,
                              @NotNull PsiFile file) {
        PsiDocumentManager.getInstance(project).commitAllDocuments();

        if (handleCustomUsage(editor, file)) {
            return;
        }

        DumbService.getInstance(project).withAlternativeResolveEnabled(() -> {
            if (!findSymbols(project, editor, file) && !findTarget(project, editor, file)) {
                handleNoUsageTargets(file, editor, project);
            }
        });
    }

    public static boolean highlight(@NotNull Project project, @NotNull Editor editor,
                                    @NotNull PsiFile file,@NotNull PsiElement element,@NotNull boolean undo){
        final int offset=element.getTextOffset();
        final Collection<Symbol> allTargets = TargetsKt.targetSymbols(file, offset);
        if (allTargets.isEmpty()) {
            return false;
        }
        if (editor instanceof EditorWindow) {
            editor = ((EditorWindow) editor).getDelegate();
        }

        if (file instanceof PsiCompiledFile) {
            file = ((PsiCompiledFile) file).getDecompiledPsiFile();
        }

        file = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);

        //final boolean shouldClear = isClearHighlights(editor);
        for (Symbol symbol : allTargets) {
            final Couple<List<TextRange>> usages =
                    IdentifierHighlighterPass.getUsages(file, symbol);

            //highlightUsages(project, editor, usages, shouldClear);
            highlightUsages(project, editor, usages, undo);
        }

        return true;
    }

    private static boolean findSymbols(@NotNull Project project, @NotNull Editor editor,
                                       @NotNull PsiFile file) {
        final int offset = editor.getCaretModel().getOffset();
        final Collection<Symbol> allTargets = TargetsKt.targetSymbols(file, offset);
        if (allTargets.isEmpty()) {
            return false;
        }

        if (editor instanceof EditorWindow) {
            editor = ((EditorWindow) editor).getDelegate();
        }

        if (file instanceof PsiCompiledFile) {
            file = ((PsiCompiledFile) file).getDecompiledPsiFile();
        }

        file = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);

        final boolean shouldClear = isClearHighlights(editor);
        for (Symbol symbol : allTargets) {
            final Couple<List<TextRange>> usages =
                    IdentifierHighlighterPass.getUsages(file, symbol);

            highlightUsages(project, editor, usages, shouldClear);
        }

        return true;
    }

    private static boolean handleCustomUsage(@NotNull Editor editor, @NotNull PsiFile file) {
        final HighlightUsagesHandlerBase handler =
                HighlightUsagesHandler.createCustomHandler(editor, file);

        if (handler == null) {
            return false;
        }

        final String featureId = handler.getFeatureId();
        if (featureId != null) {
            FeatureUsageTracker.getInstance().triggerFeatureUsed(featureId);
        }

        final List targets = handler.getTargets();
        if (targets == null) {
            return false;
        }

        try {
            // TODO: 06/02/2017 handle custom usages
            handler.highlightUsages();
//            targets
//            handler.computeUsages(targets);
//            final List readUsages = handler.getReadUsages();
//            final List writeUsages = handler.getWriteUsages();
            Log.className("handleCustomUsage", handler);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean findTarget(@NotNull Project project, @NotNull Editor editor,
                                      @NotNull PsiFile file) {
        UsageTarget[] usageTargets = UsageTargetUtil.findUsageTargets(editor, file);
        if (usageTargets.length > 0) {
            for (UsageTarget target : usageTargets) {
                if (target instanceof PsiElement2UsageTargetAdapter) {
                    highlightPsiElement(project,
                            ((PsiElement2UsageTargetAdapter) target).getElement(), editor, file,
                            isClearHighlights(editor));
                } else {
                    Log.className("highlightUsageTarget usageTarget", target);
                    target.highlightUsages(file, editor, isClearHighlights(editor));
                }
            }
        } else {
            final PsiElement psiElement = findTargetElement(editor, file);
            if (psiElement != null) {
                highlightPsiElement(project, psiElement, editor, file, isClearHighlights(editor));
            } else {
                boolean found = false;

                final PsiReference ref = TargetElementUtil.findReference(editor);
                if (ref instanceof PsiPolyVariantReference) {
                    ResolveResult[] results = ((PsiPolyVariantReference) ref).multiResolve(false);
                    if (results.length > 0) {
                        final boolean shouldClear = isClearHighlights(editor);
                        for (ResolveResult result : results) {
                            final PsiElement element = result.getElement();
                            if (element != null) {
                                highlightPsiElement(project, element, editor, file, shouldClear);
                                found = true;
                            }
                        }
                    }
                }

                return found;
            }
        }

        return true;
    }

    private static boolean isClearHighlights(@NotNull Editor editor) {
        if (editor instanceof EditorWindow) {
            editor = ((EditorWindow) editor).getDelegate();
        }

        final Project project = editor.getProject();
        if (project == null) {
            Log.error("isClearHighlights: editor.getProject() == null");
            return false;
        }

        int caretOffset = editor.getCaretModel().getOffset();
        final RangeHighlighter[] highlighters =
                ((HighlightManagerImpl) HighlightManager.getInstance(project)).getHighlighters(
                        editor);
        for (RangeHighlighter highlighter : highlighters) {
            if (TextRange.create(highlighter).grown(1).contains(caretOffset)) {
                return true;
            }
        }

        return false;
    }

    private static PsiElement findTargetElement(@NotNull Editor editor, @NotNull PsiFile file) {
        PsiElement targetElement = TargetElementUtil.findTargetElement(editor,
                TargetElementUtil.getInstance().getReferenceSearchFlags());

        if (targetElement != null && targetElement != file) {
            if (targetElement instanceof NavigationItem) {
                targetElement = (targetElement).getNavigationElement();
            }

            if (targetElement instanceof NavigationItem) {
                return targetElement;
            }
        }

        return null;
    }

    private static void highlightPsiElement(@NotNull Project project,
                                            @NotNull PsiElement psiElement, @NotNull Editor editor, @NotNull PsiFile file,
                                            boolean shouldClear) {
        final PsiElement target = SmartPointerManager.getInstance(psiElement.getProject())
                .createSmartPsiElementPointer(psiElement)
                .getElement();

        if (target == null) {
            return;
        }

        if (file instanceof PsiCompiledFile) {
            file = ((PsiCompiledFile) file).getDecompiledPsiFile();
        }

        final Couple<List<TextRange>> usages = getUsages(target, file);
        highlightUsages(project, editor, usages, shouldClear);
    }

    private static void highlightUsages(@NotNull Project project, @NotNull Editor editor,
                                        Couple<List<TextRange>> usages, boolean shouldClear) {

        final List<TextRange> readRanges = usages.first;
        final List<TextRange> writeRanges = usages.second;

        final HighlightManager highlightManager = HighlightManager.getInstance(project);
        if (shouldClear) {
            clearHighlights(editor, highlightManager, readRanges);
            clearHighlights(editor, highlightManager, writeRanges);
            WindowManager.getInstance().getStatusBar(project).setInfo("");
            return;
        }

        // TODO: 06/02/2017 highlight write and read access
        ArrayList<RangeHighlighter> highlighters = new ArrayList<>();
        final TextAttributes ta = TextAttributesFactory.getInstance().get();
        highlight(highlightManager, readRanges, editor, ta, highlighters);
        highlight(highlightManager, writeRanges, editor, ta, highlighters);

        final Document doc = editor.getDocument();
        for (RangeHighlighter highlighter : highlighters) {
            highlighter.setErrorStripeTooltip(
                    HighlightHandlerBase.getLineTextErrorStripeTooltip(doc,
                            highlighter.getStartOffset(), true));
        }

        int refCount = readRanges.size() + writeRanges.size();
        String msg;
        if (refCount > 0) {
            msg = MessageFormat.format("{0} {0, choice, 1#usage|2#usages} highlighted", refCount);
        } else {
            msg = "No usages highlighted";
        }

        WindowManager.getInstance().getStatusBar(project).setInfo(msg);
    }

    @NotNull
    private static Couple<List<TextRange>> getUsages(@NotNull PsiElement target,
                                                     @NotNull PsiElement psiElement) {
        List<TextRange> readRanges = new ArrayList<>();
        List<TextRange> writeRanges = new ArrayList<>();
        final ReadWriteAccessDetector detector = ReadWriteAccessDetector.findDetector(target);
        final FindUsagesManager findUsagesManager = ((FindManagerImpl) FindManager.getInstance(
                target.getProject())).getFindUsagesManager();
        final FindUsagesHandler findUsagesHandler =
                findUsagesManager.getFindUsagesHandler(target, true);
        final LocalSearchScope scope = new LocalSearchScope(psiElement);
        Collection<PsiReference> refs =
                findUsagesHandler != null ? findUsagesHandler.findReferencesToHighlight(target,
                        scope) : ReferencesSearch.search(target, scope).findAll();
        for (PsiReference psiReference : refs) {
            if (psiReference == null) {
                Log.error("Null reference returned, findUsagesHandler=" + findUsagesHandler
                        + "; target=" + target + " of " + target.getClass());
                continue;
            }
            List<TextRange> destination;
            if (detector == null || detector.getReferenceAccess(target, psiReference)
                    == ReadWriteAccessDetector.Access.Read) {
                destination = readRanges;
            } else {
                destination = writeRanges;
            }
            HighlightUsagesHandler.collectRangesToHighlight(psiReference, destination);
        }

        final TextRange declareRange =
                HighlightUsagesHandler.getNameIdentifierRange(psiElement.getContainingFile(),
                        target);
        if (declareRange != null) {
            if (detector != null && detector.isDeclarationWriteAccess(target)) {
                writeRanges.add(declareRange);
            } else {
                readRanges.add(declareRange);
            }
        }

        return Couple.of(readRanges, writeRanges);
    }

    private static void clearHighlights(Editor editor, HighlightManager highlightManager,
                                        List<TextRange> toRemoves) {
        if (editor instanceof EditorWindow) {
            editor = ((EditorWindow) editor).getDelegate();
        }

        RangeHighlighter[] highlighters =
                ((HighlightManagerImpl) highlightManager).getHighlighters(editor);

        Arrays.sort(highlighters, (o1, o2) -> o1.getStartOffset() - o2.getStartOffset());
        Collections.sort(toRemoves, (o1, o2) -> o1.getStartOffset() - o2.getStartOffset());

        int i = 0;
        int j = 0;
        while (i < highlighters.length && j < toRemoves.size()) {
            RangeHighlighter highlighter = highlighters[i];
            final TextAttributes ta = highlighter.getTextAttributes();
            final TextRange textRange = TextRange.create(highlighter);
            final TextRange toRemove = toRemoves.get(j);
            if (ta != null && ta instanceof NamedTextAttr // wrap
                    && highlighter.getLayer() == HighlighterLayer.SELECTION - 1 // wrap
                    && toRemove.equals(textRange)) {
                highlightManager.removeSegmentHighlighter(editor, highlighter);
                i++;
            } else if (toRemove.getStartOffset() > textRange.getEndOffset()) {
                i++;
            } else if (toRemove.getEndOffset() < textRange.getStartOffset()) {
                j++;
            } else {
                i++;
                j++;
            }
        }
    }

    private static void highlight(@NotNull HighlightManager highlightManager,
                                  @NotNull Collection<TextRange> textRanges, @NotNull Editor editor,
                                  @NotNull TextAttributes ta, @Nullable Collection<RangeHighlighter> holder) {
        final Color scrollMarkColor;
        if (ta.getErrorStripeColor() != null) {
            scrollMarkColor = ta.getErrorStripeColor();
        } else if (ta.getBackgroundColor() != null) {
            scrollMarkColor = ta.getBackgroundColor().darker();
        } else {
            scrollMarkColor = null;
        }

        for (TextRange range : textRanges) {
            highlightManager.addOccurrenceHighlight(editor, range.getStartOffset(),
                    range.getEndOffset(), ta, 0, holder, scrollMarkColor);
        }
    }

    private static void handleNoUsageTargets(PsiFile file, @NotNull Editor editor,
                                             @NotNull Project project) {
        final SelectionModel selectionModel = editor.getSelectionModel();
        if (!selectionModel.hasSelection()) {
            selectionModel.selectWordAtCaret(false);
        }

        final String text = selectionModel.getSelectedText();
        selectionModel.removeSelection();
        Matcher m = Pattern.compile("\\b" + text + "\\b").matcher(file.getText());
        List<TextRange> writeRanges = new ArrayList<>();
        while(m.find())
        {
            writeRanges.add(new TextRange(m.start(),m.end()));
        }

        final HighlightManager highlightManager = HighlightManager.getInstance(project);
        if (isClearHighlights(editor)) {
            clearHighlights(editor, highlightManager, writeRanges);
            WindowManager.getInstance().getStatusBar(project).setInfo("");
            return;
        }

        ArrayList<RangeHighlighter> highlighters = new ArrayList<>();
        final TextAttributes ta = TextAttributesFactory.getInstance().get();
        highlight(highlightManager, writeRanges, editor, ta, highlighters);
    }
}
