package cz.syntea.xdef.translator.data.json.model

import cz.syntea.xdef.model.DataNode

/**
 * TODO CLASS_DESCRIPTION
 *
 * @author [Filip Šmíd](mailto:smidfil3@fit.cvut.cz)
 */

sealed class JsonDataNode : DataNode

data class JsonObjectDataNode(
    override val name: String,
    override val parent: DataNode?
) : JsonDataNode() {

    override var value: String? = null
    override val attributes: Map<String, String> = emptyMap()

    override var children: List<DataNode> = emptyList()
        internal set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "JsonObjectDataNode(name='$name', value=$value, parent=${parent?.name}, attributes=$attributes, children=$children)"
    }
}

data class JsonArrayDataNode(
    override val name: String,
    override val parent: DataNode?
) : JsonDataNode() {

    override var value: String? = null
    override val attributes: Map<String, String> = emptyMap()

    override var children: List<DataNode> = emptyList()
        internal set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "JsonArrayDataNode(name='$name', value=$value, parent=${parent?.name}, attributes=$attributes, children=$children)"
    }
}

data class JsonBooleanDataNode(
    override val name: String,
    override var value: String? = null,
    override val parent: DataNode?
) : JsonDataNode() {

    override val attributes: Map<String, String> = emptyMap()
    override val children: List<DataNode> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "JsonBooleanDataNode(name='$name', value=$value, parent=${parent?.name}, attributes=$attributes, children=$children)"
    }
}

data class JsonNumberDataNode(
    override val name: String,
    override var value: String? = null,
    override val parent: DataNode?
) : JsonDataNode() {

    override val attributes: Map<String, String> = emptyMap()
    override val children: List<DataNode> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "JsonNumberDataNode(name='$name', value=$value, parent=${parent?.name}, attributes=$attributes, children=$children)"
    }
}

data class JsonStringDataNode(
    override val name: String,
    override var value: String? = null,
    override val parent: DataNode?
) : JsonDataNode() {

    override val attributes: Map<String, String> = emptyMap()
    override val children: List<DataNode> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataNode) return false

        if (name != other.name) return false
        if (value != other.value) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }

    override fun toString(): String {
        return "JsonStringDataNode(name='$name', value=$value, parent=${parent?.name}, attributes=$attributes, children=$children)"
    }
}
