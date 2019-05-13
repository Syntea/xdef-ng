package org.xdef.translator.dataset.xml


const val minimalElement = "<A/>"
const val minimalText = "<T>minimalElement text</T>"
const val minimalEmptyAndComment = """
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
    c="c"/>
"""

const val simpleElementWithComment = """
<!-- xxx -->
<root
    a="a"
    b="b"
    c="c"
><!-- yyy --></root>
<!-- zzz -->
"""

const val simpleWithNamespace = """
<root>

<h:table xmlns:h="http://www.w3.org/TR/html4/"><h:tr/></h:table>

<f:table xmlns:f="https://www.w3schools.com/furniture"><f:name/></f:table>

</root>
"""

const val simpleDefElement = """
<root xmlns:xd="http://www.syntea.cz/xdef/3.1"
    a="a"
    b="b"
    xd:script="required"
    c="c"/>
"""
