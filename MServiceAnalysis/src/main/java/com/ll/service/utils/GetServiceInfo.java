package com.ll.service.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.ll.service.bean.MPathInfo;
import com.ll.common.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.*;

/**
 * Created by Lei on 2019/11/29 15:45
 * @author
 */
public class GetServiceInfo {

    private static Logger logger = LogManager.getLogger(GetSourceCode.class);

    public static MService getMservice(MPathInfo pathInfo) {
        MService mService = getConfig(pathInfo.getApplicationPath());
        mService.setServiceName(mService.getServiceName().toLowerCase());
        mService.setServiceId(mService.getServiceName() + "_" + mService.getServiceVersion().toCommonStr());
        Map<String, MSvcInterface> map = new HashMap<>();
        for (String s : pathInfo.getControllerListPath()) {
            map.putAll(getServiceInfo(s,mService.getBasePath()));
        }
        mService.setServiceInterfaceMap(map);
        return mService;
    }

    public static MService getConfig(String path) {
        String[] paths = path.split("\\.");
        if ("yml".equals(paths[paths.length - 1]) || "yaml".equals(paths[paths.length - 1])) {
            try {
                return getFromYml(new File(path));
            } catch (IOException e) {
               logger.error(e);
               return null;
            }
        } else {
            return getInfoFromproperties(path);
        }
    }

    public static MService getFromYml(File source) throws IOException {
        MService mService = new MService();
        DumperOptions OPTIONS = new DumperOptions();
        Yaml yaml = new Yaml(OPTIONS);
        Map obj = (Map) yaml.load(new FileReader(source));
        Map server = (Map) obj.get("server");
        if(server == null){
            mService.setPort(8080);
            mService.setBasePath("/");
        }else{
            if (server.get("port") == null) {
                mService.setPort(8080);
            } else {
                mService.setPort((int) server.get("port"));
            }
            if (server.get("servlet") == null) {
                mService.setBasePath("/");
            } else {
                mService.setBasePath(((Map) server.get("servlet")).get("context-path").toString());
            }
        }
        Map spring = (Map) obj.get("spring");
        String serviceNmae = ((Map) spring.get("application")).get("nameQW").toString();
        mService.setServiceName(serviceNmae);
        Map mvf4ms = (Map)obj.get("mvf4ms");
        String version = (String) mvf4ms.get("version");
        MSvcVersion mSvcVersion = MSvcVersion.fromStr(version);
        mService.setServiceVersion(mSvcVersion);
        return mService;
    }


    public static MService getInfoFromproperties(String path) {
        MService mService = new MService();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
            if (properties.get("server.port") == null) {
                mService.setPort(8080);
            } else {
                mService.setPort(Integer.parseInt(properties.getProperty("server.port")));
            }
            mService.setServiceName(properties.getProperty("spring.application.name"));
            if (properties.getProperty("server.context-path") == null) {
                mService.setBasePath("/");
            } else {
                mService.setBasePath(properties.getProperty("server.context-path"));
            }
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return mService;
    }


    /**
     * 根据路径得到接口信息
     *
     * @return Service类，返回该类的信息
     */
    public static Map<String, MSvcInterface> getServiceInfo(String codepath, String contextPath) {
        Map<String, MSvcInterface> map = new HashMap<>();
        CompilationUnit compilationUnit = null;
        File file;
        try {
            file = new File(codepath);
            if (!file.exists()) {
                logger.info ("源码路径错误");
            } else {
                compilationUnit = JavaParser.parse(file);
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }

        if (compilationUnit == null) {
            throw new RuntimeException("Failed to parse java file " + file.getAbsolutePath());
        }

        String[] strings = codepath.split("/");
        String className = strings[strings.length - 1].split("\\.")[0];

        Optional<ClassOrInterfaceDeclaration> cOptional = compilationUnit.getClassByName(className);
        if (cOptional.isPresent()) {
            ClassOrInterfaceDeclaration c = cOptional.get();
            NodeList<AnnotationExpr> annotations = c.getAnnotations();
            // 所有的基础路径
            List<String> pathContexts = getContextPath(contextPath, annotations);
            if (pathContexts.size() == 0) {
                pathContexts.add("/");
            }
            /*得到interface方面*/
            List<MethodDeclaration> methodDeclarationList = c.getMethods();
            // 遍历每一个方法
            for (MethodDeclaration m : methodDeclarationList) {
                MSvcInterface mSvcInterface = new MSvcInterface();
                mSvcInterface.setFunctionName(m.getName().toString());
                mSvcInterface.setReturnType(m.getType().toString());
                List<String> pathurl = new ArrayList<>();
                NodeList<AnnotationExpr> anno = m.getAnnotations();
                List<String> pathContextsFunction = new ArrayList<>();
                for (AnnotationExpr annotationExpr : anno) {
                    List<Node> childNodes = annotationExpr.getChildNodes();
                    String annoName = childNodes.get(0).toString();
                    if ("RequestMapping".equals(annoName) || "GetMapping".equals(annoName) || "PostMapping".equals(annoName) || "DeleteMapping".equals(annoName) || "PutMapping".equals(annoName)) {
                        Node s = childNodes.get(1);
                        List<Node> sUrl = s.getChildNodes();
                        if (sUrl.size() == 0) {
                            String h = s.toString();
                            pathContextsFunction.add(h.substring(1, h.length() - 1));
                        } else {
                            Node node1 = sUrl.get(1);
                            if (node1.getChildNodes().size() == 0) {
                                String h = node1.toString();
                                pathContextsFunction.add(h.substring(1, h.length() - 1));
                            } else {
                                for (Node node : node1.getChildNodes()) {
                                    String h = node.toString();
                                    pathContextsFunction.add(h.substring(1, h.length() - 1));
                                }
                            }
                        }
                    }
                    for (String string1 : pathContexts) {
                        for (String string2 : pathContextsFunction) {
                            String p = string1 + string2;
                            pathurl.add(p.replaceAll("/+", "/"));
                        }
                    }
                    if ("RequestMapping".equals(annoName)) {
                        if (childNodes.size() == 2) {
                            mSvcInterface.setRequestMethod("RequestMethod");
                        } else {
                            String[] requestmethods = childNodes.get(2).toString().split("=");
                            String requestmethod = requestmethods[1].trim();
                            mSvcInterface.setRequestMethod(requestmethod);
                        }
                    } else if ("GetMapping".equals(annoName)) {
                        mSvcInterface.setRequestMethod(" RequestMethod.GET");
                    } else if ("PostMapping".equals(annoName)) {
                        mSvcInterface.setRequestMethod(" RequestMethod.POST");
                    } else if ("DeleteMapping".equals(annoName)) {
                        mSvcInterface.setRequestMethod("RequestMethod.DELETE");
                    } else if ("PutMapping".equals(annoName)) {
                        mSvcInterface.setRequestMethod("RequestMethod.PUT");
                    }
                }
                if (pathurl.size() == 0) {
                    continue;
                }
                /*获取  接口层级的参数*/
                List<MParamer> paramerList = getParamers(m.getParameters());
                mSvcInterface.setParams(paramerList);
                /* 遍历函数内部寻找 版本依赖的调用方法 */
//                Optional<BlockStmt> bodyOptional = m.getBody();
//                if (bodyOptional.isPresent()) {
//                    List<MDependency> dependences = getDependence(bodyOptional.get());
//                    mSvcInterface.setMDependencies(dependences);
//                    for (String string : pathurl) {
//                        mSvcInterface.setPatternUrl(string);
//                        map.put(string, mSvcInterface);
//                    }
//                }
                for (String patternUrl : pathurl) {
                    mSvcInterface.setPatternUrl(patternUrl);
                    map.put(mSvcInterface.getPatternUrl(), new MSvcInterface(mSvcInterface));
                }
                map.put(mSvcInterface.getPatternUrl(), mSvcInterface);
            }
        }
        return map;
    }

    public static List<String> getContextPath(String contextPath,NodeList<AnnotationExpr> annotations){
        List<String> pathContexts = new ArrayList<>();
        for (AnnotationExpr annotationExpr : annotations) {
            List<Node> childNodes = annotationExpr.getChildNodes();
            String annoName = childNodes.get(0).toString();
            if ("RequestMapping".equals(annoName)) {
                // 在此得到路径信息
                Node s = childNodes.get(1);
                if (s.getChildNodes().size() == 0) {
                    String h = s.toString();
                    pathContexts.add(contextPath+h.substring(1, h.length() - 1));
                } else {
                    Node node1 = s.getChildNodes().get(1);
                    if (node1.getChildNodes().size() == 0) {
                        String h = node1.toString();
                        pathContexts.add(contextPath+h.substring(1, h.length() - 1));
                    } else {
                        for (Node node : node1.getChildNodes()) {
                            String h = node.toString();
                            pathContexts.add(contextPath+h.substring(1, h.length() - 1));
                        }
                    }
                }
            }
        }
        return pathContexts;
    }

    public static List<MParamer> getParamers(NodeList<Parameter> parameters){
        List<MParamer> paramerList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            MParamer paramer = new MParamer();
            List<Node> childNodes = parameter.getChildNodes();

            // jump over the parameters without annotation
            if (childNodes.size() != 3) {
                continue;
            }

            paramer.setName(childNodes.get(2).toString());
            paramer.setType(childNodes.get(1).toString());
            Node node = childNodes.get(0);
            List<Node> annoInfo = node.getChildNodes();
            String method = annoInfo.get(0).toString();
            if ("RequestBody".equals(method)) {
                paramer.setMethod(method);
                paramer.setRequestname("实体类");
                paramer.setDefaultObject("");
            } else {
                paramer.setMethod(method);
                String name = annoInfo.get(1).toString();
                if (annoInfo.size() == 2) {
                    String trueName = name.trim().replace("\"", "");
                    paramer.setRequestname(trueName);
                    paramer.setDefaultObject("");
                } else {
                    String trueName = name.split("=")[1].trim().replace("\"", "");
                    String defauleValue = annoInfo.get(2).toString().split("=")[1].trim().replace("\"", "");
                    paramer.setRequestname(trueName);
                    paramer.setDefaultObject(defauleValue);
                }
            }
            paramerList.add(paramer);
        }
        return paramerList;
    }


//    public static List<MDependency> getDependence(BlockStmt blockStmt){
//        List<Node> nodes = blockStmt.getChildNodes();
//        List<MDependency> dependences = new ArrayList<>();
//        for (Node node : nodes) {
//            String string = node.toString();
//            if (string.contains("mSendRequest.sendRequest")) {
//                MDependency mDependency = new MDependency();
//                String[] s = string.substring(string.indexOf("mSendRequest.sendRequest(") + 25, string.indexOf(");")).split(",");
//                String version = s[1].trim().replace("\"", "");
//                MSvcVersion mSvcVersion = new MSvcVersion();
//                String[] versions = version.split("\\.");
//                mSvcVersion.setMainVersionNum(Integer.parseInt(versions[0]));
//                mSvcVersion.setChildVersionNum(Integer.parseInt(versions[1]));
//                mSvcVersion.setFixVersionNum(Integer.parseInt(versions[2]));
//                String requst = s[0].trim().replace("\"", "");
//                String requestService = requst.split("/")[3];
//                List<MSvcVersion> list = new ArrayList<>();
//                list.add(mSvcVersion);
//                mDependency.setServiceName(requestService);
//                mDependency.setPatternUrl(requst);
//                mDependency.setVersions(list);
//                dependences.add(mDependency);
//            }
//        }
//        return dependences;
//    }
}
