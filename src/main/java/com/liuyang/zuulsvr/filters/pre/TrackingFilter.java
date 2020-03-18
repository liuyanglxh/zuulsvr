package com.liuyang.zuulsvr.filters.pre;

import com.liuyang.zuulsvr.filters.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    private FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() {
        if (isCorrelationIdPresent()) {
            LOGGER.debug("correlation-id found : {}", filterUtils.getCorrelationId());
        } else {
            String correlationId = generateCorrelationId();
            filterUtils.setCorrelationId(correlationId);
            LOGGER.debug("generate correlation-id : {}", correlationId);
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        LOGGER.debug("processing request {}", ctx.getRequest().getRequestURI());
        return null;
    }

    private boolean isCorrelationIdPresent() {
        return filterUtils.getCorrelationId() != null;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
