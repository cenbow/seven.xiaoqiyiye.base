package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StartUp {

	private static XPathFactory factory = XPathFactory.newInstance();
	
	//配置文件
	private static final String config = "/generatorConfig.xml";
	
	//属性文件
	private static final String props = "/generatorConfig.properties";
	
	//XPath获取<table>节点路径
	private static final String TABLE_NODE = "generatorConfiguration/context/table";
	
	//<table>节点的别名，值设置成false|true，表示是否自动生成相应的代码
	private static final String TABLE_ATTRUBITE_GENERATE = "alias";
	
	//<table>节点的表名
	private static final String TABLE_ATTRUBITE_TABLENAME = "tableName";
	
	//属性文件中配置的schema
	private static final String PROPS_SCHEMA = "mybatis.schema";
	
	//配置mybatis.outputDirectory到属性文件中去
	private static final String PROPS_OUTPUTDIRECTORY = "mybatis.outputDirectory";
	
	private static Properties properties;
	
	public static void main(String[] args) {
		try {
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(getProperties(), warnings);
			InputStream is = StartUp.class.getResourceAsStream(config);
			Configuration config = cp.parseConfiguration(is);
			DefaultShellCallback shellCallback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
			Set<String> fullyQualifiedTableNames = parseFullQualifiedTableNames();
			myBatisGenerator.generate(null, null, fullyQualifiedTableNames);
			System.out.println(warnings);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Properties getProperties(){
		if(properties == null){
			try {
				properties = new Properties();
				properties.load(StartUp.class.getResourceAsStream(props));
				setPropsOutputDirectory();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	/**
	 * 设置文件输出的路径${mybatis.outputDirectory}的值
	 * @return
	 */
	private static void setPropsOutputDirectory(){
		String classPath = StartUp.class.getResource("/").getPath();
		String sourcePath = classPath.replace("target/classes", "src/main/java");
		properties.put(PROPS_OUTPUTDIRECTORY, sourcePath);
	}
	
	/**
	 * 功能： 获取fullyQualifiedTableNames的集合数据，该集合数据存放的是需要自动生成代码的表的信息，格式为
	 * 		schame.tablename
	 * 	1. schame从属性文件中获取，tablename从<table>节点属性中获取
	 *  2. 为了每次执行是不都全部重新生成所有表，使用<table generate="true">来表示需要自动生成代码，
	 *     解析出<table>节点，如果节点属性generate=true表示要自动生成代码，否则不生成代码
	 *  3. fullyQualifiedTableNames为null或空时，表示自动生成所有<table>节点配置的表
	 * 
	 * @return Set
	 */
	private static Set<String> parseFullQualifiedTableNames(){
		Set<String> fullyQualifiedTableNames = new HashSet<String>();
		try {
			//获取schema
			Properties properties = getProperties();
			String schema = String.valueOf(properties.get(PROPS_SCHEMA));
			
			//获取<table>节点
			XPath xpath = factory.newXPath();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream is = StartUp.class.getResourceAsStream(config);
			Document root = builder.parse(is);
			NodeList tableList = (NodeList)xpath.evaluate(TABLE_NODE, root, XPathConstants.NODESET);
			
			//遍历<table>节点
			for(int i = 0; i < tableList.getLength(); i ++){
				Node table = tableList.item(i);
				//获取节点属性并遍历
				NamedNodeMap nnm = table.getAttributes();
				boolean generate = false;
				String tablename = null;
				for (int j = 0; j < nnm.getLength(); j++) {
					Node attribute = nnm.item(j);
					String value = attribute.getNodeValue();
					String name = attribute.getNodeName();
					//获取generate属性
					if(TABLE_ATTRUBITE_GENERATE.equals(name)){
						try {
							generate = Boolean.valueOf(value);
						} catch (Exception e) {
							generate = false;
						}
					//获取tableName属性
					}else if(TABLE_ATTRUBITE_TABLENAME.equals(name)){
						tablename = value;
					}
				}
				
				//按照schame.tablename格式存入到fullyQualifiedTableNames
				if(generate && tablename != null){
					fullyQualifiedTableNames.add(schema + "." + tablename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fullyQualifiedTableNames;
	}
	
}
