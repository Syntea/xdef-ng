package org.xdef.sample;

import org.xdef.XDef;
import org.xdef.translator.SupportedDataType;

import javax.json.JsonValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//TODO: Add real example
public class JavaSample {

    public static void main(String[] args) throws IOException {
        XDef xdef = new XDef.Builder()
                .addRuleSource(SupportedDataType.XML, new File("example.xdef"))
                .build();

        @SuppressWarnings("unused")
        JsonValue jsonValue = xdef.parseXMLtoJSON("example", new FileInputStream(new File("example.xml")), null, System.err);
    }
}
