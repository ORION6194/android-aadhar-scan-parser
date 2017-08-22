package com.sadtech.aadharcardscanparser;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by ISRO on 12/9/2016.
 */
public class AadharDAO {

    public static final int FATHER_SPOUSE_NAME_FIRST_PART_REMOVAL = 4;

    public static AadharDetail getAadharDetailFromXML(String xml) {
        xml = fixAadharXMLString(xml);
        XmlPullParserFactory xmlFactoryObject = null;
        AadharDetail aadharDetail = new AadharDetail();

        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser aadharparser = xmlFactoryObject.newPullParser();
            aadharparser.setInput(new StringReader(xml));

            int event = aadharparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = aadharparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name != null && name.equals("PrintLetterBarcodeData")) {
                            aadharDetail.setAadharNumber(aadharparser.getAttributeValue(null, "uid"));
                            aadharDetail.setName(aadharparser.getAttributeValue(null, "name"));

                            String yob = aadharparser.getAttributeValue(null, "yob");

                            Calendar c = Calendar.getInstance();
                            c.set(Integer.parseInt(yob), 1, 1);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            aadharDetail.setDob(sdf.format(c.getTime()));

                            String gender = aadharparser.getAttributeValue(null, "gender");
                            if (Objects.equals(gender, "M") || Objects.equals(gender, "MALE"))
                                gender = "Male";
                            else if (Objects.equals(gender, "F") || Objects.equals(gender, "FEMALE"))
                                gender = "Female";

                            aadharDetail.setGender(gender);
                            Log.d("AadharDAO", "Gender: " + gender);
                            aadharDetail.setHouse(getAttributeValue(aadharparser, "house"));
                            aadharDetail.setStreet(getAttributeValue(aadharparser, "street"));
                            aadharDetail.setLm(getAttributeValue(aadharparser, "lm"));
                            aadharDetail.setPo(getAttributeValue(aadharparser, "po"));
                            aadharDetail.setDist(getAttributeValue(aadharparser, "dist"));
                            aadharDetail.setSubdist(getAttributeValue(aadharparser, "subdist"));
                            aadharDetail.setState(getAttributeValue(aadharparser, "state"));
                            aadharDetail.setPincode(getAttributeValue(aadharparser, "pc"));
                            aadharDetail.setVtc(getAttributeValue(aadharparser, "vtc"));
                            aadharDetail.setAddress(getAttributeValue(aadharparser, "house") +" "+
                                    getAttributeValue(aadharparser, "street") +" "+
                                    getAttributeValue(aadharparser, "lm") +" "+
                                    getAttributeValue(aadharparser, "po") +" "+
                                    getAttributeValue(aadharparser, "dist") +" "+
                                    getAttributeValue(aadharparser, "subdist") +" "+
                                    getAttributeValue(aadharparser, "state") +" "+
                                    getAttributeValue(aadharparser, "pc")
                            );
                        }
                        break;
                }
                event = aadharparser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aadharDetail;
    }

    private static String getAttributeValue(XmlPullParser aadharParser, String attributeName) {
        String value = aadharParser.getAttributeValue(null, attributeName);
        if (value == null)
            value = "";
        return value;
    }

    private static String fixAadharXMLString(String xml) {
        if (xml.startsWith("&lt;?xml")) {
            int firstDeclarationTagEnd = xml.indexOf("&gt;");
            return xml.substring(firstDeclarationTagEnd + 1);
        }
        return xml;
    }
}