package seven.xiaoqiyiye.base.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import seven.xiaoqiyiye.base.util.DataUtils;
import seven.xiaoqiyiye.base.util.DecryptUtils;


/**
 * 该类继承了PropertyPlaceholderConfigurer，用来使用Spring注入属性文件，在原来功能的基础上，暴露了属性值的获取。
 * @author linya 2016-02-18
 */
public class PropertyPlaceholderConfigurerHelper extends PropertyPlaceholderConfigurer{

	//暴露属性文件
	private static Properties exposedProperties = new Properties();
	
	//加密文件
	private Resource[] decryptLocations;
	
	//加密Key
	private Resource keyLocation;
	
	private String fileEncoding;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		exposedProperties.putAll(props);
	}
	
	public static Properties getProperties(){
		return exposedProperties;
	}
	
	private static String getRawValue(String key){
		String value = null;
		if(exposedProperties != null){
			value = exposedProperties.getProperty(key, "");
		}
		return value;
	}
	
	public static String getValue(String key){
		return DataUtils.toString(getRawValue(key));
	}
	
	public static boolean getBooleanValue(String key){
		return DataUtils.toBoolean(getRawValue(key));
	}
	
	public static int getIntegerValue(String key){
		return DataUtils.toInt(getRawValue(key));
	}
	
	public static long getLongValue(String key){
		return DataUtils.toLong(getRawValue(key));
	}
	
	public void setDecryptLocations(Resource[] decryptLocations) {
		this.decryptLocations = decryptLocations;
	}

	public void setKeyLocation(Resource keyLocation) {
		this.keyLocation = keyLocation;
	}
	
	/**
	 * 加载加密文件
	 * @param props
	 * @throws IOException
	 */
	protected void loadDecryptProperties(Properties props) throws IOException {
		
		if(decryptLocations == null){
			return;
		}
		
		for (int i = 0; i < decryptLocations.length; i++) {
			Resource location = decryptLocations[i];
			InputStream is = null; // 属性文件输入流
			InputStream keyStream = null; // 密钥输入流
			InputStream readIs = null; // 解密后属性文件输入流
			try {
				// 属性文件输入流
				is = location.getInputStream();
				// 密钥输入流
				keyStream = keyLocation.getInputStream();
				// 得到解密后的输入流对象
				readIs = DecryptUtils.decrypt(is, DecryptUtils.getKey(keyStream));
				// 以下操作按照Spring的流程做即可
				
				InputStreamResource resource = new InputStreamResource(readIs);
				PropertiesLoaderUtils.fillProperties(props, new EncodedResource(resource, this.fileEncoding));
				
			} catch (Exception ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
				}
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(keyStream);
				IOUtils.closeQuietly(readIs);
			}
		}
		
	}	
	
	@Override
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		loadDecryptProperties(props);
	}

	@Override
	public void setFileEncoding(String encoding) {
		this.fileEncoding = encoding;
		super.setFileEncoding(encoding);
	}
	
}
