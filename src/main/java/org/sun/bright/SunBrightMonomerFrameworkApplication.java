package org.sun.bright;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * spring boot run application
 */
// 开启异步
@EnableAsync
// 开启缓存
@EnableCaching
@SpringBootApplication
public class SunBrightMonomerFrameworkApplication {

	public static void main(String[] args) {
		// SpringApplication.run(SunBrightMonomerFrameworkApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SunBrightMonomerFrameworkApplication.class) ;
		builder.web(WebApplicationType.SERVLET);
		builder.application().setAdditionalProfiles("prod");
		builder.bannerMode(Banner.Mode.OFF).run(args) ;
	}

	@PostConstruct
	public void setDefaultTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

}
