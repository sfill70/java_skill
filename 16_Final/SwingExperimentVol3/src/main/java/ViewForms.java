public class ViewForms {
    private final MainFrame mainFrame;
    private final MainForm formThreeFields;
    private final MainForm formOneFields;

    public ViewForms() {
        this.mainFrame = MainFrame.getInstance();
        this.formThreeFields = new FormThreeFields();
        this.formOneFields = new FormOneFields();
    }


    public void run() {
        mainFrame.initialization();
        formThreeFields.setListener(new CollapseListener(mainFrame, formOneFields, formThreeFields));
        formOneFields.setListener(new ExpandListener(mainFrame, formOneFields, formThreeFields));
        viewFormThreeFields();
    }

    private void viewFormOneFields() {
        mainFrame.setContent(formOneFields);
    }

    private void viewFormThreeFields() {
        mainFrame.setContent(formThreeFields);
    }
}
