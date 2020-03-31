package com.lxr.config;

import com.lxr.io.Resources;
import com.lxr.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class XMLConfigBuilder {
    private Configuration configuration;

    public XMLConfigBuilder(){
        configuration = new Configuration();
    }

    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("userName"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);

        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element mapperElement : mapperList) {
            String path = mapperElement.attributeValue("resource");
            InputStream mapperStream = Resources.getResourceAsStream(path);
            XMLMappedBuilder xmlMappedBuilder = new XMLMappedBuilder(configuration);
            xmlMappedBuilder.parse(mapperStream);
        }
        return configuration;
    }
}
