package parsing;
import java.util.ArrayList;
import java.util.List;

import model.Annotation;
public class AnnotationParser {

	String segmentsText;
	String annotationText;
	
	public AnnotationParser(String segmentsText , String annotationText) {
		// TODO Auto-generated constructor stub
		this.segmentsText = segmentsText;
		this.annotationText =  annotationText;
	}
	
	public List<Annotation> parse()
	{
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		List<String> annotationTypes = getAnnotationTypes();
		String[] pargraphs = splitToParagraphs(segmentsText);
		int atCounter = 0;
		for(int i = 1 ; i < pargraphs.length; i++) // skip title 
		{
			String[] lines = splitToLines(pargraphs[i]);
			for(int j = 0 ; j < lines.length ; j++)
			{
				Annotation annotation = new Annotation();
				annotation.setText(lines[j]);
				annotation.setType(annotationTypes.get(atCounter));
				annotations.add(annotation);
			}
		}
		return annotations;
	}
	
	private List<String> getAnnotationTypes()
	{
		String[] lines = annotationText.split("\n");
		ArrayList<String> annotationTypes = new ArrayList<String>();
		if(lines.length< 2)
			return annotationTypes;
		for(int i = 1 ; i < lines.length-1; i++)
		{
			String[] tokens = lines[i].split("\\t");
			annotationTypes.add(tokens[3]);
		}
		return annotationTypes;
	}
	private String[] splitToParagraphs(String text)
	{
		return text.split("\\n\\n");
	}
	
	private String[] splitToLines(String text)
	{
		return text.split("\\n");
	}
	
}
