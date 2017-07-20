package com.maryun;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.maryun.utils.SpringBeanUtil;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
public class ServerOneApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerOneApplication.class, args);
	}
//	@Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//        tomcat.setUriEncoding(Charset.forName("utf-8"));
//        return tomcat;
//    }
		
	
	/**
	 * 注册SpringBeanUtil
     */
    @Bean
    public SpringBeanUtil springUtil2(){return new SpringBeanUtil();}
	
//	
//	@Value("${server.port}")
//	private int port;
//	//实现http协议转https协议的配置
//		 @Bean
//	    public EmbeddedServletContainerFactory servletContainer(){
//	        TomcatEmbeddedServletContainerFactory tomcat=new TomcatEmbeddedServletContainerFactory(){
//	            @Override
//	            protected void postProcessContext(Context context) {
//	                SecurityConstraint securityConstraint=new SecurityConstraint();
//	                securityConstraint.setDisplayName("CONFIDENTIAL");
//	                SecurityCollection collection=new SecurityCollection();
//	                collection.addPattern("/*");
//	                securityConstraint.addCollection(collection);
//	                context.addConstraint(securityConstraint);
//	            }
//	        };
//	        tomcat.addAdditionalTomcatConnectors(httpConnector());//添加连接
//	        return tomcat;
//	    }
//	    @Bean
//	    public Connector httpConnector(){
//	        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//	        connector.setScheme("http");
//	        connector.setPort(8779);
//	        connector.setSecure(false);
//	        connector.setRedirectPort(port);
//	        return connector;
//	    }
//	    @Bean
//	    public TomcatEmbeddedServletContainerFactory embeddedTomcat(){
//	    	TomcatEmbeddedServletContainerFactory tomcat=new TomcatEmbeddedServletContainerFactory();
//	    	tomcat.setss
//	    }
}
