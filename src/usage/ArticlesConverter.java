package usage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream.GetField;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;

import de.aitools.ie.uima.type.arguana.Sentiment;
import de.aitools.ie.uima.usage.UIMAAnnotationFileWriter;

import model.*;
import parsing.*;
public class ArticlesConverter {


	private static final String ANALYSIS_ENGINE_PATH = 
			"conf/primitive-AEs/"
			+ "DummyAnalysisEngine.xml";

	/**
	 * The path of the directory where the XMI files shall be written to
	 */
	private static final String OUTPUT_COLLECTION_DIR = 
			"data/xmi-output/";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String segmentsText = readFile("segments.txt");
		String annotationText = readFile("wasek-records.txt");
		AnnotationParser parser = new AnnotationParser(segmentsText, annotationText);
		List<Annotation> annotations = parser.parse();
	}
	
	public  void processFile(String path)
	{
		AnalysisEngine ae = this.createAnalysisEngine(ANALYSIS_ENGINE_PATH);
		UIMAAnnotationFileWriter xmiWriter = new UIMAAnnotationFileWriter();
		int i = 0;
		
				
				CAS aCAS = ae.newCAS();
				JCas jcas = aCAS.getJCas();
				
				// adding Sentiment 
				Sentiment snti = new Sentiment(jcas);
				double score =0 ;

				if(tokens!=null && tokens[0]!=null)
				{
					jcas.setDocumentText(tokens[1]);
					snti.setPolarity(tokens[0]);
					snti.setBegin(0);
					snti.setEnd(tokens[1].length());
					switch(tokens[0])
					{
					case "Positive":
						score = 1;
						break;
					case "Negative":
						score = -1;
						break;
					default :
						score = 0;
						break;
					}
					
				}
				snti.setScore(score);
				snti.addToIndexes();
  				xmiWriter.write(jcas.getCas(), OUTPUT_COLLECTION_DIR, 
  						String.format("%03d.txt", i));
				i++;
			}
		ae.destroy();
	}
	
	private AnalysisEngine createAnalysisEngine(String aePath){
		System.out.print("Initializing \"" + aePath + "\"...");
		long count = System.currentTimeMillis();
		AnalysisEngine ae = null;
		try{
			// Create analysis engine from XML descriptor
			XMLInputSource xmlInputSource = new XMLInputSource(aePath);
			ResourceSpecifier specifier = 
				UIMAFramework.getXMLParser().parseResourceSpecifier(
						xmlInputSource);
			ae = UIMAFramework.produceAnalysisEngine(specifier);
			//ae.setConfigParameterValue("AbbreviationLexicon", lexiconPath);
			//ae.reconfigure();
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		count = System.currentTimeMillis() - count;
		System.out.println("\nfinished in " + (count/1000.0) + "s\n");
		return ae;
	}
	
	public static String readFile(String path)
	{
		String everything =null;
		BufferedReader br =null;
		try {
			 br = new BufferedReader(new FileReader(path));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
			br.close();
		    everything= sb.toString();

		} 
		catch(Exception e )
		{
			System.out.print(e.getMessage());
		}
		finally {
		}
	    return everything;
	}
}
