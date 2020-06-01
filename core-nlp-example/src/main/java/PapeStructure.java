import java.util.List;

public class PapeStructure {

    private String paperTitle;
    private String journalTitle;
    private List<String> listAuthors;
    private String abstractInfo;
    private List<SectionPaper> listSection;


    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public List<String> getListAuthors() {
        return listAuthors;
    }

    public void setListAuthors(List<String> listAuthors) {
        this.listAuthors = listAuthors;
    }

    public List<SectionPaper> getListSection() {
        return listSection;
    }

    public void setListSection(List<SectionPaper> listSection) {
        this.listSection = listSection;
    }


    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }
}
