package com.lxr;

import com.lxr.classLoader.MyClassLoader;
import com.lxr.core.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public class Bootstrap {
    private int port ;

    private String serverPath = "server.xml";


    private Mapper mapper = new Mapper();

    private Map<String,HttpServlet> servletMap = new HashMap<>();

    private Server server;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
       Bootstrap b = new Bootstrap();
       b.start();
    }
    public void start() throws Exception {
        //读取server.xml配置文件
        loadServer();
        //监听端口
        port = server.getServices().get(0).getConnectors().get(0).getPort();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Minicat 启动======>port:"+port);
        //loadServlet();
        //启动Mapper
        loadMapper();

        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100l;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
//        while(true){
//            Socket socket = serverSocket.accept();
//            OutputStream outputStream = socket.getOutputStream();
//            String data = "Hello Minicat";
//            String responseText = com.lxr.HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            outputStream.write(responseText.getBytes());
//            socket.close();
//        }

//        while(true){
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//            if(servletMap.get(request.getUrl())==null){
//                response.outputHtml(request.getUrl());
//            }else{
//                servletMap.get(request.getUrl()).service(request,response);
//            }
//            socket.close();
//        }
        while(true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,mapper);
//            requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }
    }

    private void loadServlet(String baseName,Context context) throws Exception {
        File file = new File(baseName+"\\"+context.getName()+"\\"+"web.xml");
//        this.getClass().getClassLoader().getResourceAsStream("web.xml");
//        InputStream inputStream = new FileInputStream(file);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.selectNodes("//servlet");

        for(int i = 0 ; i <elements.size();i++){
            Element element = elements.get(i);
            Element nameNode = (Element)element.selectSingleNode("servlet-name");
            String servletName = nameNode.getStringValue();
            Element classNode = (Element)element.selectSingleNode("servlet-class");
            String servletClass= classNode.getStringValue();

            Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
            String url = servletMapping.selectSingleNode("url-pattern").getStringValue();
//            servletMap.put(url, (HttpServlet) Class.forName(servletClass).newInstance());
            Wrapper wrapper = new Wrapper();
            wrapper.setBasePath(baseName);
            wrapper.setContext(context);
            wrapper.setUrl(url);
//            classLoader.loadClass(servletClass).newInstance()
            wrapper.setServlet(servletClass);
            context.addWrapper(wrapper);
        }
    }

    private void loadServer() throws DocumentException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(serverPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Server server = new StandardServer();
        List<Element> serviceList = rootElement.selectNodes("//Service");
        for(Element service:serviceList){
            Service standardService = new StandardService();
            List<Element> connectorList = service.selectNodes("//Connector");
            for(Element connectorNode:connectorList){
                String port = connectorNode.attributeValue("port");
                Connector connector = new Connector();
                connector.setPort(Integer.parseInt(port));
                standardService.addConnector(connector);
                Element engine = (Element) service.selectSingleNode("//Engine");
                if(engine!=null){
                    StandardEngine standardEngine = new StandardEngine();
                    standardService.setEngine(standardEngine);
                    List<Element> hosts = engine.selectNodes("//Host");
                    for(Element host:hosts) {
                        Host standardHost = new StandardHost();
                        standardHost.setAppBase(host.attributeValue("appBase"));
                        standardEngine.addHost(standardHost);
                        mapper.addHosts(standardHost);
                    }
                }
            }
            server.addService(standardService);
        }

        this.server = server;
    }

    public void loadMapper() throws  Exception{
        List<Host> hosts = mapper.getHosts();
        for(Host host:hosts){
            String appBase = host.getAppBase();
            File file = new File(appBase);
            File[] files = file.listFiles();
            for(File contextFile:files){
                Context context = new Context();
                context.setName(contextFile.getName());
                loadServlet(appBase,context);
                host.addContext(context);
            }
        }
        System.out.println();
    }

}
