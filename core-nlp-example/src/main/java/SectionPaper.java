import java.util.List;

public class SectionPaper {
    private String title;
    private List<String> content;
    private List<SectionPaper> listSubSections;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public List<SectionPaper> getListSubSections() {
        return listSubSections;
    }

    public void setListSubSections(List<SectionPaper> listSubSections) {
        this.listSubSections = listSubSections;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
