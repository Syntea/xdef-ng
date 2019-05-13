package org.xdef.translator.definition

import io.kotlintest.shouldNotThrow
import org.xdef.translator.JSON_ROOT_NODE_NAME
import org.xdef.translator.dataset.json.*
import org.xdef.translator.definition.xml.XmlDefinitionTranslator.Companion.X_SCRIPT_NAME
import org.xdef.translator.definition.xml.XmlDefinitionTranslator.Companion.X_SCRIPT_NAMESPACE
import org.xdef.translator.definition.xml.XmlDefinitionTranslator.Companion.X_SCRIPT_PREFIX
import org.xmlunit.assertj.XmlAssert

/**
 * Integration tests for translation X-definition document from JSON to XML
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */
internal class FromJsonToXmlTest : BaseDefinitionTranslatorTest() {

    private val minimalDefObjectAsXml =
        """<$JSON_ROOT_NODE_NAME xmlns:xd="$X_SCRIPT_NAMESPACE" $X_SCRIPT_PREFIX:$X_SCRIPT_NAME="required"/>"""
    private val minimalDefArrayAsXml = """<$JSON_ROOT_NODE_NAME>optional; onTrue xyz</$JSON_ROOT_NODE_NAME>"""
    private val minimalDefValueAsXml = """<$JSON_ROOT_NODE_NAME>optional; onTrue xyz</$JSON_ROOT_NODE_NAME>"""
    private val simpleDefObjectAsXml =
        """<$JSON_ROOT_NODE_NAME xmlns:xd="$X_SCRIPT_NAMESPACE"
            |$X_SCRIPT_PREFIX:$X_SCRIPT_NAME="1..*"
            |a="1"
            |b="c"/>""".trimMargin()
    private val simpleDefArrayAsXml =
        """<$JSON_ROOT_NODE_NAME>
            |required
            |1
            |true</$JSON_ROOT_NODE_NAME>""".trimMargin()
    private val complexMixedAsXml = """<$JSON_ROOT_NODE_NAME xmlns:xd="$X_SCRIPT_NAMESPACE"
        $X_SCRIPT_PREFIX:$X_SCRIPT_NAME="0..200"
        _id="required string(10)"
        index="optional int()"
        guid="string(20)"
        isActive="false"
        balance=",790.49"
        picture="required url() onTrue println(&quot;Yes&quot;)"
        age="37"
        eyeColor="green"
        email="1..1 email()"
        phone="required telNumber()"
        about="string()"
        latitude="optional latitude()"
        longitude="optional longitude()">
  <name $X_SCRIPT_PREFIX:$X_SCRIPT_NAME="required" first="string(20)" last="string(20)" />
  <tags>1..* string(10, 20) onAbsence XYZ</tags>
  <friends>
    <friends.0 $X_SCRIPT_PREFIX:$X_SCRIPT_NAME="1..*" id="0" name="Davis Montgomery" />
  </friends>
</$JSON_ROOT_NODE_NAME>"""

    init {
        "definition" - {
            "minimal" - {
                "object" { jsonAssertEquals(minimalDefObject, minimalDefObjectAsXml) }
                "array" { jsonAssertEquals(minimalDefArray, minimalDefArrayAsXml) }
                "value" { jsonAssertEquals(minimalDefValue, minimalDefValueAsXml) }
            }
            "simple" - {
                "object" { jsonAssertEquals(simpleDefObject, simpleDefObjectAsXml) }
                "array" { jsonAssertEquals(simpleDefArray, simpleDefArrayAsXml) }
            }
            "complex" - {
                "mixed" { jsonAssertEquals("complexMixedDefinition".jsonResToString(), complexMixedAsXml) }
            }
        }
    }

    private fun jsonAssertEquals(json: String, xml: String) {
        val document = jsonTranslator.readDefinition(autoClose(json.reader()))
        xmlTranslator.writeDefinition(document, outputWriter)

        shouldNotThrow<AssertionError> {
            XmlAssert.assertThat(outputWriter.toString())
                .and(xml)
                .ignoreWhitespace()
                .areSimilar()
        }
    }

    private fun String.jsonResToString() = "$this.jdef".resToString()

    private fun String.resToDefinition() = "$this.jdef".resToDefinition(jsonTranslator)
}
