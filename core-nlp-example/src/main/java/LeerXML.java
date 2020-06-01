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
import java.util.List;



public class LeerXML {

    public static void cargarXml(String xml) {
        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(xml);


        try {
            //Se crea el documento a traves del archivo
            Document document = (Document) builder.build(xmlFile);

            //Se obtiene la raiz 'tables'
            Element rootNode = document.getRootElement();

            //Se obtiene la lista de hijos de la raiz 'tables'
            List list = rootNode.getChildren("article");

            //Se recorre la lista de hijos de 'tables'
            for (int i = 0; i < list.size(); i++) {

                //Se obtiene el elemento 'tabla'
                Element article = (Element) list.get(i);
                List lista_campos = article.getChildren();

                //Se recorre la lista de campos
                for (int j = 0; j < lista_campos.size(); j++) {
                    //Se obtiene el elemento 'campo'

                    Element campo = (Element) lista_campos.get(j);

                    if(campo.getName().equals("front")) {

                        System.out.println("Nombre Conferencia/Revista:" + campo.getChildTextTrim("outsider"));
                        Element childTitle = (Element) campo.getChild("title-group");
                        System.out.println("Título Paper:" + childTitle.getChildTextTrim("article-title"));
                        List  info_au=campo.getChildren("region");
                        Element info= (Element) info_au.get(1);
                        System.out.println("Autores info:" + info.getValue());
                        System.out.println("Abstract Paper:" + campo.getChildTextTrim("abstract"));
                    }
                    if(campo.getName().equals("body")){
                        List sections = campo.getChildren("section");
                        for(int k=0;k<sections.size();k++){

                            Element element_section= (Element) sections.get(k);
                            if(element_section.getAttributeValue("class").equals("deo:Introduction")){

                                System.out.println("Introduction:"+element_section.getChildTextTrim("region"));
                            }

                            if(element_section.getAttributeValue("class").equals("DoCO:Section")){

                               System.out.println("Title section:"+element_section.getChildTextTrim("h1"));
                            }

                            if(element_section.getAttributeValue("class").equals("deo:Methods")){

                                System.out.println("Method: "+element_section.getChildTextTrim("h1")+ " ,text:"
                                        + element_section.getChildTextTrim("region"));
                            }

                            if(element_section.getAttributeValue("class").equals("deo:Results")){

                                System.out.println("Results: "+ element_section.getChildTextTrim("region"));
                            }

                            if(element_section.getAttributeValue("class").equals("deo:Conclusion")){

                               System.out.println("Conclusion:"+element_section.getChildTextTrim("region"));
                            }

                            if(element_section.getAttributeValue("class").equals("DoCO:Bibliography")){

                                List references = element_section.getChildren("ref-list");
                                for(int m=0;m<references.size();m++){

                                    Element ref= (Element) references.get(m);
                                    System.out.println("References:"+ref.getValue());
                                }

                            }
                        }


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




