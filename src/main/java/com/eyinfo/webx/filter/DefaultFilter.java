package com.eyinfo.webx.filter;

import jakarta.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class DefaultFilter extends BaseFilter {

}
