import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.SimpleTextExtractingPdfContentStreamProcessor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeerXMLCermine {

    public static void cargarXml(String xml) {
        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(xml);

        PapeStructure paper = new PapeStructure();
        try {
            //Se crea el documento a traves del archivo
            Document document = (Document) builder.build(xmlFile);

            //Se obtiene la raiz 'tables'
            Element rootNode = document.getRootElement();

            //Se obtiene la lista de hijos de la raiz 'tables'
            List list = rootNode.getChildren("front");

            //Se recorre la lista de hijos de 'front'
            for (int i = 0; i < list.size(); i++) {

                //Se obtiene el elemento 'tabla'
                Element article = (Element) list.get(i);
                List lista_campos = article.getChildren();

                //Se recorre la lista de campos

                for (int j = 0; j < lista_campos.size(); j++) {
                    //Se obtiene el elemento 'campo'

                    Element campo = (Element) lista_campos.get(j);

                    if(campo.getName().equals("article-meta")) {

                        //System.out.println("Nombre Conferencia/Revista:" + campo.getChildTextTrim("outsider"));
                        Element childTitle = (Element) campo.getChild("title-group");
                        //System.out.println("Título Paper:" + childTitle.getChildTextTrim("article-title"));
                        paper.setPaperTitle(childTitle.getChildTextTrim("article-title"));
                        Element  info_au=campo.getChild("contrib-group");

                        List element =  info_au.getChildren();
                        List<String> authors = new ArrayList<String>();
                        for (int l=0 ; l<element.size();l++) {
                              Element elementTmp =  (Element) element.get(l);
                              authors.add(elementTmp.getChildTextTrim("string-name"));
                          //    System.out.println("Autores info:" + elementTmp.getChildTextTrim("string-name"));
                              // System.out.println("Autores info:" + element.getChildTextTrim("string-name"));

                        }

                        paper.setListAuthors(authors);

                        Element childAbstract = (Element) campo.getChild("abstract");
                        paper.setAbstractInfo(childAbstract.getChildTextTrim("p"));
                        //System.out.println("Abstract Paper:" + childAbstract.getChildTextTrim("p"));
                    }

                   /* if(campo.getName().equals("journal-meta")) {

                        //System.out.println("Nombre Conferencia/Revista:" + campo.getChildTextTrim("outsider"));
                       if(campo.getChild("journal-title-group")!= null){
                           Element childTitle = (Element) campo.getChild("journal-title-group");
                           paper.setJournalTitle(childTitle.getChildTextTrim("journal-title"));
                         //  System.out.println("Título Journal:" + childTitle.getChildTextTrim("journal-title"));
                       }


                    }*/

                }
            }

            //Se obtiene la lista de hijos de la raiz 'tables'
            List listBody = rootNode.getChildren("body");

            for (int i = 0; i < listBody.size(); i++) {

                //Se obtiene el elemento 'tabla'
                Element article = (Element) listBody.get(i);
                List lista_campos = article.getChildren();

                //Se recorre la lista de campos
                List<SectionPaper> listSections = new ArrayList<SectionPaper>();
                for (int j = 0; j < lista_campos.size(); j++) {
                    //Se obtiene el elemento 'campo'


                    Element campo = (Element) lista_campos.get(j);

                    if(campo.getName().equals("sec")) {

                        //System.out.println("Nombre Conferencia/Revista:" + campo.getChildTextTrim("outsider"));
                        Element childTitle = (Element) campo.getChild("title");
                      //  System.out.println("Título Section:" + childTitle.getValue());



                        Element subSection = (Element) campo.getChild("sec");
                        if(subSection == null){
                            SectionPaper section = new SectionPaper();
                            section.setTitle(childTitle.getValue());
                            List lista_p = campo.getChildren("p");
                            List<String> listContent = new ArrayList<String>();
                            for(int k=0; k< lista_p.size(); k++){

                                Element childSection = (Element) lista_p.get(k);
                              //  System.out.println(" Section:" + childSection.getValue());
                                listContent.add(childSection.getValue());
                            }
                            section.setContent(listContent);
                            listSections.add(section);
                            paper.setListSection(listSections);

                        }else{

                            List lista_section = campo.getChildren("sec");
                            List<SectionPaper> subSectionsList = new ArrayList<SectionPaper>();

                            SectionPaper section = new SectionPaper();
                            section.setTitle(childTitle.getValue());

                            for(int k=0; k< lista_section.size(); k++){
                                SectionPaper subSectionInfo = new SectionPaper();
                                Element childSection = (Element) lista_section.get(k);
                                Element childTitleSection = (Element) childSection.getChild("title");
                                subSectionInfo.setTitle(childTitleSection.getValue());
                               // System.out.println(" Sub Section Title:" + childTitleSection.getValue());
                                Element childSubSection = (Element) childSection.getChild("p");

                              //  System.out.println(" Sub Section:" + childSubSection.getValue());
                                List<String> listInfoSubSection = new ArrayList<String>();
                                listInfoSubSection.add(childSubSection.getValue());
                                subSectionInfo.setContent(listInfoSubSection);
                                subSectionsList.add(subSectionInfo);
                            }
                            section.setListSubSections(subSectionsList);
                            listSections.add(section);
                            paper.setListSection(listSections);


                        }





                    }

                }
            }

            System.out.println("article title:" + paper.getPaperTitle());
            System.out.println("article abstract:" + paper.getAbstractInfo());
            //System.out.println("article journal tittle:" + paper.getJournalTitle());
            for(int i= 0; i<paper.getListAuthors().size();i++)
            {
                System.out.println("article authors:" + paper.getListAuthors().get(i).toString());
            }

            for(int i= 0; i<paper.getListSection().size();i++)
            {

                SectionPaper section = new SectionPaper();
                section  = paper.getListSection().get(i);
                System.out.println("article sections:" + section.getTitle());
                if(section.getListSubSections()!= null){
                    for(int j = 0; j< section.getListSubSections().size(); j++)
                    {

                        SectionPaper subSection = new SectionPaper();
                        subSection =  section.getListSubSections().get(j);
                        System.out.println("title subsection:" + subSection.getTitle());
                        System.out.println("content subsection:" + subSection.getContent());
                    }

                }

            }

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
    }


    public static void main(String[] args) throws FileNotFoundException, IOException, TransformerConfigurationException {

        JFileChooser window = new JFileChooser();
        int a = window.showOpenDialog(null);

        if (a == JFileChooser.APPROVE_OPTION) {
            String name = window.getSelectedFile().getName();
            String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
            String data = null;

            if (extension.equals("xml")) {
                cargarXml(window.getSelectedFile().getPath());

            }
        }

    }
}




