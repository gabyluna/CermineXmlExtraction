
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import com.lowagie.text.pdf.parser.SimpleTextExtractingPdfContentStreamProcessor;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import javax.swing.*;
import javax.xml.transform.TransformerConfigurationException;
import java.io.*;
import java.util.List;
import java.util.Properties;

public class DemoClass {
    public static void analyse(String text) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        // text = "What is the Weather in Barcelona right now?";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File("./target/", "nlp.xml"));
            pipeline.xmlPrint(document, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println(String.format("Print: word [%s] pos: [%s] ne: [%s]", word, pos, ne));

            }

        }
    }

    public static byte[] getContentBytesForPage(int var1,PdfReader reader) throws IOException {
        RandomAccessFileOrArray var2 = reader.getSafeFile();
        byte[] var3 = reader.getPageContent(var1, var2);
        var2.close();
        return var3;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException, TransformerConfigurationException {
         SimpleTextExtractingPdfContentStreamProcessor extractionProcessor=new SimpleTextExtractingPdfContentStreamProcessor();

        JFileChooser window=new JFileChooser();
        int a=window.showOpenDialog(null);

        if(a==JFileChooser.APPROVE_OPTION){
            String name=window.getSelectedFile().getName();
            String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
            String data = null;

            if(extension.equals("pdf")){
                //System.out.println(window.getSelectedFile());
                PdfReader reader=new PdfReader(new FileInputStream(window.getSelectedFile()));
                int n=reader.getNumberOfPages();
                for(int i=1;i<n;i++)
                {

                    PdfDictionary var2 = reader.getPageN(i);
                    PdfDictionary var3 = var2.getAsDict(PdfName.RESOURCES);
                    extractionProcessor.processContent(getContentBytesForPage(i,reader), var3);
                    //System.out.println(data);
                    data= extractionProcessor.getResultantText() +  data;
                }
            }

            else{
                System.out.println("format not supported");
            }

            analyse(data);
        }

    }
}
