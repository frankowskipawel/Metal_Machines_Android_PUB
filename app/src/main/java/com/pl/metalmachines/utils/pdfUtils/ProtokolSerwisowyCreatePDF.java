package com.pl.metalmachines.utils.pdfUtils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.pl.metalmachines.R;
import com.pl.metalmachines.dao.CategoryDao;
import com.pl.metalmachines.dao.CustomerDao;
import com.pl.metalmachines.dao.DeviceDao;
import com.pl.metalmachines.model.Category;
import com.pl.metalmachines.model.Customer;
import com.pl.metalmachines.model.Device;
import com.pl.metalmachines.model.Service;
import com.pl.metalmachines.model.enums.ServiceType;
import com.pl.metalmachines.settings.CompanyData;
import com.pl.metalmachines.ui.devices.service.enums.EnumValueServiceOperations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProtokolSerwisowyCreatePDF {

    private static final int PERMISSION_STORAGE_CODE = 1000;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    public static File createPdfServiceProtocol(Activity activity, Service service, Bitmap signatureUser, Bitmap signatureCustomer, String nameOfTheSignatory) throws SQLException, ClassNotFoundException, IOException {

        Customer customer = CustomerDao.findCustomerById(service.getIdCustomer());
        Device device = DeviceDao.findDeviceById(service.getIdDevice());
        Category category = CategoryDao.findCategoryByName(device.categoryName);

        verifyStoragePermissions(activity);

        Paint paintBlackBorder = new Paint();
        paintBlackBorder.setStyle(Paint.Style.STROKE);
        paintBlackBorder.setColor(Color.BLACK);

        TextPaint textBig = new TextPaint();
        textBig.setColor(Color.BLACK);
        textBig.setTextSize(20);

        TextPaint textNormal = new TextPaint();
        textNormal.setColor(Color.BLACK);
        textNormal.setTextSize(14);

        TextPaint textMiddle = new TextPaint();
        textMiddle.setColor(Color.BLACK);
        textMiddle.setTextSize(10);

        TextPaint textSmall = new TextPaint();
        textSmall.setColor(Color.BLACK);
        textSmall.setTextSize(8);

        //  A4 sized page 595 x 842
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

// CREATE PAGE 1 //
        int y = 22;
        ///// HEADER FRAGMENT /////
        y = drawHead(activity, canvas, y);
        //
        if (service.getNumber() != null) {
            canvas.drawText("PROTOKÓŁ SERWISOWY Nr " + service.getNumber(), 30, y + 30, textBig);
        } else {
            canvas.drawText("PROTOKÓŁ SERWISOWY Nr ........................", 30, y + 30, textBig);
        }
        y += 26;
        canvas.drawRect(20, y + 20, 300, y + 110, paintBlackBorder);
        canvas.drawText("Zlecający / adres", 25, y + 30, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 30, y + 35, 295, y + 105, customer.getFullName() + " \n" + customer.getStreetAddress() + " \n" + customer.getZipCode() + " " + customer.getCity() + " \nNIP: " + customer.getNip());
        canvas.drawRect(300, y + 20, 580, y + 50, paintBlackBorder);
        canvas.drawText("Data", 305, y + 30, textSmall);
        canvas.drawText(service.getDate(), 310, y + 45, textNormal);
        canvas.drawRect(300, y + 50, 580, y + 80, paintBlackBorder);
        canvas.drawText("Rodzaj maszyny", 305, y + 60, textSmall);
        canvas.drawText(device.getName(), 310, y + 75, textNormal);
        canvas.drawRect(300, y + 80, 580, y + 110, paintBlackBorder);
        canvas.drawText("Nr seryjny", 305, y + 90, textSmall);
        canvas.drawText(device.getSerialNumber(), 310, y + 105, textNormal);
        y += 110;
        canvas.drawRect(20, y, 580, y + 30, paintBlackBorder);
        canvas.drawText("Rodzaj czynności", 25, y + 10, textSmall);
        String serviceType = "";
        for (ServiceType type : service.getServiceType()) {
            serviceType += type.getName() + ", ";
        }
        canvas.drawText(serviceType, 30, y + 25, textNormal);
        y += 30;
        canvas.drawRect(20, y, 580, y + 30, paintBlackBorder);
        canvas.drawText("Koszt", 25, y + 10, textSmall);
        canvas.drawText(service.getPaymentType().toString(), 30, y + 25, textNormal);
        y += 30;
        ///// ZAKRES ZLECENIA OPIS USTERKI /////
        canvas.drawRect(20, y, 580, y + 87, paintBlackBorder); //Rodzaj maszyny
        canvas.drawText("Zakres zlecenia / opis usterki", 23, y + 12, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 35, y + 17, 575, y + 85, service.getFaultDescription());
        y += 87;
        ///// ZAKRES PRAC /////
        canvas.drawRect(20, y, 580, y + 87, paintBlackBorder); //Rodzaj maszyny
        canvas.drawText("Zakres prac", 23, y + 12, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 35, y + 17, 575, y + 85, service.getRangeOfWorks());
        y += 87;
        ///// ZUŻYTE MATERIALY /////
        canvas.drawRect(20, y, 580, y + 87, paintBlackBorder); //Rodzaj maszyny
        canvas.drawText("Zużyte materiały", 23, y + 12, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 35, y + 17, 575, y + 85, service.getMaterialsUsed());
        y += 87;
        ///// UWAGI / ZALECENIA /////
        canvas.drawRect(20, y, 580, y + 87, paintBlackBorder); //Rodzaj maszyny
        canvas.drawText("Uwagi / zalecenia", 23, y + 12, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 35, y + 17, 575, y + 85, service.getComments());
        y += 87;
        ///// ROZLICZENIE FRAGMENT /////
        canvas.drawRect(20, y, 580, y + 25, paintBlackBorder);
        canvas.drawText("ROZLICZENIE", 240, y + 19, textBig);
        canvas.drawRect(20, y + 25, 300, y + 40, paintBlackBorder);
        canvas.drawRect(300, y + 25, 580, y + 40, paintBlackBorder);
        canvas.drawText("Rozpoczęcie prac", 125, y + 35, textSmall);
        canvas.drawText("Zakończenie prac", 407, y + 35, textSmall);
        canvas.drawRect(20, y + 40, 300, y + 60, paintBlackBorder);
        canvas.drawRect(300, y + 40, 580, y + 60, paintBlackBorder);
        canvas.drawText(service.getStartDate() + " (" + service.getStartTime() + ")", 100, y + 55, textNormal);
        canvas.drawText(service.getEndDate() + " (" + service.getEndTime() + ")", 377, y + 55, textNormal);
        canvas.drawRect(20, y + 60, 206, y + 75, paintBlackBorder);
        canvas.drawRect(206, y + 60, 394, y + 75, paintBlackBorder);
        canvas.drawRect(394, y + 60, 580, y + 75, paintBlackBorder);
        canvas.drawText("Czas pracy razem", 80, y + 70, textSmall);
        canvas.drawText("Dojazd razem", 275, y + 70, textSmall);
        canvas.drawText("Nocleg razem", 460, y + 70, textSmall);
        canvas.drawRect(20, y + 75, 206, y + 95, paintBlackBorder);
        canvas.drawRect(206, y + 75, 394, y + 95, paintBlackBorder);
        canvas.drawRect(394, y + 75, 580, y + 95, paintBlackBorder);
        canvas.drawText(service.getWorkingTime() + " rbh", 95, y + 90, textNormal);
        canvas.drawText(service.getDriveDistance() + " km", 275, y + 90, textNormal);
        canvas.drawText(service.getDaysAtHotel() + " szt.", 465, y + 90, textNormal);
        y += 95;
        ////// SIGNATURE FRAGMENT //////
        y += 3;
        drawSignature(service, signatureUser, signatureCustomer, nameOfTheSignatory, paintBlackBorder, textMiddle, textSmall, canvas, y);
        float x;
        document.finishPage(page);

// CREATE PAGE 2 //
        if (service.getDeviceInspectionReport() != null) {
            pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();

            y = 22;
            ///// HEADER FRAGMENT /////
            y = drawHead(activity, canvas, y);
            ///// TITLE FRAGMENT //////
            canvas.drawText("PROTOKÓŁ PRZEGLĄDU SERWISOWEGO ", 30, y + 30, textBig);
            canvas.drawText(category.getName()+" ("+device.getName()+", S/N:"+device.getSerialNumber()+")", 30, y + 49, textNormal);
            if (service.getNumber() != null) {
                canvas.drawText("Załącznik do protokołu serwisowego nr " + service.getNumber(), 30, y + 67, textNormal);
            } else {
                canvas.drawText("Załącznik do protokołu serwisowego nr ........................", 30, y + 67, textNormal);
            }
            y += 83;
            ///////////////
            canvas.drawRect(20,y,160,y+20,paintBlackBorder);
            canvas.drawText("Data",77,y+15,textNormal);
            canvas.drawRect(160,y,300,y+20,paintBlackBorder);
            canvas.drawText(service.getDate(),193,y+15,textNormal);
            canvas.drawRect(300,y,440,y+20,paintBlackBorder);
            canvas.drawText("Okres",350,y+15,textNormal);
            canvas.drawRect(440,y,580,y+20,paintBlackBorder);
            canvas.drawText(service.getDeviceInspectionReport().getPeriodicService(),480,y+15,textNormal);
            y+=20;
            ////////////////
            canvas.drawRect(20,y,500,y+20,paintBlackBorder);
            canvas.drawText("CZYNNOŚCI SERWISOWE",180,y+15,textNormal);
            canvas.drawRect(500,y,580,y+20,paintBlackBorder);
            canvas.drawText("STAN",523,y+15,textNormal);
            y+=20;
            //// czynności serwisowe
            LinkedHashMap<String, String> serviceOperationsMap = service.getDeviceInspectionReport().getServiceOperations();

            for (java.util.Map.Entry value : serviceOperationsMap.entrySet()) {
                canvas.drawRect(20,y,500,y+13,paintBlackBorder);
                String extendedValue="";
                if (value.getValue().toString().contains("@dodatkowePola")){
                    extendedValue= value.getValue().toString().replaceAll(".*@dodatkowePola","").replaceAll("\\+"," ").replaceAll(";","}{");}
                drawMultiLineEllipsizedText(canvas, textSmall, 25, y + 2, 538, y + 15, value.getKey().toString()+extendedValue);
                Log.i(TAG, "createPdfServiceProtocol:mnmnmnmnmnmmnmnm "+value.getValue().toString().replaceAll(" @dodatkowePola.*", ""));
                if (value.getValue().toString().replaceAll(" @dodatkowePola.*", "").equals(EnumValueServiceOperations.POPRAWNE + "")) {
                    canvas.drawText("✓", 538, y + 11, textNormal);
                }
                if (value.getValue().toString().replaceAll(" @dodatkowePola.*", "").equals(EnumValueServiceOperations.NIEPOPRAWNE + "")) {
                    canvas.drawText("✕   !", 538, y + 11, textNormal);
                }

                canvas.drawRect(500, y, 580, y + 13, paintBlackBorder);
                y += 13;
            }
            canvas.drawRect(20, y, 580, y + 80, paintBlackBorder);
            canvas.drawText("UWAGI :", 25, y + 10, textSmall);
            drawMultiLineEllipsizedText(canvas, textMiddle, 35, y + 15, 575, y + 80, service.getDeviceInspectionReport().getDescription());
            y+=80;
            ////// SIGNATURE FRAGMENT //////
            y += 3;
            drawSignature(service, signatureUser, signatureCustomer, nameOfTheSignatory, paintBlackBorder, textMiddle, textSmall, canvas, y);

            document.finishPage(page);
        }
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().toString() + "/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "ProtokolSerwisowy.pdf";
        File filePath = new File(targetPdf);
        document.writeTo(new FileOutputStream(filePath));
        // close the document
        document.close();

        return filePath;
    }

    public static int drawHead(Activity activity, Canvas canvas, int y){

        Paint paintBlackBorder = new Paint();
        paintBlackBorder.setStyle(Paint.Style.STROKE);
        paintBlackBorder.setColor(Color.BLACK);

        TextPaint textNormal = new TextPaint();
        textNormal.setColor(Color.BLACK);
        textNormal.setTextSize(14);

        TextPaint textMiddle = new TextPaint();
        textMiddle.setColor(Color.BLACK);
        textMiddle.setTextSize(10);

        Bitmap logo = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
        canvas.drawBitmap(logo, null, new RectF(20, y, 100, y+80), null);
        canvas.drawText(CompanyData.COMPANY_NAME, 120, y+10, textMiddle);
        canvas.drawText(CompanyData.COMPANY_ADDRESS_STREET + ", " + CompanyData.COMPANY_ADDRESS_ZIP_CODE + " " + CompanyData.COMPANY_ADDRESS_CITY, 120, y+25, textMiddle);

        canvas.drawText("NIP : " + CompanyData.COMPANY_NIP, 120, y+40, textMiddle);
        canvas.drawText("Regon : " + CompanyData.COMPANY_REGON, 120, y+55, textMiddle);
        canvas.drawText("KRS : " + CompanyData.COMPANY_KRS, 120, y+70, textMiddle);
        canvas.drawText("kontakt: ", 450, y+10, textMiddle);
        canvas.drawText(CompanyData.COMPANY_PHONES[0], 495, y+10, textMiddle);
        canvas.drawText(CompanyData.COMPANY_PHONES[1], 495, y+25, textMiddle);
        canvas.drawText("serwis: ", 450, y+40, textMiddle);
        canvas.drawText(CompanyData.COMPANY_PHONES_SERVICE[0], 495, y+40, textMiddle);
        paintBlackBorder.setColor(Color.parseColor("#D12323"));
        canvas.drawLine(120, y+75, 387, y+75, paintBlackBorder);
        paintBlackBorder.setColor(Color.BLACK);
        textNormal.setColor(Color.parseColor("#D12323"));
        canvas.drawText("TWÓJ DOSTAWCA LASERÓW", 389, y+75, textNormal);
        textNormal.setColor(Color.BLACK);

        return y+75;
    }

    public static void drawSignature(Service service, Bitmap signatureUser, Bitmap signatureCustomer, String nameOfTheSignatory, Paint paintBlackBorder, TextPaint textMiddle, TextPaint textSmall, Canvas canvas, int y) {
        float x;
        if (signatureUser != null) {
            x = signatureUser.getHeight() / 80;
            canvas.drawBitmap(signatureCustomer, null, new RectF(40, y, signatureCustomer.getWidth() / x + 40, y + 80), null);
        }
        if (signatureCustomer != null) {
            x = signatureUser.getHeight() / 80;
            canvas.drawBitmap(signatureUser, null, new RectF(320, y, signatureUser.getWidth() / x + 320, y + 80), null);
            canvas.drawText(nameOfTheSignatory, 90, y + 30, textMiddle);
        }
        canvas.drawLine(70, y + 70, 250, y + 70, paintBlackBorder);
        canvas.drawText("Potwierdzam wykonanie prac", 110, y + 80, textSmall);
        canvas.drawText(service.getDate(),278,y+70,textSmall);
        canvas.drawLine(350, y + 70, 530, y + 70, paintBlackBorder);
        canvas.drawText("Podpis wykonawcy", 405, y + 80, textSmall);
        drawMultiLineEllipsizedText(canvas, textMiddle, 390, y + 30, 530, y + 70, service.getUser());
    }

    public static void drawMultiLineEllipsizedText(final Canvas _canvas, final TextPaint _textPaint, final float _left,
                                                   final float _top, final float _right, final float _bottom, final String _text) {
        final float height = _bottom - _top;

        final StaticLayout measuringTextLayout = new StaticLayout(_text, _textPaint, (int) Math.abs(_right - _left),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int line = 0;
        final int totalLineCount = measuringTextLayout.getLineCount();
        for (line = 0; line < totalLineCount; line++) {
            final int lineBottom = measuringTextLayout.getLineBottom(line);
            if (lineBottom > height) {
                break;
            }
        }
        line--;

        if (line < 0) {
            return;
        }

        int lineEnd;
        try {
            lineEnd = measuringTextLayout.getLineEnd(line);
        } catch (Throwable t) {
            lineEnd = _text.length();
        }
        String truncatedText = _text.substring(0, Math.max(0, lineEnd));

        if (truncatedText.length() < 3) {
            return;
        }

        if (truncatedText.length() < _text.length()) {
            truncatedText = truncatedText.substring(0, Math.max(0, truncatedText.length() - 3));
            truncatedText += "...";
        }
        final StaticLayout drawingTextLayout = new StaticLayout(truncatedText, _textPaint, (int) Math.abs(_right
                - _left), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        _canvas.save();
        _canvas.translate(_left, _top);
        drawingTextLayout.draw(_canvas);
        _canvas.restore();
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
