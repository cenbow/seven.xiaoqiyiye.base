package com.cairenhui.core.base.invoke.response;

import com.cairenhui.core.base.invoke.Response;

public class AbstractEntityResponse implements Response {

	public boolean success() {
		return true;
	}

	public boolean error() {
		return false;
	}


}
