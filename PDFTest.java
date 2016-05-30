import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import java.io.*;

public class PDFTest {
	public static void main(String[] args) {
		PDDocument doc = new PDDocument();

		PDPage page=new PDPage();
		PDFont font = PDType1Font.HELVETICA_BOLD;
		doc.addPage(page);

        
        
		try{
			PDPageContentStream content = new PDPageContentStream(doc, page);
	        content.beginText();
	        content.setFont( font, 12 );
	        content.moveTextPositionByAmount( 100, 700 );
	        content.drawString("Hello from www.printmyfolders.com");
	        content.endText();
	        content.close();
			doc.save("Empty PDF.pdf");
			doc.close();
		} catch (Exception io){
			System.out.println(io);
		}
	}
}