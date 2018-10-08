package com.f.schoolsintouchteachers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pdf);
       // Document document=new Document();
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "report.pdf");
            file.createNewFile();
            Rectangle small = new Rectangle(290,100);
            Font smallfont = new Font(Font.FontFamily.HELVETICA, 10);
            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN  , 20, Font.BOLD);
            Document document = new Document(PageSize.A4,0,0,0,0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            InputStream ims = getAssets().open("logo.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleAbsolute(205,87.5f);
           // image.scalePercent(50);
            Log.d("SCALED", String.valueOf(image.getScaledHeight()) +" "+ String.valueOf(image.getScaledWidth()));

            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            Paragraph paragraph=new Paragraph("ALLIDINA VISRAM HIGH SCHOOL",font1);
           // paragraph.set
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph.setPaddingTop(10);
           // paragraph.setFont(new Font(Font.FontFamily.TIMES_ROMAN,30,Font.BOLD));
            document.add(paragraph);
            Paragraph paragraph1=new Paragraph("P.O. BOX 1235,MOMBASA,KENYA.",new Font(Font.FontFamily.TIMES_ROMAN,15));
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph1.setPaddingTop(20);
            document.add(paragraph1);
            Paragraph paragraph2=new Paragraph("Student's results slip.",new Font(Font.FontFamily.TIMES_ROMAN,15,Font.UNDERLINE));
            paragraph2.setPaddingTop(40);
            paragraph2.setSpacingAfter(15);
            paragraph2.setIndentationLeft(60);
            document.add(paragraph2);
            PdfPTable pdfPTable=new PdfPTable(6);
          //  pdfPTable.setPaddingTop(20);

            PdfPCell cell1 = new PdfPCell(new Phrase("Exam:"));

           // cell.setFixedHeight(30);

             cell1.setBorder(Rectangle.NO_BORDER);
           //  cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            // cell1.setBorder(Rectangle.BOTTOM);
            PdfPCell cell2 = new PdfPCell(new Phrase(""));
            // cell.setFixedHeight(30);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
           // cell1.setBorder(Rectangle.BOTTOM);
            PdfPCell cell3 = new PdfPCell(new Phrase("Term:"));
            // cell.setFixedHeight(30);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
           // cell3.setBorder(Rectangle.BOTTOM);
            PdfPCell cell4 = new PdfPCell(new Phrase(""));
            // cell.setFixedHeight(30);
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
          //  cell4.setBorder(Rectangle.BOTTOM);
            PdfPCell cell5 = new PdfPCell(new Phrase("Year:"));
            // cell.setFixedHeight(30);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
          //  cell5.setBorder(Rectangle.BOTTOM);
            PdfPCell cell6 = new PdfPCell(new Phrase(""));
            // cell.setFixedHeight(30);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
          //  cell6.setBorder(Rectangle.BOTTOM);
            pdfPTable.addCell(cell1);
            pdfPTable.addCell(cell2);
            pdfPTable.addCell(cell3);
            pdfPTable.addCell(cell4);
            pdfPTable.addCell(cell5);
            pdfPTable.addCell(cell6);
            Chunk chunk=new Chunk("Examination:");
            chunk.setUnderline(1,-2f);

            document.add(pdfPTable);
          //  document.add(new Chunk("Term"));
            PdfPTable table = new PdfPTable(2);
           // table.setTotalWidth(new float[]{ 160, 120 });
           // table.setLockedWidth(true);
            PdfContentByte cb = writer.getDirectContent();
            // first row
            PdfPCell cell = new PdfPCell(new Phrase("Some text here"));
            cell.setFixedHeight(30);
          //  cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);
            // second row
            cell = new PdfPCell(new Phrase("Some more text", smallfont));
            cell.setFixedHeight(30);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
           // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            Barcode128 code128 = new Barcode128();
            code128.setCode("14785236987541");
            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);
            cell = new PdfPCell(code128Image, true);
           // cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(30);
            table.addCell(cell);
            // third row
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("and something else here", smallfont));
           // cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
