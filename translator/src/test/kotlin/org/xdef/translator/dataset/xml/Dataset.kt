package org.xdef.translator.dataset.xml


const val minimalElement = "<A/>"
const val minimalText = "<T>minimalElement text</T>"
const val minimalWithComment = """
<!-- Comment -->
<A/>
"""
const val minimalEmptyWithComment = """
<A><!-- Comment --></A>
"""

const val simpleElement = """
<root
    a="a"
    b="b"
    c="c"
/>
"""

const val simpleElementWithComment = """
<root
    a="a"
    b="b"
    c="c"
>
<!-- ssss -->

</root>
"""

const val simpleWithNamespace = """
<root>

<h:table xmlns:h="http://www.w3.org/TR/html4/">
  <h:tr>
    <h:td>Apples</h:td>
    <h:td>Bananas</h:td>
  </h:tr>
</h:table>

<f:table xmlns:f="https://www.w3schools.com/furniture">
  <f:name>African Coffee Table</f:name>
  <f:width>80</f:width>
  <f:length>120</f:length>
</f:table>

</root>
"""