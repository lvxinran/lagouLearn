package com.lxr.config;

import com.lxr.pojo.Configuration;
import com.lxr.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class XMLMappedBuilder {
    private Configuration configuration;
    public XMLMappedBuilder(Configuration configuration){
        this.configuration = configuration;
    }
    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectNodes = rootElement.selectNodes("//select");
        for (Element selectNode : selectNodes) {
            String id = selectNode.attributeValue("id");
            String resultType = selectNode.attributeValue("resultType");
            String paramterType = selectNode.attributeValue("paramterType");
            String sql = selectNode.getTextTrim();
            MappedStatement statement = new MappedStatement();
            statement.setId(id);
            statement.setResultType(resultType);
            statement.setParamterType(paramterType);
            statement.setSql(sql);
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,statement);
        }
        List<Element> updateNodes = new ArrayList<>();
        updateNodes.addAll(rootElement.selectNodes("//update"));
        updateNodes.addAll(rootElement.selectNodes("//delete"));
        updateNodes.addAll(rootElement.selectNodes("//insert"));

        for (Element node : updateNodes) {
            String id = node.attributeValue("id");
            String paramterType = node.attributeValue("paramterType");
            String sql = node.getTextTrim();
            MappedStatement statement = new MappedStatement();
            statement.setId(id);
            statement.setParamterType(paramterType);
            statement.setSql(sql);
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,statement);
        }
    }
}
