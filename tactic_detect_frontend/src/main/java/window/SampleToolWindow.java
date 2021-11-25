package window;

import api.MethodApi;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import highlight.HighLightHandler;
import mopgenerate.GenerateMop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class SampleToolWindow {
    private JTextField dequeueTextField;
    private JButton enqueueHighlightButton;
    private JPanel panel;
    private JButton dequeueHighlightButton;
    private JTextField enqueueTextField;
    private JPanel generateMopPanel;
    private JTextField destinationTextField;
    private JButton directoryChooseButton;
    private JTextField pingTextField;
    private JButton pingHighlightButton;
    private JTextField echoTextField;
    private JTextField exceptionTextField;
    private JTextField handleExceptionTextField;
    private JButton echoHighlightButton;
    private JButton exceptionHighlightButton;
    private JButton handleExceptionHighlightButton;
    private JPanel pingecho;
    private JButton generatePingechoJarButton;
    private JButton notifyCkpHighlightButton;
    private JTextField notifyCkpTextField;
    private JTextField storeCkpTextField;
    private JTextField failTaskTextField;
    private JTextField commitTextField;
    private JTextField recoverTextField;
    private JButton storeCkpHighlightButton;
    private JButton failTaskHighlightButton;
    private JButton commitHighlightButton;
    private JButton recoverHighlightButton;
    private JPanel ckprbk;
    private JButton generateCkpRbkJarButton;
    private JPanel heartbeat;
    private JTextField receiveTextField;
    private JTextField aliveTextField;
    private JTextField lostTextField;
    private JTextField updateTextField;
    private JButton receiveHighlightButton;
    private JButton aliveHighlightButton;
    private JButton lostHighlightButton;
    private JButton updateHighlightButton;
    private JButton generateHeartbeatJarButton;
    private JTextField requestServiceTextField;
    private JTextField selectResultTextField;
    private JButton requestServiceHighilghtButton;
    private JButton selectResultHighilghtButton;
    private JButton updateStateHighlightButton;
    private JButton generateRedundancyJarButton;
    private JTextField updateStateTextField;
    private JTextField voteRequestTextField;
    private JTextField voteTextField;
    private JTextField failServiceTextField;
    private JTextField stopServiceTextField;
    private JButton voteRequestHighlightButton;
    private JButton voteHighlightButton;
    private JButton failServiceHighlightButton;
    private JButton stopServiceHighlightButton;
    private JButton generateVotingJarButton;
    private JPanel redundancy;
    private JPanel voting;
    private JButton voteRequestUndoButton;
    private JButton voteUndoButton;
    private JButton failServiceUndoButton;
    private JButton stopServiceUndoButton;
    private JButton requestServiceUndoButton;
    private JButton selectResultUndoButton;
    private JButton updateStateUndoButton;
    private JButton receiveUndoButton;
    private JButton aliveUndoButton;
    private JButton lostUndoButton;
    private JButton updateUndoButton;
    private JButton notifyCheckpointUndoButton;
    private JButton storeCheckUndoButton;
    private JButton failTaskUndoButton;
    private JButton commitUndoButton;
    private JButton recoverUndoButton;
    private JButton pingUndoButton;
    private JButton echoUndoButton;
    private JButton exceptionUndoButton;
    private JButton handleExceptionUndoButton;
    private JTabbedPane tabbedPane1;
    private JTabbedPane subtabbed2;
    private JTabbedPane subtabbed3;
    private JTabbedPane subtabbed1;
    //private HashMap<PsiMethod,Boolean> methodUndoHighlightMap = new HashMap<>();


    public SampleToolWindow() throws IOException {
        pingHighlightButton.addActionListener(createHighlightActionListener(pingTextField,false));
        pingUndoButton.addActionListener(createHighlightActionListener(pingTextField,true));
        echoHighlightButton.addActionListener(createHighlightActionListener(echoTextField,false));
        echoUndoButton.addActionListener(createHighlightActionListener(echoTextField,true));
        exceptionHighlightButton.addActionListener(createHighlightActionListener(exceptionTextField,false));
        exceptionUndoButton.addActionListener(createHighlightActionListener(exceptionTextField,true));
        handleExceptionHighlightButton.addActionListener(createHighlightActionListener(handleExceptionTextField,false));
        handleExceptionUndoButton.addActionListener(createHighlightActionListener(handleExceptionTextField,true));
        notifyCkpHighlightButton.addActionListener(createHighlightActionListener(notifyCkpTextField,false));
        notifyCheckpointUndoButton.addActionListener(createHighlightActionListener(notifyCkpTextField,true));
        storeCkpHighlightButton.addActionListener(createHighlightActionListener(storeCkpTextField,false));
        storeCheckUndoButton.addActionListener(createHighlightActionListener(storeCkpTextField,true));
        failTaskHighlightButton.addActionListener(createHighlightActionListener(failTaskTextField,false));
        failTaskUndoButton.addActionListener(createHighlightActionListener(failTaskTextField,true));
        commitHighlightButton.addActionListener(createHighlightActionListener(commitTextField, false));// !revise!
        commitUndoButton.addActionListener(createHighlightActionListener(commitTextField, true)); // !revise!
        recoverHighlightButton.addActionListener(createHighlightActionListener(recoverTextField,false));
        recoverUndoButton.addActionListener(createHighlightActionListener(recoverTextField,true));
        receiveHighlightButton.addActionListener(createHighlightActionListener(receiveTextField,false));
        receiveUndoButton.addActionListener(createHighlightActionListener(receiveTextField,true));
        aliveHighlightButton.addActionListener(createHighlightActionListener(aliveTextField,false));
        aliveUndoButton.addActionListener(createHighlightActionListener(aliveTextField,true));
        lostHighlightButton.addActionListener(createHighlightActionListener(lostTextField,false));
        lostUndoButton.addActionListener(createHighlightActionListener(lostTextField,true));
        updateHighlightButton.addActionListener(createHighlightActionListener(updateTextField,false));
        updateUndoButton.addActionListener(createHighlightActionListener(updateTextField,true));
        requestServiceHighilghtButton.addActionListener(createHighlightActionListener(requestServiceTextField,false));
        requestServiceUndoButton.addActionListener(createHighlightActionListener(requestServiceTextField,true));
        selectResultHighilghtButton.addActionListener(createHighlightActionListener(selectResultTextField,false));
        selectResultUndoButton.addActionListener(createHighlightActionListener(selectResultTextField,true));
        updateStateHighlightButton.addActionListener(createHighlightActionListener(updateStateTextField,false));
        updateStateUndoButton.addActionListener(createHighlightActionListener(updateStateTextField,true));
        voteHighlightButton.addActionListener(createHighlightActionListener(voteTextField,false));
        voteUndoButton.addActionListener(createHighlightActionListener(voteTextField,true));
        voteRequestHighlightButton.addActionListener(createHighlightActionListener(voteRequestTextField,false));
        voteRequestUndoButton.addActionListener(createHighlightActionListener(voteRequestTextField,true));
        failServiceHighlightButton.addActionListener(createHighlightActionListener(failServiceTextField,false));
        failServiceUndoButton.addActionListener(createHighlightActionListener(failServiceTextField,true));
        stopServiceHighlightButton.addActionListener(createHighlightActionListener(stopServiceTextField,false));
        stopServiceUndoButton.addActionListener(createHighlightActionListener(stopServiceTextField,true));

        directoryChooseButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choose = jFileChooser.showDialog(new JLabel(), "Choose");
            if (choose == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooser.getSelectedFile();
                boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
                if (!isWindows) {
                    destinationTextField.setText(jFileChooser.getCurrentDirectory().getAbsolutePath());
                } else {
                    destinationTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        generatePingechoJarButton.addActionListener(createGenerateJarActionListener("pingecho"));
        generateCkpRbkJarButton.addActionListener(createGenerateJarActionListener("ckprbk"));
        generateHeartbeatJarButton.addActionListener(createGenerateJarActionListener("heartbeat"));
        generateVotingJarButton.addActionListener(createGenerateJarActionListener("voting"));
        generateRedundancyJarButton.addActionListener(createGenerateJarActionListener("redundancy"));
    }

    public ActionListener createGenerateJarActionListener(String tactic) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HashMap<String, String> methodHashMap = createMethodHashMap(tactic);
                    if (destinationTextField.getText().equals("") || destinationTextField.getText() == null){
                        Messages.showMessageDialog("Please input the destination path of jar!","Destination Path Error",Messages.getErrorIcon());
                    } else if (methodHashMap.containsValue("")){
                        Messages.showMessageDialog("The name of method cannot be null!","Method Name Error",Messages.getErrorIcon());
                    } else {
                        final boolean[] uploadStatus = new boolean[1];
                        //generatePingechoJarButton.setEnabled(false);
                        ProgressManager.getInstance().run(new Task.Modal(ProjectUtil.guessCurrentProject(panel), "Generating Jar", false) {
                            @Override
                            public void run(@NotNull ProgressIndicator indicator) {
                                indicator.setIndeterminate(false);
                                indicator.setText("Please wait for seconds...");
                                indicator.setFraction(0);
                                indicator.setFraction(0.5);
                                try {
                                    GenerateMop.Generate(tactic, destinationTextField.getText(), methodHashMap);
                                    uploadStatus[0] = MethodApi.getMethodInfo(tactic, methodHashMap);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                indicator.setFraction(1);
                            }

                        });

                        if (uploadStatus[0]){
                            Messages.showMessageDialog("Upload success!","Success!",Messages.getInformationIcon());
                        } else {
                            Messages.showMessageDialog("Upload fail!","Fail!",Messages.getErrorIcon());
                        }
                    }
                    //generatePingechoJarButton.setEnabled(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }

    public ActionListener createHighlightActionListener(JTextField textField,boolean needUndo){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = ProjectUtil.guessCurrentProject(panel);
                ProjectFileIndex.SERVICE.getInstance(project).iterateContent(new ContentIterator() {
                    @Override
                    public boolean processFile(@NotNull VirtualFile fileOrDir) {
                        PsiFile file = PsiManager.getInstance(project).findFile(fileOrDir);
                        if (file instanceof PsiJavaFile) {
                            file.accept(new PsiRecursiveElementVisitor() {
                                @Override
                                public void visitElement(@NotNull PsiElement element) {
                                    super.visitElement(element);
                                    if (element instanceof PsiMethod && ((PsiMethod) element).getName().equals(textField.getText())) {
                                        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, element.getContainingFile().getVirtualFile());
                                        Editor editor = FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);
                                        if (editor != null) {
                                            HighLightHandler.highlight(project, editor, element.getContainingFile(), element, needUndo);
                                        }
                                    }
                                }
                            });
                        }
                        return true;
                    }
                });
            }
        };
    }

    public HashMap<String,String> createMethodHashMap(String tactic) {
        HashMap<String,String> hashMap = new HashMap<>();
        switch (tactic) {
            case "pingecho":
                hashMap.put("ping", pingTextField.getText());
                hashMap.put("echo", echoTextField.getText());
                hashMap.put("exception", exceptionTextField.getText());
                hashMap.put("handleexception", handleExceptionTextField.getText());
                break;
            case "ckprbk":
                hashMap.put("notifyCkp", notifyCkpTextField.getText());
                hashMap.put("storeCkp", storeCkpTextField.getText());
//                hashMap.put("failTask", failServiceTextField.getText());
                hashMap.put("failTask", failTaskTextField.getText());// !revise!
                hashMap.put("commit", commitTextField.getText());
                hashMap.put("recover", recoverTextField.getText());
                break;
            case "heartbeat":
//                hashMap.put("receive", recoverTextField.getText());
                hashMap.put("receive", receiveTextField.getText());// !revise!
                hashMap.put("alive", aliveTextField.getText());
                hashMap.put("lost", lostTextField.getText());
//                hashMap.put("update", updateStateTextField.getText());
                hashMap.put("update", updateTextField.getText());// !revise!
                break;
            case "redundancy":
                hashMap.put("requestService", requestServiceTextField.getText());
                hashMap.put("selectResult", selectResultTextField.getText());
                hashMap.put("updateState", updateStateTextField.getText());
                break;
            case "voting":
                hashMap.put("request", voteRequestTextField.getText());
                hashMap.put("vote", voteTextField.getText());
                hashMap.put("failService", failServiceTextField.getText());
                hashMap.put("stopService", stopServiceTextField.getText());
                break;
        }
        return hashMap;
    }

    public JPanel getContent() {
        return panel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
