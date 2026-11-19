package com.abc.sid.notifsrvc.engine.mapping;

import com.abc.sid.notifsrvc.common.constants.AppConstants;
import com.abc.sid.notifsrvc.common.exception.XmlTransformationException;
import com.abc.sid.notifsrvc.common.util.DotPathResolver;
import com.abc.sid.notifsrvc.common.util.SafeSpelEvaluator;
import com.ctc.wstx.stax.WstxOutputFactory;
import org.springframework.expression.Expression;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Thread-safe, zero-intermediate-DOM XML streaming mapper backed by Woodstox StAX.
 * Evaluates pre-compiled SpEL conditions (when / switch) and uses a lazy ancestor stack
 * to suppress empty parent XML containers automatically.
 */
public class CompiledYamlXmlMapper {

    public record SwitchBranch(Expression whenCondition, Expression valueExpression) {}

    public record XmlNodeAst(
            String name,
            Expression whenCondition,
            String valueFromPath,
            Expression literalValueExpr,
            List<SwitchBranch> switchBranches,
            Map<String, String> attributePaths,
            List<XmlNodeAst> children
    ) {}

    private final WstxOutputFactory xmlOutputFactory;
    private final String targetNamespace;
    private final XmlNodeAst rootNode;

    public CompiledYamlXmlMapper(WstxOutputFactory xmlOutputFactory, String targetNamespace, XmlNodeAst rootNode) {
        this.xmlOutputFactory = xmlOutputFactory;
        this.targetNamespace = targetNamespace;
        this.rootNode = rootNode;
    }

    public byte[] transformToXmlBytes(Map<String, Object> payload) {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(AppConstants.DEFAULT_XML_BUFFER_BYTES);
        XMLStreamWriter writer = null;
        try {
            writer = xmlOutputFactory.createXMLStreamWriter(outBuffer, StandardCharsets.UTF_8.name());
            writer.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");

            Deque<XmlNodeAst> pendingAncestors = new ArrayDeque<>(16);
            writeNodeRecursive(rootNode, payload, writer, pendingAncestors, true);

            writer.writeEndDocument();
            writer.flush();
            return outBuffer.toByteArray();
        } catch (Exception e) {
            throw new XmlTransformationException("Failed to stream XML via Woodstox StAX", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException ignored) {
                }
            }
        }
    }

    private void writeNodeRecursive(
            XmlNodeAst node,
            Map<String, Object> payload,
            XMLStreamWriter writer,
            Deque<XmlNodeAst> pendingAncestors,
            boolean isRoot) throws XMLStreamException {

        // 1. Evaluate node guard condition ('when'); skip entire subtree if false
        if (node.whenCondition() != null && !SafeSpelEvaluator.evaluateBoolean(node.whenCondition(), payload)) {
            return;
        }

        // 2. Container node: push onto pendingAncestors so tag is only emitted if a child leaf produces output
        if (node.children() != null && !node.children().isEmpty()) {
            pendingAncestors.addLast(node);
            for (XmlNodeAst child : node.children()) {
                writeNodeRecursive(child, payload, writer, pendingAncestors, false);
            }
            if (!pendingAncestors.isEmpty() && pendingAncestors.peekLast() == node) {
                pendingAncestors.removeLast(); // All children were skipped/empty -> suppress empty parent tag
            } else {
                writer.writeEndElement();
            }
            return;
        }

        // 3. Leaf node: resolve text value
        String leafValue = resolveLeafValue(node, payload);
        if (leafValue == null || leafValue.isEmpty()) {
            return;
        }

        // 4. Flush any deferred ancestor tags before writing this leaf element
        while (!pendingAncestors.isEmpty()) {
            XmlNodeAst ancestor = pendingAncestors.removeFirst();
            writer.writeStartElement(ancestor.name());
            if (ancestor == rootNode && targetNamespace != null && !targetNamespace.isBlank()) {
                writer.writeDefaultNamespace(targetNamespace);
            }
        }

        // 5. Write leaf element, attributes, and characters
        writer.writeStartElement(node.name());
        if (isRoot && targetNamespace != null && !targetNamespace.isBlank()) {
            writer.writeDefaultNamespace(targetNamespace);
        }
        if (node.attributePaths() != null) {
            for (Map.Entry<String, String> attr : node.attributePaths().entrySet()) {
                Object attrVal = DotPathResolver.resolve(payload, attr.getValue());
                if (attrVal != null) {
                    writer.writeAttribute(attr.getKey(), attrVal.toString());
                }
            }
        }
        writer.writeCharacters(leafValue);
        writer.writeEndElement();
    }

    private String resolveLeafValue(XmlNodeAst node, Map<String, Object> payload) {
        if (node.valueFromPath() != null) {
            Object val = DotPathResolver.resolve(payload, node.valueFromPath());
            return val != null ? val.toString() : null;
        }
        if (node.literalValueExpr() != null) {
            Object val = SafeSpelEvaluator.evaluateValue(node.literalValueExpr(), payload);
            return val != null ? val.toString() : null;
        }
        if (node.switchBranches() != null) {
            for (SwitchBranch branch : node.switchBranches()) {
                if (branch.whenCondition() == null || SafeSpelEvaluator.evaluateBoolean(branch.whenCondition(), payload)) {
                    Object val = SafeSpelEvaluator.evaluateValue(branch.valueExpression(), payload);
                    return val != null ? val.toString() : null;
                }
            }
        }
        return null;
    }
}
