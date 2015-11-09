package seven.ebatong;

import java.util.Map;

public interface YbaRequest {

	String getRequestUrl();
	
	Map<String, Object> paramMap();
}
