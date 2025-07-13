package app.hakai.backend;

import org.kahai.framework.Kahai;
import org.kahai.framework.KahaiApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Kahai
public class BackendApplication {
	public static void main(String[] args) {
		KahaiApplication.run(BackendApplication.class, args);
	};
};
